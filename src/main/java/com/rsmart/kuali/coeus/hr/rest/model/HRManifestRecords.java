package com.rsmart.kuali.coeus.hr.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "records")
public class HRManifestRecords {

  @XmlElement(name = "record", type = HRManifestRecord.class)
  private List<HRManifestRecord> records = new ArrayList<HRManifestRecord>();

  public HRManifestRecords() {}

  public HRManifestRecords(List<HRManifestRecord> records) {
    this.records = records;
  }
  
  public List<HRManifestRecord> getRecords() {
    return records;
  }

  public void setRecords(List<HRManifestRecord> records) {
    this.records = records;
  }
    
}
