package com.rsmart.kuali.coeus.hr.service.adapter;

import static org.kuali.kra.logging.BufferedLogger.warn;

import java.util.Comparator;

import org.kuali.rice.core.api.mo.common.Defaultable;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.service.BusinessObjectService;

public abstract class PersistableBoMergeAdapter <T extends PersistableBusinessObject, X>
   implements Comparator<T> {
  
  protected boolean boIsInactivatable = false;
  protected boolean boIsDefaultable = false;
  
  public PersistableBoMergeAdapter() {
    final Class<T> clazz = getBusinessObjectClass();
    
    boIsInactivatable = Inactivatable.class.isAssignableFrom(clazz);
    boIsDefaultable = Defaultable.class.isAssignableFrom(clazz);
  }
  
  public final int nullSafeCompare (final String val0, final String val1) {
    final String comp0 = val0 == null ? "" : val0;
    final String comp1 = val1 == null ? "" : val1;
    
    return comp0.compareTo(comp1);
  }

  public final int nullSafeCompareIgnoreCase (final String val0, final String val1) {
    final String comp0 = val0 == null ? "" : val0;
    final String comp1 = val1 == null ? "" : val1;
    
    return comp0.compareToIgnoreCase(comp1);
  }

  public final int compare (T bo0, T bo1) {
    int comp = 0;
    
    if (isBoDefaultable()) {
      final int bo0Dft = ((Defaultable)bo0).isDefaultValue() ? 1 : 0;
      final int bo1Dft = ((Defaultable)bo1).isDefaultValue() ? 1 : 0;
      
      comp = bo1Dft - bo0Dft;
      if (comp != 0) {
        return comp;
      }
    }
    
    if (isBoInactivatable()) {
      final int bo0Active = ((Inactivatable)bo0).isActive() ? 1 : 0;
      final int bo1Active = ((Inactivatable)bo1).isActive() ? 1 : 0;
      
      comp = bo1Active - bo0Active;
      if (comp != 0) {
        return comp;
      }
    }
    
    return compareBOProperties(bo0, bo1);
  }
  
  public boolean isBoInactivatable() {
    return boIsInactivatable;
  }
  
  public boolean isBoDefaultable() {
    return boIsDefaultable;
  }
  
  public void save(final BusinessObjectService boService, T bo) {
    boService.save(bo);
  }
  
  public void delete(final BusinessObjectService boService, T bo) {
    boService.delete(bo);
  }
  
  public abstract int compareBOProperties (T bo0, T bo1);
  
  public abstract T newBO(String entityId);
  
  public abstract T setFields (T bo, X source);
  
  public abstract Class<T> getBusinessObjectClass();
  
  public abstract Class<X> getIncomingClass();
}
