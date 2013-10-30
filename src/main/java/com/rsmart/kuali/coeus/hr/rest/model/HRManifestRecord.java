package com.rsmart.kuali.coeus.hr.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "record")
public class HRManifestRecord {

  @XmlAttribute
  protected String principalId = null;
  @XmlAttribute
  protected String principalName = null;

  @XmlElement(name = "affiliations")
  protected Affiliations affiliations = null;
  @XmlElement(name = "addresses")
  protected Addresses addresses = null;
  @XmlElement(name = "names")
  protected Names names = null;
  @XmlElement(name = "phones")
  protected Phones phones = null;
  @XmlElement(name = "emails")
  protected Emails emails = null;
  @XmlElement(name = "kcExtendedAttributes")
  protected KCExtendedAttributes kcExtendedAttributes = null;
  @XmlElement(name = "degrees")
  protected Degrees degrees = null;
  @XmlElement(name = "appointments")
  protected Appointments appointments = null;

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

  public Affiliations getAffiliations() {
    return affiliations;
  }

  public void setAffiliations(Affiliations affiliations) {
    this.affiliations = affiliations;
  }

  public Addresses getAddresses() {
    return addresses;
  }

  public void setAddresses(Addresses addresses) {
    this.addresses = addresses;
  }

  public Names getNames() {
    return names;
  }

  public void setNames(Names names) {
    this.names = names;
  }

  public Phones getPhones() {
    return phones;
  }

  public void setPhones(Phones phones) {
    this.phones = phones;
  }

  public Emails getEmails() {
    return emails;
  }

  public void setEmails(Emails emails) {
    this.emails = emails;
  }

  public KCExtendedAttributes getKcExtendedAttributes() {
    return kcExtendedAttributes;
  }

  public void setKcExtendedAttributes(KCExtendedAttributes kcExtendedAttributes) {
    this.kcExtendedAttributes = kcExtendedAttributes;
  }

  public Degrees getDegrees() {
    return degrees;
  }

  public void setDegrees(Degrees degrees) {
    this.degrees = degrees;
  }

  public Appointments getAppointments() {
    return appointments;
  }

  public void setAppointment(Appointments appointments) {
    this.appointments = appointments;
  }

}