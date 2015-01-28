package com.rsmart.kuali.coeus.data.migration;

import com.googlecode.flyway.core.Flyway;
import com.googlecode.flyway.core.api.MigrationInfo;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

/**
 * Needs to be injected and migrated *before* OJB/JPA or any other database
 * framework. This only depends on a working DataSource, usually a JDBC
 * connection pool.
 */
public class FlywayMigrator {
	private static final Logger LOG = Logger.getLogger(FlywayMigrator.class);
	public static final String MYSQL_MIGRATIONS_PATH = "co/kuali/coeus/data/migration/sql/mysql";
	public static final String ORACLE_MIGRATIONS_PATH = "com/rsmart/kuali/coeus/data/migration/sql/oracle";

	protected String initVersion;
	protected DataSource dataSource;
	protected DataSource riceDataSource;

	protected String mysqlMigrationPath = MYSQL_MIGRATIONS_PATH;
	protected String oracleMigrationPath = ORACLE_MIGRATIONS_PATH;
	//KC database scripts
	protected String kcPath = "kc";
	//Rice database scripts, applied whether embedded or bundled
	protected String ricePath = "rice";
	//Rice database scripts, applied if managing rice, should always be applied for bundled (manageRice)
	protected String riceServer = "rice_server";
	//Rice database scripts, applied if not managing rice (manageRice = false) 
	protected String riceDataOnly = "rice_data_only";
	//KRC scripts, applied to the KC database is embedded
	protected String embeddedClientScripts = "kc/embedded_client_scripts";
	protected String bootstrapPath = "bootstrap";
	protected String testingPath = "testing";
	protected String stagingDataPath = "staging";
	protected String demoDataPath = "demo";

	protected Boolean applyBootstrap;
	protected Boolean applyTesting;
	protected Boolean applyStaging;
	protected Boolean applyDemo;
	protected Boolean manageRice;

	//if embeddedMode is false (either set or detected) implies manageRice
	protected Boolean embeddedMode;
	
	public void migrate() throws SQLException {
		if (dataSource == null) {
			throw new IllegalStateException("dataSource == null");
		}
		if (riceDataSource == null) {
			throw new IllegalStateException("riceDataSource == null");
		}

		if (!embeddedMode) {
			manageRice = true;
		}

		List<String> kcLocations = new ArrayList<String>();
		kcLocations.addAll(buildLocations(kcPath));
		if (!embeddedMode) {
			kcLocations.addAll(buildLocations(ricePath));
			kcLocations.addAll(buildLocations(riceServer));
		} else {
			kcLocations.add(embeddedClientScripts);
		}
		performMigration(dataSource, kcLocations);
		
		if (embeddedMode) {
			List<String> riceLocations = new ArrayList<String>();
			riceLocations.addAll(buildLocations(ricePath));
			if (manageRice) {
				riceLocations.addAll(buildLocations(riceServer));
			} else {
				riceLocations.addAll(buildLocations(riceDataOnly));
			}
			performMigration(riceDataSource, riceLocations);
		}
	}
	
