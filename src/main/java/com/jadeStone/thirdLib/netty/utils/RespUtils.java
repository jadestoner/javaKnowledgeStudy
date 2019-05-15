/**
 * 
 */
package com.jadeStone.thirdLib.netty.utils;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import org.junit.Assert;

/**
 * @author hongxu
 *
 */
public class RespUtils {
	
	private static ChannelHandlerContext ctx;
	private static FullHttpRequest request;
	private static FullHttpResponse response;
	
	public static void of(ChannelHandlerContext ctx,
			FullHttpRequest request,
			FullHttpResponse response){
		RespUtils.ctx = ctx;
		RespUtils.request = request;
		RespUtils.response = response;
	}
	
	private static void clear(){
		RespUtils.ctx = null;
		RespUtils.request = null;
		RespUtils.response = null;
	}
	private static void check(){
		org.junit.Assert.assertNotNull(RespUtils.ctx);
		Assert.assertNotNull(RespUtils.request);
		Assert.assertNotNull(RespUtils.response);
	}
	 public static void sendAndCleanupConnection() {check();
          final boolean keepAlive = HttpUtil.isKeepAlive(request);
          HttpUtil.setContentLength(response, response.content().readableBytes());
          if (!keepAlive) {
              // We're going to close the connection as soon as the response is sent,
              // so we should also make it clear for the client.
              response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
          } else if (request.protocolVersion().equals(HttpVersion.HTTP_1_0)) {
              response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
          }
  
          ChannelFuture flushPromise = ctx.writeAndFlush(response);
  
          if (!keepAlive) {
              // Close the connection as soon as the response is sent.
              flushPromise.addListener(ChannelFutureListener.CLOSE);
          }
          clear();
      }
	 
	      public static void sendRedirect(String newUri) {
		          FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
		          response.headers().set(HttpHeaderNames.LOCATION, newUri);
		  
		          sendAndCleanupConnection();
		      }
		  
		      public void sendError(HttpResponseStatus status) {
		          FullHttpResponse response = new DefaultFullHttpResponse(
		        		  HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status + "\r\n", CharsetUtil.UTF_8));
		          response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
		  
		          sendAndCleanupConnection();
		      }
		      
}
