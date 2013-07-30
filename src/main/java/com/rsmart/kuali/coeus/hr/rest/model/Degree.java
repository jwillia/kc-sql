package com.rsmart.kuali.coeus.hr.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "degree")
public class Degree {

  @XmlAttribute
  protected String degree;
  @XmlAttribute
  protected String degreeType;
  @XmlAttribute
  protected String graduationYear;
  @XmlAttribute
  protected String fieldOfStudy;
  @XmlAttribute
  protected String specialization;
  @XmlAttribute
  protected String school;
  @XmlAttribute
  protected String schoolId;
  @XmlAttribute
  protected String schoolIdCode;

  public String getDegree() {
    return degree;
  }
  public void setDegree(String degree) {
    this.degree = degree;
  }
  public String getDegreeType() {
    return degreeType;
  }
  public void setDegreeType(String degreeType) {
    this.degreeType = degreeType;
  }
  public String getGraduationYear() {
    return graduationYear;
  }
  public void setGraduationYear(String graduationYear) {
    this.graduationYear = graduationYear;
  }
  public String getFieldOfStudy() {
    return fieldOfStudy;
  }
  public void setFieldOfStudy(String fieldOfStudy) {
    this.fieldOfStudy = fieldOfStudy;
  }
  public String getSpecialization() {
    return specialization;
  }
  public void setSpecialization(String specialization) {
    this.specialization = specialization;
  }
  public String getSchool() {
    return school;
  }
  public void setSchool(String school) {
    this.school = school;
  }
  public String getSchoolId() {
    return schoolId;
  }
  public void setSchoolId(String schoolId) {
    this.schoolId = schoolId;
  }
  public String getSchoolIdCode() {
    return schoolIdCode;
  }
  public void setSchoolIdCode(String schoolIdCode) {
    this.schoolIdCode = schoolIdCode;
  }

}
