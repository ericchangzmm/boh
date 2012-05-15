package com.jije.boh.mvc;

import java.io.File;

import com.jije.boh.mvc.router.MvcRouterMode;

/**
 * 配置
 * 
 * @author eric.zhang
 * 
 */
public class MvcConfig {

	// 是否跨域请求
	public static boolean crossDomain;

	/**
	 * 路由模式
	 * 
	 * @see MvcRouterMode
	 */
	public static String routerMode;

	//前端json文件夹目录
	public static final String UI_JSON_FOLDER = "WEB-INF" + File.separator + "static"
			+ File.separator;
	
	//转发的url
	public static String forwardUrl;

	public void setRouterMode(String routerMode) {
		MvcConfig.routerMode = routerMode;
	}

	public void setCrossDomain(boolean crossDomain) {
		MvcConfig.crossDomain = crossDomain;
	}

	public void setForwardUrl(String forwardUrl) {
		MvcConfig.forwardUrl = forwardUrl;
	}
	
}
