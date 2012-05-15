package com.jije.boh.mvc.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jije.boh.mvc.MvcConfig;
import com.jije.boh.mvc.MvcException;
import com.jije.boh.mvc.router.MvcRouter;
import com.jije.boh.mvc.router.MvcRouterMode;

/**
 * 静态目录为： WEB-INF/static/module/function.json
 * 例如：WEB-INF/static/core1.0.0/get_user.json
 * 
 * 访问路径为：server-path/your-project-name/ajax/request-module/request-function
 * 例如：localhost:8080/boh-mvc/ajax/core1.0.0/get_user
 * 
 * @author eric.zhang
 * 
 */
@Controller
public class MainController {

	private static Logger logger = LoggerFactory
			.getLogger(MainController.class);

	@Autowired
	private MvcRouter mvcRouterUIDebug;

	@Autowired
	private MvcRouter mvcRouterUI;

	@Autowired
	private MvcRouter mvcRouterJAVA;

	@RequestMapping("/ajax/{module}/{function}")
	@ResponseBody
	public String handleAjax(
			@RequestParam(value = "callback", required = false) String callbackName,
			@PathVariable("module") String module,
			@PathVariable("function") String function,
			HttpServletRequest request, HttpServletResponse response) {

		return execute(callbackName, module, null, function, request, response);
	}

	@RequestMapping("/ajax/{module}/{subModule}/{function}")
	@ResponseBody
	public String handleAjax(
			@RequestParam(value = "callback", required = false) String callbackName,
			@PathVariable("module") String module,
			@PathVariable("subModule") String subModule,
			@PathVariable("function") String function,
			HttpServletRequest request, HttpServletResponse response) {

		return execute(callbackName, module, subModule, function, request,
				response);
	}

	private String execute(String callbackName, String module,
			String subModule, String function, HttpServletRequest request,
			HttpServletResponse response) {

		if (MvcConfig.crossDomain && StringUtils.isEmpty(callbackName)) {
			return prepareError("need param callback");
		}

		String result = "";
		try {
			switch (MvcRouterMode.getMode(MvcConfig.routerMode)) {
			case UI:
				result = mvcRouterUI.service(module, subModule, function,
						request, response);
				break;
			case UI_DEBUG:
				result = mvcRouterUIDebug.service(module, subModule, function,
						request, response);
				break;
			case JAVA:
				result = mvcRouterJAVA.service(module, subModule, function,
						request, response);
				break;
			default:
				result = mvcRouterUI.service(module, subModule, function,
						request, response);
				break;
			}
		} catch (MvcException e) {
			logger.error(e.getMessage());
			return prepareError(e.getMessage());
		}

		try {
			result = URLEncoder.encode(result, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// never
		}
		return (MvcConfig.crossDomain && !result.startsWith(callbackName)) ? (callbackName
				+ "(\"" + result + "\")")
				: "\"" + result + "\"";
	}

	private String prepareError(String err) {
		return "{\"error\":\"" + err + "\"}";
	}

}
