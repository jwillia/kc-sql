package com.rsmart.kuali.coeus.hr.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class HRManifestValidationErrorHandler implements ErrorHandler {

  private static final Log LOG = LogFactory
      .getLog(HRManifestValidationErrorHandler.class);

  public void warning(SAXParseException exception) throws SAXException {
    LOG.warn(exception);
  }

  public void error(SAXParseException exception) throws SAXException {
    LOG.error(exception);
  }

  public void fatalError(SAXParseException exception) throws SAXException {
    LOG.fatal(exception);
  }

}
