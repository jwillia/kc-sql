package com.rsmart.kuali.coeus.hr.service.adapter.impl;

import java.sql.Date;

import org.kuali.kra.bo.PersonAppointment;
import org.kuali.kra.bo.Unit;
import org.kuali.kra.budget.BudgetDecimal;
import org.kuali.kra.budget.personnel.AppointmentType;
import org.kuali.kra.service.UnitService;

import com.rsmart.kuali.coeus.hr.rest.model.Appointment;
import com.rsmart.kuali.coeus.hr.service.adapter.PersistableBoMergeAdapter;

public class PersonAppointmentBoAdapter extends PersistableBoMergeAdapter<PersonAppointment, Appointment> {

  protected UnitService unitService = null;
  
  public void setUnitService (final UnitService unitService) {
    this.unitService = unitService;
  }
  
  @Override
  public int compareBOProperties(PersonAppointment bo0, PersonAppointment bo1) {
    int comp = 0;
    
    comp = nullSafeCompare(bo0.getTypeCode(), bo1.getTypeCode());
    if (comp != 0) {
      return comp;
    }
    
    final Date endDate0 = bo0.getEndDate();
    final Date endDate1 = bo1.getEndDate();
    
    if (endDate0 != null) {
      comp = endDate0.compareTo(endDate1);
    } else {
      if (endDate1 != null) {
        return 1;
      }
      comp = 0;
    }
    if (comp != 0) {
      return comp;
    }
    
    comp = nullSafeCompare(bo0.getJobCode(), bo1.getJobCode());
    if (comp != 0) {
      return comp;
    }
    comp = nullSafeCompare(bo0.getJobTitle(), bo1.getJobTitle());
    if (comp != 0) {
      return comp;
    }
    comp = nullSafeCompare(bo0.getPreferedJobTitle(), bo1.getPreferedJobTitle());
    if (comp != 0) {
      return comp;
    }
    
    final BudgetDecimal sal0 = BudgetDecimal.returnZeroIfNull(bo0.getSalary());
    final BudgetDecimal sal1 = BudgetDecimal.returnZeroIfNull(bo1.getSalary());
    
    comp = Float.compare(sal0.getFloatValue(),sal1.getFloatValue());
    if (comp != 0) {
      return comp;
    }

    final Date startDate0 = bo0.getStartDate();
    final Date startDate1 = bo1.getStartDate();
    
    if (startDate0 != null) {
      comp = startDate0.compareTo(startDate1);
    } else {
      if (startDate1 != null) {
        return 1;
      }
      comp = 0;
    }
    if (comp != 0) {
      return comp;
    }
    
    comp = nullSafeCompare(bo0.getUnitNumber(), bo1.getUnitNumber());
    
    return comp;
  }

  @Override
  public PersonAppointment newBO(String entityId) {
    final PersonAppointment appt = new PersonAppointment();
    
    appt.setPersonId(entityId);

    return appt;
  }

  @Override
  public PersonAppointment setFields(PersonAppointment bo, Appointment source) {
    final AppointmentType apptType = new AppointmentType();
    apptType.setAppointmentTypeCode(source.getAppointmentType());

    bo.setAppointmentType(apptType);
    bo.setJobCode(source.getJobCode());
    bo.setJobTitle(source.getJobTitle());
    bo.setPreferedJobTitle(source.getPreferedJobTitle());
    bo.setSalary(new BudgetDecimal(source.getSalary()));
    bo.setStartDate(new java.sql.Date(source.getStartDate().getTime()));
    if (source.getEndDate() != null) {
      bo.setEndDate(new java.sql.Date(source.getEndDate().getTime()));
    }
    
    if (unitService == null) {
      throw new IllegalStateException (PersonAppointmentBoAdapter.class.getSimpleName() + " does not have a UnitService implementation");
    }
    Unit unit = unitService.getUnit(source.getUnitNumber());
    bo.setUnit(unit);
    bo.setUnitNumber(unit.getUnitNumber());
    
    return bo;
  }

  @Override
  public Class<PersonAppointment> getBusinessObjectClass() {
    return PersonAppointment.class;
  }

  @Override
  public Class<Appointment> getIncomingClass() {
    return Appointment.class;
  }

}
