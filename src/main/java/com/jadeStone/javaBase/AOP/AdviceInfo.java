package com.jadeStone.javaBase.AOP;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;

import com.jadeStone.javaBase.proxy.DoService;
import com.jadeStone.javaBase.proxy.DoServiceImpl;

/**
 * @author 
 *
 */
public class AdviceInfo {
	public static void main(String[] args) {
		// 创建代理工厂
		ProxyFactory proxyFactory = new ProxyFactory();
		
		DoService doService = new DoServiceImpl();
		// 设置代理对象
		proxyFactory.setTarget(doService);
		// 设置拦截器(增强器)
		proxyFactory.addAdvice(new MethodInterceptor() {
			@Override
			public Object invoke(MethodInvocation invocation) throws Throwable {
				System.out.println("被拦截喽");
				Object result = invocation.proceed();
				System.out.println("被爆菊喽");

				return result;
			}
		});
		doService = (DoService)proxyFactory.getProxy();
		doService.doo();
		doService.doo2();
		// 可见，只有Advice的话，代理对象的所有方法都被增强了，如果我们只是想增强某个方法呢，请看 Pointcut （切入点）类
		
	}
}

