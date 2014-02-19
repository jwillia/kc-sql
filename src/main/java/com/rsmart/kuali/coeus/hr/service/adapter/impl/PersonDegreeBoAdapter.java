package com.rsmart.kuali.coeus.hr.service.adapter.impl;

import org.kuali.kra.bo.PersonDegree;

import com.rsmart.kuali.coeus.hr.rest.model.Degree;
import com.rsmart.kuali.coeus.hr.service.adapter.PersistableBoMergeAdapter;

public class PersonDegreeBoAdapter extends PersistableBoMergeAdapter<PersonDegree, Degree> {

  @Override
  public int compareBOProperties(PersonDegree bo0, PersonDegree bo1) {
    int comp = 0;
    
    comp = nullSafeCompare(bo0.getDegree(), bo1.getDegree());
    if (comp != 0) {
      return comp;
    }
    
    comp = nullSafeCompare(bo0.getDegreeCode(), bo1.getDegreeCode());
    if (comp != 0) {
      return comp;
    }
    
    comp = nullSafeCompare(bo0.getFieldOfStudy(), bo1.getFieldOfStudy());
    if (comp != 0) {
      return comp;
    }
    
    comp = nullSafeCompare(bo0.getGraduationYear(), bo1.getGraduationYear());
    if (comp != 0) {
      return comp;
    }
    
    comp = nullSafeCompare(bo0.getSchool(), bo1.getSchool());
    if (comp != 0) {
      return comp;
    }
    
    comp = nullSafeCompare(bo0.getSchoolId(), bo1.getSchoolId());
    if (comp != 0) {
      return comp;
    }
    
    comp = nullSafeCompare(bo0.getSchoolIdCode(), bo1.getSchoolIdCode());
    if (comp != 0) {
      return comp;
    }
    
    return nullSafeCompare(bo0.getSpecialization(), bo1.getSpecialization());
  }

  @Override
  public PersonDegree newBO(String entityId) {
    final PersonDegree degree = new PersonDegree();
    
    degree.setPersonId(entityId);
    return degree;
  }

  @Override
  public PersonDegree setFields(PersonDegree bo, Degree source) {
    bo.setDegree(source.getDegree());
    bo.setDegreeCode(source.getDegreeCode());
    bo.setFieldOfStudy(source.getFieldOfStudy());
    bo.setGraduationYear(source.getGraduationYear());
    bo.setSchool(source.getSchool());
    bo.setSchoolId(source.getSchoolId());
    bo.setSchoolIdCode(source.getSchoolIdCode());
    bo.setSpecialization(source.getSpecialization());

    return bo;
  }

  @Override
  public Class<PersonDegree> getBusinessObjectClass() {
    return PersonDegree.class;
  }

  @Override
  public Class<Degree> getIncomingClass() {
    return Degree.class;
  }

}
