package com.rsmart.kuali.coeus.hr.rest.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

public class StAXHRImport implements HRImport {

  protected XMLStreamReader               xsr;
  protected FileInputStream               fis;
  
  protected BigDecimal                    schemaVersion;
  protected String                        statusEmailRecipient;
  protected Date                          reportDate;
  protected int                           recordCount;
  protected StAXHRImportRecordCollection  records;
  
  public StAXHRImport(final String importFile) {
    try {
      final XMLInputFactory xif = XMLInputFactory.newFactory();
      
      // this is intentionally not closed until finalize
      fis = new FileInputStream(importFile);
      final StreamSource xml = new StreamSource(fis);
      
      xsr = xif.createXMLStreamReader(xml);

      readAttributes();
      records = new StAXHRImportRecordCollection(new File(importFile), recordCount);
      
      xsr.close();
    } catch (XMLStreamException e) {
      throw new IllegalStateException ("error reading XML import: " + e.getMessage(), e);
    } catch (FileNotFoundException e) {
      throw new IllegalStateException ("could not find import file: " + importFile, e);
    } finally {
      if (fis != null) {
        try {
          fis.close();
        } catch (Exception e) {
          //no-op
        }
      }
    }
  }
  
  protected void readAttributes() {
    try {
      xsr.nextTag();
      if(xsr.getLocalName().equals("hrmanifest")) {
        final int attribCount = xsr.getAttributeCount();
        for (int i = 0; i < attribCount; i++) {
          final QName attribName = xsr.getAttributeName(i);
          final String attribLocalPart = attribName.getLocalPart();
          final String attribValue = xsr.getAttributeValue(i);
          if ("schemaVersion".equals(attribLocalPart)) {
            schemaVersion = DatatypeConverter.parseDecimal(attribValue);
          } else if ("statusEmailRecipient".equals(attribLocalPart)) {
            statusEmailRecipient = attribValue;
          } else if ("reportDate".equals(attribLocalPart)) {
            final Calendar cal = DatatypeConverter.parseDateTime(attribValue);
            reportDate = cal.getTime();
          } else if ("recordCount".equals(attribLocalPart)) {
            recordCount = DatatypeConverter.parseInt(attribValue);
          }
        }
      }
    } catch (XMLStreamException e) {
      throw new IllegalStateException ("Failed to parse XML: " + e.getMessage(), e);
    }
  }
  
  @Override
  public BigDecimal getSchemaVersion() {
    return schemaVersion;
  }

  @Override
  public String getStatusEmailRecipient() {
    return statusEmailRecipient;
  }

  @Override
  public int getRecordCount() {
    return recordCount;
  }

  @Override
  public HRImportRecordCollection getRecords() {
    return records;
  }

  @Override
  public Date getReportDate() {
    return reportDate;
  }

}