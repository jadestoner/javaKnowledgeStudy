package com.jadeStone.javaBase.proxy;


public interface DoService{
	void doo();
	/**
	 * 新增方法，测试切点
	 * 补充：静态代理的弊端初步暴露：接口新增方法后，实现类和代理类全部都要重新实现该方法，而动态代理则不需要
	 */
	void doo2();
}
