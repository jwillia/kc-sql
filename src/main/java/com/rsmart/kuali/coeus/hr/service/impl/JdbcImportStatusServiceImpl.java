package com.rsmart.kuali.coeus.hr.service.impl;

import static org.kuali.kra.logging.BufferedLogger.debug;
import static org.kuali.kra.logging.BufferedLogger.error;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.rsmart.kuali.coeus.hr.service.ImportError;
import com.rsmart.kuali.coeus.hr.service.ImportStatus;
import com.rsmart.kuali.coeus.hr.service.ImportStatus.Status;
import com.rsmart.kuali.coeus.hr.service.ImportStatusService;

public class JdbcImportStatusServiceImpl extends JdbcDaoSupport implements ImportStatusService {

  private final static String 
    SQL_CREATE_STATUS = "INSERT INTO import_status(importId, numRecords, startTime) VALUES (?, ?, ?)";
  private final static String 
    SQL_CREATE_ERROR = "INSERT INTO import_errors(importId, recordNum, exception) VALUES (?, ?, ?)";
  private final static String 
    SQL_UPDATE_STATUS = "UPDATE import_status SET status = ?, endTime = ? WHERE importId = ?";
  private final static String 
    SQL_UPDATE_STATUS_DETAIL = "UPDATE import_status SET status = ?, detail = ?, endTime = ? WHERE importId = ?";
  private final static String
    SQL_INCREMENT = "UPDATE import_status SET numProcessed = numProcessed + 1 WHERE importId = ?";
  private final static String 
    SQL_SELECT_STATUS = "SELECT importId, numRecords, numProcessed, status, detail, startTime, endTime FROM import_status WHERE importId = ?";
  private final static String 
    SQL_SELECT_ERRORS = "SELECT errorId, recordNum, exception FROM import_errors WHERE importId = ?";
  private final static String
    SQL_SELECT_MISSING_IDS = "SELECT personId FROM import_persons WHERE importId != ? AND recordStatus != 'INACTIVE'";
  private final static String
    SQL_PERSON_UPDATE = "INSERT INTO import_persons(personId, importId, recordStatus) VALUES(?, ?, ?) " +
        "ON DUPLICATE KEY UPDATE importId=?, recordStatus=?";
  
  protected ImportStatusExtractor     statusExtractor = new ImportStatusExtractor();
  protected ImportErrorMapper         errorMapper = new ImportErrorMapper();
  
  private class ImportStatusExtractor implements ResultSetExtractor<ImportStatus> {

    @Override
    public ImportStatus extractData(ResultSet rs) throws SQLException,
        DataAccessException {
      
      if (rs.next()) {
        final String importId = rs.getString("importId");
        final int recordCount = rs.getInt("numRecords");
        final ImportStatus status = new ImportStatus(importId, recordCount);
        
        status.setMessageDetail(rs.getString("detail"));
        status.setStatus(Status.valueOf(rs.getString("status")));
        status.setStartTimeInMillis(rs.getLong("startTime"));
        status.setEndTimeInMillis(rs.getLong("endTime"));
        status.setProcessed(rs.getInt("numProcessed"));
        
        return status;
      }

      return null;
    }
  }

  private class ImportErrorMapper implements RowMapper<ImportError> {

    @Override
    public ImportError mapRow(ResultSet rs, int rowNum) throws SQLException {
 
        final int recordNumber = rs.getInt("recordNum");
        Exception exception = null;
        final byte[] buf = rs.getBytes("exception");
        if (buf != null) {
          ObjectInputStream objStream;
          try {
            objStream = new ObjectInputStream(new ByteArrayInputStream(buf));
            exception = (Exception) objStream.readObject();
          } catch (Exception e) {
            error("Unexpected exception reading import errors from database: " + e.getMessage(), e);
            throw new DataRetrievalFailureException ("failed to deserialize exception", e);
          }
        }
        return new ImportError(recordNumber, exception);
 
    }
    
  }
  
  @Override
  public ImportStatus getImportStatus(final String importId) {
    final ImportStatus status = getJdbcTemplate().query (
        new PreparedStatementCreator() {
          @Override
          public PreparedStatement createPreparedStatement(Connection conn)
              throws SQLException {
            final PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_STATUS);
            stmt.setString(1, importId);
            
            return stmt;
          }
        },
        statusExtractor
      );

    if (status == null) {
      return null;
    }
    
