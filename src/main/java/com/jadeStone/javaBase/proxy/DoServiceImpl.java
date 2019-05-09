package com.jadeStone.javaBase.proxy;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class DoServiceImpl implements DoService{

	private DataSource dataSource;
	
	@Override
	public void doo() {
		System.out.println("i do");
	}
	
	@Override
	public void doo2() {
		System.out.println("i doo2");
	}

	@Override
	public void insert1() throws SQLException {
		Connection con = dataSource.getConnection();
		Statement  insert1 = con.createStatement();
		insert1.execute("insert into test (a,b,c)values(1,1,1) ");
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
