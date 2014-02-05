package com.rsmart.kuali.coeus.hr.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "emails")
public class EmailCollection {

  @XmlElement(name = "email", type = Email.class)
  @Valid
  protected List<Email> emails = new ArrayList<Email>();

  public EmailCollection() {
  }

  public EmailCollection(List<Email> emails) {
    this.emails = emails;
  }

  public List<Email> getEmails() {
    return emails;
  }

  public void setEmails(List<Email> emails) {
    this.emails = emails;
  }
}
