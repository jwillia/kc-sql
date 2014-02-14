package com.rsmart.kuali.coeus.hr.service.impl;

import static org.kuali.kra.logging.BufferedLogger.debug;
import static org.kuali.kra.logging.BufferedLogger.error;

import com.rsmart.kuali.coeus.hr.rest.model.AddressCollection;
import com.rsmart.kuali.coeus.hr.rest.model.Affiliation;
import com.rsmart.kuali.coeus.hr.rest.model.AffiliationCollection;
import com.rsmart.kuali.coeus.hr.rest.model.Appointment;
import com.rsmart.kuali.coeus.hr.rest.model.Degree;
import com.rsmart.kuali.coeus.hr.rest.model.EmailCollection;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifest;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifestRecord;
import com.rsmart.kuali.coeus.hr.rest.model.KCExtendedAttributes;
import com.rsmart.kuali.coeus.hr.rest.model.NameCollection;
import com.rsmart.kuali.coeus.hr.rest.model.PhoneCollection;
import com.rsmart.kuali.coeus.hr.service.HRManifestImportException;
import com.rsmart.kuali.coeus.hr.service.HRManifestService;
import com.rsmart.kuali.coeus.hr.service.adapter.PersistableBoMergeAdapter;
import com.rsmart.kuali.coeus.hr.service.adapter.impl.EntityAddressBoAdapter;
import com.rsmart.kuali.coeus.hr.service.adapter.impl.EntityAffiliationBoAdapter;
import com.rsmart.kuali.coeus.hr.service.adapter.impl.EntityEmailBoAdapter;
import com.rsmart.kuali.coeus.hr.service.adapter.impl.EntityEmploymentBoAdapter;
import com.rsmart.kuali.coeus.hr.service.adapter.impl.EntityNameBoAdapter;
import com.rsmart.kuali.coeus.hr.service.adapter.impl.EntityPhoneBoAdapter;

