package com.rsmart.kuali.coeus.hr.rest.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "kcExtendedAttributes")
public class KCExtendedAttributes {

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
    this.county = county;
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
    this.race = race;
  }

  public String getEducationLevel() {
    return educationLevel;
  }

  public void setEducationLevel(String educationLevel) {
    this.educationLevel = educationLevel;
  }

  public String getDegree() {
    return degree;
  }

  public void setDegree(String degree) {
    this.degree = degree;
  }

  public String getMajor() {
    return major;
  }

  public void setMajor(String major) {
    this.major = major;
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
    this.handicapType = handicapType;
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
    this.veteranType = veteranType;
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
    this.visaType = visaType;
  }

  public String getVisaCode() {
    return visaCode;
  }

  public void setVisaCode(String visaCode) {
    this.visaCode = visaCode;
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
    this.officeLocation = officeLocation;
  }

  public String getSecondaryOfficeLocation() {
    return secondaryOfficeLocation;
  }

  public void setSecondaryOfficeLocation(String secondaryOfficeLocation) {
    this.secondaryOfficeLocation = secondaryOfficeLocation;
  }

  public String getSchool() {
    return school;
  }

  public void setSchool(String school) {
    this.school = school;
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
    this.directoryDepartment = directoryDepartment;
  }

  public String getDirectoryTitle() {
    return directoryTitle;
  }

  public void setDirectoryTitle(String directoryTitle) {
    this.directoryTitle = directoryTitle;
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
    this.idProvided = idProvided;
  }

  public String getIdVerified() {
    return idVerified;
  }

  public void setIdVerified(String idVerified) {
    this.idVerified = idVerified;
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
    this.multiCampusPrincipalId = multiCampusPrincipalId;
  }

  public String getMultiCampusPrincipalName() {
    return multiCampusPrincipalName;
  }

  public void setMultiCampusPrincipalName(String multiCampusPrincipalName) {
    this.multiCampusPrincipalName = multiCampusPrincipalName;
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
    this.primaryTitle = primaryTitle;
  }
}
