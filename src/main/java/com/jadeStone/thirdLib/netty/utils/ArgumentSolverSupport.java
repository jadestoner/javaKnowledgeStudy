/**
 * 
 */
package com.jadeStone.thirdLib.netty.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author hongxu
 *
 */
public abstract class ArgumentSolverSupport implements ArgumentSolver {

	private Map<String, Object> arguments = new HashMap<>();
	
	protected void putArg(String key, Object value){
		arguments.put(key, value);
	}
	
	@Override
	public Object get(String key) {
		return arguments.get(key);
	}

	@Override
	public Set<String> keys() {
		return arguments.keySet();
	}

	@Override
	public boolean contains(String key) {
		return arguments.containsKey(key);
	}
}
