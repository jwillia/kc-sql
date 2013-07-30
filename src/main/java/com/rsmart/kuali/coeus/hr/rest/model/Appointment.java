package com.rsmart.kuali.coeus.hr.rest.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "appointment")
public class Appointment {

  @XmlAttribute
  protected String unit;
  @XmlAttribute
  protected String jobCode;
  @XmlAttribute
  protected String appointmentType;
  @XmlAttribute
  protected float salary;
  @XmlAttribute
  protected Date startDate;
  @XmlAttribute
  protected Date endDate;
  @XmlAttribute
  protected String jobTitle;
  @XmlAttribute
  protected String preferedJobTitle;

  public String getUnit() {
    return unit;
  }
  public void setUnit(String unit) {
    this.unit = unit;
  }
  public String getJobCode() {
    return jobCode;
  }
  public void setJobCode(String jobCode) {
    this.jobCode = jobCode;
  }
  public String getAppointmentType() {
    return appointmentType;
  }
  public void setAppointmentType(String appointmentType) {
    this.appointmentType = appointmentType;
  }
  public float getSalary() {
    return salary;
  }
  public void setSalary(float salary) {
    this.salary = salary;
  }
  public Date getStartDate() {
    return startDate;
  }
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  public Date getEndDate() {
    return endDate;
  }
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
  public String getJobTitle() {
    return jobTitle;
  }
  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }
  public String getPreferedJobTitle() {
    return preferedJobTitle;
  }
  public void setPreferedJobTitle(String preferedJobTitle) {
    this.preferedJobTitle = preferedJobTitle;
  }
}
