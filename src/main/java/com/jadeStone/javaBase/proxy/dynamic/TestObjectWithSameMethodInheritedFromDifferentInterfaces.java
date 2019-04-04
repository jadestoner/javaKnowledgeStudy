package com.jadeStone.javaBase.proxy.dynamic;

import java.lang.reflect.Method;

/**
 * 
 * @author 
 *
 */
public class TestObjectWithSameMethodInheritedFromDifferentInterfaces {
	public static void main(String[] args) {
		A a =new Target();
		a.doo();
		((B)a).doo();
		
	}
	
}


interface A{
	 void doo();
}
interface B{
	void doo();
}

class Target implements A,B{

	@Override
	public void doo() {
		System.out.println("doo");
	}
}