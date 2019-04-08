package com.jadeStone.javaBase.proxy.cglib;

import java.io.FileOutputStream;
import java.lang.reflect.Method;

import org.springframework.asm.ClassWriter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import com.jadeStone.javaBase.proxy.DoService;
import com.jadeStone.javaBase.proxy.DoServiceImpl;

public class CglibDemo {

	public static void main(String[] args) {
		DoService doService = new DoServiceImpl();
		doService.doo();
		
		Enhancer eh = new Enhancer();
		eh.setSuperclass(doService.getClass());
		eh.setUseFactory(false);
		eh.setCallback(new MethodInterceptor() {
			@Override
			public Object intercept(Object arg0, Method arg1, Object[] arg2,
					MethodProxy arg3) throws Throwable {
				System.out.println(arg0.getClass());
				System.out.println(arg1.getName());
				System.out.println(arg2);
				System.out.println(arg3);
                arg3.invokeSuper(arg0, arg2);
//				arg1.invoke(arg0, arg2);
				return null;
			}
		});
		doService = (DoService)eh.create();
		doService.doo();
		
		//以下代码是为了输出cglib生成的类的class文件
		try {
			 ClassWriter cw = new ClassWriter(0);
		        eh.generateClass(cw);
		        byte[] klass = cw.toByteArray();
		        FileOutputStream fileOutputStream = new FileOutputStream(doService.getClass().getName()+".class");
		        fileOutputStream.write(klass);
		        fileOutputStream.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
       
	}
}
