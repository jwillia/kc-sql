package com.rsmart.kuali.coeus.hr.rest.model;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

public class StAXHRImportRecordCollection implements HRImportRecordCollection {

  //DO NOT close this FileDescriptor, it will close other streams that are using the same
  // descriptor. This is a borrowed resource, the caller of new StAXHRImportRecordCollection(...)
  // should handle its lifecycle.
  protected FileDescriptor fd = null;
  protected int count = 0;

  public StAXHRImportRecordCollection (final FileDescriptor fd, final int count) {
    this.fd = fd;
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
    int                 numRead = 0;
    FileInputStream     fis = null;
    XMLStreamReader     xsr = null;
    JAXBContext         jc = null;
    Unmarshaller        unmarshaller = null;
    
    public RecordIterator () {
      try {
        final XMLInputFactory xif = XMLInputFactory.newFactory();

        FileInputStream fis = new FileInputStream(fd);
        
        final StreamSource xml = new StreamSource(fis);

        xsr = xif.createXMLStreamReader(xml);
        jc = JAXBContext.newInstance(HRImportRecord.class);
        unmarshaller = jc.createUnmarshaller();        

        xsr.nextTag();
        while(!xsr.getLocalName().equals("record")) {
          xsr.next();
        }
      }
      catch (Exception e) {
        throw new RuntimeException ("Error reading XML", e);
      }
    }
    
    @Override
    public boolean hasNext() {
      if (numRead >= count) {
        closeAll();
        return false;
      }
      return true;
    }

    @Override
    public HRImportRecord next() {
      try {
        final JAXBElement<HRImportRecord> recordElement = unmarshaller.unmarshal(xsr, HRImportRecord.class);
        xsr.next();
        numRead++;
        return recordElement.getValue();
      } catch (Exception e) {
        throw new RuntimeException ("Error reading XML", e);
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
