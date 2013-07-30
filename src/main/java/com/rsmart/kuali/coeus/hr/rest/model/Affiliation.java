package com.rsmart.kuali.coeus.hr.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "affiliation")
public class Affiliation {
  
  @XmlAttribute
  protected String      affiliationType     = null;
  @XmlAttribute
  protected String      campus              = null;
  @XmlAttribute
  protected String      employeeStatus      = null;
  @XmlAttribute
  protected String      employeeType        = null;
  @XmlAttribute
  protected float       baseSalaryAmount    = 0.0f;
  @XmlAttribute
  protected String      primaryDepartment   = null;
  @XmlAttribute
  protected String      employeeId          = null;
  @XmlAttribute
  protected boolean     primaryEmployment   = false;
  @XmlAttribute(name="default")
  protected boolean     isDefault           = false;
  @XmlAttribute
  protected boolean     active              = false;
  
  public String getAffiliationType() {
    return affiliationType;
  }
  public void setAffiliationType(String affiliationType) {
    this.affiliationType = affiliationType;
  }
  public String getCampus() {
    return campus;
  }
  public void setCampus(String campus) {
    this.campus = campus;
  }
  public String getEmployeeStatus() {
    return employeeStatus;
  }
  public void setEmployeeStatus(String employeeStatus) {
    this.employeeStatus = employeeStatus;
  }
  public String getEmployeeType() {
    return employeeType;
  }
  public void setEmployeeType(String employeeType) {
    this.employeeType = employeeType;
  }
  public float getBaseSalaryAmount() {
    return baseSalaryAmount;
  }
  public void setBaseSalaryAmount(float baseSalaryAmount) {
    this.baseSalaryAmount = baseSalaryAmount;
  }
  public String getPrimaryDepartment() {
    return primaryDepartment;
  }
  public void setPrimaryDepartment(String primaryDepartment) {
    this.primaryDepartment = primaryDepartment;
  }
  public String getEmployeeId() {
    return employeeId;
  }
  public void setEmployeeId(String employeeId) {
    this.employeeId = employeeId;
  }
  public boolean isPrimaryEmployment() {
    return primaryEmployment;
  }
  public void setPrimaryEmployment(boolean primaryEmployment) {
    this.primaryEmployment = primaryEmployment;
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