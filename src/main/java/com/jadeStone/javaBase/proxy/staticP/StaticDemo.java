package com.jadeStone.javaBase.proxy.staticP;

import com.jadeStone.javaBase.proxy.DoService;
import com.jadeStone.javaBase.proxy.DoServiceImpl;

public class StaticDemo {

	public static void main(String[] args) {
		DoService doService = new DoServiceImpl();
		doService.doo();
		// 代理：1.还是原来的引用，即对客户端透明）；-----> 实现同一个接口
		// 	   2. 原来的功能不变，加入了新的功能；-----> 同接口同方法，组合增强功能
		// 
		doService = new ProxyImpl(doService);
		doService.doo();
	}
}

class ProxyImpl implements DoService{

	DoService doService;
	public ProxyImpl(DoService doService) {
		this.doService = doService;
	}
	@Override
	public void doo() {
		System.out.println("before doo");
		doService.doo();
		System.out.println("after doo");
	}
}


