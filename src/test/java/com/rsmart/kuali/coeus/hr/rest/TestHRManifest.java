package com.rsmart.kuali.coeus.hr.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.rsmart.kuali.coeus.hr.rest.model.Address;
import com.rsmart.kuali.coeus.hr.rest.model.Addresses;
import com.rsmart.kuali.coeus.hr.rest.model.Affiliation;
import com.rsmart.kuali.coeus.hr.rest.model.Affiliations;
import com.rsmart.kuali.coeus.hr.rest.model.Appointment;
import com.rsmart.kuali.coeus.hr.rest.model.Appointments;
import com.rsmart.kuali.coeus.hr.rest.model.Degree;
import com.rsmart.kuali.coeus.hr.rest.model.Degrees;
import com.rsmart.kuali.coeus.hr.rest.model.Email;
import com.rsmart.kuali.coeus.hr.rest.model.Emails;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifest;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifestRecord;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifestRecords;
import com.rsmart.kuali.coeus.hr.rest.model.KCExtendedAttributes;
import com.rsmart.kuali.coeus.hr.rest.model.Name;
import com.rsmart.kuali.coeus.hr.rest.model.Names;
import com.rsmart.kuali.coeus.hr.rest.model.Phone;
import com.rsmart.kuali.coeus.hr.rest.model.Phones;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class TestHRManifest {

  public static void validateTestHRManifest(HRManifest manifest) throws Exception {
    // check top level manifest data
    assertNotNull(manifest);
    assertEquals("foo", manifest.getSubmitterId());
    assertEquals("00001", manifest.getTransactionId());
    assertEquals(1, manifest.getRecordCount());
    Date subDate = manifest.getReportDate();
    assertNotNull(subDate);

    // check submission date
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(0l); // get rid of any extra milliseconds
    cal.set(2013, 4, 20, 13, 03, 0);
    assertEquals(new Date(cal.getTimeInMillis()), subDate);

    // check records - should be 1, the John Doe record
    HRManifestRecords records = manifest.getRecords();
    assertNotNull(records);
    List<HRManifestRecord> recordList = records.getRecords();
    assertEquals(1, recordList.size());

    // get the John Doe record - we just know the content of exampleImport.xml, so know
    // this should be jdoe
    HRManifestRecord record = recordList.get(0);
    assertNotNull(record);

    Addresses addresses = record.getAddresses();
    List<Address> addressList = addresses.getAddresses();
    assertNotNull(addressList);
    assertEquals(1, addressList.size());

    Address address = addressList.get(0);
    assertNotNull(address);
    assertEquals("HOME", address.getAddressType());
    assertEquals("1234 E. Main St.", address.getAddressLine1());
    assertEquals("Apt. 8C", address.getAddressLine2());
    assertNull(address.getAddressLine3());
    assertEquals("Minneapolis", address.getCity());
    assertEquals("MN", address.getStateOrProvince());
    assertEquals("34567", address.getPostalCode());
    assertEquals("US", address.getCountry());
    assertTrue(address.isDefault());
    assertTrue(address.isActive());

    Affiliations affiliations = record.getAffiliations();
    List<Affiliation> affiliationList = affiliations.getAffiliations();
    assertNotNull(affiliationList);
    assertEquals(1, affiliationList.size());

    Affiliation affiliation = affiliationList.get(0);
    assertNotNull(affiliation);
    assertEquals("affType", affiliation.getAffiliationType());
    assertEquals("MN", affiliation.getCampus());
    assertEquals("ACTIVE", affiliation.getEmployeeStatus());
    assertEquals("APPOINTED", affiliation.getEmployeeType());
    assertEquals(50180.00f, affiliation.getBaseSalaryAmount(), 0.0f);
    assertEquals("MATH", affiliation.getPrimaryDepartment());
    assertEquals("092001234", affiliation.getEmployeeId());
    assertTrue(affiliation.isPrimaryEmployment());
    assertTrue(affiliation.isDefault());
    assertTrue(affiliation.isActive());

    Names names = record.getNames();
    List<Name> nameList = names.getNames();
    assertNotNull(nameList);
    assertEquals(1, nameList.size());

    Name name = nameList.get(0);
    assertNotNull(name);
    assertEquals("NAME", name.getNameCode());
    assertEquals("Mr.", name.getPrefix());
    assertEquals("Jonathon", name.getFirstName());
    assertEquals("Doe", name.getLastName());
    assertTrue(name.isDefault());
    assertTrue(name.isActive());

    Phones phones = record.getPhones();
    List<Phone> phoneList = phones.getPhones();
    assertNotNull(phoneList);
    assertEquals(2, phoneList.size());

    Phone phone = phoneList.get(0);
    assertNotNull(phone);
    assertEquals("OFFICE", phone.getPhoneType());
    assertEquals("204-391-1101", phone.getPhoneNumber());
    assertEquals("US", phone.getCountry());
    assertTrue(phone.isDefault());
    assertTrue(phone.isActive());

    phone = phoneList.get(1);
    assertNotNull(phone);
    assertEquals("HOME", phone.getPhoneType());
    assertEquals("204-718-3221", phone.getPhoneNumber());
    assertEquals("US", phone.getCountry());
    assertTrue(!phone.isDefault());
    assertTrue(phone.isActive());

    Emails emails = record.getEmails();
    List<Email> emailList = emails.getEmails();
    assertNotNull(emailList);
    assertEquals(1, emailList.size());

    Email email = emailList.get(0);
    assertNotNull(email);
    assertEquals("WORK", email.getEmailType());
    assertEquals("jdoe@university.edu", email.getEmailAddress());
    assertTrue(email.isDefault());
    assertTrue(email.isActive());

    KCExtendedAttributes atts = record.getKcExtendedAttributes();
    assertNotNull(atts);
    assertEquals("US", atts.getCountry());
    assertEquals(52, atts.getAgeByFiscalYear());
    assertEquals("CAUCASIAN", atts.getRace());
    assertEquals("PHD", atts.getEducationLevel());
    assertEquals("PHD", atts.getDegree());
    assertEquals("MATHEMATICS", atts.getMajor());
    assertTrue(!atts.isHandicapped());
    assertTrue(atts.isVeteran());
    assertEquals("A", atts.getVeteranType());
    assertTrue(!atts.isVisa());
    assertEquals("503 Carter Hall", atts.getOfficeLocation());
    assertEquals("MATH", atts.getSchool());
    assertEquals("1983", atts.getYearGraduated());
    assertEquals("MATH", atts.getDirectoryDepartment());
    assertTrue(atts.isVacationAccrual());
    assertEquals("John Doe", atts.getIdProvided());
    assertEquals("???", atts.getIdVerified());
    assertEquals("1", atts.getCitizenshipType());

    Calendar expected = Calendar.getInstance();

    expected.setTimeInMillis(0);
    expected.set(2010, 0, 1, 0, 0, 0);
    assertEquals(new Date(expected.getTimeInMillis()), atts.getSalaryAnniversaryDate());

    Degrees degrees = record.getDegrees();
    List<Degree> degreeList = degrees.getDegrees();
    assertNotNull(degreeList);
    assertEquals(1, degreeList.size());

    Degree degree = degreeList.get(0);
    assertEquals("PhD", degree.getDegree());
    assertEquals("GRAD", degree.getDegreeType());
    assertEquals("1983", degree.getGraduationYear());
    assertEquals("Mathematics", degree.getFieldOfStudy());
    assertEquals("non-linear algebra", degree.getSpecialization());
    assertEquals("Texas A&M University", degree.getSchool());
    assertEquals("1234", degree.getSchoolId());
    assertEquals("UMN", degree.getSchoolIdCode());

    Appointments appointments = record.getAppointments();
    List<Appointment> appointmentList = appointments.getAppointments();
    assertNotNull(appointmentList);
    assertEquals(1, appointmentList.size());

    Appointment appointment = appointmentList.get(0);
    assertEquals("MATH", appointment.getUnit());
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

  private String readXML(final String xmlFile) throws Exception {
    final InputStream bis = getClass().getResourceAsStream(xmlFile);
    final StringBuffer sb = new StringBuffer();

    byte chunk[] = new byte[64000];
    int bytesRead = 0;

    while ((bytesRead = bis.read(chunk)) > -1) {
      sb.append(new String(chunk, 0, bytesRead));
    }
    return sb.toString();
  }

  public void validateXML(String xml) throws Exception {
    String schemaLang = "http://www.w3.org/2001/XMLSchema";

    SchemaFactory factory = SchemaFactory.newInstance(schemaLang);

    Source source = new StreamSource(new StringReader(xml));
    Schema schema = factory.newSchema(new StreamSource(getClass().getResourceAsStream(
        "/hrmanifest.xsd")));
    Validator validator = schema.newValidator();

    validator.validate(source);
  }

  @Test
  public void testParseXML() throws Exception {
    final String xml = readXML("/exampleimport.xml");

    validateXML(xml);

    // create JAXB context and instantiate marshaller
    JAXBContext context = JAXBContext.newInstance(HRManifest.class);
    Unmarshaller um = context.createUnmarshaller();
    HRManifest manifest = (HRManifest) um.unmarshal(new StringReader(xml));

    TestHRManifest.validateTestHRManifest(manifest);
  }
}
