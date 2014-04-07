package com.rsmart.kuali.coeus.hr.rest.model;

import java.util.Date;

import javax.validation.constraints.NotNull;
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
@XmlRootElement(name = "appointment")
public class Appointment extends ModelObject {

  @XmlAttribute
  @NotNull
  protected String unitNumber;
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

  public String getUnitNumber() {
    return unitNumber;
  }

  public void setUnitNumber(String unitNumber) {
    this.unitNumber=trimToNull(unitNumber);
  }

  public String getJobCode() {
    return jobCode;
  }

  public void setJobCode(String jobCode) {
    this.jobCode=trimToNull(jobCode);
  }

  public String getAppointmentType() {
    return appointmentType;
  }

  public void setAppointmentType(String appointmentType) {
    this.appointmentType=trimToNull(appointmentType);
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
    this.jobTitle=trimToNull(jobTitle);
  }

  public String getPreferedJobTitle() {
    return preferedJobTitle;
  }

  public void setPreferedJobTitle(String preferedJobTitle) {
    this.preferedJobTitle=trimToNull(preferedJobTitle);
  }
}
