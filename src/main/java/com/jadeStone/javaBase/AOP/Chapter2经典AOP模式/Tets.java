/**
 * 
 */
package com.jadeStone.javaBase.AOP.Chapter2经典AOP模式;

import java.util.Map;

/**
 * @author hongxu
 *
 */
public class Tets {

	public static void main(String[] args) {
		Runnable ru = new Runnable(){
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		for(int i =0; i<10;i++){
			new Thread(ru,i+"").start();
		}
		Map<Thread, StackTraceElement[]>  map = Thread.getAllStackTraces();
		map.entrySet().forEach((item)->{
			System.out.println("----------");
			System.out.println(item.getKey().getName());
			for (StackTraceElement element: item.getValue()){
				System.out.println(element.toString());
			}
			System.out.println("----------");
		});
	}
}
