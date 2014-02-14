package com.rsmart.kuali.coeus.hr.rest.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This object represents a single user. It contains collections of sub-objects. Each
 * of the sub-objects maps to a dependent entity object from KIM or KRA.
 * 
 * @author duffy
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "record")
public class HRManifestRecord {

  @XmlAttribute
  @NotNull
  protected String principalId = null;
  @XmlAttribute
  @NotNull
  protected String principalName = null;
  @XmlAttribute
  protected boolean active = true;

  @XmlElement(name = "affiliations")
  @Valid
  protected AffiliationCollection affiliationCollection = null;
  @XmlElement(name = "addresses")
  @Valid
  protected AddressCollection addressCollection = null;
  @XmlElement(name = "names")
  @Valid
  protected NameCollection nameCollection = null;
  @XmlElement(name = "phones")
  @Valid
  protected PhoneCollection phoneCollection = null;
  @XmlElement(name = "emails")
  @Valid
  protected EmailCollection emailCollection = null;
  @XmlElement(name = "kcExtendedAttributes")
  @Valid
  protected KCExtendedAttributes kcExtendedAttributes = null;
  @XmlElement(name = "degrees")
  @Valid
  protected DegreeCollection degreeCollection = null;
  @XmlElement(name = "appointments")
  @Valid
  protected AppointmentCollection appointmentCollection = null;

  public String getPrincipalId() {
    return principalId;
  }

  public void setPrincipalId(String principalId) {
    this.principalId = principalId;
  }

  public String getPrincipalName() {
    return principalName;
  }

  public void setPrincipalName(String principalName) {
    this.principalName = principalName;
  }
  
  public boolean isActive() {
    return active;
  }
  
  public void setActive(final boolean active) {
    this.active = active;
  }

  public AffiliationCollection getAffiliationCollection() {
    return affiliationCollection;
  }

  public void setAffiliationCollection(AffiliationCollection affiliations) {
    this.affiliationCollection = affiliations;
  }

  public AddressCollection getAddressCollection() {
    return addressCollection;
  }

  public void setAddressCollection(AddressCollection addresses) {
    this.addressCollection = addresses;
  }

  public NameCollection getNameCollection() {
    return nameCollection;
  }

  public void setNameCollection(NameCollection names) {
    this.nameCollection = names;
  }

  public PhoneCollection getPhoneCollection() {
    return phoneCollection;
  }

  public void setPhoneCollection(PhoneCollection phones) {
    this.phoneCollection = phones;
  }

  public EmailCollection getEmailCollection() {
    return emailCollection;
  }

  public void setEmailCollection(EmailCollection emails) {
    this.emailCollection = emails;
  }

  public KCExtendedAttributes getKcExtendedAttributes() {
    return kcExtendedAttributes;
  }

  public void setKcExtendedAttributes(KCExtendedAttributes kcExtendedAttributes) {
    this.kcExtendedAttributes = kcExtendedAttributes;
  }

  public DegreeCollection getDegreeCollection() {
    return degreeCollection;
  }

  public void setDegreeCollection(DegreeCollection degrees) {
    this.degreeCollection = degrees;
  }

  public AppointmentCollection getAppointmentCollection() {
    return appointmentCollection;
  }

  public void setAppointmentCollection(AppointmentCollection appointments) {
    this.appointmentCollection = appointments;
  }

}