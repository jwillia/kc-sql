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
 * Part of the HRManifest object graph that is created when the HR import XML
 * file is parsed.
 * 
 * See {@link com.rsmart.kuali.coeus.hr.rest.model.HRManifest HRManifest} for more details.
 * @author duffy
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "names")
public class NameCollection {

  @XmlElement(name = "name", type = Name.class)
  @Size(min = 1)
  @Valid
  protected List<Name> names = new ArrayList<Name>();

  public NameCollection() {
  }

  public NameCollection(List<Name> names) {
    this.names = names;
  }

  public List<Name> getNames() {
    return names;
  }

  public void setNames(List<Name> names) {
    this.names = names;
  }
}
