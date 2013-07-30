package com.rsmart.kuali.coeus.hr.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "degrees")
public class Degrees {

  @XmlElement(name="degree", type = Degree.class)
  protected List<Degree> degrees = new ArrayList<Degree>();
  
  public Degrees() {}
  
  public Degrees(List<Degree> degrees) {
    this.degrees = degrees;
  }

  public List<Degree> getDegrees() {
    return degrees;
  }

  public void setDegrees(List<Degree> degrees) {
    this.degrees = degrees;
  }
}
