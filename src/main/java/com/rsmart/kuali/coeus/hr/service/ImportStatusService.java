package com.rsmart.kuali.coeus.hr.service;

import java.util.List;

public interface ImportStatusService {

  public ImportStatus getImportStatus (String importId);
  
  public ImportStatus initiateImport (String importId, int numRecords);
  
  public void abort(String importId);
  
  public void abort(String importId, String detailMessage);

  public void abnormalTermination(String importId);

  public void abnormalTermination(String importId, String detailMessage);

  public void recordInactivated(String importId, String personId);
  
  public void recordProcessed(String importId, String personId);
  
  public void recordError(String importId, String personId, ImportError error);
  
  public List<String> getActiveIdsMissingFromImport(String importId);
  
  public void completeImport(String importId);
  
}
