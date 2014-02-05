package com.rsmart.kuali.coeus.hr.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "phones")
public class PhoneCollection {

  @XmlElement(name = "phone", type = Phone.class)
  @Valid
  protected List<Phone> phones = new ArrayList<Phone>();

  public PhoneCollection() {
  }

  public PhoneCollection(List<Phone> phones) {
    this.phones = phones;
  }

  public List<Phone> getPhones() {
    return phones;
  }

  public void setPhones(List<Phone> phones) {
    this.phones = phones;
  }
}
