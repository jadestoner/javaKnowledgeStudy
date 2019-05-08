/**
 * 
 */
/**
 * @author 
 *
 * 利用动态代理和cglib代理实现一个简单的事务管理
 * 
 * 设计思路：1. 方法入口处取数据库连接,如果配了注解，则设置相关属性，放入threadlocal，方法结束时释放。
 * （获取/释放都是针对连接池）,由于使用了线程池，归还线程时记得Thread.currentThread().threadLocals = null;
 * threadlocal相关知识可参考：https://blog.csdn.net/puppylpg/article/details/80433271
 * 
 * 主体架构设计：
 * 
 * 约定：业务类定义：使用了注解@Business的类，业务类会被代理
 *    
 * 
 * 
 */
package com.jadeStone.javaBase.proxy.demo_transaction;