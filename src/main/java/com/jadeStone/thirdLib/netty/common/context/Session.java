/**
 * 
 */
package com.jadeStone.thirdLib.netty.common.context;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hongxu
 *
 */
public class Session {
		
		private static final Logger logger = LoggerFactory.getLogger(Session.class);
	 
		/** 网络连接channel */
		private Channel channel;
		
		private User user;
	 
		/** ip地址 */
		private String ipAddr;
	 
		private boolean reconnected;
		
		/** 拓展用，保存一些个人数据  */
		private Map<String, Object> attrs = new HashMap<>();
	 
		public Session() {
	 
		}
	 
		public Session(Channel channel) {
			this.channel = channel;
			this.ipAddr = ChannelUtils.getIp(channel);
		}
		public Session(Channel channel, User user) {
			this(channel);
			this.user = user;
		}
		
		/**
		 * 向客户端发送消息
		 * @param content
		 */
		public void sendObject(Object content) {
			if (content == null) {
				return;
			}
			if (channel != null) {
				channel.writeAndFlush(content);
			}
		}
		
		public User getUser() {
			return user;
		}

		public void setUser(User user2) {
			this.user = user2;
		}

		public String getIpAddr() {
			return ipAddr;
		}
	 
		public void setIpAddr(String ipAddr) {
			this.ipAddr = ipAddr;
		}
	 
		public boolean isReconnected() {
			return reconnected;
		}
	 
		public void setReconnected(boolean reconnected) {
			this.reconnected = reconnected;
		}
		
		public boolean isClose() {
			if (channel == null) {
				return true;
			}
			return !channel.isActive() ||
				   !channel.isOpen();
		}
		
		/**
		 * 关闭session 
		 * @param reason {@link SessionCloseReason}
		 */
		public void close(SessionCloseReason reason) {
			try{
				if (this.channel == null) {
					return;
				}
				if (channel.isOpen()) {
					channel.close();
					logger.info("close session[{}], reason is {}", reason);
				}else{
					logger.info("session[{}] already close, reason is {}",reason);
				}
			}catch(Exception e){
			}
		}

}
