package com.rsmart.kuali.coeus.hr.service.adapter.impl;

import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;

import com.rsmart.kuali.coeus.hr.rest.model.Name;
import com.rsmart.kuali.coeus.hr.service.adapter.PersistableBoMergeAdapter;

public class EntityNameBoAdapter extends PersistableBoMergeAdapter<EntityNameBo, Name> {

  @Override
  public int compareBOProperties(final EntityNameBo bo0, final EntityNameBo bo1) {
    int comp = 0;
    
    comp = nullSafeCompare(bo0.getNameCode(),bo1.getNameCode());
    if (comp != 0) {
      return comp;
    }
    
    comp = nullSafeCompareIgnoreCase(bo0.getLastName(),bo1.getLastName());
    if (comp != 0) {
      return comp;
    }
    
    comp = nullSafeCompareIgnoreCase(bo0.getFirstName(),bo1.getFirstName());
    if (comp != 0) {
      return comp;
    }
    
    comp = nullSafeCompareIgnoreCase(bo0.getMiddleName(),bo1.getMiddleName());
    if (comp != 0) {
      return comp;
    }
    
    comp = nullSafeCompareIgnoreCase(bo0.getNameSuffix(), bo1.getNameSuffix());
    if (comp != 0) {
      return comp;
    }
    
    return 0;
  }
  
  @Override
  public final EntityNameBo newBO(final String entityId) {
    final EntityNameBo nameBo = new EntityNameBo();
    nameBo.setEntityId(entityId);
    
    return nameBo;
  }

  @Override
  public final EntityNameBo setFields(final EntityNameBo nameBo, final Name name) {
    nameBo.setFirstName(name.getFirstName());
    nameBo.setMiddleName(name.getMiddleName());
    nameBo.setLastName(name.getLastName());
    nameBo.setNameSuffix(name.getSuffix());
    nameBo.setActive(name.isActive());
    nameBo.setDefaultValue(name.isDefault());
    nameBo.setNameCode(name.getNameCode());
    nameBo.setNamePrefix(name.getPrefix());
    nameBo.setNameTitle(name.getTitle());

    return nameBo;
  }

  @Override
  public final Class<EntityNameBo> getBusinessObjectClass() {
    return EntityNameBo.class;
  }

  @Override
  public final Class<Name> getIncomingClass() {
    return Name.class;
  }
}
