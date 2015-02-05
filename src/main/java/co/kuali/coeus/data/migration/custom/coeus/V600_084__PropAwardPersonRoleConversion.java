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
package co.kuali.coeus.data.migration.custom.coeus;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.kuali.coeus.dc.common.rice.parameter.ParameterDaoImpl;
import org.kuali.coeus.dc.pprole.ProposalPersonRoleDaoImpl;

import co.kuali.coeus.data.migration.custom.CoeusConnectionDao;
import co.kuali.coeus.data.migration.custom.CoeusResolvedMigration;

import org.flywaydb.core.api.resolver.MigrationExecutor;

public class V600_084__PropAwardPersonRoleConversion extends CoeusResolvedMigration implements MigrationExecutor {
	
	@Override
	public void execute(Connection connection) throws SQLException {
		ProposalPersonRoleDaoImpl proposalPersonRoleDao = new ProposalPersonRoleDaoImpl();
		proposalPersonRoleDao.setParameterDao(new ParameterDaoImpl());
		try (Connection riceConnection = riceDataSource.getConnection()) {
			riceConnection.setAutoCommit(false);
			proposalPersonRoleDao.setConnectionDaoService(new CoeusConnectionDao(connection, riceConnection));
			riceConnection.commit();
		}
	}

	@Override
	public Integer getChecksum() {
		return null;
	}

	@Override
	public MigrationExecutor getExecutor() {
		return this;
	}

	@Override
	public boolean executeInTransaction() {
		return true;
	}

}
