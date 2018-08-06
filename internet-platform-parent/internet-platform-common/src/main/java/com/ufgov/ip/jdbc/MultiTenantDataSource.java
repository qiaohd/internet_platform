package com.ufgov.ip.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.ufgov.ip.common.ContextThreadLocal;


public class MultiTenantDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return ContextThreadLocal.getCurrentTenant();
	}

	@Override
	public Connection getConnection() throws SQLException {
		return super.getConnection();
	}

	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		return super.getConnection(username, password);
	}
}
