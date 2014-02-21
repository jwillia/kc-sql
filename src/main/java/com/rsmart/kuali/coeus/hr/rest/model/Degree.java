package com.rsmart.kuali.coeus.hr.rest.model;

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
@XmlRootElement(name = "degree")
public class Degree {

  @XmlAttribute
  protected String degreeCode;
  @XmlAttribute
  protected String degree;
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

  public String getDegreeCode() {
    return degreeCode;
  }

  public void setDegreeCode(String degreeCode) {
    this.degreeCode = degreeCode;
  }

  public String getDegree() {
    return degree;
  }

  public void setDegree(String degree) {
    this.degree = degree;
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
