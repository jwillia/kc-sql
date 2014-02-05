package com.rsmart.kuali.coeus.hr.service.adapter.impl;

import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;

import com.rsmart.kuali.coeus.hr.rest.model.Phone;
import com.rsmart.kuali.coeus.hr.service.adapter.PersistableBoMergeAdapter;

public class EntityPhoneBoAdapter extends PersistableBoMergeAdapter<EntityPhoneBo, Phone> {

  @Override
  public int compareBOProperties(EntityPhoneBo phone0, EntityPhoneBo phone1) {
    int comp = 0;
    
    comp = nullSafeCompare(phone0.getPhoneTypeCode(), phone1.getPhoneTypeCode());
    if (comp != 0) {
      return comp;
    }
    
    comp = nullSafeCompare(phone0.getCountryCode(),phone1.getCountryCode());
    if (comp != 0) {
      return comp;
    }
    
    comp = nullSafeCompare(phone0.getPhoneNumber(),phone1.getPhoneNumber());
    if (comp != 0) {
      return comp;
    }
    
    comp = nullSafeCompare(phone0.getExtensionNumber(),phone1.getExtensionNumber());
    if (comp != 0) {
      return comp;
    }

    return 0;
  }

  @Override
  public final EntityPhoneBo newBO(final String entityId) {
    final EntityPhoneBo bo = new EntityPhoneBo();
    bo.setEntityId(entityId);
    
    return bo;
  }

  @Override
  public final EntityPhoneBo setFields(final EntityPhoneBo bo, final Phone source) {
    bo.setActive(source.isActive());
    bo.setCountryCode(source.getCountry());
    bo.setDefaultValue(source.isDefault());
    bo.setExtensionNumber(source.getExtension());
    bo.setPhoneNumber(source.getPhoneNumber());
    bo.setPhoneTypeCode(source.getPhoneType());
    return bo;
  }

  @Override
  public final Class<EntityPhoneBo> getBusinessObjectClass() {
    return EntityPhoneBo.class;
  }

  @Override
  public final Class<Phone> getIncomingClass() {
    return Phone.class;
  }
}
