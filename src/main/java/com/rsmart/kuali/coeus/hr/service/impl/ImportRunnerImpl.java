package com.rsmart.kuali.coeus.hr.service.impl;

import static org.kuali.kra.logging.BufferedLogger.debug;
import static org.kuali.kra.logging.BufferedLogger.error;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.kuali.kra.common.notification.service.KcNotificationService;
import org.kuali.kra.util.EmailAttachment;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;

import com.rsmart.kuali.coeus.hr.rest.model.HRImport;
import com.rsmart.kuali.coeus.hr.rest.model.StAXHRImport;
import com.rsmart.kuali.coeus.hr.service.HRImportService;
import com.rsmart.kuali.coeus.hr.service.ImportError;
import com.rsmart.kuali.coeus.hr.service.ImportRunner;
import com.rsmart.kuali.coeus.hr.service.ImportStatus;
import com.rsmart.kuali.coeus.hr.service.ImportStatusService;

public class ImportRunnerImpl implements ImportRunner {

  private final static int              DFT_POOL_SIZE = 5;
  private final static DateFormat       DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss (z)");
  private final static DecimalFormat    DECIMAL_FORMAT = new DecimalFormat("###.##");

  protected static final String         FROM_ADDRESS_KEY = "hrimport.report.from";
  protected static String               FROM_ADDRESS = null;
    
  protected int                         threadPoolSize = DFT_POOL_SIZE;
  protected HRImportService             importService;
  protected ExecutorService             executorService;
  protected ImportStatusService         statusService;
  protected KcNotificationService       kcNotificationService;
  
  public ImportRunnerImpl ()
  {
    if (FROM_ADDRESS == null) {
      FROM_ADDRESS = ConfigContext.getCurrentContextConfig().getProperty(FROM_ADDRESS_KEY);
      if (FROM_ADDRESS == null) {
        error(FROM_ADDRESS_KEY + " is not set. Report emails cannot be sent.");
      } else {
        debug("Report email from address set to: " + FROM_ADDRESS);
      }
    }
  }
  
  protected void setThreadPoolSize(final int size) {
    threadPoolSize = size;
  }
  
  protected int getThreadPoolSize() {
    return threadPoolSize;
  }

  public HRImportService getImportService() {
    return importService;
  }

  public void setImportService(HRImportService importService) {
    this.importService = importService;
  }

  public ImportStatusService getStatusService() {
    return statusService;
  }

  public void setStatusService(final ImportStatusService svc) {
    statusService = svc;
  }
  
  public KcNotificationService getKcNotificationService() {
    return kcNotificationService;
  }

  public void setKcNotificationService(KcNotificationService kcNotificationService) {
    this.kcNotificationService = kcNotificationService;
  }

  protected ExecutorService getExecutorService() {
    if (executorService == null) {
      executorService = Executors.newFixedThreadPool(getThreadPoolSize());
    }
    return executorService;
  }
  
  @Override
  public ImportStatus processImport(final String importId, final HRImport toImport) {
    final ImportTask task = new ImportTask(importId, toImport, importService, statusService);
    final ImportStatus status = task.getImportStatus();
    
    debug("scheduling import task with executor for import " + importId);
    getExecutorService().execute(task);
    
    return status;
  }

  @Override
  public ImportStatus processImport(final String importId, final String importFile) {
    final ImportTask task = new ImportTask(importId, importFile, importService, statusService);
    final ImportStatus status = task.getImportStatus();
    
    debug("scheduling import task with executor for import " + importId);
    getExecutorService().execute(task);
    
    return status;
  }

  @Override
  public ImportStatus getStatus(final String id) {
    return statusService.getImportStatus(id);
  }

  @Override
  public void abort(final String id) {
    debug("aborting import with id: " + id);
    importService.abort(id);
  }

  private class ImportTask implements Runnable {

    protected String importId;
    protected HRImport hrImport;
    protected HRImportService importService;
    protected ImportStatusService statusService;
    protected ImportStatus importStatus;
    protected UserSession userSession;
    
    public ImportTask (final String importId, final HRImport hrImport, final HRImportService service,
        final ImportStatusService statusService) {
      this.importId = importId;
      this.hrImport = hrImport;
      this.importService = service;
      this.statusService = statusService;
      importStatus = statusService.initiateImport(importId, hrImport.getRecordCount());

      final UserSession incomingUserSession = GlobalVariables.getUserSession();
      if (incomingUserSession == null) {
        throw new IllegalStateException ("No user logged in");
      }
      
      userSession = new UserSession(incomingUserSession.getPrincipalName());
    }
    
    public ImportTask (final String importId, final String importFile, final HRImportService service,
        final ImportStatusService statusService) {
      this.importId = importId;
//      this.hrImport = hrImport;
      this.importService = service;
      this.statusService = statusService;
      
      hrImport = new StAXHRImport(importFile);
      importStatus = statusService.initiateImport(importId, hrImport.getRecordCount());

      final UserSession incomingUserSession = GlobalVariables.getUserSession();
      if (incomingUserSession == null) {
        throw new IllegalStateException ("No user logged in");
      }
      
      userSession = new UserSession(incomingUserSession.getPrincipalName());
    }
    
