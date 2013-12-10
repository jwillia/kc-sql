package com.rsmart.kuali.coeus.hr.service.impl;

import static org.kuali.kra.logging.BufferedLogger.debug;
import static org.kuali.kra.logging.BufferedLogger.error;
import static org.kuali.kra.logging.BufferedLogger.warn;

import com.rsmart.kuali.coeus.hr.rest.model.Address;
import com.rsmart.kuali.coeus.hr.rest.model.Affiliation;
import com.rsmart.kuali.coeus.hr.rest.model.Email;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifest;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifestRecord;
import com.rsmart.kuali.coeus.hr.rest.model.Name;
import com.rsmart.kuali.coeus.hr.rest.model.Phone;
import com.rsmart.kuali.coeus.hr.service.HRManifestImportException;
import com.rsmart.kuali.coeus.hr.service.HRManifestService;

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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HRManifestServiceImpl implements HRManifestService {
  // TODO add support for KC Extended Attributes
  // TODO add support for degrees
  // TODO add support for appointments

  private static final String PERSON = "PERSON";
  private IdentityService identityService;
  private BusinessObjectService businessObjectService;

  public void importHRManifest(final HRManifest manifest) throws HRManifestImportException {
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
        error("import failed for record #: " + i, e);
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
  }

  protected void delete(final EntityBo entity) {
    // debug("Deleting Entity: ", entity);
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

    principal.setPrincipalId(record.getPrincipalId());
    principal.setPrincipalName(record.getPrincipalName());
    entity.setId(principal.getPrincipalId());
    principal.setEntityId(entity.getId());
    principal.setActive(true);

    int index = -1;
    if ((index = entity.getPrincipals().indexOf(principal)) > -1) {
      PrincipalBo existing = entity.getPrincipals().get(index);
      existing.setPrincipalName(principal.getPrincipalName());
    } else {
      entity.getPrincipals().add(principal);
    }

    entity.setActive(true);
  }

  protected void updateAffiliation(final EntityBo entity, final Affiliation affiliation) {
    final EntityAffiliationBo affiliationBo = new EntityAffiliationBo();

    affiliationBo.setAffiliationTypeCode(affiliation.getAffiliationType());
    affiliationBo.setCampusCode(affiliation.getCampus());
    affiliationBo.setDefaultValue(affiliation.isDefault());
    affiliationBo.setActive(affiliation.isActive());
    affiliationBo.setEntityId(entity.getId());

    int index = -1;
    if ((index = entity.getAffiliations().indexOf(affiliationBo)) > -1) {
      EntityAffiliationBo existing = entity.getAffiliations().get(index);
      existing.setDefaultValue(affiliationBo.getDefaultValue());
    } else {
      entity.getAffiliations().add(affiliationBo);
    }
  }

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

  protected void updateAddress(final EntityBo entity, final Address address) {
    final EntityTypeContactInfoBo contactInfo = getEntityTypeContactInfoBo(entity);
    final EntityAddressBo addressBo = new EntityAddressBo();

    addressBo.setAddressTypeCode(address.getAddressType());
    addressBo.setLine1(address.getAddressLine1());
    addressBo.setLine2(address.getAddressLine2());
    addressBo.setLine3(address.getAddressLine3());
    addressBo.setCity(address.getCity());
    addressBo.setStateProvinceCode(address.getStateOrProvince());
    addressBo.setPostalCode(address.getPostalCode());
    addressBo.setCountryCode(address.getCountry());
    addressBo.setActive(address.isActive());
    addressBo.setDefaultValue(address.isDefault());
    addressBo.setEntityId(entity.getId());

    int index = -1;
    if ((index = contactInfo.getAddresses().indexOf(addressBo)) > -1) {
      final EntityAddressBo existing = contactInfo.getAddresses().get(index);
      existing.setAddressTypeCode(addressBo.getAddressTypeCode());
      existing.setDefaultValue(addressBo.getDefaultValue());
      existing.setActive(addressBo.getActive());
    } else {
      contactInfo.getAddresses().add(addressBo);
    }
  }

  protected void updateEmployment(final EntityBo entity, final Affiliation affiliation) {
    final EntityEmploymentBo employment = new EntityEmploymentBo();

    employment.setEmployeeStatusCode(affiliation.getEmployeeStatus());
    employment.setEmployeeTypeCode(affiliation.getEmployeeType());
    employment.setEmployeeId(affiliation.getEmployeeId());
    employment.setPrimaryDepartmentCode(affiliation.getPrimaryDepartment());
    employment.setPrimary(affiliation.isPrimaryEmployment());
    KualiDecimal baseSal = new KualiDecimal(affiliation.getBaseSalaryAmount());
    employment.setBaseSalaryAmount(baseSal);

    employment.setActive(true);
    employment.setEntityId(entity.getId());

    int index = -1;
    if ((index = entity.getEmploymentInformation().indexOf(employment)) > -1) {
      EntityEmploymentBo existing = entity.getEmploymentInformation().get(index);
      existing.setBaseSalaryAmount(employment.getBaseSalaryAmount());
      existing.setPrimary(employment.getPrimary());
      existing.setEmployeeStatusCode(employment.getEmployeeStatusCode());
      existing.setEmployeeTypeCode(employment.getEmployeeTypeCode());
    } else {
      entity.getEmploymentInformation().add(employment);
    }
  }

  protected void updateName(final EntityBo entity, final Name name) {
    final EntityNameBo nameBo = new EntityNameBo();

    nameBo.setNameCode(name.getNameCode());
    nameBo.setFirstName(name.getFirstName());
    // TODO add support for middle name
    nameBo.setLastName(name.getLastName());
    nameBo.setActive(name.isActive());
    nameBo.setDefaultValue(name.isDefault());

    int index = -1;
    if ((index = entity.getNames().indexOf(nameBo)) > -1) {
      EntityNameBo existing = entity.getNames().get(index);
      existing.setLastName(name.getLastName());
      existing.setFirstName(name.getFirstName());
      existing.setNameSuffix(name.getSuffix());
    } else {
      entity.getNames().add(nameBo);
    }
    nameBo.setEntityId(entity.getId());
  }

  protected void updatePhone(final EntityBo entity, final Phone phone) {
    final EntityTypeContactInfoBo contactInfo = getEntityTypeContactInfoBo(entity);
    final EntityPhoneBo phoneBo = new EntityPhoneBo();

    phoneBo.setPhoneTypeCode(phone.getPhoneType());
    phoneBo.setPhoneNumber(phone.getPhoneNumber());
    phoneBo.setActive(phone.isActive());
    phoneBo.setDefaultValue(phone.isDefault());
    phoneBo.setEntityId(entity.getId());
    phoneBo.setCountryCode(phone.getCountry());

    int index = -1;
    if ((index = contactInfo.getPhoneNumbers().indexOf(phoneBo)) > -1) {
      EntityPhoneBo existing = contactInfo.getPhoneNumbers().get(index);

      existing.setPhoneTypeCode(phoneBo.getPhoneTypeCode());
      existing.setDefaultValue(phoneBo.getDefaultValue());
      existing.setActive(phoneBo.getActive());
    } else {
      contactInfo.getPhoneNumbers().add(phoneBo);
    }
  }

  protected void updateEmail(final EntityBo entity, final Email email) {
    final EntityTypeContactInfoBo contactInfo = getEntityTypeContactInfoBo(entity);
    final EntityEmailBo emailBo = new EntityEmailBo();

    emailBo.setEmailTypeCode(email.getEmailType());
    emailBo.setEmailAddress(email.getEmailAddress());
    emailBo.setActive(email.isActive());
    emailBo.setDefaultValue(email.isDefault());
    emailBo.setEntityId(entity.getId());

    int index = -1;
    if ((index = contactInfo.getEmailAddresses().indexOf(emailBo)) > -1) {
      final EntityEmailBo existing = contactInfo.getEmailAddresses().get(index);
      existing.setEmailTypeCode(emailBo.getEmailTypeCode());
      existing.setDefaultValue(emailBo.getDefaultValue());
      existing.setActive(emailBo.getActive());
    } else {
      contactInfo.getEmailAddresses().add(emailBo);
    }
  }

  protected void updateEntity(final EntityBo entity, HRManifestRecord record) {
    updatePrincipal(entity, record);
    for (Affiliation affiliation : record.getAffiliations().getAffiliations()) {
      updateAffiliation(entity, affiliation);
      updateEmployment(entity, affiliation);
    }
    for (final Address address : record.getAddresses().getAddresses()) {
      updateAddress(entity, address);
    }
    for (Name name : record.getNames().getNames()) {
      updateName(entity, name);
    }
    for (Phone phone : record.getPhones().getPhones()) {
      updatePhone(entity, phone);
    }
    for (Email email : record.getEmails().getEmails()) {
      updateEmail(entity, email);
    }
  }

  public void setIdentityService(IdentityService identityService) {
    this.identityService = identityService;
  }

  public void setBusinessObjectService(BusinessObjectService businessObjectService) {
    this.businessObjectService = businessObjectService;
  }

}
