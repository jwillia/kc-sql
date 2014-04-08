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
 * See {@link com.rsmart.kuali.coeus.hr.rest.model.DOMHRImport HRImport} for more details.
 * @author duffy
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "degrees")
public class DegreeCollection {

  @XmlElement(name = "degree", type = Degree.class)
  @Valid
  protected List<Degree> degrees = new ArrayList<Degree>();

  public DegreeCollection() {
  }

  public DegreeCollection(List<Degree> degrees) {
    this.degrees = degrees;
  }

  public List<Degree> getDegrees() {
    return degrees;
  }

  public void setDegrees(List<Degree> degrees) {
    this.degrees = degrees;
  }
}
