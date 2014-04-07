package com.rsmart.kuali.coeus.hr.service;

import java.util.List;

import org.kuali.rice.kim.api.identity.principal.Principal;

public interface ImportStatusService {

  public ImportStatus getImportStatus (String importId);
  
  public ImportStatus initiateImport (String importId, int numRecords);
  
  public void abort(String importId);
  
  public void abort(String importId, String detailMessage);

  public void abnormalTermination(String importId);

  public void abnormalTermination(String importId, String detailMessage);

  public void recordInactivated(String importId, String personId);
  
  public void recordProcessed(String importId, String personId);
  
  public void recordError(String importId, ImportError error);
  
  public List<String> getActivePrincipalNamesMissingFromImport(String importId);
  
  public List<String> getPrincipalNamesUnmanagedByHRImport();
  
  public void completeImport(String importId);
  
}
