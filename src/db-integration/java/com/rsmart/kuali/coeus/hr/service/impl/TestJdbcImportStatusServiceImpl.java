package com.rsmart.kuali.coeus.hr.service.impl;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.rsmart.kuali.coeus.hr.service.ImportError;
import com.rsmart.kuali.coeus.hr.service.ImportStatus;
import com.rsmart.kuali.coeus.hr.service.ImportStatus.Status;

public class TestJdbcImportStatusServiceImpl {

  ApplicationContext context;
  JdbcImportStatusServiceImpl statusService;
  DriverManagerDataSource datasource;
  
  @Before
  public void setupStatusService() throws Exception {
    context = new ClassPathXmlApplicationContext(getClass().getName() + ".xml");
    statusService = (JdbcImportStatusServiceImpl) context.getBean("statusService");
    datasource = (DriverManagerDataSource) context.getBean("dataSource");
  }
  
  @After
  public void resetDb() throws Exception {
    final Connection conn = datasource.getConnection();
    PreparedStatement stmt; 
    
    stmt = conn.prepareStatement("delete from cx_hrapi_import_errors");
    stmt.execute();
    
    stmt = conn.prepareStatement("delete from cx_hrapi_import_persons");
    stmt.execute();
    
    stmt = conn.prepareStatement("delete from cx_hrapi_import_status");
    stmt.execute();
  }

  public ImportStatus initiateImport() throws Exception {
    return statusService.initiateImport("testImport", 5);
  }
  
  @Test
  public void testInitiatImportCreatedAndGetable() throws Exception {
    final long now = System.currentTimeMillis();
    ImportStatus status = initiateImport();

    assertNotNull(status);
    assertTrue(status.getEndTimeInMillis() < 0);
    assertEquals(0, status.getErrorCount());
    assertEquals("testImport", status.getImportId());
    assertEquals(0, status.getProcessedRecordCount());
    assertEquals(5, status.getRecordTotal());
    assertTrue(status.getStartTimeInMillis() >= now);
    assertEquals(Status.PROCESSING, status.getStatus());
    
    status = statusService.getImportStatus("testImport");

    assertNotNull(status);
    assertTrue(status.getEndTimeInMillis() < 0);
    assertEquals(0, status.getErrorCount());
    assertEquals("testImport", status.getImportId());
    assertEquals(0, status.getProcessedRecordCount());
    assertEquals(5, status.getRecordTotal());
    assertTrue(status.getStartTimeInMillis() >= now);
    assertEquals(Status.PROCESSING, status.getStatus());
  }

  @Test
  public void testNormalStatusLifecycle() throws Exception {
    final long now = System.currentTimeMillis();
    ImportStatus status = initiateImport();

    assertNotNull(status);
    assertTrue(status.getEndTimeInMillis() < 0);
    assertEquals(0, status.getErrorCount());
    assertEquals("testImport", status.getImportId());
    assertEquals(0, status.getProcessedRecordCount());
    assertEquals(5, status.getRecordTotal());
    assertTrue(status.getStartTimeInMillis() >= now);
    assertEquals(Status.PROCESSING, status.getStatus());

    statusService.recordProcessed("testImport", "person1");
    status = statusService.getImportStatus("testImport");

    assertNotNull(status);
    assertTrue(status.getEndTimeInMillis() < 0);
    assertEquals(0, status.getErrorCount());
    assertEquals("testImport", status.getImportId());
    assertEquals(1, status.getProcessedRecordCount());
    assertEquals(5, status.getRecordTotal());
    assertTrue(status.getStartTimeInMillis() >= now);
    assertEquals(Status.PROCESSING, status.getStatus());

    statusService.recordProcessed("testImport", "person2");
    status = statusService.getImportStatus("testImport");

    assertNotNull(status);
    assertTrue(status.getEndTimeInMillis() < 0);
    assertEquals(0, status.getErrorCount());
    assertEquals("testImport", status.getImportId());
    assertEquals(2, status.getProcessedRecordCount());
    assertEquals(5, status.getRecordTotal());
    assertTrue(status.getStartTimeInMillis() >= now);
    assertEquals(Status.PROCESSING, status.getStatus());

    statusService.recordProcessed("testImport", "person3");
    status = statusService.getImportStatus("testImport");

    assertNotNull(status);
    assertTrue(status.getEndTimeInMillis() < 0);
    assertEquals(0, status.getErrorCount());
    assertEquals("testImport", status.getImportId());
    assertEquals(3, status.getProcessedRecordCount());
    assertEquals(5, status.getRecordTotal());
    assertTrue(status.getStartTimeInMillis() >= now);
    assertEquals(Status.PROCESSING, status.getStatus());

    statusService.recordProcessed("testImport", "person4");
    status = statusService.getImportStatus("testImport");

    assertNotNull(status);
    assertTrue(status.getEndTimeInMillis() < 0);
    assertEquals(0, status.getErrorCount());
    assertEquals("testImport", status.getImportId());
    assertEquals(4, status.getProcessedRecordCount());
    assertEquals(5, status.getRecordTotal());
    assertTrue(status.getStartTimeInMillis() >= now);
    assertEquals(Status.PROCESSING, status.getStatus());

    statusService.recordProcessed("testImport", "person5");
    status = statusService.getImportStatus("testImport");

    assertNotNull(status);
    assertTrue(status.getEndTimeInMillis() < 0);
    assertEquals(0, status.getErrorCount());
    assertEquals("testImport", status.getImportId());
    assertEquals(5, status.getProcessedRecordCount());
    assertEquals(5, status.getRecordTotal());
    assertTrue(status.getStartTimeInMillis() >= now);
    assertEquals(Status.PROCESSING, status.getStatus());

    final long end = System.currentTimeMillis();
    statusService.completeImport("testImport");
    status = statusService.getImportStatus("testImport");

    assertNotNull(status);
    assertTrue(status.getEndTimeInMillis() >= end);
    assertEquals(0, status.getErrorCount());
    assertEquals("testImport", status.getImportId());
    assertEquals(5, status.getProcessedRecordCount());
    assertEquals(5, status.getRecordTotal());
    assertTrue(status.getStartTimeInMillis() >= now);
    assertEquals(Status.COMPLETE, status.getStatus());
  }
  
