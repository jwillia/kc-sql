package com.rsmart.kuali.coeus.hr.service;

public class ImportError {
  
  protected int recordNumber = 0;
  protected String principalName = null;
  protected Exception exception = null;
  
  public ImportError (final int record, final String principalName, final Exception e) {
    recordNumber = record;
    this.principalName = principalName;
    exception = e;
  }
  
  public int getRecordNumber() {
    return recordNumber;
  }

  public String getPrincipalName() {
    return principalName;
  }
  
  public Exception getException() {
    return exception;
  }
  
}
