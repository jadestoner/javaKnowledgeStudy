/**
 * 
 */
package com.jadeStone.thirdLib.netty.utils;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

/**
 * @author 
 *
 */
public class HttpGetArgumentSolver extends ArgumentSolverSupport {

	public HttpGetArgumentSolver(FullHttpRequest request){
		 QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
         decoder.parameters().entrySet().forEach( entry -> {
        	 putArg(entry.getKey(), entry.getValue().get(0));
         });
	}
}
