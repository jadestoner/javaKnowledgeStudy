/**
 * 
 */
package com.jadeStone.thirdLib.netty.common.context;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

/**
 * @author hongxu
 *
 */
public class ChannelUtils {
		
		public static AttributeKey<Session> SESSION_KEY = AttributeKey.valueOf("session");

		
		public static String getIp(Channel channel) {
			return ((InetSocketAddress)channel.remoteAddress()).getAddress().toString().substring(1);
		}
	 
	
}
