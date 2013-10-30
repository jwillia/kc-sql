package com.rsmart.kuali.coeus.hr.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "affiliations")
public class Affiliations {

  @XmlElement(name = "affiliation", type = Affiliation.class)
  protected List<Affiliation> affiliations = new ArrayList<Affiliation>();

  public Affiliations() {
  }

  public Affiliations(List<Affiliation> affiliations) {
    this.affiliations = affiliations;
  }

  public List<Affiliation> getAffiliations() {
    return affiliations;
  }

  public void setAffiliations(List<Affiliation> affiliations) {
    this.affiliations = affiliations;
  }
}
