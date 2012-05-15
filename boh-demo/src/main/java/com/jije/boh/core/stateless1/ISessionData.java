package com.jije.boh.core.stateless1;

import java.io.Serializable;
import java.util.Map;

/**
 * session 数据接口
 * 
 * @author eric.zhang
 * 
 */
public interface ISessionData extends Serializable {

	/**
	 * session attributes
	 * 
	 * @return
	 */
	Map<String, Object> getContent();

	/**
	 * session creation time stamp
	 * 
	 * @return
	 */
	long getCreationTime();

	/**
	 * session id
	 * 
	 * @return
	 */
	String getId();

	/**
	 * Checks if session is valid
	 * 
	 * @return
	 */
	boolean isValid();
}
