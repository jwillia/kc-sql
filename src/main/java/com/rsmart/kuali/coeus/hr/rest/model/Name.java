package com.rsmart.kuali.coeus.hr.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
@XmlRootElement(name = "name")
public class Name extends ModelObject {

  @XmlAttribute
  protected String nameCode;
  @XmlAttribute
  protected String prefix;
  @XmlAttribute
  protected String firstName;
  @XmlAttribute
  protected String middleName;
  @XmlAttribute
  protected String lastName;
  @XmlAttribute
  protected String suffix;
  @XmlAttribute
  protected String title;
  @XmlAttribute(name = "default")
  protected boolean isDefault;
  @XmlAttribute
  protected boolean active;

  public String getNameCode() {
    return nameCode;
  }

  public void setNameCode(String nameCode) {
    this.nameCode=trimToNull(nameCode);
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix=trimToNull(prefix);
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName=trimToNull(firstName);
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName=trimToNull(middleName);
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName=trimToNull(lastName);
  }

  public String getSuffix() {
    return suffix;
  }

  public void setSuffix(String suffix) {
    this.suffix=trimToNull(suffix);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title=trimToNull(title);
  }

  public boolean isDefault() {
    return isDefault;
  }

  public void setDefault(boolean isDefault) {
    this.isDefault = isDefault;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
