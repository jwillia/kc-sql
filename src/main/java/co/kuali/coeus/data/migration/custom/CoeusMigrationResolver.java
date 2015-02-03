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

import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.flywaydb.core.api.resolver.MigrationResolver;
import org.flywaydb.core.api.resolver.ResolvedMigration;
import org.reflections.Reflections;

public class CoeusMigrationResolver implements MigrationResolver {

	protected DataSource riceDataSource;

	@Override
	public Set<ResolvedMigration> resolveMigrations() {
		Set<ResolvedMigration> migrations = new HashSet<>();
		Reflections reflections = new Reflections("co.kuali.coeus.data.migration.custom.coeus");
		Set<Class<? extends CoeusResolvedMigration>> migrationClasses = reflections.getSubTypesOf(CoeusResolvedMigration.class);
		for (Class<? extends CoeusResolvedMigration> clazz : migrationClasses) {
			try {
				CoeusResolvedMigration migration = clazz.newInstance();
				migration.setRiceDataSource(riceDataSource);
				migrations.add(migration);
			} catch (InstantiationException|IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return migrations;
	}
	
	public CoeusMigrationResolver() { }
	public CoeusMigrationResolver(DataSource riceDataSource) {
		this.riceDataSource = riceDataSource;
	}
	
	public DataSource getRiceDataSource() {
		return riceDataSource;
	}

	public void setRiceDataSource(DataSource riceDataSource) {
		this.riceDataSource = riceDataSource;
	}

}
