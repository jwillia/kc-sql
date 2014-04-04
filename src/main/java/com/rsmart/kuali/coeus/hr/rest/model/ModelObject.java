package com.rsmart.kuali.coeus.hr.rest.model;

public class ModelObject {
  protected final String trimToNull(final String from) {
    return (from == null || from.trim().length() == 0) ? null : from;
  }
}
