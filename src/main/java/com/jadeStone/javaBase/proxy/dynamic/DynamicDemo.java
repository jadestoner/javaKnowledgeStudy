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

