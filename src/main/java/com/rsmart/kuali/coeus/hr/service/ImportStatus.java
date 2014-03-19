package com.rsmart.kuali.coeus.hr.service;

import java.util.LinkedList;
import java.util.List;

import com.rsmart.kuali.coeus.hr.service.ImportStatus;

public class ImportStatus {

  public enum Status { PROCESSING, COMPLETE, ABORTED, ABNORMAL_TERMINATION };
  
  private static String ABORTED_MESSAGE = "Import aborted";
  private static String ABNORMAL_MESSAGE = "Import terminated abnormally";
  private static String COMPLETE_MESSAGE = "Import completed normally";
  private static String PROCESSING_MESSAGE = "Import is processing";
  
  protected String id = null;
  protected Status status = Status.PROCESSING;
  protected int processed = 0;
  protected int total = 0;
  protected List<ImportError> importErrors = new LinkedList<ImportError>();
  protected long startTime = 0l;
  protected long endTime = Long.MIN_VALUE;
  protected String detail = null;
  
  public ImportStatus (final String importId, final int totalRecords) {
    if (importId == null || importId.length() < 1) {
      throw new IllegalArgumentException ("import id is required for ImportStatus");
    }
    id = importId;
    
    if (totalRecords < 0) {
      throw new IllegalArgumentException ("Import status cannot have a negative number of records");
    }
    total = totalRecords;
    startTime = System.currentTimeMillis();
  }
  
  public synchronized String getImportId() {
    return id;
  }
  
  public synchronized void setStatus(final Status status) {
    this.status = status;
  }
  
  public synchronized Status getStatus() {
    return status;
  }

  public synchronized void recordProcessed () {
    recordProcessed(null);
  }
  
  public synchronized void recordProcessed (final ImportError error) {
    if (!Status.PROCESSING.equals(status)) {
      throw new IllegalStateException ("Records cannot be processed when Import status is in " 
          + status.toString() + " state");
    }

    if (error != null) {
      importErrors.add(error);
    }
    if (++processed == total) {
      endTime = System.currentTimeMillis();
      setStatus(Status.COMPLETE);
    }
  }
  
  public synchronized int getProcessedRecordCount() {
    return processed;
  }

  public synchronized int getErrorCount() {
    return importErrors.size();
  }

  public synchronized int getRecordTotal() {
    return total;
  }

  public synchronized long getStartTimeInMillis() {
    return startTime;
  }

  public synchronized long getEndTimeInMillis() {
    return endTime;
  }

  public synchronized void setMessageDetail(final String detail) {
    this.detail = detail;
  }
  
  public synchronized String getMessage() {
    final StringBuffer message = new StringBuffer();
    switch (status) {
      case PROCESSING:
      {
        message.append(PROCESSING_MESSAGE);
        break;
      }
      case COMPLETE:
      {
        message.append(COMPLETE_MESSAGE);
        break;
      }
      case ABORTED:
      {
        message.append(ABORTED_MESSAGE);
        break;
      }
      case ABNORMAL_TERMINATION:
      {
        message.append(ABNORMAL_MESSAGE);
        break;
      }
    }
    
    if (detail != null) {
      message.append(':').append(detail);
    }

    return message.toString();
  }
  
  public synchronized String toString() {
    final StringBuffer sb = new StringBuffer();
    final long ending = Status.PROCESSING.equals(status) ? System.currentTimeMillis() : endTime;
    
    sb.append("Import ").append(id).append(" status: ").append(status.toString()).append(" [ processed: ")
      .append(processed).append("/").append(total).append(", errors: ").append(importErrors.size())
      .append(", time: ").append((ending - startTime)/1000f).append(" ]");
    
    return sb.toString();
  }

  public synchronized void setStartTimeInMillis(long time) {
    startTime = time;
  }

  public synchronized void setEndTimeInMillis(long time) {
    endTime = time;
  }
  
  public synchronized void setProcessed(int num) {
    processed = num;
  }

  public void setErrors(List<ImportError> errors) {
    this.importErrors = errors;
  }
  
  public List<ImportError> getErrors() {
    return importErrors;
  }
}