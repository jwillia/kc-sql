package com.rsmart.kuali.coeus.hr.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

import com.rsmart.kuali.coeus.hr.rest.HRManifestResource;
import com.rsmart.kuali.coeus.hr.rest.model.Address;
import com.rsmart.kuali.coeus.hr.rest.model.AddressCollection;
import com.rsmart.kuali.coeus.hr.rest.model.Affiliation;
import com.rsmart.kuali.coeus.hr.rest.model.AffiliationCollection;
import com.rsmart.kuali.coeus.hr.rest.model.Appointment;
import com.rsmart.kuali.coeus.hr.rest.model.AppointmentCollection;
import com.rsmart.kuali.coeus.hr.rest.model.Degree;
import com.rsmart.kuali.coeus.hr.rest.model.DegreeCollection;
import com.rsmart.kuali.coeus.hr.rest.model.Email;
import com.rsmart.kuali.coeus.hr.rest.model.EmailCollection;
import com.rsmart.kuali.coeus.hr.rest.model.Employment;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifest;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifestRecord;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifestRecordCollection;
import com.rsmart.kuali.coeus.hr.rest.model.KCExtendedAttributes;
import com.rsmart.kuali.coeus.hr.rest.model.Name;
import com.rsmart.kuali.coeus.hr.rest.model.NameCollection;
import com.rsmart.kuali.coeus.hr.rest.model.Phone;
import com.rsmart.kuali.coeus.hr.rest.model.PhoneCollection;
import com.rsmart.kuali.coeus.hr.rest.model.TestHRManifest;
import com.rsmart.kuali.coeus.hr.service.HRManifestImportException;
import com.rsmart.kuali.coeus.hr.service.adapter.impl.EntityAddressBoAdapter;
import com.rsmart.kuali.coeus.hr.service.impl.HRManifestServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.kra.bo.KcPerson;
import org.kuali.kra.bo.KcPersonExtendedAttributes;
import org.kuali.kra.bo.PersonAppointment;
import org.kuali.kra.bo.PersonDegree;
import org.kuali.kra.bo.Unit;
import org.kuali.kra.service.UnitService;
import org.kuali.rice.core.api.cache.CacheManagerRegistry;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.mockito.*;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
  UnitService unitService;
  @Mock
  CacheManagerRegistry cacheManagerRegistry;
  @Mock
  CacheManager cacheManager;
  @Mock
  Cache cache;
  @Mock
  KcPerson kcPerson;
  @Captor
  ArgumentCaptor<PersistableBusinessObject> persistableBusinessObject;
  @Captor
  ArgumentCaptor<EntityBo> entityBoCaptor;
  @Captor
  ArgumentCaptor<EntityAddressBo> addressBoCaptor;

  HRManifestResource resource;

  @Before
  public void setup() throws Exception {
    // default to user does not already exist case
    when(identityService.getEntity(eq("0001"))).thenReturn(null);
    final KcPersonExtendedAttributes kcpea = new KcPersonExtendedAttributes();
    when(kcPerson.getExtendedAttributes()).thenReturn(kcpea);
    final Unit unit = new Unit();
    unit.setActive(true);
    unit.setUnitName("CARDIOLOGY");
    unit.setUnitNumber("IN-CARD");
    when(unitService.getUnit(eq("IN-CARD"))).thenReturn(unit);
    when(cacheManager.getCache(anyString())).thenReturn(cache);
    when(cacheManagerRegistry.getCacheManagerByCacheName(anyString())).thenReturn(cacheManager);

    importService.setIdentityService(identityService);
    importService.setBusinessObjectService(businessObjectService);
    importService.setUnitService(unitService);
    importService.setCacheManagerRegistry(cacheManagerRegistry);

    resource = new HRManifestResource();
    resource.setManifestService(importService);
  }

  private final Phone createSamplePhone(final boolean active, final String country, 
      final boolean dft, final String extension, final String phoneNumber, 
      final String phoneType) {
    final Phone phone = new Phone();
    
    phone.setActive(active);
    phone.setCountry(country);
    phone.setDefault(dft);
    phone.setExtension(extension);
    phone.setPhoneNumber(phoneNumber);
    phone.setPhoneType(phoneType);
    
    return phone;
  }
  
  private final PhoneCollection createSamplePhones(final Phone phoneArr[]) {
    final PhoneCollection phones = new PhoneCollection();
    final List<Phone> phoneList = phones.getPhones();
    
    for (Phone phone : phoneArr) {
      phoneList.add(phone);
    }
    
    return phones;
  }

  private final Name createSampleName(final boolean active, final boolean dft, 
      final String firstName, final String lastName, final String middleName, final String nameCode,
      final String prefix, final String suffix) {
    final Name name = new Name();
    
    name.setActive(active);
    name.setDefault(dft);
    name.setFirstName(firstName);
    name.setLastName(lastName);
    name.setMiddleName(middleName);
    name.setNameCode(nameCode);
    name.setPrefix(prefix);
    name.setSuffix(suffix);
    
    return name;
  }
  
  private final NameCollection createSampleNames(final Name[] nameArr) {
    final NameCollection names = new NameCollection();
    final List<Name> nameList = names.getNames();
    
    for (Name name : nameArr) {
      nameList.add(name);
    }
    
    return names;
  }

  private final KCExtendedAttributes createSampleKCExtendedAttributes(final int ageByFY, final Integer citizenshipType,
      final String county, final String degree, final String dirDepartment, final String dirTitle, final String edLevel,
      final boolean handicapped, final String handicapType, final String idProvided, final String idVerified,
      final String major, final String multiCampusPrinId, final String multiCampusPrinName, final String ofcLocation,
      final boolean sabbatical, final String primaryTitle, final String race, final Date salAnnivDate, final String school,
      final String secOfficeLocation, final boolean vacAccrual, final boolean vetran, final String vetranType,
      final boolean visa, final String visaCode, final Date visaRenewal, final String visaType, final Integer yearGraduated) {
    final KCExtendedAttributes kcExtendedAttributes = new KCExtendedAttributes();
    
    kcExtendedAttributes.setAgeByFiscalYear(ageByFY);
    kcExtendedAttributes.setCitizenshipType(citizenshipType);
    kcExtendedAttributes.setCounty(county);
    kcExtendedAttributes.setDegree(degree);
    kcExtendedAttributes.setDirectoryDepartment(dirDepartment);
    kcExtendedAttributes.setDirectoryTitle(dirTitle);
    kcExtendedAttributes.setEducationLevel(edLevel);
    kcExtendedAttributes.setHandicapped(handicapped);
    kcExtendedAttributes.setHandicapType(handicapType);
    kcExtendedAttributes.setIdProvided(idProvided);
    kcExtendedAttributes.setIdVerified(idVerified);
    kcExtendedAttributes.setMajor(major);
    kcExtendedAttributes.setMultiCampusPrincipalId(multiCampusPrinId);
    kcExtendedAttributes.setMultiCampusPrincipalName(multiCampusPrinName);
    kcExtendedAttributes.setOfficeLocation(ofcLocation);
    kcExtendedAttributes.setOnSabbatical(sabbatical);
    kcExtendedAttributes.setPrimaryTitle(primaryTitle);
    kcExtendedAttributes.setRace(race);
    kcExtendedAttributes.setSalaryAnniversaryDate(salAnnivDate);
    kcExtendedAttributes.setSchool(school);
    kcExtendedAttributes.setSecondaryOfficeLocation(secOfficeLocation);
    kcExtendedAttributes.setVacationAccrual(vacAccrual);
    kcExtendedAttributes.setVeteran(vetran);
    kcExtendedAttributes.setVeteranType(vetranType);
    kcExtendedAttributes.setVisa(visa);
    kcExtendedAttributes.setVisaCode(visaCode);
    kcExtendedAttributes.setVisaRenewalDate(visaRenewal);
    kcExtendedAttributes.setVisaType(visaType);
    kcExtendedAttributes.setYearGraduated(yearGraduated);
    
    return kcExtendedAttributes;
  }

  private final Email createSampleEmail(final boolean active, final boolean dft, 
      final String emailAddress, final String emailType) {
    final Email email = new Email();
    
    email.setActive(active);
    email.setDefault(dft);
    email.setEmailAddress(emailAddress);
    email.setEmailType(emailType);
    
    return email;
  }
  
  private final EmailCollection createSampleEmails(final Email emailArr[]) {
    final EmailCollection emails = new EmailCollection();
    final List<Email> emailList = emails.getEmails();
    
    for (Email email : emailArr) {
      emailList.add(email);
    }
    
    return emails;
  }
  
  private final Degree createSampleDegree(final String deg, final String degreeCode, final String field,
      final Integer gradYear, final String school, final String schoolId, final String schoolIdCode,
      final String specialization) {
    final Degree degree = new Degree();
    
    degree.setDegree(deg);
    degree.setDegreeCode(degreeCode);
    degree.setFieldOfStudy(field);
    degree.setGraduationYear(gradYear);
    degree.setSchool(school);
    degree.setSchoolId(schoolId);
    degree.setSchoolIdCode(schoolIdCode);
    degree.setSpecialization(specialization);
    
    return degree;
  }

  private final DegreeCollection createSampleDegrees(final Degree degreeArr[]) {
    final DegreeCollection degrees = new DegreeCollection();
    final List<Degree> degreeList = degrees.getDegrees();
    
    for (Degree degree : degreeArr) {
      degreeList.add(degree);
    }
    
    return degrees;
  }

  private final Appointment createSampleAppointment(final String apptType, final Date endDate, final String jobCode,
      final String jobTitle, final String prefJobTitle, final float salary, final Date startDate, final String unitNum) {
    final Appointment appointment = new Appointment();
    
    appointment.setAppointmentType(apptType);
    appointment.setEndDate(endDate);
    appointment.setJobCode(jobCode);
    appointment.setJobTitle(jobTitle);
    appointment.setPreferedJobTitle(prefJobTitle);
    appointment.setSalary(salary);
    appointment.setStartDate(startDate);
    appointment.setUnitNumber(unitNum);
    
    return appointment;
  }
  
  private final AppointmentCollection createSampleAppointments(final Appointment appointmentArr[]) {
    final AppointmentCollection appointments = new AppointmentCollection();
    final List<Appointment> appointmentList = appointments.getAppointments();
    
    for (Appointment appointment : appointmentArr) {
      appointmentList.add(appointment);
    }
    
    return appointments;
  }
  
  private final Employment createSampleEmployment (final float baseSal, final String empId,
      final String empStatus, final String empType, final String primaryDept, 
      final boolean primaryEmployment) {
    final Employment employment = new Employment();
    employment.setBaseSalaryAmount(baseSal);
    employment.setEmployeeId(empId);
    employment.setEmployeeStatus(empStatus);
    employment.setEmployeeType(empType);
    employment.setPrimaryDepartment(primaryDept);
    employment.setPrimaryEmployment(primaryEmployment);
    
    return employment;
  }
  
  private final Affiliation createSampleAffiliation(final boolean active, final String affType,
      final String campus, final boolean dft, final Employment employment) {
      
    final Affiliation affiliation = new Affiliation();
    
    affiliation.setActive(active);
    affiliation.setAffiliationType(affType);
    affiliation.setCampus(campus);
    affiliation.setDefault(dft);
    affiliation.setEmployment(employment);

    return affiliation;
  }

  private final AffiliationCollection createSampleAffiliations(final Affiliation affiliationArr[]) {
    final AffiliationCollection affiliations = new AffiliationCollection();
    final List<Affiliation> affiliationList = affiliations.getAffiliations();

    for (Affiliation affiliation : affiliationArr) {
      affiliationList.add(affiliation);
    }
    
    return affiliations;
  }

  private final Address createSampleAddress(final boolean active, final String address1, final String address2, final String address3,
      final String addressTypeCode, final String city, final String country, final boolean dft, final String postalCode,
      final String stateOrProvince) {
    final Address address = new Address();
    
    address.setActive(active);
    address.setAddressLine1(address1);
    address.setAddressLine2(address2);
    address.setAddressLine3(address3);
    address.setAddressTypeCode(addressTypeCode);
    address.setCity(city);
    address.setCountry(country);
    address.setDefault(dft);
    address.setPostalCode(postalCode);
    address.setStateOrProvince(stateOrProvince);

    return address;
  }
  
  private final AddressCollection createSampleAddresses(final Address addressArr[]) {
    final AddressCollection addresses = new AddressCollection();
    final List<Address> addressList = addresses.getAddresses();
    
    for (Address address : addressArr) {
      addressList.add(address);
    }
    
    return addresses;
  }
  
  private final HRManifestRecord createSampleRecord() {
    final HRManifestRecord record = new HRManifestRecord();
  
    record.setAddressCollection(createSampleAddresses( new Address[] {
       //boolean active,  String address1,  String address2,  String address3,
       //String addressTypeCode,  String city,  String country,  boolean dft,  String postalCode,
       //String stateOrProvince
       createSampleAddress(true, "1234 E. Main St.", "Apt. 8C", null, "HM", "Minneapolis", "US", true, "34567", "MN")
    }));
    record.setAffiliationCollection(createSampleAffiliations( new Affiliation[] {
       //boolean active,  String affType,  float baseSal,
       //String campus,  boolean dft,  String empId,  String empStatus,  String empType,
       //String primaryDept, boolean primaryEmployment
       createSampleAffiliation(true, "FCLTY", "MN", true,
           createSampleEmployment(50180f,  "092001234", "A", "P", "MATH", true))
    }));
    Calendar cal = Calendar.getInstance();
    cal.set(2010, 0, 1, 0, 0, 0);
    cal.set(Calendar.MILLISECOND, 0);
    java.sql.Date startDate = new java.sql.Date(cal.getTimeInMillis());
    record.setAppointmentCollection(createSampleAppointments( new Appointment[] {
       //String apptType,  Date endDate,  String jobCode,
       //String jobTitle,  String prefJobTitle,  float salary,  Date startDate,  String unitNum
       createSampleAppointment ("APP", null, "0010", "Professor", "Professor", 50180f, startDate, "IN-CARD")
    }));
    record.setDegreeCollection(createSampleDegrees( new Degree[] {
       //String deg,  String degreeCode,  String field,
       //Integer gradYear,  String school,  String schoolId,  String schoolIdCode,
       //String specialization
       createSampleDegree("PhD", "DD", "Mathematics", 1983, "Texas A&M University", "1234", "UMN", "non-linear algebra")
    }));
    record.setEmailCollection(createSampleEmails( new Email[] {
      // boolean active,  boolean dft, 
      // String emailAddress,  String emailType
      createSampleEmail(true, true, "jdoe@university.edu", "WRK")
    }));
    record.setKcExtendedAttributes(
      //  int ageByFY,  Integer citizenshipType,
      //  String county,  String degree,  String dirDepartment,  String dirTitle,  String edLevel,
      //  boolean handicapped,  String handicapType,  String idProvided,  String idVerified,
      //  String major,  String multiCampusPrinId,  String multiCampusPrinName,  String ofcLocation,
      //  boolean sabbatical,  String primaryTitle,  String race,  Date salAnnivDate,  String school,
      //  String secOfficeLocation,  boolean vacAccrual,  boolean vetran,  String vetranType,
      //  boolean visa,  String visaCode,  Date visaRenewal,  String visaType, Integer yearGraduated
      createSampleKCExtendedAttributes(52, 1, "Maricopa", "PhD", "MATH", "Master of the Universe", "PHD", true, 
          "TODO:handicapType", "John_Doe", "John_Doe_Verified", "MATHEMATICS", "0002", "jdoe2", "503 Carter Hall", 
          true, "Leader of the Evil Horde", "CAUCASIAN", new java.sql.Date(1262329200000L), "MATH", 
          "88 Regan Hall", true, true, "A", true, "TODO:visaCode", new java.sql.Date(1387954800000L), 
          "TODO:visaType", 1983));
    record.setNameCollection(createSampleNames( new Name[] {
      //  boolean active,  boolean dft, 
      //  String firstName,  String lastName,  String middleName,  String nameCode,
      //  String prefix,  String suffix
      createSampleName(true, true, "Jonathon", "Doe", "R", "PRM", "Mr.", "Jr.")
    }));
    record.setPhoneCollection(createSamplePhones( new Phone[] {
      // boolean active, String country, boolean default,
      // String extension, String phoneNumber, String phoneType
      createSamplePhone(true, "US", true, "520", "204-391-1101", "WRK"),
      createSamplePhone(true, "US", false, "520", "204-718-3221", "HM")
    }));
    record.setPrincipalId("0001");
    record.setPrincipalName("jdoe");

    return record;
  }
  
  private final HRManifestRecordCollection createSampleRecords() {
    final HRManifestRecordCollection records = new HRManifestRecordCollection();
    
    final HRManifestRecord record = createSampleRecord();
    
    records.getRecords().add(record);
    
    return records;
  }
  
  private final HRManifest createSampleManifest() {
    final HRManifest manifest = new HRManifest();
    
    final HRManifestRecordCollection records = createSampleRecords();
    
    manifest.setRecords(records);
    manifest.setRecordCount(records.getRecords().size());
    
    return manifest;
  }

  private void whiteBoxImportServiceTest() throws Exception {
    verify(importService).updateEntityBo(entityBoCaptor.capture(), any(HRManifestRecord.class));

    verify(businessObjectService, times(3)).save(persistableBusinessObject.capture());

    // first the entity should be saved ///////////////////////////////////////

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
    
    // second the principal should be saved ///////////////////////////////////
    
    final PrincipalBo principalBo = (PrincipalBo) persistableBusinessObject
        .getAllValues().get(1);
    assertEquals("0001", principalBo.getPrincipalId());
    assertEquals("jdoe", principalBo.getPrincipalName());
    assertTrue(principalBo.getActive());

    // third the KcPersonExtendedAttributes should be saved ///////////////////
    
    final KcPersonExtendedAttributes kcpea = (KcPersonExtendedAttributes) persistableBusinessObject
        .getAllValues().get(2);
    assertEquals("Maricopa", kcpea.getCounty());
    assertEquals(new Integer(52), kcpea.getAgeByFiscalYear());
    assertEquals("CAUCASIAN", kcpea.getRace());
    assertEquals("PHD", kcpea.getEducationLevel());
    assertEquals("PhD", kcpea.getDegree());
    assertEquals("MATHEMATICS", kcpea.getMajor());
    assertTrue(kcpea.getHandicappedFlag());
    assertEquals("TODO:handicapType", kcpea.getHandicapType());
    assertTrue(kcpea.getVeteranFlag());
    assertEquals("A", kcpea.getVeteranType());
    assertTrue(kcpea.getHasVisa());
    assertEquals("TODO:visaCode", kcpea.getVisaCode());
    assertEquals("TODO:visaType", kcpea.getVisaType());
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

    // degrees
    assertTrue(kcpea.getPersonDegrees().size() == 1);
    final PersonDegree personDegree = kcpea.getPersonDegrees().get(0);
    assertEquals("DD", personDegree.getDegreeCode());
    assertEquals("PhD", personDegree.getDegree());
    assertEquals("1983", personDegree.getGraduationYear());
    assertEquals("Mathematics", personDegree.getFieldOfStudy());
    assertEquals("non-linear algebra", personDegree.getSpecialization());
    assertEquals("Texas A&M University", personDegree.getSchool());
    assertEquals("1234", personDegree.getSchoolId());
    assertEquals("UMN", personDegree.getSchoolIdCode());

    // appointments
    assertTrue(kcpea.getPersonAppointments().size() == 1);
    final PersonAppointment appointment = kcpea.getPersonAppointments().get(0);
    assertEquals("APP", appointment.getAppointmentType().getAppointmentTypeCode());
    assertEquals("0010", appointment.getJobCode());
    assertEquals("Professor", appointment.getJobTitle());
    assertEquals("Professor", appointment.getPreferedJobTitle());
    assertEquals(50180f, (float)appointment.getSalary().floatValue(), .00000000001f);
    Calendar cal = Calendar.getInstance();
    cal.set(2010, 0, 1, 0, 0, 0);
    cal.set(Calendar.MILLISECOND, 0);
    java.sql.Date expectedDate = new java.sql.Date(cal.getTimeInMillis());
    assertEquals(expectedDate, appointment.getStartDate());
    assertNull(appointment.getEndDate());
    assertEquals("IN-CARD", appointment.getUnit().getUnitNumber());

  }
  
