package com.rsmart.kuali.coeus.hr.rest.model;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "employment")
public class Employment {

  @XmlAttribute
  protected float baseSalaryAmount = 0.0f;
  @XmlAttribute
  protected String employeeId = null;
  @XmlAttribute
  protected String employeeStatus = null;
  @XmlAttribute
  protected String employeeType = null;
  @XmlElement(name = "employment", type = Employment.class)
  @Valid
  protected Employment employment = null;
  @XmlAttribute
  protected String primaryDepartment = null;
  @XmlAttribute
  protected boolean primaryEmployment = false;
  
  public float getBaseSalaryAmount() {
    return baseSalaryAmount;
  }
  public String getEmployeeId() {
    return employeeId;
  }
  public String getEmployeeStatus() {
    return employeeStatus;
  }
  public String getEmployeeType() {
    return employeeType;
  }
  public String getPrimaryDepartment() {
    return primaryDepartment;
  }
  public boolean isPrimaryEmployment() {
    return primaryEmployment;
  }
  public void setBaseSalaryAmount(float baseSalaryAmount) {
    this.baseSalaryAmount = baseSalaryAmount;
  }
  public void setEmployeeId(String employeeId) {
    this.employeeId = employeeId;
  }
  public void setEmployeeStatus(String employeeStatus) {
    this.employeeStatus = employeeStatus;
  }
  public void setEmployeeType(String employeeType) {
    this.employeeType = employeeType;
  }
  public void setPrimaryDepartment(String primaryDepartment) {
    this.primaryDepartment = primaryDepartment;
  }
  public void setPrimaryEmployment(boolean primaryEmployment) {
    this.primaryEmployment = primaryEmployment;
  }

}
