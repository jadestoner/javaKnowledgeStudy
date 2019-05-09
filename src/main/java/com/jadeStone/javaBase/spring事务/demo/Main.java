/**
 * 
 */
package com.jadeStone.javaBase.spring事务.demo;

import java.io.File;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.jadeStone.javaBase.proxy.DoService;

/**
 * @author hongxu
 *
 */
public class Main {
	public static void main(String[] args) throws Exception {
		 ApplicationContext context = new FileSystemXmlApplicationContext("src/main/java/com/jadeStone/javaBase/spring事务/demo/bean.xml");
	
		 DemoService dao = (DemoService)context.getBean("demoServiceProxy",DemoService.class);
	     dao.insert1();
	}
}
