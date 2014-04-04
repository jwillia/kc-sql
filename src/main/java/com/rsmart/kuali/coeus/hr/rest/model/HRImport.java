package com.rsmart.kuali.coeus.hr.rest.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is the root object for the HR import. It contains a collection of 
 * {@link com.rsmart.kuali.coeus.hr.rest.model.HRImportRecord HRImportRecord} objects.
 * Each {@link com.rsmart.kuali.coeus.hr.rest.model.HRImportRecord HRImportRecord} represents
 * a single user to be imported.
 * 
 * @author duffy
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "hrmanifest")
public class HRImport extends ModelObject {
  @XmlAttribute
  protected BigDecimal schemaVersion;
  @XmlAttribute
  protected String statusEmailRecipient;
  @XmlAttribute
  protected int recordCount;
  @XmlAttribute
  protected Date reportDate;

  @XmlElement(name = "records", type = HRImportRecordCollection.class)
  @Valid
  protected HRImportRecordCollection records;

  public BigDecimal getSchemaVersion() {
    return schemaVersion;
  }

  public void setSchemaVersion(BigDecimal schemaVersion) {
    this.schemaVersion = schemaVersion;
  }

  public String getStatusEmailRecipient() {
    return statusEmailRecipient;
  }

  public void setStatusEmailRecipient(String statusEmailRecipient) {
    this.statusEmailRecipient=trimToNull(statusEmailRecipient);
  }

  public int getRecordCount() {
    return recordCount;
  }

  public void setRecordCount(int recordCount) {
    this.recordCount = recordCount;
  }

  public HRImportRecordCollection getRecords() {
    return records;
  }

  public void setRecords(HRImportRecordCollection records) {
    this.records = records;
  }

  public Date getReportDate() {
    return reportDate;
  }

  public void setReportDate(Date reportDate) {
    this.reportDate = reportDate;
  }
  
}
