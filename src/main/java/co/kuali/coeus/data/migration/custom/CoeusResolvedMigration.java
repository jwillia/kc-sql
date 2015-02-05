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
package co.kuali.coeus.data.migration.custom;

import javax.sql.DataSource;

import org.flywaydb.core.api.MigrationType;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.resolver.ResolvedMigration;

public abstract class CoeusResolvedMigration implements ResolvedMigration {

	protected DataSource riceDataSource;
	
	public DataSource getRiceDataSource() {
		return riceDataSource;
	}

	public void setRiceDataSource(DataSource riceDataSource) {
		this.riceDataSource = riceDataSource;
	}

	@Override
	public Integer getChecksum() {
		return null;
	}

	@Override
	public String getPhysicalLocation() {
		return this.getClass().getCanonicalName();
	}

	@Override
	public String getScript() {
		return this.getClass().getSimpleName();
	}

	@Override
	public MigrationType getType() {
		return MigrationType.CUSTOM;
	}

	@Override
	public MigrationVersion getVersion() {
		return MigrationVersion.fromVersion(this.getClass().getSimpleName().split("__")[0].substring(1));
	}
	
	public String getDescription() {
		return this.getClass().getSimpleName().split("__")[1];
	}

}
