package com.rsmart.kuali.coeus.hr.rest;

import static org.kuali.kra.logging.BufferedLogger.debug;
import static org.kuali.kra.logging.BufferedLogger.error;
import static org.kuali.kra.logging.BufferedLogger.info;

import com.rsmart.kuali.coeus.hr.rest.model.HRManifest;
import com.rsmart.kuali.coeus.hr.service.HRManifestService;
import com.rsmart.kuali.coeus.hr.service.impl.HRManifestServiceImpl;
import com.sun.jersey.multipart.FormDataParam;

import org.kuali.kra.service.UnitService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

@Path("hrmanifest")
public class HRManifestResource {
  public static final String SCHEMA_PATH = "/hrmanifest.xsd";
  public static final String MANIFEST_SERVICE_NAME = "hrManifestService";

  protected transient HRManifestService manifestService = null;
  protected transient JAXBContext jaxbContext = null;
  protected transient Schema hrManifestSchema = null;
  protected transient Unmarshaller hrManifestUnmarshaller = null;

  public HRManifestResource() throws Exception {
    info("HRManifestResource created");
    jaxbContext = JAXBContext.newInstance(HRManifest.class);
    SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    hrManifestSchema = sf.newSchema(new StreamSource(getClass().getResourceAsStream(
        SCHEMA_PATH)));
    debug("schema loaded from " + SCHEMA_PATH);
    hrManifestUnmarshaller = jaxbContext.createUnmarshaller();
    hrManifestUnmarshaller.setSchema(hrManifestSchema);
  }

  @POST
  @Path("/import")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response processManifest(@FormDataParam("file") InputStream uploadedInputStream)
      throws Exception {
	  
  	if (uploadedInputStream == null) {
  		error("/import called without file argument");
  		return Response.status(Response.Status.BAD_REQUEST).build();
  	}
  	
    File tempFile = File
        .createTempFile("hrmanifest", Long.toString(new Date().getTime()));

    debug("writing uploaded HR manifest to : " + tempFile.getAbsolutePath());

    try {
      OutputStream out = new FileOutputStream(tempFile);
      int read = 0;
      byte[] bytes = new byte[32768];

      while ((read = uploadedInputStream.read(bytes)) != -1) {
        out.write(bytes, 0, read);
      }
      out.flush();
      out.close();
    } catch (IOException e) {
      error("Error while processing an hrmanifest file upload", e);
      throw e;
    }

    HRManifest manifest = (HRManifest) hrManifestUnmarshaller.unmarshal(tempFile);

    Response res;

    try {
      getManifestService().importHRManifest(manifest);
      res = Response.ok().build();
    } catch (Exception e) {
      res = Response.noContent().status(500).build();
      error("Failed to import manifest", e);
    }
    return res;
  }

  @DELETE
  @Path("/delete/{entityId}")
  public Response deleteRecord(@PathParam("entityId") String entityId) {
    try {
      getManifestService().deletePerson(entityId);
    } catch (Exception e) {
      return Response.status(500).build();
    }
    return Response.ok().build();
  }
  
  public HRManifestService getManifestService() {
  	if (manifestService == null) {
  	  debug ("no HRManifestService implementation registered - creating one and setting dependencies via ServiceLocators");
  	  final HRManifestServiceImpl impl = new HRManifestServiceImpl();
  	  impl.setIdentityService(KimApiServiceLocator.getIdentityService());
  	  impl.setBusinessObjectService(KRADServiceLocator.getBusinessObjectService());
      impl.setUnitService((UnitService)GlobalResourceLoader.getService("unitService"));
  	  
  	  manifestService = impl;
  	}
  	
  	return manifestService;
  }

  public void setManifestService(HRManifestService manifestService) {
    this.manifestService = manifestService;
  }
}
