/**
 * 
 */
package com.jadeStone.thirdLib.quartz.chapter1;

/**
 * @author hongxu
 *
 */
public class Test {

	public static void main(String[] args) {
		
		String str1 = "hello world";
		
		String str2 = "hello" + new String(" world");
		
		String str3 =  "hello "+ "world";
		System.out.println(str1 == str2);
		System.out.println(str1 == str3);
		System.out.println(str1.equals(str2));

	}
	
}
