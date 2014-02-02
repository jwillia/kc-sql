package com.rsmart.kuali.coeus.hr.service.impl;

import static org.kuali.kra.logging.BufferedLogger.debug;
import static org.kuali.kra.logging.BufferedLogger.error;
import static org.kuali.kra.logging.BufferedLogger.warn;

import com.rsmart.kuali.coeus.hr.rest.model.Address;
import com.rsmart.kuali.coeus.hr.rest.model.Affiliation;
import com.rsmart.kuali.coeus.hr.rest.model.Degree;
import com.rsmart.kuali.coeus.hr.rest.model.Email;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifest;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifestRecord;
import com.rsmart.kuali.coeus.hr.rest.model.KCExtendedAttributes;
import com.rsmart.kuali.coeus.hr.rest.model.Name;
import com.rsmart.kuali.coeus.hr.rest.model.Phone;
import com.rsmart.kuali.coeus.hr.service.HRManifestImportException;
import com.rsmart.kuali.coeus.hr.service.HRManifestService;

import org.kuali.kra.bo.KcPerson;
import org.kuali.kra.bo.KcPersonExtendedAttributes;
import org.kuali.kra.bo.PersonDegree;
import org.kuali.kra.service.KcPersonService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

public class HRManifestServiceImpl implements HRManifestService {
  // TODO add support for appointments

  private static final String PERSON = "PERSON";
  private IdentityService identityService;
  private BusinessObjectService businessObjectService;
  private KcPersonService kcPersonService;

  public void importHRManifest(final HRManifest manifest)
      throws HRManifestImportException {
    final List<HRManifestRecord> records = manifest.getRecords().getRecords();
    final LinkedList<Object[]> errors = new LinkedList<Object[]>();
    final int numRecords = records.size();
    if (numRecords != manifest.getRecordCount()) {
      throw new IllegalStateException(
          "Manifest record count does NOT match actual record count!");
    }

    for (int i = 0; i < numRecords; i++) {
      final HRManifestRecord record = records.get(i);
      try {
        persist(newInstance(i, record));
      } catch (Exception e) {

    	final StringWriter strWriter = new StringWriter();
        final PrintWriter errorWriter = new PrintWriter(strWriter);
        errorWriter.append("import failed for record ").append(Integer.toString(i))
        		   .append(": ").append(e.getMessage()).append('\n');
        e.printStackTrace(errorWriter);
        errorWriter.flush();
        error(strWriter.toString());
        
        errors.add(new Object[] { new Integer(i + 1), e });
      }
    }

    if (errors.size() > 0) {
      throw new HRManifestImportException(errors);
    }
  }

  public void persist(final EntityBo entity) throws Exception {
    debug("Saving entity: ", entity);
    businessObjectService.save(entity);
    for (final PrincipalBo principal : entity.getPrincipals()) {
      debug("Saving principal: ", principal);
      businessObjectService.save(principal);
    }
    businessObjectService.save(getKcPersonExtendedAttributes(entity));
  }

  protected void delete(final EntityBo entity) {
    debug("Deleting Entity: ", entity);
    businessObjectService.delete(entity.getPrincipals());
    businessObjectService.delete(entity.getNames());
    businessObjectService.delete(entity.getAffiliations());
    businessObjectService.delete(entity.getEmploymentInformation());

    for (final EntityTypeContactInfoBo contactInfo : entity.getEntityTypeContactInfos()) {
      businessObjectService.delete(contactInfo.getAddresses());
      businessObjectService.delete(contactInfo.getPhoneNumbers());
      businessObjectService.delete(contactInfo.getEmailAddresses());
      contactInfo.refresh();
      contactInfo.refreshNonUpdateableReferences();
    }
    businessObjectService.delete(entity.getEntityTypeContactInfos());
    // IIUC this should cascade and delete degrees, appointments, etc.
    businessObjectService.delete(getKcPersonExtendedAttributes(entity));
    entity.refresh();
    entity.refreshNonUpdateableReferences();
    businessObjectService.delete(entity);
  }

