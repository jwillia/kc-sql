package com.rsmart.kuali.coeus.hr.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "names")
public class Names {

  @XmlElement(name = "name", type = Name.class)
  protected List<Name> names = new ArrayList<Name>();

  public Names() {
  }

  public Names(List<Name> names) {
    this.names = names;
  }

  public List<Name> getNames() {
    return names;
  }

  public void setNames(List<Name> names) {
    this.names = names;
  }
}
