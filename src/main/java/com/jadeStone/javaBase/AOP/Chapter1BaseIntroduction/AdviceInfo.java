package com.jadeStone.javaBase.AOP;

import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;

import com.jadeStone.javaBase.proxy.DoService;
import com.jadeStone.javaBase.proxy.DoServiceImpl;

/**
 * 【通知】 Advice，表示在连接点上执行的行为，这个行为是目标类类所没有的。将之理解为【增强】更好
 * 包括前置增强（before advice）、后置增强(after advice)、环绕增强（around advice）
 * 
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
		// getProxy()先根据target是否有接口决定了调用cglib代理，还是jdk的动态代理;然后再生成相应的代理类
		doService = (DoService)proxyFactory.getProxy();
		doService.doo();
		doService.doo2();
		
		// 可见，只有 Advice 的 话，代理对象的所有方法都被增强了，如果我们只是想增强某个方法呢，请看 Pointcut （切入点）类
		// 补充：1.Advisor代表切面，切面 Advisor = 切点 （Pointcut）+通知  Advice
		//    2. 拦截器 Interceptor 是 Advice的一个子类，MethodInterceptor 又是 Interceptor 的子类，这一类都是一般切面（将拦截所有的方法），不推荐使用
		//    3. 推荐使用切点切面 PointcutAdvisor。可通过配置切点，准确的直达目标
		//    4. 引介切面 IntroductionAdvisor
		
		/*
		 * 先来一个简单的 
		 */
		proxyFactory = new ProxyFactory();
		proxyFactory.setTarget(doService);
		// 设置切点切面
		proxyFactory.addAdvisor(new NameMatchMethodPointcutAdvisor(){
			@Override
			public NameMatchMethodPointcut addMethodName(String name) {
				return super.addMethodName("doo2");
			}
		});
		
		doService = (DoService)proxyFactory.getProxy();
		doService.doo();
		doService.doo2();
	}
	
	private Advice getMethodBeforeAdvice(){
		return new MethodBeforeAdvice (){
			@Override
			public void before(Method method, Object[] args, Object target)
					throws Throwable {
				System.out.println("==前置增强");
				System.out.println("==方法名：" + method.getName());
				if (null != args && args.length > 0) {
					for (int i = 0; i < args.length; i++) {
						System.out.println("==第" + (i + 1) + "参数：" + args[i]);
					}
				}
				System.out.println("==目标类信息：" + target.toString());
			}
		};
	}
	
	private Advice getMethodAfterAdvice(){
		// 后置通知，能看结果，即returnValue，但是不能改变它
		return new AfterReturningAdvice (){
			@Override
			public void afterReturning(Object returnValue, Method method,
					Object[] args, Object target) throws Throwable {
				System.out.println("==后置增强");
				System.out.println("==方法名：" + method.getName());
				if (null != args && args.length > 0) {
					for (int i = 0; i < args.length; i++) {
						System.out.println("==第" + (i + 1) + "参数：" + args[i]);
					}
				}
				System.out.println("==目标类信息：" + target.toString());
			}
		};
	}
	
	private Advice getThrowsAdvice (){
		// 后置通知，能看结果，即returnValue，但是不能改变它
		return new ThrowsAdvice  (){
			
		};
	}
}

