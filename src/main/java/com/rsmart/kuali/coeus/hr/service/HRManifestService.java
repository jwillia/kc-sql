package com.rsmart.kuali.coeus.hr.service;

import com.rsmart.kuali.coeus.hr.rest.model.HRManifest;

/**
 * Interface for the service which imports the 
 * {@link com.rsmart.kuali.coeus.hr.rest.model.HRManifest HRManifest}.
 * @author duffy
 *
 */
public interface HRManifestService {

  public void importHRManifest(HRManifest manifest) throws HRManifestImportException;
  
  public void deletePerson(String entityId) throws Exception;

}
