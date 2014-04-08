package com.rsmart.kuali.coeus.hr.rest.model;

import java.util.ArrayList;
import java.util.Iterator;
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
 * See {@link com.rsmart.kuali.coeus.hr.rest.model.DOMHRImport HRImport} for more details.
 * @author duffy
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "records")
public class DOMHRImportRecordCollection implements HRImportRecordCollection {

  @XmlElement(name = "record", type = HRImportRecord.class)
  @Valid
  private List<HRImportRecord> records = new ArrayList<HRImportRecord>();

  public DOMHRImportRecordCollection() {
  }

  public DOMHRImportRecordCollection(List<HRImportRecord> records) {
    this.records = records;
  }

  @Override
  public Iterator<HRImportRecord> iterator() {
    return records.iterator();
  }

  public void setRecords(List<HRImportRecord> records) {
    this.records = records;
  }

  public List<HRImportRecord> getRecords() {
    return records;
  }
  
  @Override
  public int getCount() {
    return records.size();
  }

}
