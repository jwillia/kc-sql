package com.rsmart.kuali.coeus.hr.rest.model;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Part of the HRImport object graph that is created when the HR import XML
 * file is parsed.
 * 
 * See {@link com.rsmart.kuali.coeus.hr.rest.model.HRImport HRImport} for more details.
 * @author duffy
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "address")
public class Address extends ModelObject {

  @XmlAttribute
  @NotNull
  protected String addressTypeCode;
  @XmlAttribute
  @NotNull
  protected String addressLine1;
  @XmlAttribute
  protected String addressLine2;
  @XmlAttribute
  protected String addressLine3;
  @XmlAttribute
  @NotNull
  protected String city;
  @XmlAttribute
  @NotNull
  protected String stateOrProvince;
  @XmlAttribute
  @NotNull
  protected String postalCode;
  @XmlAttribute
  @NotNull
  protected String country;
  @XmlAttribute(name = "default")
  protected boolean isDefault;
  @XmlAttribute
  protected boolean active;

  public String getAddressTypeCode() {
    return addressTypeCode;
  }

  public void setAddressTypeCode(String addressTypeCode) {
    this.addressTypeCode=trimToNull(addressTypeCode);
  }

  public String getAddressLine1() {
    return addressLine1;
  }

  public void setAddressLine1(String addressLine1) {
    this.addressLine1=trimToNull(addressLine1);
  }

  public String getAddressLine2() {
    return addressLine2;
  }

  public void setAddressLine2(String addressLine2) {
    this.addressLine2=trimToNull(addressLine2);
  }

  public String getAddressLine3() {
    return addressLine3;
  }

  public void setAddressLine3(String addressLine3) {
    this.addressLine3=trimToNull(addressLine3);
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city=trimToNull(city);
  }

  public String getStateOrProvince() {
    return stateOrProvince;
  }

  public void setStateOrProvince(String stateOrProvince) {
    this.stateOrProvince=trimToNull(stateOrProvince);
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode=trimToNull(postalCode);
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country=trimToNull(country);
  }

  public boolean isDefault() {
    return isDefault;
  }

  public void setDefault(boolean isDefault) {
    this.isDefault = isDefault;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

}
