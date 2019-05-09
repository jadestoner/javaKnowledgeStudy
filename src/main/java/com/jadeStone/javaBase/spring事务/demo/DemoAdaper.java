/**
 * 
 */
package com.jadeStone.javaBase.spring事务.demo;

import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * 适配器
 *
 */
public class DemoAdaper implements DemoService{

	protected DataSource dataSource;

	@Override
	public void insert1() throws SQLException {}

	@Override
	public void batch() throws Exception {}
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