	protected void performMigration(DataSource dataSource, List<String> locations) {		
		Connection connection = null;
		String rootPath = null;
		try {
			connection = dataSource.getConnection();
			final String dbProduct = connection.getMetaData()
					.getDatabaseProductName();
			LOG.info("flyway database product: " + dbProduct);
			if ("MySQL".equalsIgnoreCase(dbProduct)) {
				rootPath = mysqlMigrationPath;
			}
			if (dbProduct.toUpperCase().contains("ORACLE")) {
				rootPath = oracleMigrationPath;
			}
			if (rootPath == null) {
				throw new IllegalArgumentException("Unsupported database detected :" + dbProduct);
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
		flyway.setLocations(prefixLocationsWithDb(rootPath, locations));
		//there is no way to turn off placeholder replacement and the default(${}) is used in
		//sql scripts. So use a unlikely string to make sure no placeholders are detected
		flyway.setPlaceholderPrefix("PLACEHOLDERS_DISABLED$$$$$");

		//make sure if the database hasn't been initialized, it is here.
		flyway.setInitOnMigrate(true);
		if (initVersion != null) {
			LOG.info("flyway.setInitVersion(" + initVersion + ")");
			flyway.setInitVersion(initVersion);
		}
		for (final MigrationInfo i : flyway.info().all()) {
			LOG.info("flyway migration: " + i.getVersion() + " : '"
					+ i.getDescription() + "' from file: " + i.getScript());
		}
		final int numApplied = flyway.migrate();
		LOG.info("flyway migrations applied: " + numApplied);		
	}
	
	protected String[] prefixLocationsWithDb(String dbPath, List<String> locations) {
		String[] result = new String[locations.size()];
		for (int i = 0; i < locations.size(); i++) {
			result[i] = dbPath + "/" + locations.get(i);
		}
		return result;
	}
	
	protected List<String> buildLocations(String rootPath) {
		List<String> locations = new ArrayList<String>();
		if (getApplyBootstrap()) {
			locations.add(rootPath + "/" + bootstrapPath);
		}
		if (getApplyTesting()) {
			locations.add(rootPath + "/" + testingPath);
		}
		if (getApplyStaging()) {
			locations.add(rootPath + "/" + stagingDataPath);
		}
		if (getApplyDemo()) {
			locations.add(rootPath + "/" + demoDataPath);
		}
		return locations;
	}
	
	protected Boolean getDefinedOption(String option, Boolean defaultValue) {
		if (System.getProperty(option) != null) {
			return Boolean.valueOf(System.getProperty(option));
		} else if (System.getenv().containsKey(option)) {
			return Boolean.valueOf(System.getProperty(option));
		} else {
			return defaultValue;
		}
	}

	/**
	 * Sets the version to tag an existing schema with when executing init.
	 * 
	 * @param initVersion
	 *            The version to tag an existing schema with when executing
	 *            init. (default: 1)
	 */
	public void setInitVersion(final String initVersion) {
		this.initVersion = initVersion;
	}

	/**
	 * Sets the KC datasource
	 * @param dataSource
	 */
	public void setDataSource(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getRiceDataSource() {
		return riceDataSource;
	}

	/**
	 * Sets the Rice server datasource
	 * @param riceDataSource
	 */
	public void setRiceDataSource(DataSource riceDataSource) {
		this.riceDataSource = riceDataSource;
	}

	public String getMysqlMigrationPath() {
		return mysqlMigrationPath;
	}

	/**
	 * Sets the base path for mysql migrations
	 * @param mysqlMigrationPath (default : com/rsmart/kuali/coeus/data/migration/sql/mysql)
	 */
	public void setMysqlMigrationPath(String mysqlMigrationPath) {
		this.mysqlMigrationPath = mysqlMigrationPath;
	}

	public String getOracleMigrationPath() {
		return oracleMigrationPath;
	}

	/**
	 * Sets the base path for Oracle migrations
	 * @param oracleMigrationPath (default : com/rsmart/kuali/coeus/data/migration/sql/oracle)
	 */
	public void setOracleMigrationPath(String oracleMigrationPath) {
		this.oracleMigrationPath = oracleMigrationPath;
	}

	public String getBootstrapPath() {
		return bootstrapPath;
	}

	/**
	 * Sets the path for the bootstrap data scripts
	 * @param bootstrapPath (default : bootstrap)
	 */
	public void setBootstrapPath(String bootstrapPath) {
		this.bootstrapPath = bootstrapPath;
	}

	public String getTestingPath() {
		return testingPath;
	}

	/**
	 * Sets the path for the testing data scripts
	 * @param testingPath (default : testing)
	 */
	public void setTestingPath(String testingPath) {
		this.testingPath = testingPath;
	}

	public String getStagingDataPath() {
		return stagingDataPath;
	}

	/**
	 * Sets the path for staging data scripts
	 * @param stagingDataPath (default : staging)
	 */
	public void setStagingDataPath(String stagingDataPath) {
		this.stagingDataPath = stagingDataPath;
	}

	public String getDemoDataPath() {
		return demoDataPath;
	}

	/**
	 * Sets the path for the demo data scripts
	 * @param demoDataPath (default : demo)
	 */
	public void setDemoDataPath(String demoDataPath) {
		this.demoDataPath = demoDataPath;
	}

	public Boolean getApplyBootstrap() {
		if (applyBootstrap == null) {
			applyBootstrap = getDefinedOption("kuali.coeus.flyway.boostrap", Boolean.TRUE);
		}
		return applyBootstrap;
	}

	/**
	 * Sets whether to apply bootstrap data scripts or not
	 * @param applyBootstrap (default : Configuration parameter flyway.migrations.apply_bootstrap or true if not set)
	 */
	public void setApplyBootstrap(boolean applyBootstrap) {
		this.applyBootstrap = applyBootstrap;
	}

	public Boolean getApplyTesting() {
		if (applyTesting == null) {
			applyTesting = getDefinedOption("kuali.coeus.flyway.testing", Boolean.FALSE);
		}
		return applyTesting;
	}

	/**
	 * Sets whether to apply testing data scripts or not
	 * @param applyTesting (default : Configuration parameter flyway.migrations.apply_testing or false if not set)
	 */
	public void setApplyTesting(boolean applyTesting) {
		this.applyTesting = applyTesting;
	}

	public Boolean getApplyStaging() {
		if (applyStaging == null) {
			applyStaging = getDefinedOption("kuali.coeus.flyway.staging", Boolean.FALSE);
		}
		return applyStaging;
	}

	/**
	 * Sets whether to apply staging data scripts or not
	 * @param applyStaging (default : Configuration parameter flyway.migrations.apply_staging or false if not set)
	 */
	public void setApplyStaging(boolean applyStaging) {
		this.applyStaging = applyStaging;
	}

	public Boolean getApplyDemo() {
		if (applyDemo == null) {
			applyDemo = getDefinedOption("kuali.coeus.flyway.demo", Boolean.FALSE);
		}
		return applyDemo;
	}

	/**
	 * Sets whether to apply demo data scripts or not
	 * @param applyDemo (default : Configuration parameter flyway.migrations.apply_demo or false if not set)
	 */
	public void setApplyDemo(boolean applyDemo) {
		this.applyDemo = applyDemo;
	}

	public String getInitVersion() {
		return initVersion;
	}

	public DataSource getDataSource() {
		return dataSource;
	}
	
	public Boolean getManageRice() {
		if (manageRice == null) {
			manageRice = getDefinedOption("kuali.coeus.flyway.manageRice", Boolean.TRUE);
		}
		return manageRice;
	}

	public void setManageRice(Boolean manageRice) {
		this.manageRice = manageRice;
	}

	public Boolean getEmbeddedMode() {
		if (embeddedMode == null) {
			embeddedMode = getDefinedOption("kuali.coeus.flyway.embedded", Boolean.FALSE);
		}
		return embeddedMode;
	}

	public void setEmbeddedMode(Boolean embeddedMode) {
		this.embeddedMode = embeddedMode;
	}

}
