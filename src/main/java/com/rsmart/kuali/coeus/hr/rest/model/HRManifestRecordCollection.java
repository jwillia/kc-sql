package com.rsmart.kuali.coeus.hr.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Part of the HRManifest object graph that is created when the HR import XML
 * file is parsed.
 * 
 * See {@link com.rsmart.kuali.coeus.hr.rest.model.HRManifest HRManifest} for more details.
 * @author duffy
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "records")
public class HRManifestRecordCollection {

  @XmlElement(name = "record", type = HRManifestRecord.class)
  @Valid
  private List<HRManifestRecord> records = new ArrayList<HRManifestRecord>();

  public HRManifestRecordCollection() {
  }

  public HRManifestRecordCollection(List<HRManifestRecord> records) {
    this.records = records;
  }

  public List<HRManifestRecord> getRecords() {
    return records;
  }

  public void setRecords(List<HRManifestRecord> records) {
    this.records = records;
  }

}
