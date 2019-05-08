/**
 * 
 */
package com.jadeStone.javaBase.proxy.demo_transaction;

import java.lang.reflect.Method;
import java.sql.Connection;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author hongxu
 *
 */
//public class CglibProxy implements MethodInterceptor { 
//    private Object target; 
//    private IDb db; 
//     
//    public CglibProxy(Object target) { 
//        this.target = target; 
//        this.db = (IDb) target; 
//    } 
//     
//    @Override 
//    public Object intercept(Object obj, Method method, Object[] args, 
//            MethodProxy methodProxy) throws Throwable { 
//        System.out.println("method invoke begin:" + method.getName()); 
//         
//        Object result = null; 
//        boolean isNeedTransaction = false; 
//        boolean isAutoCommit = false; 
//        if (method.isAnnotationPresent(Transactional.class)) { 
//            isNeedTransaction = true; 
//            isAutoCommit = method.getAnnotation(Transactional.class).autoCommit(); 
//        } 
//        boolean isLocalOpen = false; 
//          
//        boolean rollback = false; 
//         
//        Connection conn = db.getConnection(); 
//         
//        System.out.println("isNeedTransaction:" + isNeedTransaction); 
//        System.out.println("isAutoCommit:" + isAutoCommit); 
//        System.out.println("isLocalOpen:" + isLocalOpen); 
//        try { 
//            if (isNeedTransaction) { 
//                if (conn == null) { 
//                    isLocalOpen = true; 
//                    try { 
//                        conn = DbUtil.getConnection(); 
//                        if (conn == null) throw new Exception("数据库连接获取错误"); 
//                        conn.setAutoCommit(isAutoCommit); 
//                    } 
//                    catch (Exception e) { 
//                        throw e; 
//                    } 
//                    db.setConnection(conn); 
//                } 
//            } 
//             
//            //result = methodProxy.invokeSuper(obj, args); 
//            result = method.invoke(this.target, args); 
//        } 
//        catch(Exception e) { 
//            rollback = true; 
//            //e.printStackTrace(); 
//            throw (e.getCause()==null)?e:e.getCause(); 
//        } 
//        finally { 
//            if (conn != null && isLocalOpen) { 
//                if (!isAutoCommit) { 
//                    if (rollback) conn.rollback(); 
//                    else conn.commit(); 
//                } 
//                db.closeConnection(); 
//            } 
//        } 
//        System.out.println("method invoke complete:" + method.getName()); 
//        return result; 
//    } 
//     
//} 