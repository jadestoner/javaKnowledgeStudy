/**
 * 
 */
package com.jadeStone.thirdLib.netty.utils;

import java.util.Set;

/**
 * @author 
 *
 */
public interface ArgumentSolver {

	Object get(String key);
	
	Set<String> keys();
	
	boolean contains(String key);
}