  @Test
  public void testRecordError() throws Exception {
    final long now = System.currentTimeMillis();
    ImportStatus status = initiateImport();
    ImportError error = new ImportError(42, "fordprefect", new RuntimeException("foo"));
    
    statusService.recordError("testImport", error);
    status = statusService.getImportStatus("testImport");

    assertNotNull(status);
    assertTrue(status.getEndTimeInMillis() < 0);
    assertEquals(1, status.getErrorCount());
    assertEquals("testImport", status.getImportId());
    assertEquals(1, status.getProcessedRecordCount());
    assertEquals(5, status.getRecordTotal());
    assertTrue(status.getStartTimeInMillis() >= now);
    assertEquals(Status.PROCESSING, status.getStatus());
    
    List<ImportError> errors = status.getErrors();
    assertNotNull(errors);
    assertEquals(1, errors.size());
    error = errors.get(0);
    
    assertEquals(42, error.getRecordNumber());
    assertEquals("fordprefect", error.getPrincipalName());
    RuntimeException re = (RuntimeException)error.getException();
    assertNotNull(re);
    assertEquals("foo", re.getMessage());
  }

  public void assertPersonStatus (final String importId, final String personId, final String status) throws Exception {
    final Connection conn = datasource.getConnection();
    PreparedStatement stmt; 
    
    stmt = conn.prepareStatement("select recordStatus from cx_hrapi_import_persons where importId = '" + importId +
        "' and personId = '" + personId + "'");
    ResultSet rs = stmt.executeQuery();
    assertTrue(rs.next());
    assertEquals(status, rs.getString("recordStatus"));
  }
  
  @Test
  public void testInactivate() throws Exception {
    final long now = System.currentTimeMillis();
    ImportStatus status = initiateImport();

    statusService.recordInactivated("testImport", "person1");
    
    status = statusService.getImportStatus("testImport");

    assertNotNull(status);
    assertTrue(status.getEndTimeInMillis() < 0);
    assertEquals(0, status.getErrorCount());
    assertEquals("testImport", status.getImportId());
    assertEquals(1, status.getProcessedRecordCount());
    assertEquals(5, status.getRecordTotal());
    assertTrue(status.getStartTimeInMillis() >= now);
    assertEquals(Status.PROCESSING, status.getStatus());
    
    assertPersonStatus("testImport", "person1", "INACTIVE");
  }

  @Test
  public void testAbortImport() throws Exception {
    final long now = System.currentTimeMillis();
    ImportStatus status = initiateImport();

    final long end = System.currentTimeMillis();
    statusService.abort("testImport");
    
    status = statusService.getImportStatus("testImport");

    assertNotNull(status);
    assertTrue(status.getEndTimeInMillis() >= end);
    assertEquals(0, status.getErrorCount());
    assertEquals("testImport", status.getImportId());
    assertEquals(0, status.getProcessedRecordCount());
    assertEquals(5, status.getRecordTotal());
    assertTrue(status.getStartTimeInMillis() >= now);
    assertEquals(Status.ABORTED, status.getStatus());
  }

  @Test
  public void testAbnormalTermination() throws Exception {
    final long now = System.currentTimeMillis();
    ImportStatus status = initiateImport();

    final long end = System.currentTimeMillis();
    statusService.abnormalTermination("testImport");
    
    status = statusService.getImportStatus("testImport");

    assertNotNull(status);
    assertTrue(status.getEndTimeInMillis() >= end);
    assertEquals(0, status.getErrorCount());
    assertEquals("testImport", status.getImportId());
    assertEquals(0, status.getProcessedRecordCount());
    assertEquals(5, status.getRecordTotal());
    assertTrue(status.getStartTimeInMillis() >= now);
    assertEquals(Status.ABNORMAL_TERMINATION, status.getStatus());
  }
}
