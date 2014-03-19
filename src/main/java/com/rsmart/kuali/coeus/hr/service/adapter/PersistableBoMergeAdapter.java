package com.rsmart.kuali.coeus.hr.service.adapter;

import java.util.Comparator;

import org.kuali.rice.core.api.mo.common.Defaultable;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.service.BusinessObjectService;

/**
 * This manages all logic required to adapt an object from an 
 * {@link com.rsmart.kuali.coeus.hr.rest.model.HRImportRecord HRImportRecord} to
 * a PersistableBusinessObject within the KIM domain model.
 * 
 * @author duffy
 *
 * @param <T> type of KRA PersistableBusinessObject
 * @param <X> type of JAXB import object
 */
public abstract class PersistableBoMergeAdapter <T extends PersistableBusinessObject, X>
   implements Comparator<T> {
  
  /** track whether the type T implements Inactivatable */
  protected boolean boIsInactivatable = false;
  /** track whether the type T implements Defaultable */
  protected boolean boIsDefaultable = false;
  
  public PersistableBoMergeAdapter() {
    final Class<T> clazz = getBusinessObjectClass();

    // set these once for the types bound to this class
    boIsInactivatable = Inactivatable.class.isAssignableFrom(clazz);
    boIsDefaultable = Defaultable.class.isAssignableFrom(clazz);
  }
  
  /**
   * Compare two strings accounting for null values.
   * nulls are replaced with empty Strings for the purposes of comparison.
   * 
   * @param val0
   * @param val1
   * @return 0 for equality; < 0 if val0 comes first, > 0 if val1 comes first
   */
  public final int nullSafeCompare (final String val0, final String val1) {
    final String comp0 = val0 == null ? "" : val0;
    final String comp1 = val1 == null ? "" : val1;
    
    return comp0.compareTo(comp1);
  }

  /**
   * Compare two strings accounting for null values, ignoring case.
   * nulls are replaced with empty Strings for the purposes of comparison.
   * 
   * @param val0
   * @param val1
   * @return 0 for equality; < 0 if val0 comes first, > 0 if val1 comes first
   */
  public final int nullSafeCompareIgnoreCase (final String val0, final String val1) {
    final String comp0 = val0 == null ? "" : val0;
    final String comp1 = val1 == null ? "" : val1;
    
    return comp0.compareToIgnoreCase(comp1);
  }

  /**
   * Compare two objects of type T to impose a strict ordering.
   */
  public final int compare (T bo0, T bo1) {
    int comp = 0;
    
    // include consideration for the isDefaultValue flag - default value comes first
    if (isBoDefaultable()) {
      final int bo0Dft = ((Defaultable)bo0).isDefaultValue() ? 1 : 0;
      final int bo1Dft = ((Defaultable)bo1).isDefaultValue() ? 1 : 0;
      
      comp = bo1Dft - bo0Dft;
      if (comp != 0) {
        return comp;
      }
    }
    
    // include consideration for the isActive flag - active comes first
    if (isBoInactivatable()) {
      final int bo0Active = ((Inactivatable)bo0).isActive() ? 1 : 0;
      final int bo1Active = ((Inactivatable)bo1).isActive() ? 1 : 0;
      
      comp = bo1Active - bo0Active;
      if (comp != 0) {
        return comp;
      }
    }
    
    // compare the rest of the fields on the object
    return compareBOProperties(bo0, bo1);
  }
  
  public boolean isBoInactivatable() {
    return boIsInactivatable;
  }
  
  public boolean isBoDefaultable() {
    return boIsDefaultable;
  }
  
  /**
   * Implements the default save logic for the business object. This
   * functionality is encapsulated in PersistableBoMergeAdapter so
   * overrides can perform updates to dependent business objects.
   * 
   * (c.f. @link com.rsmart.kuali.coeus.hr.service.adapter.impl.EntityEmploymentBoAdapter.save EntityEmploymentBoAdapter)
   * @param boService
   * @param bo
   */
  public void save(final BusinessObjectService boService, T bo) {
    boService.save(bo);
  }
  
  /**
   * Implements the default delet logic for the business object. This
   * functionality is encapsulated in PersistableBoMergeAdapter so
   * overrides can perform updates to dependent business objects.
   * 
   * (c.f. @link com.rsmart.kuali.coeus.hr.service.adapter.impl.EntityEmploymentBoAdapter.delete EntityEmploymentBoAdapter)
   * @param boService
   * @param bo
   */
  public void delete(final BusinessObjectService boService, T bo) {
    boService.delete(bo);
  }
  
  /**
   * Implements comparison logic to allow sorting of BusinessObjects. This should return 0 if and only if all
   * fields that are available in the XML import are equivalent. Fields that are not visible (eg. foreign key
   * fields that relate dependant objects) should ***NOT*** be included in the comparison. Since the XML
   * will never contain these deep identifiers including those foreign key references will cause all imports
   * to fail equivalence tests and to be replaced on every import.
   * 
   * @param bo0
   * @param bo1
   * @return
   */
  public abstract int compareBOProperties (T bo0, T bo1);
  
  /**
   * This is a factory method for the business object. All business objects handled by subclasses of
   * PersistableBOMergeAdapter are dependent entities on the EntityBo. For this reason the entityId
   * is passed in. It should be set on the underlying business object before return.
   * 
   * @param entityId
   * @return
   */
  public abstract T newBO(String entityId);
  
  /**
   * This is the method which translates the incoming JAXB object to the business object. It
   * should be assumed that the business object is not null and is empty except for the entityId field.
   * 
   * @param bo
   * @param source
   * @return
   */
  public abstract T setFields (T bo, X source);
  
  /**
   * This is a method which allows {@link com.rsmart.kuali.coeus.hr.rest.service.HrImportService}
   * to determine the specific type of business object handled by this class. Type erasure prevents
   * determining the type of the generic parameter T at runtime, so this explicit method is
   * necessary.
   * 
   * @return
   */
  public abstract Class<T> getBusinessObjectClass();
  
  /**
   * This is a method which allows {@link com.rsmart.kuali.coeus.hr.rest.service.HrImportService}
   * to determine the specific type of incoming JAXB object handled by this class. Type erasure prevents
   * determining the type of the generic parameter X at runtime, so this explicit method is
   * necessary.
   * 
   * @return
   */
  public abstract Class<X> getIncomingClass();
}
