package com.jadeStone.javaBase.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.jadeStone.javaBase.proxy.DoService;
import com.jadeStone.javaBase.proxy.DoServiceImpl;

public class DynamicDemo {

	public static void main(String[] args) throws Exception {
		DoService doo = new DoServiceImpl();
		doo.doo();
		doo = (DoService)new ProxyFactory(doo).getInstence();
		doo.doo();
		
		// 网上看到一个有意思的用法（RPC代理），只代理接口，这里实现以下
		// 场景：客户端只有一个DoService接口，调用doo方法时，反射时调用远程方法即可
		doo = (DoService)new ProxyOnlyInterface(DoService.class).getInstence();
		doo.doo();
	}	
}

class ProxyFactory{
	private Object o;
	public ProxyFactory(Object o) throws Exception{
		this.o = o;
	}
	
	public Object getInstence(){
		return java.lang.reflect.Proxy.newProxyInstance(
				o.getClass().getClassLoader(), 
				o.getClass().getInterfaces(), 
				new InvocationHandler(){
					@Override
					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {
						System.out.println("pre");
						Object a = method.invoke(o, args);
						System.out.println("post");
						return a;
					}
					
				});
	}
}

class ProxyOnlyInterface{
	// 这是一个接口的class对象，如上文的DoService.class
	private Class o;
	public ProxyOnlyInterface(Class o) throws Exception{
		this.o = o;
	}
	
	public Object getInstence(){
		return java.lang.reflect.Proxy.newProxyInstance(
				o.getClassLoader(),
				new Class[]{o},
				new InvocationHandler(){
					@Override
					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {
						System.out.println("pre,代理的接口名称"+o.getName()+",接口方法"+method.getName());
						// 这里就不能再使用method.invoke方法了，因为这里的o不是实例对象
						// 使用场景是一个RPC的调用,这里将接口名，方法名，参数，版本，等信息拼装成一个对象，序列化后发送出去即可实现简单的RPC代理调用
//						Object a = method.invoke(o, args);
						Object result = "远程调用返回的结果";
//						result = 远程调用返回的结果
						System.out.println("post");
						return result;
					}
					
				});
	}
}
