package com.rsmart.kuali.coeus.hr.service;

public class ImportError {
  
  protected int recordNumber = 0;
  protected Exception exception = null;
  
  public ImportError (final int record, final Exception e) {
    recordNumber = record;
    exception = e;
  }
  
  public int getRecordNumber() {
    return recordNumber;
  }

  public Exception getException() {
    return exception;
  }
  
}
