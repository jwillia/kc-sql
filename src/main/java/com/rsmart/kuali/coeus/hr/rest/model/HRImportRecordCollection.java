package com.rsmart.kuali.coeus.hr.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Part of the HRImport object graph that is created when the HR import XML
 * file is parsed.
 * 
 * See {@link com.rsmart.kuali.coeus.hr.rest.model.HRImport HRImport} for more details.
 * @author duffy
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "records")
public class HRImportRecordCollection {

  @XmlElement(name = "record", type = HRImportRecord.class)
  @Valid
  private List<HRImportRecord> records = new ArrayList<HRImportRecord>();

  public HRImportRecordCollection() {
  }

  public HRImportRecordCollection(List<HRImportRecord> records) {
    this.records = records;
  }

  public List<HRImportRecord> getRecords() {
    return records;
  }

  public void setRecords(List<HRImportRecord> records) {
    this.records = records;
  }

}
