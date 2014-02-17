package com.rsmart.kuali.coeus.hr.service.adapter.impl;

import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo;

import com.rsmart.kuali.coeus.hr.rest.model.Affiliation;
import com.rsmart.kuali.coeus.hr.service.adapter.PersistableBoMergeAdapter;

/**
 * Implements logic necessary for merging {@link com.rsmart.kuali.coeus.hr.rest.model.Affiliation Affiliation}
 * objects from import into the list of {@link org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo EntityAffiliationBo}
 * objects already attached to an Entity.
 * 
 * @author duffy
 *
 */
public class EntityAffiliationBoAdapter extends PersistableBoMergeAdapter<EntityAffiliationBo, Affiliation> {

  @Override
  public int compareBOProperties(final EntityAffiliationBo aff0, final EntityAffiliationBo aff1) {

    int comp = 0;
    
    comp = nullSafeCompare(aff0.getAffiliationTypeCode(),aff1.getAffiliationTypeCode());
    if (comp != 0) {
      return comp;
    }
    
    return nullSafeCompare(aff0.getCampusCode(),aff1.getCampusCode());
  }
  
  @Override
  public final EntityAffiliationBo newBO(final String entityId) {
    final EntityAffiliationBo bo = new EntityAffiliationBo();
    
    bo.setEntityId(entityId);
    
    return bo;
  }

  @Override
  public final EntityAffiliationBo setFields(final EntityAffiliationBo bo, final Affiliation source) {
    bo.setActive(source.isActive());
    bo.setAffiliationTypeCode(source.getAffiliationType());
    bo.setCampusCode(source.getCampus());
    bo.setDefaultValue(source.isDefault());
    return bo;
  }

  @Override
  public final Class<EntityAffiliationBo> getBusinessObjectClass() {
    return EntityAffiliationBo.class;
  }

  @Override
  public final Class<Affiliation> getIncomingClass() {
    return Affiliation.class;
  }
}
