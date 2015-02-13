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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.api.MigrationType;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.resolver.MigrationExecutor;
import org.flywaydb.core.api.resolver.MigrationResolver;
import org.flywaydb.core.api.resolver.ResolvedMigration;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

public class CoeusMigrationResolver implements MigrationResolver {

    protected DataSource riceDataSource;
    protected String javaMigrationPath;

    @Override
    public Set<ResolvedMigration> resolveMigrations() {
        Set<ResolvedMigration> migrations = new HashSet<>();

        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
        // add include filters which matches all the classes... without it it returns nothing
        provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));
        final Set<BeanDefinition> components = provider.findCandidateComponents(pathToPackage(javaMigrationPath));
        for (BeanDefinition component : components) {
            try {
                Class<?> clazz = Class.forName(component.getBeanClassName());
                if (isMigratorCompatible(clazz)) {
                    final Object migration = clazz.newInstance();
                    if (containsSetRiceDataSource(clazz)) {
                        execSetRiceDataSource(migration, riceDataSource);
                    }
                    migrations.add(new GenericMigrationAdapter(migration));
                }
            } catch (InstantiationException|IllegalAccessException|ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return migrations;
    }

    private String pathToPackage(String path) {
        String pkg =  path.replaceAll("/", ".");

        while (pkg.endsWith(".")) {
            pkg = StringUtils.removeEnd(pkg, ".");
        }
        return pkg;
    }

    private boolean isMigratorCompatible(Class<?> c) {
        try {
            c.getMethod("executeInTransaction");
            c.getMethod("execute", Connection.class);
        } catch (NoSuchMethodException e) {
            return false;
        }
        return true;
    }

    private boolean containsSetRiceDataSource(Class<?> c) {
        try {
            c.getMethod("setRiceDataSource", DataSource.class);
        } catch (NoSuchMethodException e) {
            return false;
        }
        return true;
    }

    private void execSetRiceDataSource(Object o, DataSource riceDataSource) {
        try {
            Method setRiceDataSource = o.getClass().getMethod("setRiceDataSource", DataSource.class);
            setRiceDataSource.invoke(o, riceDataSource);
        } catch (NoSuchMethodException|InvocationTargetException|IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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

    public String getJavaMigrationPath() {
        return javaMigrationPath;
    }

    public void setJavaMigrationPath(String javaMigrationPath) {
        this.javaMigrationPath = javaMigrationPath;
    }

    private static class GenericMigrationAdapter implements ResolvedMigration, MigrationExecutor {

        private Object migration;

        private GenericMigrationAdapter(Object o) {
            migration = o;
        }

        @Override
        public void execute(Connection connection) throws SQLException {
            try {
                Method execute = migration.getClass().getMethod("execute", Connection.class);
                execute.invoke(migration, connection);
            } catch (NoSuchMethodException|InvocationTargetException|IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean executeInTransaction() {
            try {
                Method executeInTransaction = migration.getClass().getMethod("executeInTransaction");
                return (boolean) executeInTransaction.invoke(migration);
            } catch (NoSuchMethodException|InvocationTargetException|IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Integer getChecksum() {
            return null;
        }

        @Override
        public String getPhysicalLocation() {
            return migration.getClass().getCanonicalName();
        }

        @Override
        public String getScript() {
            return migration.getClass().getSimpleName();
        }

        @Override
        public MigrationType getType() {
            return MigrationType.CUSTOM;
        }

        @Override
        public MigrationVersion getVersion() {
            return MigrationVersion.fromVersion(migration.getClass().getSimpleName().split("__")[0].substring(1));
        }

        @Override
        public String getDescription() {
            return migration.getClass().getSimpleName().split("__")[1];
        }

        @Override
        public MigrationExecutor getExecutor() {
            return this;
        }
    }
}