    final List<ImportError> errors = getJdbcTemplate().query(
        new PreparedStatementCreator() {
          @Override
          public PreparedStatement createPreparedStatement(Connection conn)
              throws SQLException {
            final PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ERRORS);
            stmt.setString(1, importId);
            
            return stmt;
          }
        },
        errorMapper
      );
    
    status.setErrors(errors);

    return status;
  }

  @Override
  public ImportStatus initiateImport(final String importId, final int numRecords) {
    final long start = System.currentTimeMillis();
    getJdbcTemplate().update(SQL_CREATE_STATUS, importId, numRecords, start);
    final ImportStatus status = new ImportStatus(importId, numRecords);
    status.setStartTimeInMillis(start);
    
    return status;
  }

  protected void updateStatus(final Status status, final String importId) {
    updateStatus(status, importId, null);
  }

  protected void updateStatus(final Status status, final String importId, final String detail) {
    getJdbcTemplate().update(new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection conn)
          throws SQLException {
        final PreparedStatement stmt = detail == null ?
            conn.prepareStatement(SQL_UPDATE_STATUS) :
            conn.prepareStatement(SQL_UPDATE_STATUS_DETAIL);
        
        int i = 1;
        
        stmt.setString(i++, status.toString());
        if (detail != null) {
          stmt.setString(i++, detail);
        }
        stmt.setLong(i++, System.currentTimeMillis());
        stmt.setString(i, importId);

        return stmt;
      }
    });
  }
  
  @Override
  public void abort(final String importId) {
    updateStatus(Status.ABORTED, importId);
  }

  @Override
  public void abort(final String importId, final String detailMessage) {
    updateStatus(Status.ABORTED, importId, detailMessage);
  }

  @Override
  public void abnormalTermination(final String importId) {
    updateStatus(Status.ABNORMAL_TERMINATION, importId);
  }

  @Override
  public void abnormalTermination(final String importId, final String detailMessage) {
    updateStatus(Status.ABNORMAL_TERMINATION, importId, detailMessage);
  }

  @Override
  public void completeImport(final String importId) {
    final ImportStatus status = getImportStatus(importId);
    
    if (!Status.PROCESSING.equals(status.getStatus())) {
      debug("completeImport called on an import that has already stopped - doing nothing");
      return;
    }
    
    if (status.getProcessedRecordCount() != status.getRecordTotal()) {
      abnormalTermination(importId, "import completed without processing all records");
    } else {
      updateStatus(Status.COMPLETE, importId);
    }
  }
  
  protected void updatePersonStatus (final String importId, final String personId, final String status) {
    getJdbcTemplate().update(SQL_PERSON_UPDATE, personId, importId, status, importId, status);
  }
  
  @Override
  public void recordProcessed(final String importId, final String personId) {
    getJdbcTemplate().update(SQL_INCREMENT, importId);
    updatePersonStatus(importId, personId, "ADDED");
  }

  @Override
  public void recordInactivated(final String importId, final String personId) {
    getJdbcTemplate().update(SQL_INCREMENT, importId);
    updatePersonStatus(importId, personId, "INACTIVE");
  }

  @Override
  public void recordError(final String importId, final String personId, final ImportError error) {
    final JdbcTemplate tmpl = getJdbcTemplate();
    tmpl.execute(new ConnectionCallback<Object>() {

      @Override
      public Object doInConnection(final Connection conn) throws SQLException,
          DataAccessException {
        final PreparedStatement errStmt = conn.prepareStatement(SQL_CREATE_ERROR);
        
        errStmt.setString(1, importId);
        errStmt.setInt(2, error.getRecordNumber());
        errStmt.setObject(3, error.getException());

        errStmt.executeUpdate();

        return null;
      }
      
    });
    updatePersonStatus(importId, personId, "ERROR");
    tmpl.update(SQL_INCREMENT, importId);
  }

  @Override
  public List<String> getActiveIdsMissingFromImport(final String importId) {
    return getJdbcTemplate().query(new PreparedStatementCreator() {
  
        @Override
        public PreparedStatement createPreparedStatement(final Connection conn)
            throws SQLException {
          final PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_MISSING_IDS);
          
          stmt.setString(1, importId);
          
          return stmt;
        }
        
      },
      new RowMapper<String>() {

        @Override
        public String mapRow(final ResultSet rs, final int rowNum) throws SQLException {
          return rs.getString("personId");
        }
        
      });
  }

}