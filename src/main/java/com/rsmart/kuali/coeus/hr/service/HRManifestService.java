package com.rsmart.kuali.coeus.hr.service;

import com.rsmart.kuali.coeus.hr.rest.model.HRManifest;

public interface HRManifestService {

  public void importHRManifest(HRManifest manifest) throws HRManifestImportException;

}
