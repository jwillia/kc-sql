package com.rsmart.kuali.coeus.hr.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "name")
public class Name {

  @XmlAttribute
  protected String nameCode;
  @XmlAttribute
	protected String prefix;
  @XmlAttribute
	protected String firstName;
  @XmlAttribute
	protected String lastName;
  @XmlAttribute
	protected String suffix;
  @XmlAttribute (name="default")
	protected boolean isDefault;
  @XmlAttribute
	protected boolean active;
	
  public String getNameCode() {
    return nameCode;
  }
  public void setNameCode(String nameCode) {
    this.nameCode = nameCode;
  }
  public String getPrefix() {
    return prefix;
  }
  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }
  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  public String getSuffix() {
    return suffix;
  }
  public void setSuffix(String suffix) {
    this.suffix = suffix;
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
