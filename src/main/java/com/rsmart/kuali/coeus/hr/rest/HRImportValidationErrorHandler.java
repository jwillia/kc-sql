package com.rsmart.kuali.coeus.hr.rest;

import static org.kuali.kra.logging.BufferedLogger.warn;
import static org.kuali.kra.logging.BufferedLogger.fatal;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This adapter ensures that parsing exceptions are logged.
 * 
 * @author duffy
 *
 */
public class HRImportValidationErrorHandler implements ErrorHandler {

  public void warning(SAXParseException exception) throws SAXException {
    warn(exception);
  }

  public void error(SAXParseException exception) throws SAXException {
    org.kuali.kra.logging.BufferedLogger.error(exception);
  }

  public void fatalError(SAXParseException exception) throws SAXException {
    fatal(exception);
  }

}
