package com.rsmart.kuali.coeus.hr.service;

import java.util.List;

/**
 * HRManifestImportException is a wrapper around Exceptions that occur
 * during processing an import. Any Exception that is caught will be
 * added to the collection so all Exceptions can be reported at the end
 * of the import. In this way processing can continue for those records
 * that cause no import errors.
 * 
 * @author duffy
 *
 */
public class HRManifestImportException extends Exception {

  private static final long serialVersionUID = -4755759601554962891L;

  protected List<Object[]> errors;

  public HRManifestImportException() {
  }

  public HRManifestImportException(List<Object[]> errors) {
    this.errors = errors;
  }

  public List<Object[]> getErrors() {
    return errors;
  }

  public void setErrors(List<Object[]> errors) {
    this.errors = errors;
  }

  @Override
  public String getMessage() {
    return (Integer.toString(errors.size()) + " errors encountered");
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();

    sb.append(getMessage()).append(":\n");

    for (final Object[] error : getErrors()) {
      final Integer recNo = (Integer) error[0];
      final Exception e = (Exception) error[1];

      sb.append('\t').append(recNo).append('\t').append('[')
          .append(e.getClass().getName()).append("]: ").append(e.getMessage())
          .append('\n');
    }

    return sb.toString();
  }

}