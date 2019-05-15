/**
 * 
 */
package com.jadeStone.thirdLib.netty.common.context;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hongxu
 *
 */
public enum  ServerManager {
		 
		INSTANCE;
	 
		private Logger logger = LoggerFactory.getLogger(ServerManager.class);
	 
		/** 缓存用户id与对应的会话 */
		private ConcurrentMap<String, Session> userId2Sessions = new ConcurrentHashMap<>();
	 
	 
		public void sendObjectTo(Object content,Long userId){
			if(content == null || userId <= 0) return;
	 
			Session session = userId2Sessions.get(userId);
			if (session != null) {
				session.sendObject(content);
			}
		}
	 
		/**
		 *  向所有在线用户发送数据包
		 */
		public void sendObjectToAllUsers(Object content){
			if(content == null ) return;
	 
			userId2Sessions.values().forEach( (session) -> session.sendObject(content));
		}
	 
		/**
		 *  向单一在线用户发送数据包
		 */
		public void sendObjectTo(Object content,ChannelHandlerContext targetContext ){
			if(content == null || targetContext == null) return;
			targetContext.writeAndFlush(content);
		}
	 
	 
		public Session getSessionBy(String userId) {
			return this.userId2Sessions.get(userId);
		}
	 
		public boolean registerSession(User user, Session session) {
	 
			session.setUser(user);
			userId2Sessions.put(user.getUserId(), session);
	 
			logger.info("[{}] registered...", user.getUserId());
	 
			return true;
		}
	 
		/**
		 *   注销用户通信渠道
		 */
		public void ungisterUserContext(String userId){
			if(userId  == null){
				return;
			}
			Session session = userId2Sessions.remove(userId);
			if (session != null) {
				session.close(SessionCloseReason.OVER_TIME);
			}
		}
	
}
