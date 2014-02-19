package com.rsmart.kuali.coeus.hr.service.impl;

import static org.kuali.kra.logging.BufferedLogger.debug;
import static org.kuali.kra.logging.BufferedLogger.error;

import com.rsmart.kuali.coeus.hr.rest.model.AddressCollection;
import com.rsmart.kuali.coeus.hr.rest.model.Affiliation;
import com.rsmart.kuali.coeus.hr.rest.model.AffiliationCollection;
import com.rsmart.kuali.coeus.hr.rest.model.AppointmentCollection;
import com.rsmart.kuali.coeus.hr.rest.model.DegreeCollection;
import com.rsmart.kuali.coeus.hr.rest.model.EmailCollection;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifest;
import com.rsmart.kuali.coeus.hr.rest.model.HRManifestRecord;
import com.rsmart.kuali.coeus.hr.rest.model.KCExtendedAttributes;
import com.rsmart.kuali.coeus.hr.rest.model.NameCollection;
import com.rsmart.kuali.coeus.hr.rest.model.PhoneCollection;
import com.rsmart.kuali.coeus.hr.service.HRManifestImportException;
import com.rsmart.kuali.coeus.hr.service.HRManifestService;
import com.rsmart.kuali.coeus.hr.service.adapter.PersistableBoMergeAdapter;
import com.rsmart.kuali.coeus.hr.service.adapter.impl.PersonAppointmentBoAdapter;
import com.rsmart.kuali.coeus.hr.service.adapter.impl.EntityAddressBoAdapter;
import com.rsmart.kuali.coeus.hr.service.adapter.impl.EntityAffiliationBoAdapter;
import com.rsmart.kuali.coeus.hr.service.adapter.impl.EntityEmailBoAdapter;
import com.rsmart.kuali.coeus.hr.service.adapter.impl.EntityEmploymentBoAdapter;
import com.rsmart.kuali.coeus.hr.service.adapter.impl.EntityNameBoAdapter;
import com.rsmart.kuali.coeus.hr.service.adapter.impl.EntityPhoneBoAdapter;
import com.rsmart.kuali.coeus.hr.service.adapter.impl.PersonDegreeBoAdapter;