  public EntityBo newInstance(final int index, final HRManifestRecord record)
      throws Exception {

    if (record == null) {
      throw new IllegalArgumentException("Cannot create entity for null record");
    }

    final String principalId = record.getPrincipalId();
    debug("importing principal: ", principalId);

    // lookup existing entity for this principalId
    EntityBo retval = EntityBo.from(identityService.getEntity(principalId));

    if (retval != null) {
      // found an existing entity
      debug("replacing existing entity");
      try {
        // get it out of the way - we will populate all of its data from the incoming
        // record
        retval.refresh();
        retval.refreshNonUpdateableReferences();
        delete(retval);
      } catch (Exception e) {
        // Ignore exceptions trying to delete
        warn("Exception deleting existing record", e);
      }
    } else {
      // this is a new entity
      debug("creating new entity");
    }

    // new empty entity
    retval = new EntityBo();

    // map values from the incoming record to KR object model
    updateEntity(retval, record);

    return retval;
  }

  protected void updatePrincipal(final EntityBo entity, final HRManifestRecord record) {
    final PrincipalBo principal = new PrincipalBo();
    final List<PrincipalBo> principals = entity.getPrincipals();

    principal.setEntityId(entity.getId());
    principal.setPrincipalId(record.getPrincipalId());
    principal.setPrincipalName(record.getPrincipalName());
    principal.setActive(true);
    entity.setId(principal.getPrincipalId());

    final int index = principals.indexOf(principal);
    if (index > -1) {
      final PrincipalBo existing = principals.get(index);
      existing.setPrincipalName(record.getPrincipalName());
      existing.setActive(true);
    } else {
      principals.add(principal);
    }

    entity.setActive(true);
  }

  protected void updateAffiliation(final EntityBo entity, final Affiliation affiliation) {
    final EntityAffiliationBo affiliationBo = new EntityAffiliationBo();
    final List<EntityAffiliationBo> affiliations = entity.getAffiliations();

    affiliationBo.setEntityId(entity.getId());
    affiliationBo.setAffiliationTypeCode(affiliation.getAffiliationType());
    affiliationBo.setCampusCode(affiliation.getCampus());
    affiliationBo.setDefaultValue(affiliation.isDefault());
    affiliationBo.setActive(affiliation.isActive());

    int index = affiliations.indexOf(affiliationBo);
    if (index > -1) {
      final EntityAffiliationBo existing = affiliations.get(index);
      existing.setAffiliationTypeCode(affiliation.getAffiliationType());
      existing.setCampusCode(affiliation.getCampus());
      existing.setDefaultValue(affiliation.isDefault());
      existing.setActive(affiliation.isActive());
    } else {
      affiliations.add(affiliationBo);
    }
  }

  /**
   * Finds or prepares the contact info object for a given entity.
   * 
   * @param entity
   * @return
   */
  private EntityTypeContactInfoBo getEntityTypeContactInfoBo(final EntityBo entity) {
    EntityTypeContactInfoBo contactInfo = entity
        .getEntityTypeContactInfoByTypeCode(PERSON);

    if (contactInfo == null) {
      contactInfo = new EntityTypeContactInfoBo();
      contactInfo.setAddresses(new LinkedList<EntityAddressBo>());
      contactInfo.setPhoneNumbers(new LinkedList<EntityPhoneBo>());
      contactInfo.setEmailAddresses(new LinkedList<EntityEmailBo>());
      contactInfo.setEntityTypeCode(PERSON);
      contactInfo.setActive(true);

      entity.getEntityTypeContactInfos().add(contactInfo);
    }
    return contactInfo;
  }

