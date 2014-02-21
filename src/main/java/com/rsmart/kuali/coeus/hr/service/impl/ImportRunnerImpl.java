package com.rsmart.kuali.coeus.hr.service.impl;

import static org.kuali.kra.logging.BufferedLogger.error;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;

import com.rsmart.kuali.coeus.hr.rest.model.HRImport;
import com.rsmart.kuali.coeus.hr.service.HRImportService;
import com.rsmart.kuali.coeus.hr.service.ImportRunner;
import com.rsmart.kuali.coeus.hr.service.ImportStatus;
import com.rsmart.kuali.coeus.hr.service.ImportStatusService;

public class ImportRunnerImpl implements ImportRunner {

  private static int DFT_POOL_SIZE = 5;
  
  protected int threadPoolSize = DFT_POOL_SIZE;
  protected HRImportService importService;
  protected ExecutorService executorService;
  protected ImportStatusService statusService;
  
  public ImportRunnerImpl ()
  {}
  
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
    getExecutorService().execute(task);
    return status;
  }

  @Override
  public ImportStatus getStatus(final String id) {
    return statusService.getImportStatus(id);
  }

  @Override
  public void abort(final String id) {
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
    
    public ImportStatus getImportStatus() {
      return importStatus;
    }
    
    @Override
    public void run() {
      try {  
        GlobalVariables.setUserSession(userSession);
        importService.startImport(importId, hrImport);
        statusService.completeImport(importId);
      } catch (Exception e) {
        error("import stopped due to error: " + e.getMessage(), e);
        StringBuilder sb = new StringBuilder();
        sb.append("Unexpected error: ").append(e.getMessage()).append(" [").append(e.getClass().getSimpleName())
              .append(']');
        statusService.abnormalTermination(importId, sb.toString());
      } finally {
        GlobalVariables.clear();
      }
    }
    
  }
}
