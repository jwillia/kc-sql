package com.rsmart.kuali.coeus.hr.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "phones")
public class Phones {

  @XmlElement(name="phone", type = Phone.class)
  protected List<Phone> phones = new ArrayList<Phone>();

  public Phones() {}
  
  public Phones(List<Phone> phones) {
    this.phones = phones;
  }
  
  public List<Phone> getPhones() {
    return phones;
  }

  public void setPhones(List<Phone> phones) {
    this.phones = phones;
  }
}
