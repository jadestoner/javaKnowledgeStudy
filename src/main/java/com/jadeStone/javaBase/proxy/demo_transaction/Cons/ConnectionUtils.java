/**
 * 
 */
package com.jadeStone.javaBase.proxy.demo_transaction.Cons;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

/**
 * @author 
 *
 */
public class ConnectionUtils {
	
	private ConnectionUtils(){}
	
	private static ThreadLocal<Connection> th = new ThreadLocal<>();
	/**
	 * 从threadlocal取，如果不存在，则从连接池取连接再放入threadlocal。用来处理嵌套方法配置覆盖的场景
	 * 对外提供的 connection 属性统一，见 returnOrigin 方法
	 * @return
	 */
	public static Connection getConnection(){
		Connection connection = th.get();
		if(connection != null){
			return connection;
		} 
		connection = returnOrigin(ConnectionPool.getConnection());
		th.set(connection);
		return connection;
	}
	
	public static void releaseConnection(){
		Connection connection = th.get();
		if(connection != null){
			// 线程返回到线程池，需要将其各属性初始化
			returnOrigin(connection);
			ConnectionPool.releaseConnection(connection);
			// 注意：这里th是static类型的，不会内存泄漏，简单remove即可
			th.remove();
		} 
	}
	
	/**
	 * 统一属性
	 * @param connection
	 * @return
	 */
	private static Connection returnOrigin(Connection connection){
		try {
			connection.setAutoCommit(true);
			// 修改默认事务隔离级别，一般读已提交就足够了，没必要可重复读。
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return connection;
	}
}
