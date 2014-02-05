package com.rsmart.kuali.coeus.hr.rest.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.Valid;
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
  @XmlAttribute
  protected boolean updateExisting;
  @XmlAttribute
  protected String uniqueImportId;

  @XmlElement(name = "records", type = HRManifestRecordCollection.class)
  @Valid
  protected HRManifestRecordCollection records;

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

  public HRManifestRecordCollection getRecords() {
    return records;
  }

  public void setRecords(HRManifestRecordCollection records) {
    this.records = records;
  }
  
  public boolean isUpdateExisting() {
    return updateExisting;
  }
  
  public void setUpdateExisting(final boolean updateExisting) {
    this.updateExisting = updateExisting;
  }
  
  public String getUniqueImportId() {
    return uniqueImportId;
  }
  
  public void setUniqueImportId(final String id) {
    uniqueImportId = id;
  }

}
