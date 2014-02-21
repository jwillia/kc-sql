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
@XmlRootElement(name = "hrimport")
public class HRImport {
  @XmlAttribute
  protected BigDecimal schemaVersion;
  @XmlAttribute
  protected int recordCount;

  @XmlElement(name = "records", type = HRImportRecordCollection.class)
  @Valid
  protected HRImportRecordCollection records;

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

  public HRImportRecordCollection getRecords() {
    return records;
  }

  public void setRecords(HRImportRecordCollection records) {
    this.records = records;
  }
  
}
