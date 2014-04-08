package com.rsmart.kuali.coeus.hr.rest.model;

import java.math.BigDecimal;
import java.util.Date;

public interface HRImport {

  public abstract BigDecimal getSchemaVersion();

  public abstract String getStatusEmailRecipient();

  public abstract int getRecordCount();

  public abstract HRImportRecordCollection getRecords();

  public abstract Date getReportDate();

}