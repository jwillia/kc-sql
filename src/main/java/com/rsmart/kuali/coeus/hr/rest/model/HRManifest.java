package com.rsmart.kuali.coeus.hr.rest.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="hrmanifest")
public class HRManifest {
  @XmlAttribute
  protected String submitterId;
  @XmlAttribute
  protected Date reportDate;
  @XmlAttribute
  protected String transactionId;
  @XmlAttribute
  protected int recordCount;

  @XmlElement(name = "records", type = HRManifestRecords.class)
  protected HRManifestRecords records;
  
  public String getSubmitterId() {
    return submitterId;
  }
  public void setSubmitterId(String submitterId) {
    this.submitterId = submitterId;
  }
  
  public String getTransactionId() {
    return transactionId;
  }
  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }
  
  public int getRecordCount() {
    return recordCount;
  }
  public void setRecordCount(int recordCount) {
    this.recordCount = recordCount;
  }
  
  public Date getReportDate() {
    return reportDate;
  }
  public void setReportDate(Date reportDate) {
    this.reportDate = reportDate;
  }
  
  public HRManifestRecords getRecords() {
    return records;
  }
  public void setRecords (HRManifestRecords records) {
    this.records = records;
  }
    
}
