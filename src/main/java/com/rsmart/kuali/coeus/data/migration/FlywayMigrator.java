package com.rsmart.kuali.coeus.data.migration;

import com.googlecode.flyway.core.Flyway;
import com.googlecode.flyway.core.api.MigrationInfo;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * Needs to be injected and migrated *before* OJB/JPA or any other database framework.
 * This only depends on a working DataSource, usually a JDBC connection pool.
 */
public class FlywayMigrator {
  private static final Logger LOG = Logger.getLogger(FlywayMigrator.class);
  public static final String MYSQL_MIGRATIONS_PATH = "com/rsmart/kuali/coeus/data/migration/sql/mysql";
  public static final String ORACLE_MIGRATIONS_PATH = "com/rsmart/kuali/coeus/data/migration/sql/oracle";

  protected String initVersion = null;
  protected DataSource dataSource = null;

  public void migrate() {
    if (dataSource == null) {
      throw new IllegalStateException("dataSource == null");
    }
    String migrationsLocation = null;
    Connection connection = null;
    try {
      connection = dataSource.getConnection();
      final String dbProduct = connection.getMetaData().getDatabaseProductName();
      LOG.info("flyway database product: " + dbProduct);
      if ("MySQL".equalsIgnoreCase(dbProduct)) {
        migrationsLocation = MYSQL_MIGRATIONS_PATH;
      }
      if (dbProduct.toUpperCase().contains("ORACLE")) {
        migrationsLocation = ORACLE_MIGRATIONS_PATH;
      }
    } catch (SQLException e) {
      throw new Error(e);
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          throw new Error(e);
        }
      }
    }
    final Flyway flyway = new Flyway();
    flyway.setDataSource(dataSource);
    flyway.setLocations(migrationsLocation);
    // allow migrations to be run out of order (helps support git workflow)
    // flyway.setOutOfOrder(true);
    /*
     * Avoid FlywayException: Found non-empty schema `coeus` without metadata table! Use
     * init() or set initOnMigrate to true to initialize the metadata table.
     */
    flyway.setInitOnMigrate(true);
    if (initVersion != null) {
      LOG.info("flyway.setInitVersion(" + initVersion + ")");
      flyway.setInitVersion(initVersion);
    }
    for (final MigrationInfo i : flyway.info().all()) {
      LOG.info("flyway migration: " + i.getVersion() + " : '" + i.getDescription()
          + "' from file: " + i.getScript());
    }
    final int numApplied = flyway.migrate();
    LOG.info("flyway migrations applied: " + numApplied);
  }

  /**
   * Sets the version to tag an existing schema with when executing init.
   * 
   * @param initVersion
   *          The version to tag an existing schema with when executing init. (default: 1)
   */
  public void setInitVersion(final String initVersion) {
    this.initVersion = initVersion;
  }

  public void setDataSource(final DataSource dataSource) {
    this.dataSource = dataSource;
  }
}
