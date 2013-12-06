package com.rsmart.kuali.coeus.hr.rest;

import com.rsmart.kuali.coeus.hr.rest.model.HRManifest;
import com.rsmart.kuali.coeus.hr.service.HRManifestService;
import com.sun.jersey.multipart.FormDataParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
  private static final Log LOG = LogFactory.getLog(HRManifestResource.class);

  public static final String SCHEMA_PATH = "/hrmanifest.xsd";

  protected transient HRManifestService manifestService = null;
  protected transient JAXBContext jaxbContext = null;
  protected transient Schema hrManifestSchema = null;
  protected transient Unmarshaller hrManifestUnmarshaller = null;

  public HRManifestResource() throws Exception {
    LOG.info("HRManifestResource created");
    jaxbContext = JAXBContext.newInstance(HRManifest.class);
    SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    hrManifestSchema = sf.newSchema(new StreamSource(getClass().getResourceAsStream(
        SCHEMA_PATH)));
    LOG.debug("schema loaded from " + SCHEMA_PATH);
    hrManifestUnmarshaller = jaxbContext.createUnmarshaller();
    hrManifestUnmarshaller.setSchema(hrManifestSchema);
  }

  @POST
  @Path("/import")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response processManifest(@FormDataParam("file") InputStream uploadedInputStream)
      throws Exception {
    File tempFile = File
        .createTempFile("hrmanifest", Long.toString(new Date().getTime()));

    LOG.debug("writing uploaded HR manifest to : " + tempFile.getAbsolutePath());

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
      LOG.error("Error while processing an hrmanifest file upload", e);
      throw e;
    }

    HRManifest manifest = (HRManifest) hrManifestUnmarshaller.unmarshal(tempFile);

    Response res;

    try {
      manifestService.importHRManifest(manifest);
      res = Response.ok().build();
    } catch (Exception e) {
      res = Response.noContent().status(500).build();
      LOG.error("Failed to import manifest", e);
    }
    return res;
  }

  public void setManifestService(HRManifestService manifestService) {
    this.manifestService = manifestService;
  }
}
