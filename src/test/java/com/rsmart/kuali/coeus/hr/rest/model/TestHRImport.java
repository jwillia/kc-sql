package com.rsmart.kuali.coeus.hr.rest.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
import com.rsmart.kuali.coeus.hr.rest.model.DOMHRImport;
import com.rsmart.kuali.coeus.hr.rest.model.HRImportRecord;
import com.rsmart.kuali.coeus.hr.rest.model.KCExtendedAttributes;
import com.rsmart.kuali.coeus.hr.rest.model.Name;
import com.rsmart.kuali.coeus.hr.rest.model.NameCollection;
import com.rsmart.kuali.coeus.hr.rest.model.Phone;
import com.rsmart.kuali.coeus.hr.rest.model.PhoneCollection;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class TestHRImport {
	
  @Test
  public void testParseXML() throws Exception {
    final String xml = readXML("/exampleImport.xml");

    validateXML(xml);

    // create JAXB context and instantiate marshaller
    final JAXBContext context = JAXBContext.newInstance(DOMHRImport.class);
    final Unmarshaller um = context.createUnmarshaller();
    final HRImport toImport = (HRImport) um.unmarshal(new StringReader(xml));

    TestHRImport.validateTestHRImport(toImport);
  }
  
  @Test
  public void testValidationOfAddress() throws Exception {
    final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    final javax.validation.Validator validator = factory.getValidator();
    
    Address address = new Address();
    
    Set<ConstraintViolation<Address>> constraintViolations = validator.validate(address);
    
    assertEquals( 5, constraintViolations.size());
  }
  
/*
  @Test
  public void testValidationAppliesToSubclasses() throws Exception {
    final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    final javax.validation.Validator validator = factory.getValidator();

    Address address = new Address();
    AddressCollection addresses = new AddressCollection();
    addresses.getAddresses().add(address);
    HRImportRecord record = new HRImportRecord();
    record.setAddressCollection(addresses);
    HRImportRecordCollection records = new HRImportRecordCollection();
    records.getRecords().add(record);
    HRImport toImport = new HRImport();
    toImport.setRecords(records);
    
    Set<ConstraintViolation<HRImport>> constraintViolations = validator.validate(toImport);
    
    int importViolations = 0;
    int addressViolations = 0;
    
    for (ConstraintViolation<HRImport> violation : constraintViolations) {
      Path path = violation.getPropertyPath();

      if (path.toString().startsWith("records.records[0].principal")) {
        importViolations++;
      } else if (path.toString().startsWith("records.records[0].addressCollection.addresses[0]")) {
        addressViolations++;
      }
    }
    assertEquals(8, constraintViolations.size());
    assertEquals(6, addressViolations);
    assertEquals(2, importViolations);
  }
*/
  
  public static void validateTestHRImport(HRImport toImport) throws Exception {
    // check top level toImport data
    assertNotNull(toImport);
    assertEquals(new BigDecimal("1.0"), toImport.getSchemaVersion());
    assertEquals(1, toImport.getRecordCount());

    // check records - should be 1, the John Doe record
    final HRImportRecordCollection records = toImport.getRecords();
    assertNotNull(records);
    final Iterator<HRImportRecord> recordList = records.iterator();
    assertTrue (recordList.hasNext());

    // get the John Doe record - we just know the content of exampleImport.xml, so know
    // this should be jdoe
    final HRImportRecord record = recordList.next();
    assertNotNull(record);

    assertTrue(!recordList.hasNext());
    
    final AddressCollection addresses = record.getAddressCollection();
    final List<Address> addressList = addresses.getAddresses();
    assertNotNull(addressList);
    assertEquals(1, addressList.size());

    final Address address = addressList.get(0);
    assertNotNull(address);
    assertEquals("HM", address.getAddressTypeCode());
    assertEquals("1234 E. Main St.", address.getAddressLine1());
    assertEquals("Apt. 8C", address.getAddressLine2());
    assertNull(address.getAddressLine3());
    assertEquals("Minneapolis", address.getCity());
    assertEquals("MN", address.getStateOrProvince());
    assertEquals("34567", address.getPostalCode());
    assertEquals("US", address.getCountry());
    assertTrue(address.isDefault());
    assertTrue(address.isActive());

    final AffiliationCollection affiliations = record.getAffiliationCollection();
    final List<Affiliation> affiliationList = affiliations.getAffiliations();
    assertNotNull(affiliationList);
    assertEquals(1, affiliationList.size());

    final Affiliation affiliation = affiliationList.get(0);
    assertNotNull(affiliation);
    assertEquals("FCLTY", affiliation.getAffiliationType());
    assertEquals("MN", affiliation.getCampus());
    assertTrue(affiliation.isDefault());
    assertTrue(affiliation.isActive());
    
    final Employment employment = affiliation.getEmployment();
    assertEquals("A", employment.getEmployeeStatus());
    assertEquals("P", employment.getEmployeeType());
    assertEquals(50180.00f, employment.getBaseSalaryAmount(), 0.0f);
    assertEquals("MATH", employment.getPrimaryDepartment());
    assertEquals("092001234", employment.getEmployeeId());
    assertTrue(employment.isPrimaryEmployment());

    final NameCollection names = record.getNameCollection();
    List<Name> nameList = names.getNames();
    assertNotNull(nameList);
    assertEquals(1, nameList.size());

    final Name name = nameList.get(0);
    assertNotNull(name);
    assertEquals("PRM", name.getNameCode());
    assertEquals("Mr", name.getPrefix());
    assertEquals("Jonathon", name.getFirstName());
    assertEquals("Doe", name.getLastName());
    assertTrue(name.isDefault());
    assertTrue(name.isActive());

    final PhoneCollection phones = record.getPhoneCollection();
    List<Phone> phoneList = phones.getPhones();
    assertNotNull(phoneList);
    assertEquals(2, phoneList.size());

    Phone phone = phoneList.get(0);
    assertNotNull(phone);
    assertEquals("WRK", phone.getPhoneType());
    assertEquals("204-391-1101", phone.getPhoneNumber());
    assertEquals("US", phone.getCountry());
    assertTrue(phone.isDefault());
    assertTrue(phone.isActive());

    phone = phoneList.get(1);
    assertNotNull(phone);
    assertEquals("HM", phone.getPhoneType());
    assertEquals("204-718-3221", phone.getPhoneNumber());
    assertEquals("US", phone.getCountry());
    assertTrue(!phone.isDefault());
    assertTrue(phone.isActive());

    final EmailCollection emails = record.getEmailCollection();
    final List<Email> emailList = emails.getEmails();
    assertNotNull(emailList);
    assertEquals(1, emailList.size());

    final Email email = emailList.get(0);
    assertNotNull(email);
    assertEquals("WRK", email.getEmailType());
    assertEquals("jdoe@university.edu", email.getEmailAddress());
    assertTrue(email.isDefault());
    assertTrue(email.isActive());

    final KCExtendedAttributes atts = record.getKcExtendedAttributes();
    assertNotNull(atts);
    assertEquals("Maricopa", atts.getCounty());
    assertEquals(52, atts.getAgeByFiscalYear());
    assertEquals("CAUCASIAN", atts.getRace());
    assertEquals("PHD", atts.getEducationLevel());
    assertEquals("PhD", atts.getDegree());
    assertEquals("MATHEMATICS", atts.getMajor());
    assertTrue(atts.isHandicapped());
    assertTrue(atts.isVeteran());
    assertEquals("A", atts.getVeteranType());
    assertTrue(atts.isVisa());
    assertEquals("503 Carter Hall", atts.getOfficeLocation());
    assertEquals("MATH", atts.getSchool());
    assertEquals(new Integer("1983"), atts.getYearGraduated());
    assertEquals("MATH", atts.getDirectoryDepartment());
    assertTrue(atts.isVacationAccrual());
    assertEquals("John_Doe", atts.getIdProvided());
    assertEquals("John_Doe_Verified", atts.getIdVerified());
    assertEquals(new Integer(1), atts.getCitizenshipType());

    final Calendar expected = Calendar.getInstance();

    expected.setTimeInMillis(0);
    expected.set(2010, 0, 1, 0, 0, 0);
    assertEquals(new Date(expected.getTimeInMillis()), atts.getSalaryAnniversaryDate());

    final DegreeCollection degrees = record.getDegreeCollection();
    final List<Degree> degreeList = degrees.getDegrees();
    assertNotNull(degreeList);
    assertEquals(1, degreeList.size());

    final Degree degree = degreeList.get(0);
    assertEquals("PhD", degree.getDegree());
    assertEquals("DD", degree.getDegreeCode());
    assertEquals(new Integer(1983), degree.getGraduationYear());
    assertEquals("Mathematics", degree.getFieldOfStudy());
    assertEquals("non-linear algebra", degree.getSpecialization());
    assertEquals("Texas A&M University", degree.getSchool());
    assertEquals("1234", degree.getSchoolId());
    assertEquals("UMN", degree.getSchoolIdCode());

    final AppointmentCollection appointments = record.getAppointmentCollection();
    List<Appointment> appointmentList = appointments.getAppointments();
    assertNotNull(appointmentList);
    assertEquals(1, appointmentList.size());

    final Appointment appointment = appointmentList.get(0);
    assertEquals("IN-CARD", appointment.getUnitNumber());
    assertEquals("0010", appointment.getJobCode());
    assertEquals("APP", appointment.getAppointmentType());
    assertEquals(50180.00f, appointment.getSalary(), 0.00f);
    assertEquals(new Date(expected.getTimeInMillis()), appointment.getStartDate());
    assertEquals("Professor", appointment.getJobTitle());
    assertEquals("Professor", appointment.getPreferedJobTitle());
  }

  @Before
  public void configureLogging() throws Exception {
    final Logger logger = Logger.getLogger("javax.xml.bind");
    logger.setLevel(Level.ALL);
    logger.addHandler(new ConsoleHandler());

    logger.fine("Testing logger");
  }

  /**
   * Reads an XML file from the class path.
   * 
   * @param xmlFile
   * @return
   * @throws Exception
   */
  private String readXML(final String xmlFile) throws Exception {
    final InputStream bis = getClass().getResourceAsStream(xmlFile);
    final StringBuilder sb = new StringBuilder();

    final byte chunk[] = new byte[64000];
    int bytesRead = 0;

    while ((bytesRead = bis.read(chunk)) > -1) {
      sb.append(new String(chunk, 0, bytesRead));
    }
    return sb.toString();
  }

  /**
   * Verify the XML file validates against the schema.
   * 
   * @param xml
   * @throws Exception
   */
  public void validateXML(final String xml) throws Exception {
    final String schemaLang = "http://www.w3.org/2001/XMLSchema";

    final SchemaFactory factory = SchemaFactory.newInstance(schemaLang);

    final Source source = new StreamSource(new StringReader(xml));
    final Schema schema = factory.newSchema(new StreamSource(getClass()
        .getResourceAsStream("/hrmanifest.xsd")));
    final Validator validator = schema.newValidator();

    validator.validate(source);
  }
}
