package com.rsmart.kuali.coeus.hr.service.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

import com.rsmart.kuali.coeus.hr.rest.HRManifestResource;
import com.rsmart.kuali.coeus.hr.rest.TestHRManifest;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifest;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifestRecord;
import com.rsmart.kuali.coeus.hr.service.impl.HRManifestServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.kra.bo.KcPerson;
import org.kuali.kra.bo.KcPersonExtendedAttributes;
import org.kuali.kra.service.KcPersonService;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
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
  @Mock
  KcPersonService kcPersonService;
  @Mock
  KcPerson kcPerson;
  @Captor
  ArgumentCaptor<PersistableBusinessObject> persistableBusinessObject;
  @Captor
  ArgumentCaptor<EntityBo> entityBoCapture;

  HRManifestResource resource;

  @Before
  public void setup() throws Exception {
    // default to user does not already exist case
    when(identityService.getEntity(eq("0001"))).thenReturn(null);
    when(kcPersonService.getKcPersonByPersonId(eq("0001"))).thenReturn(kcPerson);
    final KcPersonExtendedAttributes kcpea = new KcPersonExtendedAttributes();
    when(kcPerson.getExtendedAttributes()).thenReturn(kcpea);

    importService.setIdentityService(identityService);
    importService.setBusinessObjectService(businessObjectService);
    importService.setKcPersonService(kcPersonService);

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

    verify(businessObjectService, times(3)).save(persistableBusinessObject.capture());

    // first the KcPersonExtendedAttributes should be saved ///////////////////
    final KcPersonExtendedAttributes kcpea = (KcPersonExtendedAttributes) persistableBusinessObject
        .getAllValues().get(0);
    assertEquals("Maricopa", kcpea.getCounty());
    assertEquals(new Integer(52), kcpea.getAgeByFiscalYear());
    assertEquals("CAUCASIAN", kcpea.getRace());
    assertEquals("PHD", kcpea.getEducationLevel());
    assertEquals("Doctor of Psychology", kcpea.getDegree());
    assertEquals("MATHEMATICS", kcpea.getMajor());
    assertTrue(kcpea.getHandicappedFlag());
    assertEquals("TODO Need a better handicapType example", kcpea.getHandicapType());
    assertTrue(kcpea.getVeteranFlag());
    assertEquals("A", kcpea.getVeteranType());
    assertTrue(kcpea.getHasVisa());
    assertEquals("TODO need a better visaCode example", kcpea.getVisaCode());
    assertEquals("TODO need a better visaType example", kcpea.getVisaType());
    assertEquals(new java.sql.Date(1387954800000L), kcpea.getVisaRenewalDate());
    assertEquals("503 Carter Hall", kcpea.getOfficeLocation());
    assertEquals("88 Regan Hall", kcpea.getSecondaryOfficeLocation());
    assertEquals("MATH", kcpea.getSchool());
    assertEquals("1983", kcpea.getYearGraduated());
    assertEquals("MATH", kcpea.getDirectoryDepartment());
    assertEquals("Master of the Universe", kcpea.getDirectoryTitle());
    assertEquals("Leader of the Evil Horde", kcpea.getPrimaryTitle());
    assertTrue(kcpea.getVacationAccrualFlag());
    assertEquals("John_Doe", kcpea.getIdProvided());
    assertEquals("John_Doe_Verified", kcpea.getIdVerified());
    assertEquals("0002", kcpea.getMultiCampusPrincipalId());
    assertEquals("jdoe2", kcpea.getMultiCampusPrincipalName());
    assertTrue(kcpea.getOnSabbaticalFlag());
    assertEquals(new Integer("1"), kcpea.getCitizenshipTypeCode());
    assertEquals(new java.sql.Date(1262329200000L), kcpea.getSalaryAnniversaryDate());

    // second the entity should be saved //////////////////////////////////////
    final EntityBo entityBo = (EntityBo) persistableBusinessObject.getAllValues().get(1);
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
    assertTrue(entityBo.getEntityTypeContactInfos().size() == 1);
    final EntityTypeContactInfoBo contactInfo = entityBo.getEntityTypeContactInfos().get(
        0);
    assertEquals("HM", contactInfo.getDefaultAddress().getAddressTypeCode());
    assertEquals("1234 E. Main St.", contactInfo.getDefaultAddress().getLine1());
    assertEquals("Apt. 8C", contactInfo.getDefaultAddress().getLine2());
    assertEquals("Minneapolis", contactInfo.getDefaultAddress().getCity());
    assertEquals("MN", contactInfo.getDefaultAddress().getStateProvinceCode());
    assertEquals("34567", contactInfo.getDefaultAddress().getPostalCode());
    assertEquals("US", contactInfo.getDefaultAddress().getCountryCode());
    assertTrue(contactInfo.getDefaultAddress().getDefaultValue());
    assertTrue(contactInfo.getDefaultAddress().getActive());

    // names
    assertTrue(entityBo.getNames().size() == 1);
    final EntityNameBo name = entityBo.getNames().get(0);
    assertEquals("PRM", name.getNameCode());
    assertEquals("Jonathon", name.getFirstName());
    assertEquals("R", name.getMiddleName());
    assertEquals("Doe", name.getLastName());
    assertTrue(name.getActive());

    // phones
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

    // TODO degrees

    // TODO appointments

    // third the principal should be saved ////////////////////////////////////
    final PrincipalBo principalBo = (PrincipalBo) persistableBusinessObject
        .getAllValues().get(2);
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
