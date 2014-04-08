package com.rsmart.kuali.coeus.hr.rest;

import static org.kuali.kra.logging.BufferedLogger.debug;
import static org.kuali.kra.logging.BufferedLogger.error;
import static org.kuali.kra.logging.BufferedLogger.info;

import com.rsmart.kuali.coeus.hr.rest.model.DOMHRImport;
import com.rsmart.kuali.coeus.hr.rest.model.HRImport;
import com.rsmart.kuali.coeus.hr.service.HRImportService;
import com.rsmart.kuali.coeus.hr.service.ImportError;
import com.rsmart.kuali.coeus.hr.service.ImportRunner;
import com.rsmart.kuali.coeus.hr.service.ImportStatus;
import com.rsmart.kuali.coeus.hr.service.ImportStatusService;
import com.sun.jersey.multipart.FormDataParam;

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

/**
 * This is the main REST resource which defines the endpoints for HR import. It uses the Jersey implementation
 * of JAX-RS. Therefore all configuration is accomplished via java.ws.rs annotations.
 * 
 * The base path for this resource is "/hrimport".
 * 
 * Specific REST calls:
 * 
 *   /import                [POST]
 *      expects multipart form data with a single file (named "file") included. This file
 *      should be an XML document conforming to hrmanifest.xsd
 *      
 *   /delete/<principal ID> [DELETE]
 *      deletes the Entity and all dependent objects represented by principal ID.
 *      
 * @author duffy
 *
 */
@Path("hrimport")
public class HRImportResource {
  public static final String SCHEMA_PATH = "/hrmanifest.xsd";
  public static final String IMPORT_SERVICE_NAME = "hrImportService";
  public static final String HR_IMPORT_IN_MEMORY = "hrimport.inMemory";

  /**
   * HRImportService implements the business logic to preform the import.
   */
  protected transient HRImportService     importService = null;
  
  protected transient ImportRunner        importRunner = null;
  
  protected transient ImportStatusService statusService = null;

   /**
   * JAXB innards
   */
  protected transient JAXBContext         jaxbContext = null;
  /**
   * The schema for validation.
   */
  protected transient Schema              hrImportSchema = null;
  /**
   * Parses the incoming XML document
   */
  protected transient Unmarshaller        hrImportUnmarshaller = null;

  public HRImportResource() throws Exception {
    info("HRImportResource created");
    jaxbContext = JAXBContext.newInstance(DOMHRImport.class);
    SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    hrImportSchema = sf.newSchema(new StreamSource(getClass().getResourceAsStream(
        SCHEMA_PATH)));
    debug("schema loaded from " + SCHEMA_PATH);
    hrImportUnmarshaller = jaxbContext.createUnmarshaller();
    hrImportUnmarshaller.setSchema(hrImportSchema);
  }

  public String statusToJson (final ImportStatus status) {
    final StringBuffer sb = new StringBuffer();
    
    sb.append('{');
    sb.append("\"importId\":\"").append(status.getImportId()).append("\",");
    sb.append("\"status\":\"").append(status.getStatus()).append("\",");
    final String msg = status.getMessage();
    if (msg != null) {
      sb.append("\"message\":\"").append(msg).append("\",");
    }
    sb.append("\"startTime\":\"").append(status.getStartTimeInMillis()).append("\",");
    final long end = status.getEndTimeInMillis();
    if (end > 0) {
      sb.append("\"endTime\":\"").append(end).append("\",");
    }
    sb.append("\"recordTotal\":\"").append(status.getRecordTotal()).append("\",");
    sb.append("\"processedRecords\":\"").append(status.getProcessedRecordCount()).append("\",");
    final int errorCount = status.getErrorCount();
    sb.append("\"errorCount\":\"").append(errorCount).append("\"");
    if (errorCount > 0) {
      sb.append(",\"errors\":[");
      final List<ImportError> errors = status.getErrors();
      String separator = "";
      for (final ImportError error : errors) {
        sb.append(separator);
        separator = ",";
        sb.append("{\"recordNumber\":\"").append(error.getRecordNumber()).append("\",");
        sb.append("\"principalName\":\"").append(error.getPrincipalName()).append("\",");
        final Exception e = error.getException();
        sb.append("\"exception\":{\"type\":\"").append(e.getClass().getSimpleName()).append("\",");
        sb.append("\"message\":\"").append(e.getMessage()).append("\"}}");
      }
      sb.append(']');
    }
    sb.append('}');
    return sb.toString();
  }
  
