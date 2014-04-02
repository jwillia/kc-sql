package com.rsmart.kuali.coeus.hr.rest.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
@XmlRootElement(name = "kcExtendedAttributes")
public class KCExtendedAttributes extends ModelObject {

  @XmlAttribute
  protected String county;
  @XmlAttribute
  protected int ageByFiscalYear;
  @XmlAttribute
  protected String race;
  @XmlAttribute
  protected String educationLevel;
  @XmlAttribute
  protected String degree;
  @XmlAttribute
  protected String major;
  @XmlAttribute
  protected boolean handicapped;
  @XmlAttribute
  protected String handicapType;
  @XmlAttribute
  protected boolean veteran;
  @XmlAttribute
  protected String veteranType;
  @XmlAttribute
  protected boolean visa;
  @XmlAttribute
  protected String visaType;
  @XmlAttribute
  protected String visaCode;
  @XmlAttribute
  protected Date visaRenewalDate;
  @XmlAttribute
  protected String officeLocation;
  @XmlAttribute
  protected String secondaryOfficeLocation;
  @XmlAttribute
  protected String school;
  @XmlAttribute
  protected Integer yearGraduated;
  @XmlAttribute
  protected String directoryDepartment;
  @XmlAttribute
  protected String directoryTitle;
  @XmlAttribute
  protected String primaryTitle;
  @XmlAttribute
  protected boolean vacationAccrual;
  @XmlAttribute
  protected boolean onSabbatical;
  @XmlAttribute
  protected String idProvided;
  @XmlAttribute
  protected String idVerified;
  @XmlAttribute
  protected Integer citizenshipType;
  @XmlAttribute
  protected String multiCampusPrincipalId;
  @XmlAttribute
  protected String multiCampusPrincipalName;
  @XmlAttribute
  protected Date salaryAnniversaryDate;

  public String getCounty() {
    return county;
  }

  public void setCounty(String county) {
    this.county=trimToNull(county);
  }

  public int getAgeByFiscalYear() {
    return ageByFiscalYear;
  }

  public void setAgeByFiscalYear(int ageByFiscalYear) {
    this.ageByFiscalYear = ageByFiscalYear;
  }

  public String getRace() {
    return race;
  }

  public void setRace(String race) {
    this.race=trimToNull(race);
  }

  public String getEducationLevel() {
    return educationLevel;
  }

  public void setEducationLevel(String educationLevel) {
    this.educationLevel=trimToNull(educationLevel);
  }

  public String getDegree() {
    return degree;
  }

  public void setDegree(String degree) {
    this.degree=trimToNull(degree);
  }

  public String getMajor() {
    return major;
  }

  public void setMajor(String major) {
    this.major=trimToNull(major);
  }

  public boolean isHandicapped() {
    return handicapped;
  }

  public void setHandicapped(boolean handicapped) {
    this.handicapped = handicapped;
  }

  public String getHandicapType() {
    return handicapType;
  }

  public void setHandicapType(String handicapType) {
    this.handicapType=trimToNull(handicapType);
  }

  public boolean isVeteran() {
    return veteran;
  }

  public void setVeteran(boolean veteran) {
    this.veteran = veteran;
  }

  public String getVeteranType() {
    return veteranType;
  }

  public void setVeteranType(String veteranType) {
    this.veteranType=trimToNull(veteranType);
  }

  public boolean isVisa() {
    return visa;
  }

  public void setVisa(boolean visa) {
    this.visa = visa;
  }

  public String getVisaType() {
    return visaType;
  }

  public void setVisaType(String visaType) {
    this.visaType=trimToNull(visaType);
  }

  public String getVisaCode() {
    return visaCode;
  }

  public void setVisaCode(String visaCode) {
    this.visaCode=trimToNull(visaCode);
  }

  public Date getVisaRenewalDate() {
    return visaRenewalDate;
  }

  public void setVisaRenewalDate(Date visaRenewalDate) {
    this.visaRenewalDate = visaRenewalDate;
  }

  public String getOfficeLocation() {
    return officeLocation;
  }

  public void setOfficeLocation(String officeLocation) {
    this.officeLocation=trimToNull(officeLocation);
  }

  public String getSecondaryOfficeLocation() {
    return secondaryOfficeLocation;
  }

  public void setSecondaryOfficeLocation(String secondaryOfficeLocation) {
    this.secondaryOfficeLocation=trimToNull(secondaryOfficeLocation);
  }

  public String getSchool() {
    return school;
  }

  public void setSchool(String school) {
    this.school=trimToNull(school);
  }

  public Integer getYearGraduated() {
    return yearGraduated;
  }

  public void setYearGraduated(Integer yearGraduated) {
    this.yearGraduated = yearGraduated;
  }

  public String getDirectoryDepartment() {
    return directoryDepartment;
  }

  public void setDirectoryDepartment(String directoryDepartment) {
    this.directoryDepartment=trimToNull(directoryDepartment);
  }

  public String getDirectoryTitle() {
    return directoryTitle;
  }

  public void setDirectoryTitle(String directoryTitle) {
    this.directoryTitle=trimToNull(directoryTitle);
  }

  public boolean isVacationAccrual() {
    return vacationAccrual;
  }

  public void setVacationAccrual(boolean vacationAccrual) {
    this.vacationAccrual = vacationAccrual;
  }

  public boolean isOnSabbatical() {
    return onSabbatical;
  }

  public void setOnSabbatical(boolean onSabbatical) {
    this.onSabbatical = onSabbatical;
  }

  public String getIdProvided() {
    return idProvided;
  }

  public void setIdProvided(String idProvided) {
    this.idProvided=trimToNull(idProvided);
  }

  public String getIdVerified() {
    return idVerified;
  }

  public void setIdVerified(String idVerified) {
    this.idVerified=trimToNull(idVerified);
  }

  public Integer getCitizenshipType() {
    return citizenshipType;
  }

  public void setCitizenshipType(Integer citizenshipType) {
    this.citizenshipType = citizenshipType;
  }

  public String getMultiCampusPrincipalId() {
    return multiCampusPrincipalId;
  }

  public void setMultiCampusPrincipalId(String multiCampusPrincipalId) {
    this.multiCampusPrincipalId=trimToNull(multiCampusPrincipalId);
  }

  public String getMultiCampusPrincipalName() {
    return multiCampusPrincipalName;
  }

  public void setMultiCampusPrincipalName(String multiCampusPrincipalName) {
    this.multiCampusPrincipalName=trimToNull(multiCampusPrincipalName);
  }

  public Date getSalaryAnniversaryDate() {
    return salaryAnniversaryDate;
  }

  public void setSalaryAnniversaryDate(Date salaryAnniversaryDate) {
    this.salaryAnniversaryDate = salaryAnniversaryDate;
  }

  public String getPrimaryTitle() {
    return primaryTitle;
  }

  public void setPrimaryTitle(String primaryTitle) {
    this.primaryTitle=trimToNull(primaryTitle);
  }
}
