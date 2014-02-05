package com.rsmart.kuali.coeus.hr.service.adapter.impl;

import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;
import org.kuali.rice.krad.service.BusinessObjectService;

import com.rsmart.kuali.coeus.hr.rest.model.Affiliation;
import com.rsmart.kuali.coeus.hr.rest.model.Employment;
import com.rsmart.kuali.coeus.hr.service.adapter.PersistableBoMergeAdapter;

public class EntityEmploymentBoAdapter extends
    PersistableBoMergeAdapter<EntityEmploymentBo, Affiliation> {

  protected int compareAppointment(final EntityAffiliationBo aff0, final EntityAffiliationBo aff1) {
    int comp = 0;
    
    comp = nullSafeCompare(aff0.getAffiliationTypeCode(),aff1.getAffiliationTypeCode());
    if (comp != 0) {
      return comp;
    }
    
    return nullSafeCompare(aff0.getCampusCode(),aff1.getCampusCode());
  }
  
  @Override
  public int compareBOProperties(EntityEmploymentBo emp0, EntityEmploymentBo emp1) {
    int comp = 0;
    
    final int emp0Primary = emp0.isPrimary() ? 1 : 0;
    final int emp1Primary = emp1.isPrimary() ? 1 : 0;
    
    comp = emp1Primary - emp0Primary;
    if (comp != 0) {
      return comp;
    }
    
    comp = nullSafeCompare(emp0.getEmployeeId(), emp1.getEmployeeId());
    if (comp != 0) {
      return comp;
    }
    
    comp = nullSafeCompare(emp0.getEmployeeStatusCode(), emp1.getEmployeeStatusCode());
    if (comp != 0) {
      return comp;
    }
    
    comp = nullSafeCompare(emp0.getEmployeeTypeCode(), emp1.getEmployeeTypeCode());
    if (comp != 0) {
      return comp;
    }
    
    comp = nullSafeCompare(emp0.getPrimaryDepartmentCode(), emp1.getPrimaryDepartmentCode());
    if (comp != 0) {
      return comp;
    }
    
    final KualiDecimal emp0Base = emp0.getBaseSalaryAmount();
    final KualiDecimal emp1Base = emp1.getBaseSalaryAmount();
    
    if (emp0Base == null && emp1Base != null) {
      return -1;
    } else if (emp0Base != null && emp1Base == null) {
      return 1;
    } else if (emp0Base != null && emp1Base != null) {
      comp = emp0.getBaseSalaryAmount().compareTo(emp1.getBaseSalaryAmount());
    } else {
      comp = 0;
    }
    if (comp != 0) {
      return comp;
    }
    
    final EntityAffiliationBo aff0 = emp0.getEntityAffiliation();
    final EntityAffiliationBo aff1 = emp0.getEntityAffiliation();
    
    comp = compareAppointment(aff0, aff1);
    
    return comp;
  }

  @Override
  public EntityEmploymentBo newBO(String entityId) {
    final EntityEmploymentBo empl = new EntityEmploymentBo();
    final EntityAffiliationBo aff = new EntityAffiliationBo();
    
    empl.setEntityId(entityId);
    empl.setEntityAffiliation(aff);
    
    return empl;
  }

  @Override
  public Class<EntityEmploymentBo> getBusinessObjectClass() {
    return EntityEmploymentBo.class;
  }

  @Override
  public Class<Affiliation> getIncomingClass() {
    return Affiliation.class;
  }

  @Override
  public EntityEmploymentBo setFields(EntityEmploymentBo bo, Affiliation affiliation) {
    final Employment source = affiliation.getEmployment();
    
    bo.setActive(affiliation.isActive());
    bo.setBaseSalaryAmount(new KualiDecimal(source.getBaseSalaryAmount()));
    bo.setEmployeeId(source.getEmployeeId());
    bo.setEmployeeStatusCode(source.getEmployeeStatus());
    bo.setEmployeeTypeCode(source.getEmployeeType());
    bo.setPrimary(source.isPrimaryEmployment());
    bo.setPrimaryDepartmentCode(source.getPrimaryDepartment());
    
    final EntityAffiliationBo aff = bo.getEntityAffiliation();
    aff.setActive(affiliation.isActive());
    aff.setAffiliationTypeCode(affiliation.getAffiliationType());
    aff.setCampusCode(affiliation.getCampus());
    aff.setEntityId(bo.getEntityId());
    aff.setDefaultValue(affiliation.isDefault());
    
    return bo;
  }
  
  @Override
  public void save(final BusinessObjectService boService, EntityEmploymentBo bo) {
    final EntityAffiliationBo affiliation = bo.getEntityAffiliation();
    
    boService.save(affiliation);
    bo.setEntityAffiliationId(affiliation.getId());
    boService.save(bo);
  }

  @Override
  public void delete(final BusinessObjectService boService, EntityEmploymentBo bo) {
    final EntityAffiliationBo affiliation = bo.getEntityAffiliation();
    
    boService.delete(bo);
    boService.delete(affiliation);
  }

}