import org.kuali.kra.bo.KcPerson;
import org.kuali.kra.bo.KcPersonExtendedAttributes;
import org.kuali.kra.bo.PersonAppointment;
import org.kuali.kra.bo.PersonDegree;
import org.kuali.kra.bo.Unit;
import org.kuali.kra.budget.BudgetDecimal;
import org.kuali.kra.budget.personnel.AppointmentType;
import org.kuali.kra.service.UnitService;
import org.kuali.rice.core.api.cache.CacheManagerRegistry;
import org.kuali.rice.core.api.mo.common.Defaultable;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.impl.services.CoreImplServiceLocator;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * This implements the core logic for performing an import of an HR file. This class is dependent
 * upon several services from KIM and KRA:
 * {@link org.kuali.rice.kim.api.identity.IdentityService IdentityService}, 
 * {@link org.kuali.rice.krad.service.BusinessObjectService BusinessObjectService},
 * {@link org.kuali.kra.service.UnitService UnitService}, and
 * {@link org.kuali.rice.core.api.cache.CacheManagerRegistry CacheManagerRegistry}
 * 
 * Processing starts in the {@link #importHRManifest importHRManifest} method.
 * 
 * @author duffy
 *
 */
public class HRManifestServiceImpl implements HRManifestService {
  
  private static final String PERSON = "PERSON";

  //dependencies to be injected or looked up from KIM/KC
  private IdentityService       identityService;
  private BusinessObjectService businessObjectService;
  private UnitService           unitService;
  private CacheManagerRegistry  cacheManagerRegistry;

  //TODO: figure out how to evict only those EntityBO objects affected by the update
  /**
   * Addresses a cache eviction bug. OJB implementation does not evict EntityBO objects from
   * ehcache during a businessObjectService.save() operation. Thus stale EntityBO objects
   * hang around in memory and are used by the running KC system. This method explicitly
   * flushes the cache containing EntityBO objects.
   * 
   * If this issue is resolved within KIM it should be possible to remove the dependency of
   * this class upon CacheManagerRegistry.
   */
  private final void flushCache() {
    debug("flushing EntityBO cache");
    
    if (cacheManagerRegistry == null) {
      cacheManagerRegistry = CoreImplServiceLocator.getCacheManagerRegistry();
    }
    final CacheManager manager = cacheManagerRegistry.getCacheManagerByCacheName(Entity.Cache.NAME);
    final Cache cache = manager.getCache(Entity.Cache.NAME);
    
    cache.clear();
  }
  
  /**
   * Convenience method which gets the records from the incoming manifest.
   * This method will do an error check to ensure that the number of items in the manifest
   * matches the reported record count declared in the XML header.
   * 
   * @param manifest
   * @return
   */
  private final List<HRManifestRecord> getRecords(final HRManifest manifest) {
    final List<HRManifestRecord> records = manifest.getRecords().getRecords();
    final int numRecords = records.size();

    if (numRecords != manifest.getRecordCount()) {
      throw new IllegalStateException("Manifest record count does NOT match actual record count!");
    }

    debug ("manifest contains " + numRecords + " records");
    
    return records;
  }
  
  /**
   * Saves a single EntityBo
   * 
   * @param record
   * @throws Exception
   */
  private final void saveEntityBo(final HRManifestRecord record) 
      throws Exception {
    
    final EntityBo entityBo = getOrCreateEntityBo (record);
    
    debug("Saving entity: ", entityBo);
    
    
    businessObjectService.linkAndSave(entityBo);
    for (final PrincipalBo principal : entityBo.getPrincipals()) {
      debug("Saving principal: ", principal);
      businessObjectService.linkAndSave(principal);
    }
    
  }
  
  /**
   * This duplicates the logic of the static method {@link org.kuali.kra.bo.KcPerson#fromPersonId KcPerson.fromPersonId()}.
   * It has been duplicated intentionally to expose identityService and businessObjectService
   * to me mocked. Since KcPerson.fromPersonId() is static it cannot be mocked
   * with Mockito and therefore its return object cannot be wired with the mock services.
   */
  private final KcPerson getKcPerson(final String principalId) {
    final KcPerson kcPerson = new KcPerson();
    kcPerson.setIdentityService(identityService);
    kcPerson.setBusinessObjectService(businessObjectService);
    kcPerson.setPersonId(principalId);
    kcPerson.refresh();
    
    return kcPerson;
  }
  
  /**
   * As part of saving a new user, this saves the KcExtendedAttributes associated with
   * that user.
   * 
   * @param record
   * @throws Exception
   */
  private final void saveExtendedAttributes (final HRManifestRecord record)
      throws Exception {
    final KcPerson kcPerson = getKcPerson(record.getPrincipalId());
    
    final KcPersonExtendedAttributes extendedAttributes = kcPerson.getExtendedAttributes();
    extendedAttributes.setPersonId(record.getPrincipalId());
    
    updateExtendedAttributes(extendedAttributes, record.getKcExtendedAttributes());
    for (final Degree degree : record.getDegreeCollection().getDegrees()) {
      updatePersonDegree(record.getPrincipalId(), extendedAttributes, degree);
    }
    for (final Appointment appointment : record.getAppointmentCollection().getAppointments()) {
      updateAppointment (record.getPrincipalId(), extendedAttributes, appointment);
    }
    
    debug ("Saving extended attributes");
    businessObjectService.save(extendedAttributes);
  }
  
  /**
   * In the event of an Exception while processing, this method pretty prints the Exception's
   * details to the error log for later analysis.
   * 
   * @param recordIndex
   * @param e
   */
  private final void logErrorForRecord (final int recordIndex, final Exception e) {
    final StringWriter strWriter = new StringWriter();
    final PrintWriter errorWriter = new PrintWriter(strWriter);
    errorWriter.append("import failed for record ").append(Integer.toString(recordIndex))
           .append(": ").append(e.getMessage()).append('\n');
    e.printStackTrace(errorWriter);
    errorWriter.flush();
    error(strWriter.toString());
  }
  
  /**
   * This is the workhorse of HRManifestServiceImpl. It takes an HRManifest and walks through
   * the records it contains. For each record the contained dependent business objects are 
   * merged with the list of current business objects. Each set of dependent objects is handled
   * in the following manner:
   * 
   * 1) if the list of dependent objects does not exist in the import, no change is made.
   * 2) if the list exists
   *   2.1) any new items in the list are added
   *   2.2) any old items currently stored for the user which are not in the list are deleted
   *   2.3) any new items in the list that are equivalent to items already attached to the user
   *        are ignored.
   *        
   * The net effect is that the state of incoming collections of business objects will replace
   * the state stored for the user. If no change is desired for a particular set of business
   * objects (eg. no changes have occurred for a user's name or email records) it can be omitted entirely
   * from the import. This is *different* than including an empty element in the import.
   * An empty element will cause all of that type of business object to be deleted for the user.
   */
  public void importHRManifest(final HRManifest manifest)
      throws HRManifestImportException {
    
    debug ("importing hr manifest");

    //track errors that occur during processing
    final LinkedList<Object[]> errors = new LinkedList<Object[]>();
    
    //records from the HRManifest
    final List<HRManifestRecord> records = getRecords(manifest);
    final int numRecords = manifest.getRecordCount();
    final HashSet<String> processedIds = new HashSet<String> (numRecords);
    
    // loop through records
    for (int i = 0; i < numRecords; i++) {
      final HRManifestRecord record = records.get(i);
      final String principalId = record.getPrincipalId();
      try {
        // error on duplicate records in import
        // this is critical since cache is flushed only once (after all records)
        if (processedIds.contains(principalId)) {
          throw new IllegalArgumentException ("Duplicate records for the same principalId not allowed in a single import");
        }
        
        // save the basic user information on the EntityBO object
        saveEntityBo (record);
        
        if (record.getKcExtendedAttributes() != null) {
          // save the added attributes that are important to KC
          saveExtendedAttributes (record);
        }

      } catch (Exception e) {
        // log the spot where the exception occurred, then add it to the exception collection and move on
        final int realIndex = i + 1;
        logErrorForRecord(realIndex, e);
        errors.add(new Object[] { new Integer(realIndex), e });
      } finally {
        // track the ID of the import so we can spot duplicates
        if (principalId != null) {
          processedIds.add (principalId);
        }
      }
    }

    // this compensates for issues with the cache management logic in KIM
    flushCache();
    
    if (errors.size() > 0) {
      throw new HRManifestImportException(errors);
    }
  }
  
  /**
   * Look for a record matching the incoming principal Id. If one exists, update the record.
   * If it does not exist, create a new record.
   * 
   * @param record
   * @return
   * @throws Exception
   */
  protected EntityBo getOrCreateEntityBo (final HRManifestRecord record) 
    throws Exception
  {
    if (record == null) {
      throw new IllegalArgumentException("Cannot create entity for null record");
    }
  
    final String principalId = record.getPrincipalId();
    debug("importing principal: ", principalId);
  
    // lookup existing entity for this principalId
    EntityBo retval = EntityBo.from(identityService.getEntity(principalId));
  
    if (retval == null) {
      // this is a new entity
      debug("creating new entity");
      retval = newEntityBo(record);
    } else {
      // found an existing entity
      debug("updating existing entity");
    }
    updateEntityBo (retval, record);
  
    return retval;
  }

  /**
   * This converts an incoming set of JAXB objects into their corresponding KIM entity objects.
   * The list is returned sorted so that it is possible to determine which incoming objects are
   * new versus updates, versus when an existing object has been omitted in order to delete it.
   * 
   * @param entityId
   * @param toImport
   * @param adapter
   * @return
   */
  protected final <T extends PersistableBusinessObject, Z> List<T> adaptAndSortList 
      (final String entityId, final List<Z> toImport, final PersistableBoMergeAdapter<T,Z> adapter) {
    final ArrayList<T> adaptedList = new ArrayList<T>();    
    final Class<T> boClass = adapter.getBusinessObjectClass();
    boolean defaultSet = false;
    
    for (final Z name : toImport) {
      final T newBo = adapter.setFields(adapter.newBO(entityId),name);
      
      // take this opportunity to enforce default value flag as unique within collection
      if (adapter.isBoDefaultable() && ((Defaultable)newBo).isDefaultValue()) {
        if (defaultSet) {
          throw new IllegalArgumentException ("Multiple records of type " 
                + boClass.getSimpleName() + " set as default value");
        } else {
          defaultSet = true;
        }
      }
      
      final int insertAt = Collections.binarySearch(adaptedList, newBo, adapter);
      
      if (insertAt >= 0) {
        throw new IllegalArgumentException ("Duplicate records of type " 
              + boClass.getSimpleName() + " encountered");
      } else {
        adaptedList.add(-(insertAt + 1), newBo);
      }
    }
    
    return adaptedList;
  }
  
  /**
   * This merges an incoming list of JAXB objects into a list of existing business objects
   * for a single Entity.
   * 
   * @param objsToMerge
   * @param existingBOs
   * @param adapter
   * @param entityId
   * @return
   */
  protected <T extends PersistableBusinessObject, Z> 
    boolean mergeImportedBOs (final List<Z> objsToMerge, final List<T> existingBOs,
                              final PersistableBoMergeAdapter<T, Z> adapter, final String entityId) {
    
    /* obtain the specific Class of parameter T so it can be used
     * in reporting and in business object lookup
     */
    final Class<T> boClass = adapter.getBusinessObjectClass();
    
    // create our own copy of the current BOs to sort and modify without
    // fear of EntityBO innards
    final ArrayList<T> currBOs = new ArrayList<T> (existingBOs);

    // do we have objects to merge? if not why bother?
    if (objsToMerge != null) {

      // adapt the incoming List of objects into their BO type and sort the results
      final List<T> importBOs = this.adaptAndSortList(entityId, objsToMerge, adapter);
      
      // sort the existing collection
      Collections.sort(currBOs, adapter);

      // track whether the collection has been modified and needs to be refreshed
      boolean collectionModified = false;
      final LinkedList<T> oldToDelete = new LinkedList<T>();
      final LinkedList<T> newToAdd = new LinkedList<T>();
      
      int lowerBound = 0;
      
      for (T importBO : importBOs) {
        boolean importHandled = false;
        // use this loop to check if import is a duplicate, or if some old BOs need deleting
        for (int i = lowerBound; i < currBOs.size() && !importHandled; i++) {
          final T oldBO = currBOs.get(i);
          final int comp = adapter.compare(importBO, oldBO);
          if (comp < 0) {
            // importBO comes before any of the existing BOs, add it
            newToAdd.add(importBO);
            importHandled = true;
          } else if (comp == 0) {
            // importBO already exists, skip it and currBO
            debug("import object " + boClass.getSimpleName() + " with ID " + ((Identifiable)importBO).getId() + " unchanged");
            importHandled = true;
            lowerBound++;
          } else {
            // oldBO comes before any of the imports, thus it is not in the import - delete it
            oldToDelete.add(oldBO);
            lowerBound++;
          }
        }
        // if we've exhausted the list of existing BOs without a match or a place to insert,
        // then we insert at the end.
        if (!importHandled) {
          newToAdd.add(importBO);
        }
      }
      
      // if we've exhausted the imports and there are still left over existing BOs
      // then we need to delete the remaining existing imports.
      for (int i = lowerBound; i < currBOs.size(); i++) {
        oldToDelete.add(currBOs.get(i));
      }
      
      // all the adds and deletes have been queued up - run through them here
      
      for (T oldBO : oldToDelete) {
        adapter.delete(businessObjectService, oldBO);
        collectionModified = true;
      }
      
      for (T newBO : newToAdd) {
        adapter.save(businessObjectService, newBO);
        collectionModified = true;
      }

      return collectionModified;
    }
    return false;
  }
  
  /**
   * Loops through the incoming import list of Affiliations to determine which are linked to
   * an Employment object and which are not. These are separated into the two lists that 
   * are passed in so they can be handled separately.
   * 
   * @param source
   * @param emp
   * @param nonEmp
   */
  private void splitAffiliations(final List<Affiliation> source, final List<Affiliation> emp, 
      final List<Affiliation> nonEmp) {
    for (Affiliation aff : source) {
      if (aff.getEmployment() != null) {
        emp.add(aff);
      }
      else {
        nonEmp.add(aff);
      }
    }
  }
  
  /**
   * Find those existing BOs that correspond to Affiliations which are NOT employee affiliations.
   * This is used to help divide out handling of the two types of Affilition.
   * 
   * @param affiliations
   * @param empRecs
   * @return
   */
  private List<EntityAffiliationBo> getNonEmployeeAffiliationBos(final List<EntityAffiliationBo> affiliations, 
      final List<EntityEmploymentBo> empRecs) {
    final ArrayList<EntityAffiliationBo> nonEmp = new ArrayList<EntityAffiliationBo>();
    final HashSet<String> empAffIds = new HashSet<String>(empRecs.size());
    for (final EntityEmploymentBo emp : empRecs) {
      empAffIds.add(emp.getEntityAffiliationId());
    }
    for (final EntityAffiliationBo aff : affiliations) {
      if (!empAffIds.contains(aff.getId())) {
        nonEmp.add(aff);
      }
    }
    return nonEmp;
  }
  
  /**
   * This handles each of the KIM dependent entity business object collections connected to a single
   * Entity.
   * 
   * @param entity
   * @param record
   */
  protected void updateEntityBo(final EntityBo entity, final HRManifestRecord record) {
    boolean modified = false;
    final String entityId = entity.getId();
    updatePrincipal(entity, record);

    final NameCollection nameColl = record.getNameCollection();
    if (nameColl != null && mergeImportedBOs (nameColl.getNames(), entity.getNames(), new EntityNameBoAdapter(), entityId)) {
      modified = true;
    }
    
    // divide up Affiliations to handle those with and those without Employment records separately
    final AffiliationCollection affColl = record.getAffiliationCollection();
    final List<Affiliation> employmentAffiliations = new ArrayList<Affiliation>();
    final List<Affiliation> nonEmploymentAffiliations = new ArrayList<Affiliation>();
    final List<EntityAffiliationBo> affBos = entity.getAffiliations();
    final List<EntityEmploymentBo> empBos = entity.getEmploymentInformation();
    final List<EntityAffiliationBo> nonEmpAffBos = getNonEmployeeAffiliationBos(affBos, empBos);
    
    splitAffiliations (affColl.getAffiliations(), employmentAffiliations, nonEmploymentAffiliations);

    if (affColl != null && mergeImportedBOs (employmentAffiliations, empBos, new EntityEmploymentBoAdapter(), entityId)) {
      modified = true;
    }
    
    if (affColl != null && mergeImportedBOs (nonEmploymentAffiliations, nonEmpAffBos, new EntityAffiliationBoAdapter(), entityId)) {
      modified = true;
    }
    
    // Handle all the different contact business object types
    final EntityTypeContactInfoBo contactInfo = getEntityTypeContactInfoBo(entity);
 
    final AddressCollection addrColl = record.getAddressCollection();
    if (addrColl != null && mergeImportedBOs (addrColl.getAddresses(), contactInfo.getAddresses(), new EntityAddressBoAdapter(), entityId)) {
      modified = true;
    }
    final PhoneCollection phoneColl = record.getPhoneCollection();
    if (phoneColl != null && mergeImportedBOs (phoneColl.getPhones(), contactInfo.getPhoneNumbers(), new EntityPhoneBoAdapter(), entityId)) {
      modified = true;
    }
    final EmailCollection emailColl = record.getEmailCollection();
    if (emailColl != null && mergeImportedBOs (emailColl.getEmails(), contactInfo.getEmailAddresses(), new EntityEmailBoAdapter(), entityId)) {
      modified = true;
    }

    // synch the in-memory Business Object back up. This *may* not be necessary, but having an
    // out of synch object reference around feels very dangery.
    if (modified) {
      entity.refresh();
      entity.refreshNonUpdateableReferences();
    }
    
  }

  /**
   * Removes a single person and his/her dependent entities
   */
  @Override
  public void deletePerson(final String entityId) throws Exception {
    final EntityBo entity = businessObjectService.findBySinglePrimaryKey(EntityBo.class, entityId);
    delete(entity);
  }
  
  /**
   * Deletes and entity and all its dependent entities.
   * 
   * @param entity
   */
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
    final KcPerson person = KcPerson.fromPersonId(entity.getId());
    final KcPersonExtendedAttributes attributes = person.getExtendedAttributes();
    if (attributes.getPersonId() != null) {
      businessObjectService.delete(person.getExtendedAttributes());
    }
    entity.refresh();
    entity.refreshNonUpdateableReferences();
    businessObjectService.delete(entity);
  }

  /**
   * Set the principal name and Id.
   * 
   * @param entity
   * @param record
   */
  protected void updatePrincipal(final EntityBo entity, final HRManifestRecord record) {
    final PrincipalBo principal = businessObjectService.findBySinglePrimaryKey(PrincipalBo.class, record.getPrincipalId());
    
    if (principal != null) {
      principal.setPrincipalName(record.getPrincipalName());
      principal.setActive(true);
      
      businessObjectService.save(principal);
    } else {
      PrincipalBo newPrincipal = new PrincipalBo();
      newPrincipal.setPrincipalName(record.getPrincipalName());
      newPrincipal.setActive(true);
      newPrincipal.setPrincipalId(record.getPrincipalId());
      entity.setId(newPrincipal.getPrincipalId());
      
      entity.getPrincipals().add(newPrincipal);
    }
  }

  /**
   * Finds or prepares the contact info object for a given entity.
   * 
   * @param entity
   * @return
   */
  private final EntityTypeContactInfoBo getEntityTypeContactInfoBo(final EntityBo entity) {
    EntityTypeContactInfoBo contactInfo = entity
        .getEntityTypeContactInfoByTypeCode(PERSON);

    if (contactInfo == null) {
      contactInfo = new EntityTypeContactInfoBo();
      contactInfo.setEntityId(entity.getId());
      contactInfo.setAddresses(new LinkedList<EntityAddressBo>());
      contactInfo.setPhoneNumbers(new LinkedList<EntityPhoneBo>());
      contactInfo.setEmailAddresses(new LinkedList<EntityEmailBo>());
      contactInfo.setEntityTypeCode(PERSON);
      contactInfo.setActive(true);

      businessObjectService.save(contactInfo);
      entity.refreshReferenceObject("entityTypeContactInfos");
    }
    return contactInfo;
  }

  protected EntityBo newEntityBo (final HRManifestRecord record) {
    final EntityBo entity = new EntityBo();
    
    entity.setId(record.getPrincipalId());
    entity.setActive(record.isActive());
    businessObjectService.save(entity);

    return entity;
  }

  protected void updateExtendedAttributes (final KcPersonExtendedAttributes oldAttrs, 
      final KCExtendedAttributes newAttrs) {
    oldAttrs.setAgeByFiscalYear(newAttrs.getAgeByFiscalYear());
    oldAttrs.setCitizenshipTypeCode(newAttrs.getCitizenshipType());
    oldAttrs.setCounty(newAttrs.getCounty());
    oldAttrs.setDegree(newAttrs.getDegree());
    oldAttrs.setDirectoryDepartment(newAttrs.getDirectoryDepartment());
    oldAttrs.setDirectoryTitle(newAttrs.getDirectoryTitle());
    oldAttrs.setEducationLevel(newAttrs.getEducationLevel());
    oldAttrs.setHandicappedFlag(newAttrs.isHandicapped());
    oldAttrs.setHandicapType(newAttrs.getHandicapType());
    oldAttrs.setHasVisa(newAttrs.isVisa());
    oldAttrs.setIdProvided(newAttrs.getIdProvided());
    oldAttrs.setIdVerified(newAttrs.getIdVerified());
    oldAttrs.setMajor(newAttrs.getMajor());
    oldAttrs.setMultiCampusPrincipalId(newAttrs.getMultiCampusPrincipalId());
    oldAttrs.setMultiCampusPrincipalName(newAttrs.getMultiCampusPrincipalName());
    oldAttrs.setOfficeLocation(newAttrs.getOfficeLocation());
    oldAttrs.setOnSabbaticalFlag(newAttrs.isOnSabbatical());
    oldAttrs.setPrimaryTitle(newAttrs.getPrimaryTitle());
    oldAttrs.setRace(newAttrs.getRace());
    oldAttrs.setSalaryAnniversaryDate(new java.sql.Date(newAttrs.getSalaryAnniversaryDate()
        .getTime()));
    oldAttrs.setSchool(newAttrs.getSchool());
    oldAttrs.setSecondaryOfficeLocation(newAttrs.getSecondaryOfficeLocation());
    oldAttrs.setVacationAccrualFlag(newAttrs.isVacationAccrual());
    oldAttrs.setVeteranFlag(newAttrs.isVeteran());
    oldAttrs.setVeteranType(newAttrs.getVeteranType());
    oldAttrs.setVisaCode(newAttrs.getVisaCode());
    oldAttrs.setVisaRenewalDate(new java.sql.Date(newAttrs.getVisaRenewalDate().getTime()));
    oldAttrs.setVisaType(newAttrs.getVisaType());
    oldAttrs.setYearGraduated(newAttrs.getYearGraduated().toString());
  }

  protected void updatePersonDegree(final String personId, final KcPersonExtendedAttributes attributes, final Degree degree) {

    final PersonDegree personDegree = new PersonDegree();
    personDegree.setPersonId(personId);
    personDegree.setDegreeCode(degree.getDegreeCode());
    personDegree.setDegree(degree.getDegree());
    personDegree.setFieldOfStudy(degree.getFieldOfStudy());
    personDegree.setGraduationYear(degree.getGraduationYear().toString());
    personDegree.setSchool(degree.getSchool());
    personDegree.setSchoolId(degree.getSchoolId());
    personDegree.setSchoolIdCode(degree.getSchoolIdCode());
    personDegree.setSpecialization(degree.getSpecialization());

    final List<PersonDegree> degrees = attributes.getPersonDegrees();
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
  
  protected void updateAppointment(final String personId, final KcPersonExtendedAttributes attributes, final Appointment newAppointment) {
    // the PersonAppointment object that will be updated
    final PersonAppointment pAppt = new PersonAppointment();
    
    pAppt.setPersonId(personId);

    final AppointmentType apptType = new AppointmentType();
    apptType.setAppointmentTypeCode(newAppointment.getAppointmentType());

    pAppt.setAppointmentType(apptType);
    pAppt.setJobCode(newAppointment.getJobCode());
    pAppt.setJobTitle(newAppointment.getJobTitle());
    pAppt.setPreferedJobTitle(newAppointment.getPreferedJobTitle());
    pAppt.setSalary(new BudgetDecimal(newAppointment.getSalary()));
    pAppt.setStartDate(new java.sql.Date(newAppointment.getStartDate().getTime()));
    if (newAppointment.getEndDate() != null) {
      pAppt.setEndDate(new java.sql.Date(newAppointment.getEndDate().getTime()));
    }
    Unit unit = unitService.getUnit(newAppointment.getUnitNumber());
    pAppt.setUnit(unit);
    pAppt.setUnitNumber(unit.getUnitNumber());
    
    attributes.getPersonAppointments().add(pAppt);
  }

  public void setIdentityService(final IdentityService identityService) {
    this.identityService = identityService;
  }

  public void setBusinessObjectService(final BusinessObjectService businessObjectService) {
    this.businessObjectService = businessObjectService;
  }

  public void setUnitService(final UnitService unitService) {
    this.unitService = unitService;
  }
  
  public void setCacheManagerRegistry(final CacheManagerRegistry registry) {
    cacheManagerRegistry = registry;
  }

}
