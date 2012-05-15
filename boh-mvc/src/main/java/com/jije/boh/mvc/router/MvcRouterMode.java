package com.jije.boh.mvc.router;

/**
 * 路由模式 
 * UI：直接路由到静态的json文件 
 * UI_DEBUG：将请求转发到java服务器 
 * JAVA：路由到具体的server module 获取业务数据
 * 
 * @author eric.zhang
 * 
 */
public enum MvcRouterMode {

	UI, UI_DEBUG, JAVA;

	public static MvcRouterMode getMode(String value) {
		
		if ("java".equalsIgnoreCase(value)) {
			return JAVA;
		} else if ("ui_debug".equalsIgnoreCase(value)) {
			return UI_DEBUG;
		} else {
			return UI;
		}
		
	}
}
