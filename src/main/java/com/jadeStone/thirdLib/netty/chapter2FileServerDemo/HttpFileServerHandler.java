package com.jadeStone.thirdLib.netty.chapter2FileServerDemo;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpResponseStatus.FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.METHOD_NOT_ALLOWED;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_MODIFIED;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_0;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.SystemPropertyUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;

import com.jadeStone.thirdLib.netty.common.context.ServerManager;
import com.jadeStone.thirdLib.netty.common.context.Session;
import com.jadeStone.thirdLib.netty.common.context.User;
import com.jadeStone.thirdLib.netty.utils.ArgumentSolver;
import com.jadeStone.thirdLib.netty.utils.DefaultHttpArgumentSolverAdapter;


public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	
	     public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
	     public static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
	     public static final int HTTP_CACHE_SECONDS = 60;
	     
	     public static final String LOGIN_URL = "/login";
	     public static final String URL_VALIDATE = "/validate";
	     public static final String URL_HOME_PAGE = "/";

	 
	     private FullHttpRequest request;

	     @Override
	     public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
	         this.request = request;
	         if (!request.decoderResult().isSuccess()) {
	             sendError(ctx, BAD_REQUEST);
	             return;
	         }
	 
	         if (!GET.equals(request.method())) {
	             this.sendError(ctx, METHOD_NOT_ALLOWED);
	             return;
	         }
	         ArgumentSolver solver = new DefaultHttpArgumentSolverAdapter(request);
	         final String uri = request.uri();
	         final String path = sanitizeUri(uri);
	         // 如果是登陆，则记录下userId
	         if(LOGIN_URL.equals(uri)){
	        	 this.redirectLogin(ctx, request);
	        	 return;
	         }else if(URL_VALIDATE.equals(uri)){
	        	 this.validateLogin(ctx, request,solver);
	        	 return;
	         }else{
	        	 if(!validateAlLogined(ctx, request)){
	        		 return;
	        	 }
	         }
	         
	         if (path == null) {
	             this.sendError(ctx, FORBIDDEN);
	             return;
	         }
	 
	         File file = new File(path);
	         if (file.isHidden() || !file.exists()) {
	             this.sendError(ctx, NOT_FOUND);
	             return;
	         }
	 
	         if (file.isDirectory()) {
	             if (uri.endsWith("/")) {
	                 this.sendListing(ctx, file, uri);
	             } else {
	                 this.sendRedirect(ctx, uri + '/');
	             }
	             return;
	         }
	 
	         if (!file.isFile()) {
	             sendError(ctx, FORBIDDEN);
	             return;
	         }
	 
	         // Cache Validation
	         String ifModifiedSince = request.headers().get(HttpHeaderNames.IF_MODIFIED_SINCE);
	         if (ifModifiedSince != null && !ifModifiedSince.isEmpty()) {
	             SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
	             Date ifModifiedSinceDate = dateFormatter.parse(ifModifiedSince);
	 
	             // Only compare up to the second because the datetime format we send to the client
	             // does not have milliseconds
	             long ifModifiedSinceDateSeconds = ifModifiedSinceDate.getTime() / 1000;
	             long fileLastModifiedSeconds = file.lastModified() / 1000;
	             if (ifModifiedSinceDateSeconds == fileLastModifiedSeconds) {
	                 this.sendNotModified(ctx);
	                 return;
	             }
	         }
	 
	         RandomAccessFile raf;
	         try {
	             raf = new RandomAccessFile(file, "r");
	         } catch (FileNotFoundException ignore) {
	             sendError(ctx, NOT_FOUND);
	             return;
	         }
	         long fileLength = raf.length();
	 
	         HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
	         Object arg_type;
	         if((arg_type = solver.get("type")) == null || !arg_type.toString().equals("1")){
				 response.headers().set("Content-Type","text/plain;charset=utf-8");
	         }else{
	        	 setContentTypeHeader(response, file);
	         }