    public ImportStatus getImportStatus() {
      return importStatus;
    }
    
    protected final EmailAttachment generateExceptionReport (final ImportStatus status) {
      final EmailAttachment exceptionReport = new EmailAttachment();
      final String reportFileName = "hrimport-" + status.getImportId() + ".txt";
      
      debug("generating exception report for " + reportFileName);
      exceptionReport.setFileName(reportFileName);
      exceptionReport.setMimeType("text/plain");
      final StringBuffer buff = new StringBuffer();
      final List<ImportError> errors = status.getErrors();
      for (final ImportError error : errors) {
        buff.append("record ").append(error.getRecordNumber());
        buff.append(", principal name: ").append(error.getPrincipalName()).append(": ");
        buff.append(error.getException().getMessage()).append('\n');
      }
      exceptionReport.setContents(buff.toString().getBytes());
      
      return exceptionReport;
    }
    
    protected final String formatStatusMessage(final ImportStatus status) {
      final StringBuffer buff = new StringBuffer();
      final String BREAK = "<br/>\n";
      
      buff.append("HR Import Status Report").append(BREAK)
          .append("-----------------------").append(BREAK).append(BREAK);
      
      buff.append("Unique Import ID: ").append(status.getImportId()).append(BREAK);
      buff.append("Import Status: ").append(status.getStatus().toString()).append(BREAK);
      
      final long start = status.getStartTimeInMillis();
      final long end = status.getEndTimeInMillis();
      final long duration = end - start;
      final Date startDate = new Date(start);
      final Date endDate = new Date(end);
      
      buff.append("Start:\t").append(DATE_FORMAT.format(startDate)).append(BREAK);
      buff.append("End:\t").append(DATE_FORMAT.format(endDate)).append(BREAK);
      buff.append("Total Time: ").append(DECIMAL_FORMAT.format(((float)duration)/1000f))
        .append(" seconds").append(BREAK);

      final int errorCount = status.getErrorCount();
      buff.append("Records Sumbitted: ").append(status.getRecordTotal()).append(BREAK);
      buff.append("Records Processed: ").append(status.getProcessedRecordCount()).append(BREAK);
      buff.append("Errors Encountered: ").append(status.getErrorCount()).append(BREAK);
      if (errorCount > 0) {
        buff.append("\t*** Error report is attached to this email");
      }
      buff.append(BREAK).append(BREAK);
      
      final String message = status.getMessage();
      if (message != null) {
        buff.append("Detailed Message:").append(BREAK).append("&nbsp;&nbsp;&nbsp;&nbsp;").append(message);
      }
      
      return buff.toString();
    }
    
    protected void notifyStatusEmailRecipient (final String importId) {
      debug("formulating report email");
      if (FROM_ADDRESS == null) {
        error("status email not sent; " + FROM_ADDRESS_KEY + " is not set");
        return;
      }
      final ImportStatus status = statusService.getImportStatus(importId);
      final String recipient = hrImport.getStatusEmailRecipient();
      
      if (recipient != null) {
        List<EmailAttachment> attachments = new LinkedList<EmailAttachment>();
        if (status.getErrorCount() > 0) {
          final EmailAttachment exceptionReport = generateExceptionReport(status);
          attachments.add(exceptionReport);
        }

        final HashSet<String> toAddresses = new HashSet<String>();
        toAddresses.add(hrImport.getStatusEmailRecipient());
        
        final String subject = "HR Import Results";
        final String message = formatStatusMessage(status);
        
        //work around private method access designation to send an email to a raw address instead of a
        // KIM-managed person
        Method sendEmailMethod;
        try {
          sendEmailMethod = kcNotificationService.getClass().getDeclaredMethod(
              "sendEmailNotification", String.class, Set.class, String.class, String.class, List.class);
          sendEmailMethod.setAccessible(true);
          sendEmailMethod.invoke(kcNotificationService, FROM_ADDRESS, toAddresses, subject, message, 
              attachments);
        } catch (Exception e) {
          error ("Email of results failed", e);
        }
      }
    }
    
    @Override
    public void run() {
      debug("initiating import of: " + importId);
      try {  
        GlobalVariables.setUserSession(userSession);
        importService.startImport(importId, hrImport);
        statusService.completeImport(importId);
      } catch (Exception e) {
        error("import stopped due to error: " + e.getMessage(), e);
        // TODO Duffy - Need a better way to log a stack trace here
        e.printStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append("Unexpected error: ").append(e.getMessage()).append(" [").append(e.getClass().getSimpleName())
              .append(']');
        statusService.abnormalTermination(importId, sb.toString());
      } finally {
        notifyStatusEmailRecipient(importId);
        GlobalVariables.clear();
      }
    }
    
  }
}