  /**
   * Populates all of the values for an {@link EntityAddressBo} or creates a new one with
   * the appropriate values populated.
   * 
   * @param addressBo
   *          An existing {@link EntityAddressBo} or <code>null</code> if you want to
   *          initialize a new one.
   * @param address
   * @param entity
   * @return
   */
  protected EntityAddressBo mutateAddress(EntityAddressBo addressBo,
      final Address address, final EntityBo entity) {
    if (addressBo == null) {
      addressBo = new EntityAddressBo();
      addressBo.setEntityId(entity.getId());
    }
    addressBo.setAddressTypeCode(address.getAddressTypeCode());
    addressBo.setLine1(address.getAddressLine1());
    addressBo.setLine2(address.getAddressLine2());
    addressBo.setLine3(address.getAddressLine3());
    addressBo.setCity(address.getCity());
    addressBo.setStateProvinceCode(address.getStateOrProvince());
    addressBo.setPostalCode(address.getPostalCode());
    addressBo.setCountryCode(address.getCountry());
    addressBo.setActive(address.isActive());
    addressBo.setDefaultValue(address.isDefault());
    return addressBo;
  }

  protected void updateAddress(final EntityBo entity, final Address address) {
    final List<EntityAddressBo> addresses = getEntityTypeContactInfoBo(entity)
        .getAddresses();
    final EntityAddressBo addressBo = mutateAddress(null, address, entity);

    final int index = addresses.indexOf(addressBo);
    if (index > -1) {
      mutateAddress(addresses.get(index), address, entity);
    } else {
      addresses.add(addressBo);
    }
  }

  protected void updateEmployment(final EntityBo entity, final Affiliation affiliation) {
    final List<EntityEmploymentBo> employmentInformation = entity
        .getEmploymentInformation();
    final EntityEmploymentBo employment = new EntityEmploymentBo();

    employment.setEntityId(entity.getId());
    employment.setEmployeeStatusCode(affiliation.getEmployeeStatus());
    employment.setEmployeeTypeCode(affiliation.getEmployeeType());
    employment.setEmployeeId(affiliation.getEmployeeId());
    employment.setPrimaryDepartmentCode(affiliation.getPrimaryDepartment());
    employment.setPrimary(affiliation.isPrimaryEmployment());
    employment.setBaseSalaryAmount(new KualiDecimal(affiliation.getBaseSalaryAmount()));
    employment.setActive(true);

    final int index = employmentInformation.indexOf(employment);
    if (index > -1) {
      final EntityEmploymentBo existing = employmentInformation.get(index);
      existing.setEmployeeStatusCode(affiliation.getEmployeeStatus());
      existing.setEmployeeTypeCode(affiliation.getEmployeeType());
      existing.setEmployeeId(affiliation.getEmployeeId());
      existing.setPrimaryDepartmentCode(affiliation.getPrimaryDepartment());
      existing.setPrimary(affiliation.isPrimaryEmployment());
      existing.setBaseSalaryAmount(new KualiDecimal(affiliation.getBaseSalaryAmount()));
      existing.setActive(true);
    } else {
      employmentInformation.add(employment);
    }
  }

  protected void updateName(final EntityBo entity, final Name name) {
    final List<EntityNameBo> names = entity.getNames();
    final EntityNameBo nameBo = new EntityNameBo();

    nameBo.setEntityId(entity.getId());
    nameBo.setNameCode(name.getNameCode());
    nameBo.setFirstName(name.getFirstName());
    nameBo.setMiddleName(name.getMiddleName());
    nameBo.setLastName(name.getLastName());
    nameBo.setActive(name.isActive());
    nameBo.setDefaultValue(name.isDefault());

    final int index = names.indexOf(nameBo);
    if (index > -1) {
      final EntityNameBo existing = names.get(index);
      existing.setNameCode(name.getNameCode());
      existing.setFirstName(name.getFirstName());
      existing.setMiddleName(name.getMiddleName());
      existing.setLastName(name.getLastName());
      existing.setActive(name.isActive());
      existing.setDefaultValue(name.isDefault());
    } else {
      names.add(nameBo);
    }
  }

