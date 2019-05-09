package com.jadeStone.javaBase.spring事务.demo;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DemoServiceImpl extends DemoAdaper{


	@Override
	public void insert1() throws SQLException {
		Connection con = dataSource.getConnection();
		Statement  insert1 = con.createStatement();
		insert1.execute("insert into test (a,b,c)values(1,1,1) ");
	}


}
