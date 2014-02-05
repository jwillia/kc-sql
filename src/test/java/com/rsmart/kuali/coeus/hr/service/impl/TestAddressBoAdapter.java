package com.rsmart.kuali.coeus.hr.service.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;

import com.rsmart.kuali.coeus.hr.rest.model.Address;
import com.rsmart.kuali.coeus.hr.service.adapter.impl.EntityAddressBoAdapter;

public class TestAddressBoAdapter {

  private static final String TEST_ID = "TEST_ID";
  
  EntityAddressBoAdapter adapter = new EntityAddressBoAdapter();

  private final Address getTestAddress() {
    final Address address = new Address();
    
    address.setActive(false);
    address.setAddressLine1("LINE_1");
    address.setAddressLine2("LINE_2");
    address.setAddressLine3("LINE_3");
    address.setAddressTypeCode("TYPE_CODE");
    address.setCity("CITY");
    address.setCountry("COUNTRY");
    address.setDefault(false);
    address.setPostalCode("POSTAL_CODE");
    address.setStateOrProvince("STATE");

    return address;
  }
  
  @Test
  public void testReportsCorrectClasses() throws Exception {
    assertEquals (EntityAddressBo.class, adapter.getBusinessObjectClass());
    assertEquals (Address.class, adapter.getIncomingClass());
  }
  
  @Test
  public void testNewBO() throws Exception {
    final EntityAddressBo bo = adapter.newBO(TEST_ID);
    assertNotNull(bo);
    assertEquals(TEST_ID, bo.getEntityId());
  }
  
  @Test
  public void testFieldsAreSet() throws Exception {
    final EntityAddressBo bo = adapter.newBO(TEST_ID);
    final Address address = getTestAddress();

    EntityAddressBo result = adapter.setFields(bo, address);
    assertEquals(address.getAddressLine1(), result.getLine1());
    assertEquals(address.getAddressLine2(), result.getLine2());
    assertEquals(address.getAddressLine3(), result.getLine3());
    assertEquals(address.getAddressTypeCode(), result.getAddressTypeCode());
    assertEquals(address.getCity(), result.getCity());
    assertEquals(address.getCountry(), result.getCountryCode());
    assertEquals(address.getPostalCode(), result.getPostalCode());
    assertEquals(address.getStateOrProvince(), result.getStateProvinceCode());
    assertEquals(address.isActive(), result.isActive());
    assertEquals(address.isDefault(), result.isDefaultValue());
  }
  
  @Test
  public void testComparisons() throws Exception {
    final EntityAddressBo bo = new EntityAddressBo();
    EntityAddressBo addressBo = new EntityAddressBo();
    Address compAddress = getTestAddress();
    
    adapter.setFields(bo,  compAddress);
    
    assertEquals(0, adapter.compare(bo, adapter.setFields(addressBo, compAddress)));
    
    compAddress.setActive(true);
    assertTrue(adapter.compare(bo, adapter.setFields(addressBo, compAddress)) > 0);
    compAddress = getTestAddress();
    
    compAddress.setDefault(true);
    assertTrue(adapter.compare(bo, adapter.setFields(addressBo, compAddress)) > 0);
    compAddress = getTestAddress();
    
    compAddress.setAddressLine1("A");
    assertTrue(adapter.compare(bo, adapter.setFields(addressBo, compAddress)) > 0);
    compAddress = getTestAddress();
    
    compAddress.setAddressLine1("Z");
    assertTrue(adapter.compare(bo, adapter.setFields(addressBo, compAddress)) < 0);
    compAddress = getTestAddress();
    
    compAddress.setAddressLine2("A");
    assertTrue(adapter.compare(bo, adapter.setFields(addressBo, compAddress)) > 0);
    compAddress = getTestAddress();
    
    compAddress.setAddressLine2("Z");
    assertTrue(adapter.compare(bo, adapter.setFields(addressBo, compAddress)) < 0);
    compAddress = getTestAddress();
    
    compAddress.setAddressLine3("A");
    assertTrue(adapter.compare(bo, adapter.setFields(addressBo, compAddress)) > 0);
    compAddress = getTestAddress();
    
    compAddress.setAddressLine3("Z");
    assertTrue(adapter.compare(bo, adapter.setFields(addressBo, compAddress)) < 0);
    compAddress = getTestAddress();
    
    compAddress.setAddressTypeCode("A");
    assertTrue(adapter.compare(bo, adapter.setFields(addressBo, compAddress)) > 0);
    compAddress = getTestAddress();
    
    compAddress.setAddressTypeCode("Z");
    assertTrue(adapter.compare(bo, adapter.setFields(addressBo, compAddress)) < 0);
    compAddress = getTestAddress();
    
    compAddress.setCity("A");
    assertTrue(adapter.compare(bo, adapter.setFields(addressBo, compAddress)) > 0);
    compAddress = getTestAddress();
    
    compAddress.setCity("Z");
    assertTrue(adapter.compare(bo, adapter.setFields(addressBo, compAddress)) < 0);
    compAddress = getTestAddress();
    
    compAddress.setCountry("A");
    assertTrue(adapter.compare(bo, adapter.setFields(addressBo, compAddress)) > 0);
    compAddress = getTestAddress();
    
    compAddress.setCountry("Z");
    assertTrue(adapter.compare(bo, adapter.setFields(addressBo, compAddress)) < 0);
    compAddress = getTestAddress();
    
    compAddress.setPostalCode("A");
    assertTrue(adapter.compare(bo, adapter.setFields(addressBo, compAddress)) > 0);
    compAddress = getTestAddress();
    
    compAddress.setPostalCode("Z");
    assertTrue(adapter.compare(bo, adapter.setFields(addressBo, compAddress)) < 0);
    compAddress = getTestAddress();
    
    compAddress.setStateOrProvince("A");
    assertTrue(adapter.compare(bo, adapter.setFields(addressBo, compAddress)) > 0);
    compAddress = getTestAddress();
    
    compAddress.setStateOrProvince("Z");
    assertTrue(adapter.compare(bo, adapter.setFields(addressBo, compAddress)) < 0);
    compAddress = getTestAddress();
  }

}
