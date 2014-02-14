package com.rsmart.kuali.coeus.hr.service.adapter.impl;

import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;

import com.rsmart.kuali.coeus.hr.rest.model.Email;
import com.rsmart.kuali.coeus.hr.service.adapter.PersistableBoMergeAdapter;

/**
 * Implements logic necessary for merging {@link com.rsmart.kuali.coeus.hr.rest.model.Email Email}
 * objects from import into the list of {@link org.kuali.rice.kim.impl.identity.email.EntityEmailBo EntityEmailBo}
 * objects already attached to an Entity.
 * 
 * @author duffy
 *
 */
public class EntityEmailBoAdapter extends PersistableBoMergeAdapter<EntityEmailBo, Email> {

  @Override
  public int compareBOProperties(EntityEmailBo email0, EntityEmailBo email1) {
    int comp = 0;
    
    comp = nullSafeCompare(email0.getEmailTypeCode(), email1.getEmailTypeCode());
    if (comp != 0) {
      return comp;
    }
    
    return nullSafeCompareIgnoreCase(email0.getEmailAddress(), email1.getEmailAddress());
  }
  
  @Override
  public final EntityEmailBo newBO(final String entityId) {
    final EntityEmailBo bo = new EntityEmailBo();
    bo.setEntityId(entityId);
    return bo;
  }

  @Override
  public final EntityEmailBo setFields(final EntityEmailBo bo, final Email source) {
    bo.setActive(source.isActive());
    bo.setDefaultValue(source.isDefault());
    bo.setEmailAddress(source.getEmailAddress());
    bo.setEmailTypeCode(source.getEmailType());
    return bo;
  }

  @Override
  public final Class<EntityEmailBo> getBusinessObjectClass() {
    return EntityEmailBo.class;
  }

  @Override
  public final Class<Email> getIncomingClass() {
    return Email.class;
  }
}
