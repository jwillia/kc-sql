package com.rsmart.kuali.coeus.hr.rest.model;

import java.util.Iterator;


public interface HRImportRecordCollection {

  public int getCount();
  
  public Iterator<HRImportRecord> iterator();

}