  protected void updatePhone(final EntityBo entity, final Phone phone) {
    final List<EntityPhoneBo> phoneNumbers = getEntityTypeContactInfoBo(entity)
        .getPhoneNumbers();
    final EntityPhoneBo phoneBo = new EntityPhoneBo();

    phoneBo.setPhoneTypeCode(phone.getPhoneType());
    phoneBo.setPhoneNumber(phone.getPhoneNumber());
    phoneBo.setActive(phone.isActive());
    phoneBo.setDefaultValue(phone.isDefault());
    phoneBo.setEntityId(entity.getId());
    phoneBo.setCountryCode(phone.getCountry());

    final int index = phoneNumbers.indexOf(phoneBo);
    if (index > -1) {
      final EntityPhoneBo existing = phoneNumbers.get(index);
      existing.setPhoneTypeCode(phone.getPhoneType());
      existing.setPhoneNumber(phone.getPhoneNumber());
      existing.setActive(phone.isActive());
      existing.setDefaultValue(phone.isDefault());
      existing.setEntityId(entity.getId());
      existing.setCountryCode(phone.getCountry());
    } else {
      phoneNumbers.add(phoneBo);
    }
  }

  protected void updateEmail(final EntityBo entity, final Email email) {
    final List<EntityEmailBo> emailAddresses = getEntityTypeContactInfoBo(entity)
        .getEmailAddresses();
    final EntityEmailBo emailBo = new EntityEmailBo();

    emailBo.setEntityId(entity.getId());
    emailBo.setEmailTypeCode(email.getEmailType());
    emailBo.setEmailAddress(email.getEmailAddress());
    emailBo.setActive(email.isActive());
    emailBo.setDefaultValue(email.isDefault());

    final int index = emailAddresses.indexOf(emailBo);
    if (index > -1) {
      final EntityEmailBo existing = emailAddresses.get(index);
      existing.setEmailTypeCode(email.getEmailType());
      existing.setEmailAddress(email.getEmailAddress());
      existing.setActive(email.isActive());
      existing.setDefaultValue(email.isDefault());
    } else {
      emailAddresses.add(emailBo);
    }
  }

  protected void updateEntity(final EntityBo entity, final HRManifestRecord record) {
    updatePrincipal(entity, record);
    for (Affiliation affiliation : record.getAffiliations().getAffiliations()) {
      updateAffiliation(entity, affiliation);
      updateEmployment(entity, affiliation);
    }
    for (final Address address : record.getAddresses().getAddresses()) {
      updateAddress(entity, address);
    }
    for (final Name name : record.getNames().getNames()) {
      updateName(entity, name);
    }
    for (final Phone phone : record.getPhones().getPhones()) {
      updatePhone(entity, phone);
    }
    for (final Email email : record.getEmails().getEmails()) {
      updateEmail(entity, email);
    }
    updateKcExtendedAttributes(entity, record.getKcExtendedAttributes());
    for (final Degree degree : record.getDegrees().getDegrees()) {
      updatePersonDegree(entity, degree);
    }
  }

  /**
   * Get the extended attributes for a given entity
   * 
   * @param entity
   * @return
   */
  private KcPersonExtendedAttributes getKcPersonExtendedAttributes(final EntityBo entity) {
    final KcPerson kcPerson = kcPersonService.getKcPersonByPersonId(entity.getId());
    if (kcPerson == null) {
      throw new IllegalStateException("could not find a KcPerson for entity: "
          + entity.getId());
    }
    final KcPersonExtendedAttributes kcpea = kcPerson.getExtendedAttributes();
    if (kcpea == null) {
      throw new IllegalStateException(
          "could not find a KcPersonExtendedAttributes for entity: " + entity.getId());
    }
    return kcpea;
  }

