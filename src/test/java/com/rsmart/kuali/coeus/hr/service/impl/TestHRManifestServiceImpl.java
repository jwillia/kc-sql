package com.rsmart.kuali.coeus.hr.service.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

import com.rsmart.kuali.coeus.hr.rest.HRManifestResource;
import com.rsmart.kuali.coeus.hr.rest.TestHRManifest;
import com.rsmart.kuali.coeus.hr.rest.model.Affiliation;
import com.rsmart.kuali.coeus.hr.rest.model.Email;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifest;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifestRecord;
import com.rsmart.kuali.coeus.hr.rest.model.Name;
import com.rsmart.kuali.coeus.hr.rest.model.Phone;
import com.rsmart.kuali.coeus.hr.service.HRManifestService;
import com.rsmart.kuali.coeus.hr.service.impl.HRManifestServiceImpl;

import org.drools.command.assertion.AssertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.mockito.*;

import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.math.BigDecimal;

import javax.ws.rs.core.Response;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class TestHRManifestServiceImpl {

  @Spy
  HRManifestServiceImpl importService;
  @Mock
  IdentityService identityService;
  @Mock
  BusinessObjectService businessObjectService;
  @Captor
  ArgumentCaptor<PersistableBusinessObject> persistableBusinessObject;
  @Captor
  ArgumentCaptor<EntityBo> entityBoCapture;

  HRManifestResource resource;

  @Before
  public void setup() throws Exception {
    // default to user does not already exist case
    when(identityService.getEntity(eq("0001"))).thenReturn(null);

    importService.setIdentityService(identityService);
    importService.setBusinessObjectService(businessObjectService);

    resource = new HRManifestResource();
    resource.setManifestService(importService);
  }

  @Test
  public void testCreateNewUser() throws Exception {
    final InputStream manifestStream = getClass().getResourceAsStream(
        "/exampleimport.xml");
    final Response response = resource.processManifest(manifestStream);

    verify(importService).importHRManifest(argThat(new HRManifestArgumentMatcher()));
    verify(importService).updateEntity(entityBoCapture.capture(),
        any(HRManifestRecord.class));

    verify(businessObjectService, times(2)).save(persistableBusinessObject.capture());
    // first the entity should be saved
    final EntityBo entityBo = (EntityBo) persistableBusinessObject.getAllValues().get(0);
    assertEquals("0001", entityBo.getId());
    assertTrue(entityBo.getPrincipals().size() == 1);
    assertEquals("jdoe", entityBo.getPrincipals().get(0).getPrincipalName());

    // affiliations
    assertTrue(entityBo.getAffiliations().size() == 1);
    assertEquals("FCLTY", entityBo.getAffiliations().get(0).getAffiliationTypeCode());
    assertEquals("MN", entityBo.getAffiliations().get(0).getCampusCode());
    assertEquals("A", entityBo.getPrimaryEmployment().getEmployeeStatusCode());
    assertEquals("P", entityBo.getPrimaryEmployment().getEmployeeTypeCode());
    assertEquals(new BigDecimal("50180.00"), entityBo.getPrimaryEmployment()
        .getBaseSalaryAmount().bigDecimalValue());
    assertEquals("MATH", entityBo.getPrimaryEmployment().getPrimaryDepartmentCode());
    assertEquals("092001234", entityBo.getPrimaryEmployment().getEmployeeId());
    assertTrue(entityBo.getPrimaryEmployment().getActive());

    // addresses
    // TODO FIXME add support for addresses to service

    // names
    assertTrue(entityBo.getNames().size() == 1);
    assertEquals("PRM", entityBo.getNames().get(0).getNameCode());
    assertEquals("Jonathon", entityBo.getNames().get(0).getFirstName());
    assertEquals("Doe", entityBo.getNames().get(0).getLastName());
    assertTrue(entityBo.getNames().get(0).getActive());

    // phones
    assertTrue(entityBo.getEntityTypeContactInfos().size() == 1);
    final EntityTypeContactInfoBo contactInfo = entityBo.getEntityTypeContactInfos().get(
        0);
    assertTrue(contactInfo.getPhoneNumbers().size() == 2);
    final EntityPhoneBo phone1 = contactInfo.getPhoneNumbers().get(0);
    assertEquals("WRK", phone1.getPhoneTypeCode());
    assertEquals("204-391-1101", phone1.getPhoneNumber());
    assertEquals("US", phone1.getCountryCode());
    assertTrue(phone1.getActive());

    final EntityPhoneBo phone2 = contactInfo.getPhoneNumbers().get(1);
    assertEquals("HM", phone2.getPhoneTypeCode());
    assertEquals("204-718-3221", phone2.getPhoneNumber());
    assertEquals("US", phone2.getCountryCode());
    assertTrue(phone2.getActive());

    // emails
    assertTrue(contactInfo.getEmailAddresses().size() == 1);
    final EntityEmailBo email = contactInfo.getEmailAddresses().get(0);
    assertEquals("jdoe@university.edu", email.getEmailAddress());
    assertTrue(email.getActive());

    // TODO extended attributes

    // TODO degrees

    // TODO appointments

    // next the principal should be saved
    final PrincipalBo principalBo = (PrincipalBo) persistableBusinessObject
        .getAllValues().get(1);
    assertEquals("0001", principalBo.getPrincipalId());
    assertEquals("jdoe", principalBo.getPrincipalName());
    assertTrue(principalBo.getActive());

    assertEquals(200, response.getStatus());
  }

  class HRManifestArgumentMatcher extends ArgumentMatcher<HRManifest> {
    public boolean matches(Object o) {
      if (o instanceof HRManifest) {
        final HRManifest manifest = (HRManifest) o;

        try {
          TestHRManifest.validateTestHRManifest(manifest);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        return true;
      }
      return false;
    }
  }
}
