package com.rsmart.kuali.coeus.hr.service.adapter.impl;

import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;

import com.rsmart.kuali.coeus.hr.rest.model.Address;
import com.rsmart.kuali.coeus.hr.service.adapter.PersistableBoMergeAdapter;

/**
 * Implements logic necessary for merging {@link com.rsmart.kuali.coeus.hr.rest.model.Address Address}
 * objects from import into the list of {@link org.kuali.rice.kim.impl.identity.address.EntityAddressBo EntityAddressBo}
 * objects already attached to an Entity.
 * 
 * @author duffy
 *
 */
public class EntityAddressBoAdapter extends PersistableBoMergeAdapter<EntityAddressBo, Address> {

  @Override
  public int compareBOProperties(EntityAddressBo addr0, EntityAddressBo addr1) {

    int comp = 0;
    
    comp = nullSafeCompare(addr0.getAddressTypeCode(),addr1.getAddressTypeCode());
    if (comp != 0) {
      return comp;
    }
    comp = nullSafeCompare(addr0.getCountryCode(),addr1.getCountryCode());
    if (comp != 0) {
      return comp;
    }
    comp = nullSafeCompare(addr0.getStateProvinceCode(),addr1.getStateProvinceCode());
    if (comp != 0) {
      return comp;
    }
    comp = nullSafeCompare(addr0.getCity(),addr1.getCity());
    if (comp != 0) {
      return comp;
    }
    comp = nullSafeCompare(addr0.getPostalCode(),addr1.getPostalCode());
    if (comp != 0) {
      return comp;
    }
    comp = nullSafeCompare(addr0.getLine1(),addr1.getLine1());
    if (comp != 0) {
      return comp;
    }
    comp = nullSafeCompare(addr0.getLine2(),addr1.getLine2());
    if (comp != 0) {
      return comp;
    }
    
    return nullSafeCompare(addr0.getLine3(),addr1.getLine3());
  }
  
  @Override
  public final EntityAddressBo newBO(final String entityId) {
    final EntityAddressBo bo = new EntityAddressBo();
    bo.setEntityId(entityId);
    return bo;
  }

  @Override
  public final EntityAddressBo setFields(final EntityAddressBo bo, final Address source) {
    bo.setActive(source.isActive());
    bo.setAddressTypeCode(source.getAddressTypeCode());
    bo.setCity(source.getCity());
    bo.setCountryCode(source.getCountry());
    bo.setDefaultValue(source.isDefault());
    bo.setLine1(source.getAddressLine1());
    bo.setLine2(source.getAddressLine2());
    bo.setLine3(source.getAddressLine3());
    bo.setPostalCode(source.getPostalCode());
    bo.setStateProvinceCode(source.getStateOrProvince());

    return bo;
  }

  @Override
  public final Class<EntityAddressBo> getBusinessObjectClass() {
    return EntityAddressBo.class;
  }

  @Override
  public final Class<Address> getIncomingClass() {
    return Address.class;
  }
}
