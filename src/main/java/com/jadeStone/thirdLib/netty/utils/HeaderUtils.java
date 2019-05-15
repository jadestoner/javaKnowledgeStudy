/**
 * 
 */
package com.jadeStone.thirdLib.netty.utils;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.activation.MimetypesFileTypeMap;

/**
 * @author hongxu
 *
 */
public abstract class HeaderUtils {

	
	public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
	     public static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
	     public static final int HTTP_CACHE_SECONDS = 60;
	
	public static void setContentTypeHeader(HttpResponse response, File file) {
         MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
         response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
     }
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
}
