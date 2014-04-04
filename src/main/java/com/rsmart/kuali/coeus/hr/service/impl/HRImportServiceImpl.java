package com.rsmart.kuali.coeus.hr.service.impl;

import static org.kuali.kra.logging.BufferedLogger.debug;
import static org.kuali.kra.logging.BufferedLogger.error;
import static org.kuali.kra.logging.BufferedLogger.warn;

import com.rsmart.kuali.coeus.hr.rest.model.AddressCollection;
import com.rsmart.kuali.coeus.hr.rest.model.Affiliation;
import com.rsmart.kuali.coeus.hr.rest.model.AffiliationCollection;
import com.rsmart.kuali.coeus.hr.rest.model.AppointmentCollection;
import com.rsmart.kuali.coeus.hr.rest.model.DegreeCollection;
import com.rsmart.kuali.coeus.hr.rest.model.EmailCollection;
import com.rsmart.kuali.coeus.hr.rest.model.HRImport;
import com.rsmart.kuali.coeus.hr.rest.model.HRImportRecord;
import com.rsmart.kuali.coeus.hr.rest.model.KCExtendedAttributes;
import com.rsmart.kuali.coeus.hr.rest.model.NameCollection;
import com.rsmart.kuali.coeus.hr.rest.model.PhoneCollection;
import com.rsmart.kuali.coeus.hr.service.HRImportService;
import com.rsmart.kuali.coeus.hr.service.ImportError;
import com.rsmart.kuali.coeus.hr.service.ImportStatusService;
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
import org.kuali.rice.kim.api.identity.principal.Principal;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
 * Processing starts in the {@link #startImport startImport} method.
 * 
 * @author duffy
 *
 */
public class HRImportServiceImpl implements HRImportService {
  
  private static final String PERSON = "PERSON";
  
  private static final HashSet<String> runningImports = new HashSet<String>();

  private Validator                   validator;
  
  //dependencies to be injected or looked up from KIM/KC
  private IdentityService             identityService;
  private BusinessObjectService       businessObjectService;
  private CacheManagerRegistry        cacheManagerRegistry;
  
  private ImportStatusService         statusService = null;
  
  private EntityAddressBoAdapter      addressAdapter = new EntityAddressBoAdapter();
  private EntityAffiliationBoAdapter  affiliationAdapter = new EntityAffiliationBoAdapter();
  private EntityEmailBoAdapter        emailAdapter = new EntityEmailBoAdapter();
  private EntityEmploymentBoAdapter   employmentAdapter = new EntityEmploymentBoAdapter();
  private EntityNameBoAdapter         nameAdapter = new EntityNameBoAdapter();
  private EntityPhoneBoAdapter        phoneAdapter = new EntityPhoneBoAdapter();
  private PersonAppointmentBoAdapter  appointmentAdapter = new PersonAppointmentBoAdapter();
  private PersonDegreeBoAdapter       degreeAdapter = new PersonDegreeBoAdapter();

  public HRImportServiceImpl() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }
  
  public void setImportStatusService(final ImportStatusService svc) {
    statusService = svc;
  }
  
  private static boolean isRunning(final String importId) {
    synchronized (runningImports) {
      return runningImports.contains(importId);
    }
  }
  
  private static void addRunningImport(final String importId) {
    synchronized (runningImports) {
      runningImports.add(importId);
    }
  }
  
  private static void stopRunningImport(final String importId) {
    synchronized (runningImports) {
      runningImports.remove(importId);
    }
  }

  // Duffy - not sure what purpose this might have served or should serve.
  @SuppressWarnings("unused")
  private HashSet<String> getAllIDs() {
    final Collection<KcPersonExtendedAttributes> allAttribs  = (Collection<KcPersonExtendedAttributes>)businessObjectService.findAll(KcPersonExtendedAttributes.class);
    final HashSet<String> ids = new HashSet<String>();
    
    for (KcPersonExtendedAttributes attribs : allAttribs) {
      final String id = attribs.getPersonId();
      
      if (ids.contains(id)) {
        // this a duplicate - shouldn't happen, but this is not the place to deal with it
        // log the issue and skip the duplicate
        warn("duplicate ID '" + id + "' found when getting all IDs");
      } else {
        ids.add(id);
      }
    }
    
    return ids;
  }
  
  protected void deactivatePeople(final List<String> ids) {
    for (final String id : ids) {
      final EntityBo person = EntityBo.from(identityService.getEntity(id));
      // TODO Duffy - I am not sure if this is the correct behavior for this method.
      // But it will avoid java.lang.NullPointerException:
      // at com.rsmart.kuali.coeus.hr.service.impl.HRImportServiceImpl.deactivatePeople(HRImportServiceImpl.java:154)
      if (person != null) {
        person.setActive(false);
        businessObjectService.save(person);
      }
    }
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
    final Cache entityCache = manager.getCache(Entity.Cache.NAME);
    final Cache principalCache = manager.getCache(Principal.Cache.NAME);
    
    entityCache.clear();
    principalCache.clear();
  }
  
  /**
   * Convenience method which gets the records from the incoming import.
   * This method will do an error check to ensure that the number of items in the import
   * matches the reported record count declared in the XML header.
   * 
   * @param toImport
   * @return
   */
  private final List<HRImportRecord> getRecords(final HRImport toImport) {
    final List<HRImportRecord> records = toImport.getRecords().getRecords();
    final int numRecords = records.size();

    if (numRecords != toImport.getRecordCount()) {
      throw new IllegalStateException("Import record count does NOT match actual record count!");
    }

    debug ("import contains " + numRecords + " records");
    
    return records;
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
  
  protected void validateRecord (final HRImportRecord record) {
    validator.validate(record);
  }
  
  protected void handleRecord (final HRImportRecord record)
      throws Exception {
    
    final String principalName = record.getPrincipalName();
    
    EntityBo entity = EntityBo.from(identityService.getEntityByPrincipalName(principalName));
    if (entity != null) {
      debug("updating existing entity");
    } else {
      // check for ID collisions
      final String suppliedEntityId = record.getEntityId();
      final HashMap<String, String> fields = new HashMap<String, String>();
      fields.put("id", suppliedEntityId);
      if(businessObjectService.countMatching(EntityBo.class, fields) > 0) {
        error("entity ID collision: " + suppliedEntityId);
        throw new IllegalArgumentException ("A person already exists with entity ID: " + suppliedEntityId);
      }
      
      debug("creating new entity");
      validateRecord(record);
      entity = newEntityBo(record);
    }          
    
    //TODO: It would be super-great to have updateEntityBo and updateExtendedAttributes in a single transaction
    //      that rolls back if there is an error
    //TODO: updateEntityBo now returns principal because of KIM caching issues. identityService.getPrincipal...
    //      calls are not returning the new Principal even though it has been successfully inserted in the DB!!!
    final PrincipalBo principal = updateEntityBo(entity, record);

    updateExtendedAttributes (principal.getPrincipalId(), record);
    
  }
  
  /**
   * This is the workhorse of HRImportServiceImpl. It takes an HRImport and walks through
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
  public void startImport(final String importId, final HRImport toImport) {
    debug ("starting import " + importId);
    
    try {
      addRunningImport(importId);
      
      //records from the HRImport
      final List<HRImportRecord> records = getRecords(toImport);
      final int numRecords = toImport.getRecordCount();
      final HashSet<String> processedPrincipals = new HashSet<String> (numRecords);
      
      // loop through records
      for (int i = 0; i < numRecords; i++) {
        if (!isRunning(importId)) {
          debug("import aborted. stopping at record " + (i+1));
          break;
        }
        final HRImportRecord record = records.get(i);
        final String principalName = record.getPrincipalName();
        try {
          // error on duplicate records in import
          // this is critical since cache is flushed only once (after all records)
          if (processedPrincipals.contains(principalName)) {
            throw new IllegalArgumentException ("Duplicate records for the same principalId not allowed in a single import");
          }
          
          handleRecord(record);

          if (record.isActive()) {
            statusService.recordProcessed(importId, principalName);
          } else {
            statusService.recordInactivated(importId, principalName);
          }
        } catch (Exception e) {
          // log the spot where the exception occurred, then add it to the exception collection and move on
          final int realIndex = i + 1;
          statusService.recordError(importId, principalName, new ImportError(realIndex, e));
          logErrorForRecord(realIndex, e);
        } finally {
          // track the ID of the import so we can spot duplicates
          if (principalName != null) {
            processedPrincipals.add (principalName);
          }
        }
      }

      // if the import has been aborted, do not try to inactivate records
      if (isRunning(importId)) {
        deactivatePeople(statusService.getActiveIdsMissingFromImport(importId));
      }
      
      // this compensates for issues with the cache management logic in KIM
      flushCache();          
    } finally {
      stopRunningImport(importId);
    }
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
  protected PrincipalBo updateEntityBo(final EntityBo entity, final HRImportRecord record) {
    boolean modified = false;
    final String entityId = entity.getId();
    final PrincipalBo principal = updatePrincipal(entity, record);

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
    
    return principal;
  }

  /**
   * Removes a single person and his/her dependent entities
   */
  @Override
  public void deletePerson(final String entityId) {
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
  protected PrincipalBo updatePrincipal(final EntityBo entity, final HRImportRecord record) {
    final String principalName = record.getPrincipalName();
    PrincipalBo principal = PrincipalBo.from(identityService.getPrincipalByPrincipalName(principalName));
    final String suppliedId = record.getPrincipalId();
        
    boolean modified = false;
    
    if (principal != null) {
      final String principalId = principal.getPrincipalId();
      
      if (suppliedId != null && !principalId.equals(suppliedId)) {
        // This seems silly, but an error was encountered where IDs '2' and '0002' were being treated as equivalent.
        // See: https://jira.kuali.org/browse/KULRICE-12298
        throw new IllegalStateException ("selected for principal with ID " + principalName 
            + " but received principal with ID " + principal.getPrincipalId());        
      }
      if (!entity.getId().equals(principal.getEntityId())) {
        throw new IllegalArgumentException ("principal with ID " + principalName + " is already assigned to another person");
      }
      if (principal.isActive() ^ record.isActive()) {
        principal.setActive(record.isActive());
        modified = true;
      }
    } else {
      
      //check for principal ID collisions
      final HashMap<String, String> fields = new HashMap<String, String>();
      fields.put("principalId", suppliedId);
      final int count = businessObjectService.countMatching(PrincipalBo.class, fields);
      
      if (count > 0) {
        error ("principal ID collision: " + suppliedId);
        throw new IllegalArgumentException ("A principal already exists with ID: " + suppliedId);
      }
      
      modified = true;
      
      principal = new PrincipalBo();
      principal.setPrincipalName(record.getPrincipalName());
      principal.setActive(true);
      principal.setPrincipalId(suppliedId);
      principal.setEntityId(entity.getId());
    }
    if (modified) {
      businessObjectService.save(principal);
    }
    
    return principal;
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

  protected EntityBo newEntityBo (final HRImportRecord record) {
    final EntityBo entity = new EntityBo();
    final String entityId = record.getEntityId();
    
    if (entityId != null) {
      entity.setId(entityId);
    }
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

    final Date oldSalDate = oldAttrs.getSalaryAnniversaryDate();
    final Date newSalDate = newAttrs.getSalaryAnniversaryDate();
    if (oldSalDate != null) {
      if (newSalDate == null || oldSalDate.getTime() != newSalDate.getTime()) {
        return false;
      }
    } else {
      if (newSalDate != null) {
        return false;
      }
    }
    
    if (!nullSafeEquals(oldAttrs.getSchool(),newAttrs.getSchool())) return false;
    if (!nullSafeEquals(oldAttrs.getSecondaryOfficeLocation(),newAttrs.getSecondaryOfficeLocation())) return false;
    if (oldAttrs.getVacationAccrualFlag() != newAttrs.isVacationAccrual()) return false;
    if (oldAttrs.getVeteranFlag() != newAttrs.isVeteran()) return false;
    if (!nullSafeEquals(oldAttrs.getVeteranType(),newAttrs.getVeteranType())) return false;
    if (!nullSafeEquals(oldAttrs.getVisaCode(),newAttrs.getVisaCode())) return false;

    final Date oldVisaDate = oldAttrs.getVisaRenewalDate();
    final Date newVisaDate = newAttrs.getVisaRenewalDate();
    if (oldVisaDate != null) {
      if (newVisaDate == null || oldVisaDate.getTime() != newVisaDate.getTime()) {
        return false;
      }
    } else {
      if (newVisaDate != null) {
        return false;
      }
    }

    if (!nullSafeEquals(oldAttrs.getVisaType(),newAttrs.getVisaType())) return false;
    final String oldGradYear = oldAttrs.getYearGraduated();
    final Integer newGradYearInt = newAttrs.getYearGraduated();
    final String newGradYear = newGradYearInt != null ? newGradYearInt.toString() : null;
    
    if (!nullSafeEquals(oldGradYear,newGradYear)) return false;
    return true;
  }
  
  protected boolean updateExtendedAttributes (final String principalId, final HRImportRecord record) {
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
      final Date annvDate = newAttrs.getSalaryAnniversaryDate();
      if (annvDate != null) {
        attrs.setSalaryAnniversaryDate(new java.sql.Date(annvDate.getTime()));        
      }
      attrs.setSchool(newAttrs.getSchool());
      attrs.setSecondaryOfficeLocation(newAttrs.getSecondaryOfficeLocation());
      attrs.setVacationAccrualFlag(newAttrs.isVacationAccrual());
      attrs.setVeteranFlag(newAttrs.isVeteran());
      attrs.setVeteranType(newAttrs.getVeteranType());
      attrs.setVisaCode(newAttrs.getVisaCode());
      final Date visaRenewDate = newAttrs.getVisaRenewalDate();
      if (visaRenewDate != null) {
        attrs.setVisaRenewalDate(new java.sql.Date(visaRenewDate.getTime()));        
      }
      attrs.setVisaType(newAttrs.getVisaType());
      final Integer gradYear = newAttrs.getYearGraduated();
      if (gradYear != null) {
        attrs.setYearGraduated(gradYear.toString());
      }

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
    appointmentAdapter.setUnitService(unitService);
  }
  
  public void setCacheManagerRegistry(final CacheManagerRegistry registry) {
    cacheManagerRegistry = registry;
  }
  
  @Override
  public void abort(final String importId) {
    stopRunningImport(importId);
  }

}
