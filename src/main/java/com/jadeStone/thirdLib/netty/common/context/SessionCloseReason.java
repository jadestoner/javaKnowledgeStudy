/**
 * 
 */
package com.jadeStone.thirdLib.netty.common.context;

/**
 * @author hongxu
 *
 */
public enum SessionCloseReason {

	/** 正常退出 */
	NORMAL,
	
	/** 链接超时 */
	OVER_TIME,
	
	END;
}
