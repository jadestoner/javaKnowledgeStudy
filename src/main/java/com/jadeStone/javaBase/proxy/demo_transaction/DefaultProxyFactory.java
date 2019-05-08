/**
 * 
 */
package com.jadeStone.javaBase.proxy.demo_transaction;

/**
 * @author hongxu
 *
 */
public class DefaultProxyFactory {
	
//	public static <T> T createProxy(T target) throws Exception { 
//        if (!(target instanceof IDb)) throw new Exception("target must be instance of IDb"); 
//         
//        ClassLoader classLoader = target.getClass().getClassLoader(); 
//        Class<?>[] interfaces = target.getClass().getInterfaces(); 
//        TransactionalInvocationHandler handler = new TransactionalInvocationHandler(target); 
//        return (T) Proxy.newProxyInstance(classLoader, interfaces, handler); 
//    } 
     
    /** 
     * cglib代理 
     * @param <T> 
     * @param target 
     * @return 
     * @throws Exception 
     */ 
//    @SuppressWarnings("unchecked") 
//    public static <T> T createCglibProxy(T target) throws Exception { 
//        if (!(target instanceof IDb)) throw new Exception("target must be instance of IDb"); 
//         
//        CglibProxy proxy = new CglibProxy(target);  
//        Enhancer enhancer = new Enhancer(); 
//        enhancer.setSuperclass(target.getClass());  
//        enhancer.setCallback(proxy); 
//         
//        return (T) enhancer.create();  
//    } 
}
