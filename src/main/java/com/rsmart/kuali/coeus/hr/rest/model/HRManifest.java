package com.rsmart.kuali.coeus.hr.rest.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "hrmanifest")
public class HRManifest {
  @XmlAttribute
  protected BigDecimal schemaVersion;
  @XmlAttribute
  protected Date reportDate;
  @XmlAttribute
  protected int recordCount;

  @XmlElement(name = "records", type = HRManifestRecords.class)
  protected HRManifestRecords records;

  public BigDecimal getSchemaVersion() {
    return schemaVersion;
  }

  public void setSchemaVersion(BigDecimal schemaVersion) {
    this.schemaVersion = schemaVersion;
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

  public void setRecords(HRManifestRecords records) {
    this.records = records;
  }

}
