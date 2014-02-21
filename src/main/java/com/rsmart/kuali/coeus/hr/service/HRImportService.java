package com.rsmart.kuali.coeus.hr.service;

import com.rsmart.kuali.coeus.hr.rest.model.HRImport;

/**
 * Interface for the service which imports the 
 * {@link com.rsmart.kuali.coeus.hr.rest.model.HRImport HRImport}.
 * @author duffy
 *
 */
public interface HRImportService {

  public void startImport(String importId, HRImport toImport);
  
  public void abort(String importId);
  
  public void deletePerson(String entityId);
  
  public void setImportStatusService(ImportStatusService svc);

}
