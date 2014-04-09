package com.rsmart.kuali.coeus.hr.rest.model;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import static org.kuali.kra.logging.BufferedLogger.debug;
import static org.kuali.kra.logging.BufferedLogger.error;

public class StAXHRImportRecordCollection implements HRImportRecordCollection {

  protected File file = null;
  protected int count = 0;

  public StAXHRImportRecordCollection (final File file, final int count) {
    this.file = file;
    this.count = count;
  }
  
  @Override
  public int getCount() {
    return count;
  }
  @Override
  public Iterator<HRImportRecord> iterator() {
    return new RecordIterator();
  }
  
  class RecordIterator implements Iterator<HRImportRecord> {
    
    boolean             next = false;
    FileInputStream     fis = null;
    XMLStreamReader     xsr = null;
    JAXBContext         jc = null;
    Unmarshaller        unmarshaller = null;
    
    public RecordIterator () {
      try {
        final XMLInputFactory xif = XMLInputFactory.newFactory();

        FileInputStream fis = new FileInputStream(file);
        
        final StreamSource xml = new StreamSource(fis);

        xsr = xif.createXMLStreamReader(xml);
        jc = JAXBContext.newInstance(HRImportRecord.class);
        unmarshaller = jc.createUnmarshaller();        

        xsr.nextTag();
        while(!xsr.getLocalName().equals("record")) {
          debug(xsr.getLocalName());
          xsr.nextTag();
        }
      }
      catch (Exception e) {
        throw new RuntimeException ("Error reading XML", e);
      }
    }
    
    @Override
    public boolean hasNext() {
      if (xsr.isStartElement() && "record".equals(xsr.getLocalName())) {
        return true;
      }
      closeAll();
      return false;
    }

    @Override
    public HRImportRecord next() {
      try {
        final JAXBElement<HRImportRecord> recordElement = unmarshaller.unmarshal(xsr, HRImportRecord.class);
        xsr.nextTag();
        return recordElement.getValue();
      } catch (Exception e) {
        try {
          //attempt to recover for the next operation
          while (xsr.hasNext() && !xsr.isStartElement() && !"record".equals(xsr.getLocalName())) {
            xsr.nextTag();
          }
        }
        catch (Exception e1) {
          error ("attempt to recover from malformed record failed, parse ending", e1);
        }
        throw new RuntimeException ("Error reading XML: " + e.getMessage(), e);
      }
    }

    @Override
    public void remove() {
      throw new IllegalStateException ("Operation not permitted");
    }
    
    public void finalize() {
      closeAll();
    }
    
    protected void closeAll() {
      if (xsr != null) {
        try { xsr.close(); } catch(Exception e) {/*no-op*/}
      }
      if (fis != null) {
        try { fis.close(); } catch(Exception e) {/*no-op*/}
      }
    }
  }

}
