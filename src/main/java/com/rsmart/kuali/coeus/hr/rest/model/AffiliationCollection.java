package com.rsmart.kuali.coeus.hr.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;
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
@XmlRootElement(name = "affiliations")
public class AffiliationCollection {

  @XmlElement(name = "affiliation", type = Affiliation.class)
  @Size(min = 1)
  @Valid
  protected List<Affiliation> affiliations = new ArrayList<Affiliation>();

  public AffiliationCollection() {
  }

  public AffiliationCollection(List<Affiliation> affiliations) {
    this.affiliations = affiliations;
  }

  public List<Affiliation> getAffiliations() {
    return affiliations;
  }

  public void setAffiliations(List<Affiliation> affiliations) {
    this.affiliations = affiliations;
  }
}