/*
  @Test
  public void testCreateNewUser() throws Exception {
    importService.importHRManifest(createSampleManifest());
    
    whiteBoxImportServiceTest();
  }
  
  @Test
  public void testCreateNewUserFromImport() throws Exception {
    final InputStream manifestStream = getClass().getResourceAsStream("/exampleimport.xml");
    final Response response = resource.processManifest(manifestStream);

    verify(importService).importHRManifest(argThat(new HRManifestArgumentMatcher()));
    
    whiteBoxImportServiceTest();

    assertEquals(200, response.getStatus());
  }
*/
  @Test(expected = HRManifestImportException.class)
  public void testErrorOnDuplicateRecords() throws Exception {
    final HRManifest manifest = createSampleManifest();
    
    manifest.getRecords().getRecords().add( createSampleRecord() );
    manifest.setRecordCount(2);
    
    importService.importHRManifest(manifest);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testErrorOnIncorrectRecordCount() throws Exception {
    final HRManifest manifest = createSampleManifest();
    
    manifest.getRecords().getRecords().add( createSampleRecord() );
    
    importService.importHRManifest(manifest);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testDuplicatesCauseError() throws Exception {
    EntityAddressBoAdapter adapter = new EntityAddressBoAdapter();
    Address addr1 = createSampleAddress(false, "456 N. Euclid Ave.", null, null, "WK", "Minneapolis", "US", false, "34567", "MN");
    Address addr2 = createSampleAddress(false, "456 N. Euclid Ave.", null, null, "WK", "Minneapolis", "US", false, "34567", "MN");
    AddressCollection addresses = createSampleAddresses( new Address[] {
        addr1, addr2
    });
    
    importService.adaptAndSortList("ENTITY_ID", addresses.getAddresses(), adapter);
  }

  @Test
  public void testListSortsByFlagsFirst() throws Exception {
    EntityAddressBoAdapter adapter = new EntityAddressBoAdapter();
    Address addr1 = createSampleAddress(false, "A", "A", "A", "A", "A", "A", false, "A", "A");
    Address addr2 = createSampleAddress(false, "B", "B", "B", "B", "B", "B", false, "B", "B");
    Address addr3 = createSampleAddress(true, "Z", "Z", "Z", "Z", "Z", "Z", false, "Z", "Z");
    Address addr4 = createSampleAddress(false, "Z", "Z", "Z", "Z", "Z", "Z", true, "Z", "Z");

    AddressCollection addresses = createSampleAddresses( new Address[] {
        addr1, addr2, addr3
    });    
    List<EntityAddressBo> addrList = importService.adaptAndSortList("ENTITY_ID", addresses.getAddresses(), adapter);
    
    assertNotNull(addrList);
    assertEquals(3, addrList.size());
    assertEquals("Z", addrList.get(0).getLine1());
    assertEquals("A", addrList.get(1).getLine1());
    assertEquals("B", addrList.get(2).getLine1());
    
    addresses = createSampleAddresses( new Address[] {
        addr1, addr2, addr4
    });
    addrList = importService.adaptAndSortList("ENTITY_ID", addresses.getAddresses(), adapter);

    assertNotNull(addrList);
    assertEquals(3, addrList.size());
    assertEquals("Z", addrList.get(0).getLine1());
    assertEquals("A", addrList.get(1).getLine1());
    assertEquals("B", addrList.get(2).getLine1());
    
    addresses = createSampleAddresses( new Address[] {
        addr2, addr1
    });
    addrList = importService.adaptAndSortList("ENTITY_ID", addresses.getAddresses(), adapter);

    assertNotNull(addrList);
    assertEquals(2, addrList.size());
    assertEquals("A", addrList.get(0).getLine1());
    assertEquals("B", addrList.get(1).getLine1());
  }
  
  @Test
  public void testMergeImportedBOsSkipsEmptyLists() throws Exception {
    ArrayList<EntityAddressBo> existingBOs = new ArrayList<EntityAddressBo>();
    
    boolean result = importService.mergeImportedBOs(null, existingBOs, new EntityAddressBoAdapter(), "ENTITY_ID");

    assertFalse(result);
    verify(businessObjectService, never()).save(any(PersistableBusinessObject.class));
    verify(businessObjectService, never()).delete(any(PersistableBusinessObject.class));
  }

  @Test
  public void testMergeImportedBOsIgnoresUnchangedRecords() throws Exception {
    EntityAddressBoAdapter adapter = new EntityAddressBoAdapter();
    Address addr1 = createSampleAddress(false, "A", "A", "A", "A", "A", "A", false, "A", "A");
    Address addr2 = createSampleAddress(false, "B", "B", "B", "B", "B", "B", false, "B", "B");
    Address addr3 = createSampleAddress(true, "Z", "Z", "Z", "Z", "Z", "Z", false, "Z", "Z");

    AddressCollection addresses = createSampleAddresses( new Address[] {
        addr1, addr2, addr3
    });    

    List<EntityAddressBo> current = importService.adaptAndSortList("ENTITY_ID", addresses.getAddresses(), adapter);
    
    boolean result = importService.mergeImportedBOs(addresses.getAddresses(), current, adapter, "ENTITY_ID");

    assertFalse(result);
    verify(businessObjectService, never()).save(any(PersistableBusinessObject.class));
    verify(businessObjectService, never()).delete(any(PersistableBusinessObject.class));
  }
  
  @Test
  public void testMergeImportedBOsRemovesOmittedRecords() throws Exception {
    EntityAddressBoAdapter adapter = new EntityAddressBoAdapter();
    Address addr1 = createSampleAddress(false, "A", "A", "A", "A", "A", "A", false, "A", "A");
    Address addr2 = createSampleAddress(false, "B", "B", "B", "B", "B", "B", false, "B", "B");
    Address addr3 = createSampleAddress(true, "Z", "Z", "Z", "Z", "Z", "Z", false, "Z", "Z");

    AddressCollection addresses = createSampleAddresses( new Address[] {
        addr1, addr2, addr3
    });    

    List<EntityAddressBo> current = importService.adaptAndSortList("ENTITY_ID", addresses.getAddresses(), adapter);
    
    EntityAddressBo removed = current.get(2);
    
    addresses = createSampleAddresses ( new Address[] {
        addr1, addr3
    });
    
    boolean result = importService.mergeImportedBOs(addresses.getAddresses(), current, adapter, "ENTITY_ID");

    assertTrue(result);
    assertEquals(3, current.size());
    verify(businessObjectService, times(1)).delete(addressBoCaptor.capture());
    verify(businessObjectService, never()).save(any(EntityAddressBo.class));
    
    List<EntityAddressBo> deleted = addressBoCaptor.getAllValues();
    assertEquals(1, deleted.size());
    assertEquals(0, adapter.compare(removed, (EntityAddressBo)deleted.get(0)));
    
    removed = current.get(1);
    addresses = createSampleAddresses (new Address[] {
        addr2, addr3
    });
    reset(businessObjectService);
    
    result = importService.mergeImportedBOs(addresses.getAddresses(), current, adapter, "ENTITY_ID");

    assertTrue(result);
    assertEquals(3, current.size());
    verify(businessObjectService, times(1)).delete(addressBoCaptor.capture());
    verify(businessObjectService, never()).save(any(EntityAddressBo.class));
    
    deleted = addressBoCaptor.getAllValues();
    assertEquals(2, deleted.size());
    assertEquals(0, adapter.compare(removed, (EntityAddressBo)deleted.get(1)));

    removed = current.get(0);
    addresses = createSampleAddresses (new Address[] {
        addr1, addr2
    });
    reset(businessObjectService);
    
    result = importService.mergeImportedBOs(addresses.getAddresses(), current, adapter, "ENTITY_ID");

    assertTrue(result);
    assertEquals(3, current.size());
    verify(businessObjectService, times(1)).delete(addressBoCaptor.capture());
    verify(businessObjectService, never()).save(any(EntityAddressBo.class));
    
    deleted = addressBoCaptor.getAllValues();
    assertEquals(3, deleted.size());
    assertEquals(0, adapter.compare(removed, (EntityAddressBo)deleted.get(2)));
    reset(businessObjectService);
  }
    
  @Test
  public void testMergeImportedBOsAddsNewRecords() throws Exception {
    EntityAddressBoAdapter adapter = new EntityAddressBoAdapter();
    Address addr1 = createSampleAddress(false, "A", "A", "A", "A", "A", "A", false, "A", "A");
    Address addr2 = createSampleAddress(false, "B", "B", "B", "B", "B", "B", false, "B", "B");
    Address addr3 = createSampleAddress(true, "Z", "Z", "Z", "Z", "Z", "Z", false, "Z", "Z");

    AddressCollection importAddresses = createSampleAddresses( new Address[] {
        addr1, addr2, addr3
    });    

    List<EntityAddressBo> importBOs = importService.adaptAndSortList("ENTITY_ID", importAddresses.getAddresses(), adapter);
    
    AddressCollection currentAddresses = createSampleAddresses ( new Address[] {
        addr1, addr2
    });
    
    List<EntityAddressBo> currentBOs = importService.adaptAndSortList("ENTITY_ID", currentAddresses.getAddresses(), adapter);
    
    EntityAddressBo additionalRec = importBOs.get(0);
    
    boolean result = importService.mergeImportedBOs(importAddresses.getAddresses(), currentBOs, adapter, "ENTITY_ID");

    assertTrue(result);
    assertEquals(2, currentBOs.size());
    verify(businessObjectService, times(1)).save(addressBoCaptor.capture());
    verify(businessObjectService, never()).delete(any(EntityAddressBo.class));
    
    List<EntityAddressBo> added = addressBoCaptor.getAllValues();
    assertEquals(1, added.size());
    assertEquals(0, adapter.compare(additionalRec, (EntityAddressBo)added.get(0)));
    
    additionalRec = importBOs.get(1);
    currentAddresses = createSampleAddresses (new Address[] {
        addr2, addr3
    });
    currentBOs = importService.adaptAndSortList("ENTITY_ID", currentAddresses.getAddresses(), adapter);
    reset(businessObjectService);
    
    result = importService.mergeImportedBOs(importAddresses.getAddresses(), currentBOs, adapter, "ENTITY_ID");

    assertTrue(result);
    assertEquals(2, currentBOs.size());
    verify(businessObjectService, times(1)).save(addressBoCaptor.capture());
    verify(businessObjectService, never()).delete(any(EntityAddressBo.class));
    
    added = addressBoCaptor.getAllValues();
    assertEquals(2, added.size());
    assertEquals(0, adapter.compare(additionalRec, (EntityAddressBo)added.get(1)));

    additionalRec = importBOs.get(2);
    currentAddresses = createSampleAddresses (new Address[] {
        addr1, addr3
    });
    currentBOs = importService.adaptAndSortList("ENTITY_ID", currentAddresses.getAddresses(), adapter);
    reset(businessObjectService);
    
    result = importService.mergeImportedBOs(importAddresses.getAddresses(), currentBOs, adapter, "ENTITY_ID");

    assertTrue(result);
    assertEquals(2, currentBOs.size());
    verify(businessObjectService, times(1)).save(addressBoCaptor.capture());
    verify(businessObjectService, never()).delete(any(EntityAddressBo.class));
    
    added = addressBoCaptor.getAllValues();
    assertEquals(3, added.size());
    assertEquals(0, adapter.compare(additionalRec, (EntityAddressBo)added.get(2)));
    reset(businessObjectService);

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
