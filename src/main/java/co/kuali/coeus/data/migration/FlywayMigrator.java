/*
 * Kuali Coeus, a comprehensive research administration system for higher education.
 * 
 * Copyright 2005-2015 Kuali, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package co.kuali.coeus.data.migration;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.resolver.MigrationResolver;
import org.apache.log4j.Logger;

import co.kuali.coeus.data.migration.custom.CoeusMigrationResolver;

import java.sql.Connection;
import java.sql.ResultSet;
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
	
	protected CoeusMigrationResolver coeusMigrationResolver;

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
		coeusMigrationResolver = new CoeusMigrationResolver();
		
		performMigration(dataSource, kcLocations, new CoeusMigrationResolver(riceDataSource));
		
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
	
	protected void performMigration(DataSource dataSource, List<String> locations, MigrationResolver ... migrationResolvers) {		
		String rootPath = null;
		try (Connection connection = dataSource.getConnection()) {
			MigrationUtils.DatabaseType type = MigrationUtils.getDatabaseTypeFromConnection(connection);
			if (type == MigrationUtils.DatabaseType.Mysql) {
				rootPath = mysqlMigrationPath;
			} else if (type == MigrationUtils.DatabaseType.Oracle) {
				rootPath = oracleMigrationPath;
			} else {
				throw new IllegalArgumentException("Unsupported database detected " + connection.getMetaData().getDatabaseProductName());
			}
		} catch (Exception e) {
			LOG.warn("Unable to detect current flyway version", e);
		}
		
		final Flyway flyway = new Flyway();
		flyway.setDataSource(dataSource);
		flyway.setLocations(filterForExistence(prefixLocationsWithDb(rootPath, locations)));
		flyway.setResolvers(migrationResolvers);
		//there is no way to turn off placeholder replacement and the default(${}) is used in
		//sql scripts. So use a unlikely string to make sure no placeholders are detected
		flyway.setPlaceholderPrefix("PLACEHOLDERS_DISABLED$$$$$");
		flyway.setBaselineOnMigrate(true);
		flyway.setBaselineVersion(MigrationVersion.fromVersion(getBaselineVersion(dataSource)));

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
	
	protected String[] filterForExistence(String...locations) {
		List<String> result = new ArrayList<>();
		for (String location : locations) {
			if (this.getClass().getClassLoader().getResource(location) != null) {
				result.add(location);
			}
		}
		return result.toArray(new String[0]);
	}
	
	protected String getBaselineVersion(DataSource dataSource) {
		String maxVersion = "0";
		try (Connection conn = dataSource.getConnection()) {
			ResultSet rs = conn.createStatement().executeQuery("select max(version) from schema_version");
			if (rs.next()) {
				maxVersion = rs.getString(1);
			}
			//these version numbers were used by the old CX version. If detected clear schema version and use 600 as a baseline
			if (Double.valueOf(maxVersion).compareTo(201400000000.00) > 0) {
				conn.createStatement().executeUpdate("delete from schema_version");
				return "600";
			}
		} catch (Exception e) {
			//if we detect an exception here its likely because schema_version doesn't exist which is fine
			LOG.warn("Unable to detect flyway schema version", e);
		}
		return "0"; 

	}
	
	protected List<String> buildLocations(String rootPath) {
		List<String> locations = new ArrayList<String>();
		locations.add(rootPath + "/" + bootstrapPath);
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
	
	public CoeusMigrationResolver getCoeusMigrationResolver() {
		return coeusMigrationResolver;
	}

	public void setCoeusMigrationResolver(
			CoeusMigrationResolver coeusMigrationResolver) {
		this.coeusMigrationResolver = coeusMigrationResolver;
	}

}
