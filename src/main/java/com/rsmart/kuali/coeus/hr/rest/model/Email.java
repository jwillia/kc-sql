package com.rsmart.kuali.coeus.hr.rest.model;

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
@XmlRootElement(name = "email")
public class Email extends ModelObject {

  @XmlAttribute
  protected String emailType;
  @XmlAttribute
  protected String emailAddress;
  @XmlAttribute(name = "default")
  protected boolean isDefault;
  @XmlAttribute
  protected boolean active;

  public String getEmailType() {
    return emailType;
  }

  public void setEmailType(String emailType) {
    this.emailType=trimToNull(emailType);
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress=trimToNull(emailAddress);
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