//	         setDateAndCacheHeaders(response, file);
	         
	         final boolean keepAlive = HttpUtil.isKeepAlive(request);
	         if (!keepAlive) {
	             response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
	         } else if (request.protocolVersion().equals(HTTP_1_0)) {
	             response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
	         }
	         ctx.write(response);
	         
	         ChannelFuture lastContentFuture;
	        
	         ChannelFuture sendFileFuture = ctx.writeAndFlush(new HttpChunkedInput(new ChunkedFile(raf, 0, fileLength, 8192)),
                             ctx.newProgressivePromise());
             // HttpChunkedInput will write the end marker (LastHttpContent) for us.
             lastContentFuture = sendFileFuture;
	 
	         sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
	             @Override
	             public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
	                 if (total < 0) { // total unknown
	                     System.err.println(future.channel() + " Transfer progress: " + progress);
	                 } else {
	                     System.err.println(future.channel() + " Transfer progress: " + progress + " / " + total);
	                 }
	             }
	 
	             @Override
	             public void operationComplete(ChannelProgressiveFuture future) {
	                 System.err.println(future.channel() + " Transfer complete.");
	             }
	         });
	 
	         if (!keepAlive) {
	             lastContentFuture.addListener(ChannelFutureListener.CLOSE);
	         }
	     }
	 
	     /**
		 * @param ctx
		 * @param request2
		 * @return
		 */
		private boolean validateAlLogined(ChannelHandlerContext ctx,
				FullHttpRequest request2) {
			// TODO Auto-generated method stub
			return false;
		}

		/**
		 * @param ctx
		 * @param request2
		 */
		private boolean validateLogin(ChannelHandlerContext ctx,
				FullHttpRequest request2, ArgumentSolver solver) {
			String userId = solver.get("userId").toString();
			ServerManager manager = ServerManager.INSTANCE;
			Session session = manager.getSessionBy(userId);
			if(session == null){
				if(manager.registerSession(new User(userId), new Session(ctx.channel()))){
					sendHomePage(ctx);
					return true;
				}
				return false;
			}
			return true;
		}

		/**
		 * @param ctx
		 * @param request2
		 */
		private void redirectLogin(ChannelHandlerContext ctx,
				FullHttpRequest request2) {
	         FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
	         response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
	 
	         StringBuilder buf = new StringBuilder()
	             .append("<!DOCTYPE html>\r\n")
	             .append("<html><head><meta charset='utf-8' /><title>")
	             .append("欢迎!昵称")
	             .append("</title></head><body>\r\n")
	            .append("<form action='/validate'>")
	            .append("First name:<br>")
	            .append("<input type='text' name='userId' value='昵称'>")
	            .append("<input type='submit' value='Submit'>")
	            .append("</form>");
	 
	         buf.append("</body></html>\r\n");
	         ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
	         response.content().writeBytes(buffer);
	         buffer.release();
	 
	         this.sendAndCleanupConnection(ctx, response);
		}

		@Override
	     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	         cause.printStackTrace();
	         if (ctx.channel().isActive()) {
	             sendError(ctx, INTERNAL_SERVER_ERROR);
	         }
	     }
	 
	     private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");
	 
	     private static String sanitizeUri(String uri) {
	         // Decode the path.
	         try {
	             uri = URLDecoder.decode(uri, "UTF-8");
	         } catch (UnsupportedEncodingException e) {
	             throw new Error(e);
	         }
	 
	         if (uri.isEmpty() || uri.charAt(0) != '/') {
	             return null;
	         }
	 
	         uri = uri.replace('/', File.separatorChar);
	         if (uri.contains(File.separator + '.') ||
	             uri.contains('.' + File.separator) ||
	             uri.charAt(0) == '.' || uri.charAt(uri.length() - 1) == '.' ||
	             INSECURE_URI.matcher(uri).matches()) {
	             return null;
	         }
	        
	         // 后面要用uri当作文件名，需要把参数截掉；而且参数在前文已经保存到了argumentSolver里、
	         int argumentIndex;
	         if((argumentIndex = uri.indexOf("?"))>-1){
	        	 uri = uri.substring(0, argumentIndex);
	         }
	         return SystemPropertyUtil.get("user.dir") + File.separator + uri;
	     }
	 
	     private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[^-\\._]?[^<>&\\\"]*");
	 
	     private void sendHomePage(ChannelHandlerContext ctx){
	    	 String homePage = SystemPropertyUtil.get("user.dir").concat(File.separator).concat("/");
	    	 sendListing(ctx, new File(homePage), homePage);
	     }
	     
	     private void sendListing(ChannelHandlerContext ctx, File dir, String dirPath) {
	         FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
	         response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
	 
	         StringBuilder buf = new StringBuilder()
	             .append("<!DOCTYPE html>\r\n")
	             .append("<html><head><meta charset='utf-8' /><title>")
	             .append("Listing of: ")
	             .append(dirPath)
	             .append("</title></head><body>\r\n")
	 
	             .append("<h3>Listing of: ")
	             .append(dirPath)
	             .append("</h3>\r\n")
	 
	             .append("<ul>")
	             .append("<li><a href=\"../\">..</a></li>\r\n");
	 
	         for (File f: dir.listFiles()) {
	             if (f.isHidden() || !f.canRead()) {
	                 continue;
	             }
	 
	             String name = f.getName();
	             if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
	                 continue;
	             }
	 
	             buf.append("<li>")
	             	.append("<a href=\"")
	                .append(name)
	                .append("\">")
	                .append(name)
	                .append("</a>")
	                .append("<a href=\"")
	                .append(name).append("?type=1")
	                .append("\">")
	                .append("下载")
	                .append("</a>")
	                .append("</li>\r\n");
	         }
	 
	         buf.append("</ul></body></html>\r\n");
	         ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
	         response.content().writeBytes(buffer);
	         buffer.release();
	 
	         this.sendAndCleanupConnection(ctx, response);
	     }
	 
	     private void sendRedirect(ChannelHandlerContext ctx, String newUri) {
	         FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
	         response.headers().set(HttpHeaderNames.LOCATION, newUri);
	 
	         this.sendAndCleanupConnection(ctx, response);
	     }
	 
	     private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
	         FullHttpResponse response = new DefaultFullHttpResponse(
	                 HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status + "\r\n", CharsetUtil.UTF_8));
	         response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
	 
	         this.sendAndCleanupConnection(ctx, response);
	     }
	 
	     /**
	      * When file timestamp is the same as what the browser is sending up, send a " Not Modified"
	      *
	      * @param ctx
	      *            Context
	      */
	     private void sendNotModified(ChannelHandlerContext ctx) {
	         FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NOT_MODIFIED);
	         setDateHeader(response);
	 
	         this.sendAndCleanupConnection(ctx, response);
	     }
	 
	     /**
	      * If Keep-Alive is disabled, attaches "Connection: close" header to the response
	      * and closes the connection after the response being sent.
	      */
	     private void sendAndCleanupConnection(ChannelHandlerContext ctx, FullHttpResponse response) {
	         final FullHttpRequest request = this.request;
	         final boolean keepAlive = HttpUtil.isKeepAlive(request);
	         HttpUtil.setContentLength(response, response.content().readableBytes());
	         if (!keepAlive) {
	             // We're going to close the connection as soon as the response is sent,
	             // so we should also make it clear for the client.
	             response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
	         } else if (request.protocolVersion().equals(HTTP_1_0)) {
	             response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
	         }
	 
	         ChannelFuture flushPromise = ctx.writeAndFlush(response);
	 
	         if (!keepAlive) {
	             // Close the connection as soon as the response is sent.
	             flushPromise.addListener(ChannelFutureListener.CLOSE);
	         }
	     }
	 
	     /**
	      * Sets the Date header for the HTTP response
	      *
	      * @param response
	      *            HTTP response
	      */
	     private static void setDateHeader(FullHttpResponse response) {
	         SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
	         dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));
	 
	         Calendar time = new GregorianCalendar();
	         response.headers().set(HttpHeaderNames.DATE, dateFormatter.format(time.getTime()));
	     }
	 
	     /**
	      * Sets the Date and Cache headers for the HTTP Response
	      *
	      * @param response
	      *            HTTP response
	      * @param fileToCache
	      *            file to extract content type
	      */
	     private static void setDateAndCacheHeaders(HttpResponse response, File fileToCache) {
	         SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
	         dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));
	 
	         // Date header
	         Calendar time = new GregorianCalendar();
	         response.headers().set(HttpHeaderNames.DATE, dateFormatter.format(time.getTime()));
	 
	         // Add cache headers
	         time.add(Calendar.SECOND, HTTP_CACHE_SECONDS);
	         response.headers().set(HttpHeaderNames.EXPIRES, dateFormatter.format(time.getTime()));
	         response.headers().set(HttpHeaderNames.CACHE_CONTROL, "private, max-age=" + HTTP_CACHE_SECONDS);
	         response.headers().set(
	                 HttpHeaderNames.LAST_MODIFIED, dateFormatter.format(new Date(fileToCache.lastModified())));
	     }
	 
	     /**
	      * Sets the content type header for the HTTP Response
	      *
	      * @param response
	      *            HTTP response
	      * @param file
	      *            file to extract content type
	      */
	     private static void setContentTypeHeader(HttpResponse response, File file) {
	         MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
	         response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
	     }
	
}