  protected final boolean runInMemory() {
    return ConfigContext.getCurrentContextConfig().getBooleanProperty(HR_IMPORT_IN_MEMORY, false);
  }
  /**
   * Processes an incoming multipart form with a "file" argument. The file is an 
   * HR import that must conform to hrimport.xsd.
   * 
   * Return codes:
   *  200     import ok
   *  400     bad request
   *  401     access denied
   *  500     server error
   *  
   * @param uploadedInputStream
   * @return
   * @throws Exception
   */
  @POST
  @Path("/import")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response processImport(@FormDataParam("file") InputStream uploadedInputStream)
      throws Exception {
	  
  	if (uploadedInputStream == null) {
  		error("import called without file argument");
  		return Response.status(Response.Status.BAD_REQUEST).build();
  	}
  	
  	// store import in a temporary location
    final File tempFile = File
        .createTempFile("hrimport", Long.toString(new Date().getTime()));

    debug("writing uploaded HR import to : " + tempFile.getAbsolutePath());

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
      error("Error while processing an hrimport file upload", e);
      throw e;
    }

    final String importId = UUID.randomUUID().toString();
    debug ("initiating import with ID: " + importId);
    

    Response res;

    try {
      final ImportRunner runner = getImportRunner();
      ImportStatus status = null;
      if (runInMemory()) {
        // convert the file to an HRImport object graph (see com.rsmart.kuali.coeus.hr.rest.model package)
        HRImport toImport = (HRImport) hrImportUnmarshaller.unmarshal(tempFile);
        status = runner.processImport(importId, toImport);
      } else {
        status = runner.processImport(importId, tempFile.getAbsolutePath());
      }
      res = Response.ok().entity(statusToJson(status)).build();
    } catch (Exception e) {
      res = Response.noContent().status(500).build();
      error("Import failed ", e);
    }
    return res;
  }
  
  @DELETE
  @Path("/import/{importId}")
  public Response abort(@PathParam("importId") String importId) {
    Response res;
    try {
      final ImportRunner runner = getImportRunner();
      runner.abort(importId);
      res = Response.ok().build();
    } catch (Exception e) {
      res = Response.noContent().status(500).build();
    }
    return res;
  }

  @GET
  @Path("/import/{importId}")
  public Response status(@PathParam("importId") String importId) {
    Response res;
    try {
      final ImportStatus status = getStatusService().getImportStatus(importId);
      if (status == null) {
        res = Response.noContent().status(Status.NOT_FOUND).build();
      } else {
        res = Response.ok().entity(statusToJson(status)).build();
      }
    } catch (Exception e) {
      res = Response.noContent().status(500).build();
    }
    return res;
  }

  /**
   * Deletes a single person by his/her entity ID
   * @param entityId
   * @return
   */
  @DELETE
  @Path("/delete/{entityId}")
  public Response deleteRecord(@PathParam("entityId") String entityId) {
    try {
      getImportService().deletePerson(entityId);
    } catch (Exception e) {
      return Response.status(500).build();
    }
    return Response.ok().build();
  }
  
  @GET
  @Path("/principals/unmanaged")
  public Response unmanagedPrincipals() {
    try {
      final List<String> names = getStatusService().getPrincipalNamesUnmanagedByHRImport();
      final StringBuilder sb = new StringBuilder();
      boolean comma = false;
      sb.append('[');
      for (final String name : names) {
        if (comma) {
          sb.append(',');
        }
        comma = true;
        sb.append('\"').append(name).append('\"');
      }
      sb.append(']');
      return Response.ok(sb.toString()).build();
    } catch (Exception e) {
      return Response.status(500).build();
    }
  }
  /**
   * If an instance of HRImportService has been provided via setImportService(...) it will be returned
   * here. Otherwise a new HRImportServiceImpl will be created. KRA and RICE service locators will
   * be used to populate its dependencies.
   * 
   * @return
   */
  public HRImportService getImportService() {
    if (importService == null) {
      importService = (HRImportService)GlobalResourceLoader.getService("hrImportService");
    }
  	return importService;
  }

  public void setImportService(HRImportService importService) {
    this.importService = importService;
  }
  
  public ImportStatusService getStatusService() {
    if (statusService == null) {
      statusService = (ImportStatusService)GlobalResourceLoader.getService("importStatusService");
    }
    return statusService;
  }
  
  public void setStatusService (final ImportStatusService svc) {
    this.statusService = svc;
  }
  
  public ImportRunner getImportRunner() {
    if (importRunner == null) {
      importRunner = (ImportRunner)GlobalResourceLoader.getService("importRunner");
    }
    
    return importRunner;
  }
  
  public void setImportRunner(final ImportRunner runner) {
    importRunner = runner;
  }
}
