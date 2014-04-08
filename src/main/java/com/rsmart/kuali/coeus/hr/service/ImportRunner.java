package com.rsmart.kuali.coeus.hr.service;

import com.rsmart.kuali.coeus.hr.rest.model.HRImport;

public interface ImportRunner {
  
  public ImportStatus processImport (String id, HRImport toImport);

  public ImportStatus processImport (String id, String importFile);

  public ImportStatus getStatus (String id);
  
  public void abort(String id);
  
}
