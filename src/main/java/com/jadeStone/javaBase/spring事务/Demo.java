/**
 * 
 */
package com.jadeStone.javaBase.spring事务;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.interceptor.TransactionProxyFactoryBean;

import com.alibaba.druid.pool.DruidDataSource;
import com.jadeStone.javaBase.proxy.DoService;
import com.jadeStone.javaBase.proxy.DoServiceImpl;

/**
 * 演示 TransactionProxyFactoryBean 的用法
 * @author 
 *
 */
public class Demo {

	public static void main(String[] args) {
		DoService doService = new DoServiceImpl();
		
		TransactionProxyFactoryBean factory = new TransactionProxyFactoryBean();
		factory.setTarget(doService);
		factory.setProxyInterfaces(new Class[]{DoService.class});
		
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl("jdbc:mysql://61.160.245.8:33030/test2?allowMultiQueries=true&useAffectedRows=true");
		dataSource.setUsername("runsacrm");
		dataSource.setPassword("iqAD$hNL1PA58J^8");
		
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(dataSource);
		
		factory.setTransactionManager(transactionManager);
		
		
	}
}
