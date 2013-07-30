package com.rsmart.kuali.coeus.hr.service.impl;

import static org.kuali.kra.logging.BufferedLogger.*;

import java.util.LinkedList;
import java.util.List;

import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.service.BusinessObjectService;

import com.rsmart.kuali.coeus.hr.rest.model.Affiliation;
import com.rsmart.kuali.coeus.hr.rest.model.Email;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifest;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifestRecord;
import com.rsmart.kuali.coeus.hr.rest.model.Name;
import com.rsmart.kuali.coeus.hr.rest.model.Phone;
import com.rsmart.kuali.coeus.hr.service.HRManifestImportException;
import com.rsmart.kuali.coeus.hr.service.HRManifestService;

public class HRManifestServiceImpl implements HRManifestService {

  private IdentityService identityService;
  private BusinessObjectService businessObjectService;
  
  public void importHRManifest(HRManifest manifest) throws HRManifestImportException {
    List<HRManifestRecord> records = manifest.getRecords().getRecords();
    LinkedList<Object []> errors = new LinkedList<Object []>();
    int numRecords = records.size();

    for (int i = 0; i < numRecords; i++) {
      HRManifestRecord record = records.get(i);
      try {
        persist(newInstance(i, record));
      } catch (Exception e) {
        error ("import failed for record #: " + i, e);
        errors.add(new Object[] { new Integer(i + 1), e });
      }
    }
    
    if (errors.size() > 0) {
      throw new HRManifestImportException(errors);
    }
  }
  
  public void persist(final EntityBo entity) throws Exception {
    final BusinessObjectService service = getBusinessObjectService();
    
    debug("Saving entity: ", entity);
    service.save(entity);
    for (final PrincipalBo principal : entity.getPrincipals()) {
        debug("Saving principal: ", principal);
        service.save(principal);
    }
  }
  
  protected void delete(final EntityBo entity) {
    final BusinessObjectService service = getBusinessObjectService();
    
    // debug("Deleting Entity: ", entity);       
    service.delete(entity.getPrincipals());
    service.delete(entity.getNames());
    service.delete(entity.getAffiliations());
    service.delete(entity.getEmploymentInformation());
    
    for (final EntityTypeContactInfoBo contactInfo : entity.getEntityTypeContactInfos()) {
        service.delete(contactInfo.getPhoneNumbers());
        service.delete(contactInfo.getEmailAddresses());
        contactInfo.refresh();
        contactInfo.refreshNonUpdateableReferences();
    }
    service.delete(entity.getEntityTypeContactInfos());
    entity.refresh();
    entity.refreshNonUpdateableReferences();        
    service.delete(entity);
  }
  
  public EntityBo newInstance(final int index, final HRManifestRecord record) throws Exception {
  
    if (record == null) {
        throw new IllegalArgumentException("Cannot create entity for null record");
    }
    
    final String principalId = record.getPrincipalId();
    debug("importing principal: ", principalId);
    
    //lookup existing entity for this principalId
    EntityBo retval = EntityBo.from(getIdentityService().getEntity(principalId));

    if (retval != null) { 
      // found an existing entity
      debug ("replacing existing entity");
      try {
        // get it out of the way - we will populate all of its data from the incoming record
        retval.refresh();
        retval.refreshNonUpdateableReferences();
        delete(retval);
      }
      catch (Exception e) {
        // Ignore exceptions trying to delete
        warn ("Exception deleting existing record", e);
      }
    } else {
      // this is a new entity
      debug ("creating new entity");
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
    }
    else {
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

  protected void updatePhone (final EntityBo entity, final Phone phone) {
    EntityTypeContactInfoBo contactInfo = entity.getEntityTypeContactInfoByTypeCode("PERSON");
    
    if (contactInfo == null) {
      contactInfo = new EntityTypeContactInfoBo();
      contactInfo.setPhoneNumbers(new LinkedList<EntityPhoneBo>());
      contactInfo.setEmailAddresses(new LinkedList<EntityEmailBo>());
      contactInfo.setEntityTypeCode("PERSON");
      contactInfo.setActive(true);
      
      entity.getEntityTypeContactInfos().add(contactInfo);
    }
    
    final EntityPhoneBo phoneBo = new EntityPhoneBo();

    phoneBo.setPhoneTypeCode(phone.getPhoneType());
    phoneBo.setPhoneNumber(phone.getPhoneNumber());
    phoneBo.setActive(phone.isActive());
    phoneBo.setDefaultValue(phone.isDefault());
    phoneBo.setEntityId(entity.getId());

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
  
  protected void updateEmail (final EntityBo entity, final Email email) {
    EntityTypeContactInfoBo contactInfo = entity.getEntityTypeContactInfoByTypeCode("PERSON");
    
    if (contactInfo == null) {
      contactInfo = new EntityTypeContactInfoBo();
      contactInfo.setPhoneNumbers(new LinkedList<EntityPhoneBo>());
      contactInfo.setEmailAddresses(new LinkedList<EntityEmailBo>());
      contactInfo.setEntityTypeCode("PERSON");
      contactInfo.setActive(true);
      
      entity.getEntityTypeContactInfos().add(contactInfo);
    }
    
    final EntityEmailBo emailBo = new EntityEmailBo();

    emailBo.setEmailTypeCode(email.getEmailType());
    emailBo.setEmailAddress(email.getEmailAddress());
    emailBo.setActive(email.isActive());
    emailBo.setDefaultValue(email.isDefault());
    emailBo.setEntityId(entity.getId());

    int index = -1;
    if ((index = contactInfo.getEmailAddresses().indexOf(emailBo)) > -1) {
      EntityEmailBo existing = contactInfo.getEmailAddresses().get(index);
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

  public IdentityService getIdentityService() {
    return identityService;
  }

  public void setIdentityService(IdentityService identityService) {
    this.identityService = identityService;
  }

  public BusinessObjectService getBusinessObjectService() {
    return businessObjectService;
  }

  public void setBusinessObjectService(BusinessObjectService businessObjectService) {
    this.businessObjectService = businessObjectService;
  }

}
