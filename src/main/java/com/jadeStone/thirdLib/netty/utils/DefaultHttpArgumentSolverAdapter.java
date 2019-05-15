/**
 * 
 */
package com.jadeStone.thirdLib.netty.utils;

import static io.netty.handler.codec.http.HttpMethod.GET;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Set;

/**
 * @author hongxu
 *
 */
public class DefaultHttpArgumentSolverAdapter implements ArgumentSolver {
	
	private ArgumentSolver argumentSolver;
	
	public DefaultHttpArgumentSolverAdapter(FullHttpRequest request){
		if (GET.equals(request.method())) {
			argumentSolver = new HttpGetArgumentSolver(request);
        }else{
        	// ...
        }
	}

	@Override
	public Object get(String key) {
		return argumentSolver.get(key);
	}

	@Override
	public Set<String> keys() {
		return argumentSolver.keys();
	}

	@Override
	public boolean contains(String key) {
		return argumentSolver.contains(key);
	}
}