import org.kuali.kra.bo.KcPerson;
import org.kuali.kra.bo.KcPersonExtendedAttributes;
import org.kuali.kra.service.UnitService;
import org.kuali.rice.core.api.cache.CacheManagerRegistry;
import org.kuali.rice.core.api.mo.common.Defaultable;
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

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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

  private Validator                   validator;
  
  //dependencies to be injected or looked up from KIM/KC
  private IdentityService             identityService;
  private BusinessObjectService       businessObjectService;
  private UnitService                 unitService;
  private CacheManagerRegistry        cacheManagerRegistry;
  
  private EntityAddressBoAdapter      addressAdapter = new EntityAddressBoAdapter();
  private EntityAffiliationBoAdapter  affiliationAdapter = new EntityAffiliationBoAdapter();
  private EntityEmailBoAdapter        emailAdapter = new EntityEmailBoAdapter();
  private EntityEmploymentBoAdapter   employmentAdapter = new EntityEmploymentBoAdapter();
  private EntityNameBoAdapter         nameAdapter = new EntityNameBoAdapter();
  private EntityPhoneBoAdapter        phoneAdapter = new EntityPhoneBoAdapter();
  private PersonAppointmentBoAdapter  appointmentAdapter = new PersonAppointmentBoAdapter();
  private PersonDegreeBoAdapter       degreeAdapter = new PersonDegreeBoAdapter();

  public HRManifestServiceImpl() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }
  
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
  
  private final EntityBo getEntityBo(final String principalId) {
    return EntityBo.from(identityService.getEntity(principalId));
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
  
  private final KcPersonExtendedAttributes getKcPersonExtendedAttributes(final KcPerson person) {
    KcPersonExtendedAttributes attribs = person.getExtendedAttributes();
    
    if (attribs == null) {
      attribs = new KcPersonExtendedAttributes();
      attribs.setPersonId(person.getPersonId());
    }
    
    return attribs;
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
  
  protected void validateRecord (final HRManifestRecord record) {
    validator.validate(record);
  }
  
  protected void handleRecord (final HRManifestRecord record)
      throws Exception {
    
    final String principalId = record.getPrincipalId();
    
    EntityBo entity = getEntityBo(principalId);
    
    if (entity != null) {
      debug("updating existing entity");
    } else {
      debug("creating new entity");
      validateRecord(record);
      entity = newEntityBo(record);
    }
    updateEntityBo(entity, record);

    updateExtendedAttributes (record);
    
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
        
        handleRecord(record);

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
    boolean defaultSet = false;
    
    for (final Z name : toImport) {
      final T newBo = adapter.setFields(adapter.newBO(entityId),name);
      
      // take this opportunity to enforce default value flag as unique within collection
      if (adapter.isBoDefaultable() && ((Defaultable)newBo).isDefaultValue()) {
        if (defaultSet) {
          throw new IllegalArgumentException ("Multiple records of type " 
                + newBo.getClass().getSimpleName() + " set as default value");
        } else {
          defaultSet = true;
        }
      }
      
      final int insertAt = Collections.binarySearch(adaptedList, newBo, adapter);
      
      if (insertAt >= 0) {
        throw new IllegalArgumentException ("Duplicate records of type " 
              + newBo.getClass().getSimpleName() + " encountered");
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
    
    // create our own copy of the current BOs to sort and modify without
    // fear of EntityBO innards
    final ArrayList<T> currBOs = existingBOs != null ? new ArrayList<T> (existingBOs) : new ArrayList<T>();

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
    modified = updatePrincipal(entity, record);

    final NameCollection nameColl = record.getNameCollection();
    if (nameColl != null && mergeImportedBOs (nameColl.getNames(), entity.getNames(), nameAdapter, entityId)) {
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

    if (affColl != null && mergeImportedBOs (employmentAffiliations, empBos, employmentAdapter, entityId)) {
      modified = true;
    }
    
    if (affColl != null && mergeImportedBOs (nonEmploymentAffiliations, nonEmpAffBos, affiliationAdapter, entityId)) {
      modified = true;
    }
    
    // Handle all the different contact business object types
    final EntityTypeContactInfoBo contactInfo = getEntityTypeContactInfoBo(entity);
 
    final AddressCollection addrColl = record.getAddressCollection();
    if (addrColl != null && mergeImportedBOs (addrColl.getAddresses(), contactInfo.getAddresses(), addressAdapter, entityId)) {
      modified = true;
    }
    final PhoneCollection phoneColl = record.getPhoneCollection();
    if (phoneColl != null && mergeImportedBOs (phoneColl.getPhones(), contactInfo.getPhoneNumbers(), phoneAdapter, entityId)) {
      modified = true;
    }
    final EmailCollection emailColl = record.getEmailCollection();
    if (emailColl != null && mergeImportedBOs (emailColl.getEmails(), contactInfo.getEmailAddresses(), emailAdapter, entityId)) {
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
    businessObjectService.delete(entity.getEmploymentInformation());
    businessObjectService.delete(entity.getAffiliations());

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
  protected boolean updatePrincipal(final EntityBo entity, final HRManifestRecord record) {
    PrincipalBo principal = businessObjectService.findBySinglePrimaryKey(PrincipalBo.class, record.getPrincipalId());
    boolean modified = false;
    
    if (principal != null) {
      if (!principal.getPrincipalName().equals(record.getPrincipalName())) {
        principal.setPrincipalName(record.getPrincipalName());
        modified = true;
      }
      if (principal.isActive() ^ record.isActive()) {
        principal.setActive(record.isActive());
        modified = true;
      }
    } else {
      modified = true;
      
      principal = new PrincipalBo();
      principal.setPrincipalName(record.getPrincipalName());
      principal.setActive(true);
      principal.setPrincipalId(record.getPrincipalId());
      entity.setId(principal.getPrincipalId());
      
      entity.getPrincipals().add(principal);
    }
    if (modified) {
      businessObjectService.save(principal);
    }
    
    return modified;
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

  protected final boolean nullSafeEquals (final Object obj0, final Object obj1) {
    if (obj0 == null) {
      return (obj1 == null);
    } else {
      return obj0.equals(obj1);
    }
  }
  
  protected final boolean equals (final KcPersonExtendedAttributes oldAttrs, final KCExtendedAttributes newAttrs) {
    if (!nullSafeEquals(oldAttrs.getAgeByFiscalYear(),newAttrs.getAgeByFiscalYear())) return false;
    if (!nullSafeEquals(oldAttrs.getCitizenshipTypeCode(),newAttrs.getCitizenshipType())) return false;
    if (!nullSafeEquals(oldAttrs.getCounty(),newAttrs.getCounty())) return false;
    if (!nullSafeEquals(oldAttrs.getDegree(),newAttrs.getDegree())) return false;
    if (!nullSafeEquals(oldAttrs.getDirectoryDepartment(),newAttrs.getDirectoryDepartment())) return false;
    if (!nullSafeEquals(oldAttrs.getDirectoryTitle(),newAttrs.getDirectoryTitle())) return false;
    if (!nullSafeEquals(oldAttrs.getEducationLevel(),newAttrs.getEducationLevel())) return false;
    if (oldAttrs.getHandicappedFlag() != newAttrs.isHandicapped()) return false;
    if (!nullSafeEquals(oldAttrs.getHandicapType(),newAttrs.getHandicapType())) return false;
    if (oldAttrs.getHasVisa() != newAttrs.isVisa()) return false;
    if (!nullSafeEquals(oldAttrs.getIdProvided(),newAttrs.getIdProvided())) return false;
    if (!nullSafeEquals(oldAttrs.getIdVerified(),newAttrs.getIdVerified())) return false;
    if (!nullSafeEquals(oldAttrs.getMajor(),newAttrs.getMajor())) return false;
    if (!nullSafeEquals(oldAttrs.getMultiCampusPrincipalId(),newAttrs.getMultiCampusPrincipalId())) return false;
    if (!nullSafeEquals(oldAttrs.getMultiCampusPrincipalName(),newAttrs.getMultiCampusPrincipalName())) return false;
    if (!nullSafeEquals(oldAttrs.getOfficeLocation(),newAttrs.getOfficeLocation())) return false;
    if (oldAttrs.getOnSabbaticalFlag() != newAttrs.isOnSabbatical()) return false;
    if (!nullSafeEquals(oldAttrs.getPrimaryTitle(),newAttrs.getPrimaryTitle())) return false;
    if (!nullSafeEquals(oldAttrs.getRace(),newAttrs.getRace())) return false;
    if (oldAttrs.getSalaryAnniversaryDate().getTime() != newAttrs.getSalaryAnniversaryDate().getTime());
    if (!nullSafeEquals(oldAttrs.getSchool(),newAttrs.getSchool())) return false;
    if (!nullSafeEquals(oldAttrs.getSecondaryOfficeLocation(),newAttrs.getSecondaryOfficeLocation())) return false;
    if (oldAttrs.getVacationAccrualFlag() != newAttrs.isVacationAccrual()) return false;
    if (oldAttrs.getVeteranFlag() != newAttrs.isVeteran()) return false;
    if (!nullSafeEquals(oldAttrs.getVeteranType(),newAttrs.getVeteranType())) return false;
    if (!nullSafeEquals(oldAttrs.getVisaCode(),newAttrs.getVisaCode())) return false;
    if (oldAttrs.getVisaRenewalDate().getTime() != newAttrs.getVisaRenewalDate().getTime());
    if (!nullSafeEquals(oldAttrs.getVisaType(),newAttrs.getVisaType())) return false;
    if (!nullSafeEquals(oldAttrs.getYearGraduated(),newAttrs.getYearGraduated().toString())) return false;
    return true;
  }
  
  protected boolean updateExtendedAttributes (final HRManifestRecord record) {
    final String principalId = record.getPrincipalId();
    final KcPerson kcPerson = getKcPerson(principalId);
    
    final KCExtendedAttributes newAttrs = record.getKcExtendedAttributes();
    final KcPersonExtendedAttributes attrs = getKcPersonExtendedAttributes(kcPerson);
    boolean modified = false;
    attrs.setPersonId(principalId);

    if (!equals (attrs, newAttrs)) {
      attrs.setAgeByFiscalYear(newAttrs.getAgeByFiscalYear());
      attrs.setCitizenshipTypeCode(newAttrs.getCitizenshipType());
      attrs.setCounty(newAttrs.getCounty());
      attrs.setDegree(newAttrs.getDegree());
      attrs.setDirectoryDepartment(newAttrs.getDirectoryDepartment());
      attrs.setDirectoryTitle(newAttrs.getDirectoryTitle());
      attrs.setEducationLevel(newAttrs.getEducationLevel());
      attrs.setHandicappedFlag(newAttrs.isHandicapped());
      attrs.setHandicapType(newAttrs.getHandicapType());
      attrs.setHasVisa(newAttrs.isVisa());
      attrs.setIdProvided(newAttrs.getIdProvided());
      attrs.setIdVerified(newAttrs.getIdVerified());
      attrs.setMajor(newAttrs.getMajor());
      attrs.setMultiCampusPrincipalId(newAttrs.getMultiCampusPrincipalId());
      attrs.setMultiCampusPrincipalName(newAttrs.getMultiCampusPrincipalName());
      attrs.setOfficeLocation(newAttrs.getOfficeLocation());
      attrs.setOnSabbaticalFlag(newAttrs.isOnSabbatical());
      attrs.setPrimaryTitle(newAttrs.getPrimaryTitle());
      attrs.setRace(newAttrs.getRace());
      attrs.setSalaryAnniversaryDate(new java.sql.Date(newAttrs.getSalaryAnniversaryDate()
          .getTime()));
      attrs.setSchool(newAttrs.getSchool());
      attrs.setSecondaryOfficeLocation(newAttrs.getSecondaryOfficeLocation());
      attrs.setVacationAccrualFlag(newAttrs.isVacationAccrual());
      attrs.setVeteranFlag(newAttrs.isVeteran());
      attrs.setVeteranType(newAttrs.getVeteranType());
      attrs.setVisaCode(newAttrs.getVisaCode());
      attrs.setVisaRenewalDate(new java.sql.Date(newAttrs.getVisaRenewalDate().getTime()));
      attrs.setVisaType(newAttrs.getVisaType());
      attrs.setYearGraduated(newAttrs.getYearGraduated().toString());

      businessObjectService.save(attrs);

      modified =  true;
    }
    final AppointmentCollection apptColl = record.getAppointmentCollection();
    if (apptColl != null && mergeImportedBOs(apptColl.getAppointments(), 
        attrs.getPersonAppointments(), appointmentAdapter, principalId)) {
      modified = true;
    }
    final DegreeCollection degColl = record.getDegreeCollection();
    if (degColl != null && mergeImportedBOs(degColl.getDegrees(),
        attrs.getPersonDegrees(), degreeAdapter, principalId)) {
      modified = true;
    }
    
    return modified;
  }

  public void setIdentityService(final IdentityService identityService) {
    this.identityService = identityService;
  }

  public void setBusinessObjectService(final BusinessObjectService businessObjectService) {
    this.businessObjectService = businessObjectService;
  }

  public void setUnitService(final UnitService unitService) {
    this.unitService = unitService;
    appointmentAdapter.setUnitService(unitService);
  }
  
  public void setCacheManagerRegistry(final CacheManagerRegistry registry) {
    cacheManagerRegistry = registry;
  }

}
