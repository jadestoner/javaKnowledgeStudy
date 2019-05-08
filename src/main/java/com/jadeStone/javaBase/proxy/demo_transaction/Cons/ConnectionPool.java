/**
 * 
 */
package com.jadeStone.javaBase.proxy.demo_transaction.Cons;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * 简易线程池，方法对外不可见，获取/释放由 ConnectionUtils 类的相关方法代理
 */
public class ConnectionPool {
 
	private static Integer conSize = 10;
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/sqltestdb";
	private static String user = "root";
	private static String password = "123456";
	
	private static LinkedList<Connection> cons = new LinkedList<>();
	
	{
		// try块尽可能地小，虽然代码丑了点
		// 初始化连接
		 try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < conSize; i++){
			try {
				cons.addLast(DriverManager.getConnection(url, user, password));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 暂不响应中断，超时，后期补上
	static Connection getConnection(){
		// 数据库连接获取可能比较频繁，
		// 直接用synchronized可能上下文切换；不用的话，又会线程不安全。
		synchronized (cons) {
			// 防止线程无缘预估的醒来，用while判断。
			// wait使用的标准范式
			while(cons.size() == 0 ){
				try {
					cons.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return cons.removeLast();
		}
	}
	
	static void releaseConnection(Connection connection){
		synchronized (cons) {
			cons.addFirst(connection);
			// 随机提醒一个
			cons.notify();
		}
	}
}
