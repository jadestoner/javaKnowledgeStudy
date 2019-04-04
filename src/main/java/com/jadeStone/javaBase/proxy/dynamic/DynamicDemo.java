package com.jadeStone.javaBase.proxy.dynamic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

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
		ObjectOutputStream ooss = new ObjectOutputStream(new FileOutputStream(new File("target1")));
		ooss.writeObject(o);
		
		ooss.close();
	}
	
	public Object getInstence(){
		return java.lang.reflect.Proxy.newProxyInstance(
				o.getClass().getClassLoader(), 
				o.getClass().getInterfaces(), 
				new InvocationHandler(){
					@Override
					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {
						System.out.println(proxy.getClass());
						ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("proxy")));
						oos.writeObject(proxy);
						oos.close();
						System.out.println("pre");
						Object a = method.invoke(o, args);
						System.out.println("post");
						return a;
					}
					
				});
	}
}

