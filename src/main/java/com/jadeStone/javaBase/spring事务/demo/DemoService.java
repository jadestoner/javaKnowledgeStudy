package com.jadeStone.javaBase.spring事务.demo;

import java.sql.SQLException;


public interface DemoService{

	void insert1() throws SQLException;
	
	void batch() throws Exception;
}
