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

import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.resolver.MigrationResolver;

import co.kuali.coeus.data.migration.custom.CoeusMigrationResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

/**
 * Needs to be injected and migrated *before* OJB/JPA or any other database
 * framework. This only depends on a working DataSource, usually a JDBC
 * connection pool.
 */
public class FlywayMigrator {
    private static final Logger LOG = LoggerFactory.getLogger(FlywayMigrator.class);

    protected String initVersion;
    protected DataSource dataSource;
    protected DataSource riceDataSource;

    protected CoeusMigrationResolver coeusMigrationResolver;

    protected String sqlMigrationPath;
    protected String javaMigrationPath;

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
    protected String grmDataPath = "grm";

    protected Boolean enabled;
    protected Boolean applyTesting;
    protected Boolean applyStaging;
    protected Boolean applyDemo;

    protected Boolean grm;
    protected Boolean manageRice;

    //if embeddedMode is false (either set or detected) implies manageRice
    protected Boolean embeddedMode;
    
    private List<String> mysqlPreMigrationSql = new ArrayList<String>(){{
    	add("update schema_version set checksum = 1918600927 where version = '601.017'");
    	add("update schema_version set checksum = 1404031275 where version = '601.023'");
    	add("update schema_version set checksum = 1179166139 where version = '601.024'");
    	add("update schema_version set checksum = -1603565051 where version = '1506.018'");
    	add("update schema_version set checksum = 2015457550 where version = '1507.005'");
    }};
    
    private List<String> oraclePreMigrationSql = new ArrayList<String>(){{
    	//add("update \"schema_version\" set \"checksum\" = 1918600927 where \"version\" = '601.017'")
    }};
    

    public void migrate() throws SQLException {
        if (!enabled) {
            LOG.info("Flyway Migration is not enabled. Skipping.");
            return;
        }
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

        if (grm) {
            kcLocations.add(grmDataPath);
        }

        coeusMigrationResolver = new CoeusMigrationResolver(riceDataSource);
        coeusMigrationResolver.setJavaMigrationPath(getJavaMigrationPath());
        performMigration(dataSource, kcLocations, coeusMigrationResolver);

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

    	runPreMigrationSql(dataSource);
        final Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations(filterForExistence(prefixLocationsWithDb(getSqlMigrationPath(), locations)));
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
            LOG.warn("Unable to detect flyway schema version " + e.getMessage());
        }
        return "0";
    }
    
    protected void runPreMigrationSql(DataSource dataSource) {
    	try (Connection conn = dataSource.getConnection()) {
    		String databaseProductName = conn.getMetaData().getDatabaseProductName();
    		if (StringUtils.containsIgnoreCase(databaseProductName, "oracle")) {
    			runSpecificPreMigrationSql(conn, oraclePreMigrationSql);
    		} else {
    			runSpecificPreMigrationSql(conn, mysqlPreMigrationSql);
    		}
    	} catch (SQLException e) {
    		LOG.warn("Error getting connection to run pre migration sql " + e.getMessage());
    	}
    }

	void runSpecificPreMigrationSql(final Connection conn, final List<String> preMigrationSql) {
		for (String preSql : preMigrationSql) {
			try (Statement stmt = conn.createStatement()) {
				stmt.executeUpdate(preSql);
			} catch (SQLException e) {
				LOG.warn("Error running pre migration sql " + e.getMessage());
			}
		}
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

    protected String getDefinedOption(String option, String defaultValue) {
        if (System.getProperty(option) != null) {
            return System.getProperty(option);
        } else if (System.getenv().containsKey(option)) {
            return System.getProperty(option);
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

    public String getGrmDataPath() {
        return grmDataPath;
    }

    /**
     * Sets the path for the grm data scripts
     * @param grmDataPath (default : grm)
     */
    public void setGrmDataPath(String grmDataPath) {
        this.grmDataPath = grmDataPath;
    }


    public Boolean getApplyTesting() {
        if (applyTesting == null) {
            applyTesting = getDefinedOption("kc.flyway.testing", Boolean.FALSE);
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
            applyStaging = getDefinedOption("kc.flyway.staging", Boolean.FALSE);
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
            applyDemo = getDefinedOption("kc.flyway.demo", Boolean.FALSE);
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
            manageRice = getDefinedOption("kc.flyway.manageRice", Boolean.TRUE);
        }
        return manageRice;
    }

    public void setManageRice(Boolean manageRice) {
        this.manageRice = manageRice;
    }

    public Boolean getEmbeddedMode() {
        if (embeddedMode == null) {
            embeddedMode = getDefinedOption("kc.flyway.embedded", Boolean.FALSE);
        }
        return embeddedMode;
    }

    public void setEmbeddedMode(Boolean embeddedMode) {
        this.embeddedMode = embeddedMode;
    }

    public Boolean getGrm() {
        if (embeddedMode == null) {
            embeddedMode = getDefinedOption("kc.flyway.grm", Boolean.FALSE);
        }
        return embeddedMode;
    }

    public void setGrm(Boolean grm) {
        this.grm = grm;
    }

    public CoeusMigrationResolver getCoeusMigrationResolver() {
        return coeusMigrationResolver;
    }

    public void setCoeusMigrationResolver(
            CoeusMigrationResolver coeusMigrationResolver) {
        this.coeusMigrationResolver = coeusMigrationResolver;
    }

    public Boolean getEnabled() {
        if (enabled == null) {
            enabled = getDefinedOption("kc.flyway.enabled", Boolean.TRUE);
        }
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getSqlMigrationPath() {
        if (sqlMigrationPath == null) {
            sqlMigrationPath = getDefinedOption("kc.flyway.sql.migration.path", "");
        }

        return sqlMigrationPath;
    }

    public void setSqlMigrationPath(String migrationPath) {
        this.sqlMigrationPath = migrationPath;
    }

    public String getJavaMigrationPath() {
        if (javaMigrationPath == null) {
            javaMigrationPath = getDefinedOption("kc.flyway.java.migration.path", "");
        }

        return javaMigrationPath;
    }

    public void setJavaMigrationPath(String javaMigrationPath) {
        this.javaMigrationPath = javaMigrationPath;
    }
}
