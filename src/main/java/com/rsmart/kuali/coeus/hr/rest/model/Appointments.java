package com.rsmart.kuali.coeus.hr.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "appointments")
public class Appointments {

  @XmlElement(name = "appointment", type = Appointment.class)
  protected List<Appointment> appointments = new ArrayList<Appointment>();

  public Appointments() {
  }

  public Appointments(List<Appointment> appointments) {
    this.appointments = appointments;
  }

  public List<Appointment> getAppointments() {
    return appointments;
  }

  public void setAppointments(List<Appointment> appointments) {
    this.appointments = appointments;
  }
}