  protected void updateKcExtendedAttributes(final EntityBo entity,
      final KCExtendedAttributes attr) {
    final KcPersonExtendedAttributes kcpea = getKcPersonExtendedAttributes(entity);
    kcpea.setAgeByFiscalYear(attr.getAgeByFiscalYear());
    kcpea.setCitizenshipTypeCode(attr.getCitizenshipType());
    kcpea.setCounty(attr.getCounty());
    kcpea.setDegree(attr.getDegree());
    kcpea.setDirectoryDepartment(attr.getDirectoryDepartment());
    kcpea.setDirectoryTitle(attr.getDirectoryTitle());
    kcpea.setEducationLevel(attr.getEducationLevel());
    kcpea.setHandicappedFlag(attr.isHandicapped());
    kcpea.setHandicapType(attr.getHandicapType());
    kcpea.setHasVisa(attr.isVisa());
    kcpea.setIdProvided(attr.getIdProvided());
    kcpea.setIdVerified(attr.getIdVerified());
    kcpea.setMajor(attr.getMajor());
    kcpea.setMultiCampusPrincipalId(attr.getMultiCampusPrincipalId());
    kcpea.setMultiCampusPrincipalName(attr.getMultiCampusPrincipalName());
    kcpea.setOfficeLocation(attr.getOfficeLocation());
    kcpea.setOnSabbaticalFlag(attr.isOnSabbatical());
    kcpea.setPersonId(entity.getId());
    kcpea.setPrimaryTitle(attr.getPrimaryTitle());
    kcpea.setRace(attr.getRace());
    kcpea.setSalaryAnniversaryDate(new java.sql.Date(attr.getSalaryAnniversaryDate()
        .getTime()));
    kcpea.setSchool(attr.getSchool());
    kcpea.setSecondaryOfficeLocation(attr.getSecondaryOfficeLocation());
    kcpea.setVacationAccrualFlag(attr.isVacationAccrual());
    kcpea.setVeteranFlag(attr.isVeteran());
    kcpea.setVeteranType(attr.getVeteranType());
    kcpea.setVisaCode(attr.getVisaCode());
    kcpea.setVisaRenewalDate(new java.sql.Date(attr.getVisaRenewalDate().getTime()));
    kcpea.setVisaType(attr.getVisaType());
    kcpea.setYearGraduated(attr.getYearGraduated().toString());
  }

  /**
   * Update a degree for a KC person.
   * 
   * @param entity
   * @param degree
   */
  protected void updatePersonDegree(final EntityBo entity, final Degree degree) {
    final PersonDegree personDegree = new PersonDegree();
    personDegree.setPersonId(entity.getId());
    personDegree.setDegreeCode(degree.getDegreeCode());
    personDegree.setDegree(degree.getDegree());
    personDegree.setFieldOfStudy(degree.getFieldOfStudy());
    personDegree.setGraduationYear(degree.getGraduationYear().toString());
    personDegree.setSchool(degree.getSchool());
    personDegree.setSchoolId(degree.getSchoolId());
    personDegree.setSchoolIdCode(degree.getSchoolIdCode());
    personDegree.setSpecialization(degree.getSpecialization());

    final List<PersonDegree> degrees = getKcPersonExtendedAttributes(entity)
        .getPersonDegrees();
    final int index = degrees.indexOf(personDegree);
    if (index > -1) {
      final PersonDegree existing = degrees.get(index);
      existing.setDegreeCode(degree.getDegreeCode());
      existing.setDegree(degree.getDegree());
      existing.setFieldOfStudy(degree.getFieldOfStudy());
      existing.setGraduationYear(degree.getGraduationYear().toString());
      existing.setSchool(degree.getSchool());
      existing.setSchoolId(degree.getSchoolId());
      existing.setSchoolIdCode(degree.getSchoolIdCode());
      existing.setSpecialization(degree.getSpecialization());
    } else {
      degrees.add(personDegree);
    }
  }

  public void setIdentityService(IdentityService identityService) {
    this.identityService = identityService;
  }

  public void setBusinessObjectService(BusinessObjectService businessObjectService) {
    this.businessObjectService = businessObjectService;
  }

  public void setKcPersonService(KcPersonService kcPersonService) {
    this.kcPersonService = kcPersonService;
  }

}
