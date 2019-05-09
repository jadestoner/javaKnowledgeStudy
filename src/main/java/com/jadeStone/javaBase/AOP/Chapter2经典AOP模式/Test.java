/**
 * 
 */
package com.jadeStone.javaBase.AOP.Chapter2经典AOP模式;

import java.util.ArrayList;
import java.util.List;


/**
 * @author hongxu
 *
 */
public class Test {

	// 8*
	private  static String FIXED = new String(new byte[4555]);
	 static class OOMObject {  
		  
	    }  
	

		  
    public static void main(String[] args) throws InterruptedException {  
        List<OOMObject> list = new ArrayList<Test.OOMObject>();  
        while (true) {  
            list.add(new OOMObject());  
            Thread.sleep(200);
        }  

    }
}
