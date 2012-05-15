package com.jije.boh.core.stateless1;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * session 处理接口
 * 
 * @author eric.zhang
 * 
 */
public interface ISessionBackend {

	/**
	 * Initialize the backend using the specified configuration.
	 * 
	 * @param config
	 */
	void init(Map<String, String> config);

	/**
	 * Destroy the backend
	 */
	void destroy();

	/**
	 * 获取session
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	ISessionData get(HttpServletRequest request, HttpServletResponse response);

	/**
	 * 保存session
	 * 
	 * @param session
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	void save(ISessionData session, HttpServletRequest request,
			HttpServletResponse response);
}
