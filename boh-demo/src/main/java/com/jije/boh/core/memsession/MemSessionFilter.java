package com.jije.boh.core.memsession;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class MemSessionFilter implements Filter {

	// 配置cookie的keys
	private static final String PARAM_COOKIE_NAME = "cookiename";
	private static final String PARAM_COOKIE_PATH = "cookiepath";
	private static final String PARAM_COOKIE_DOMAIN = "cookiedomain";
	private static final String PARAM_COOKIE_MAX_AGE = "cookiemaxage";
	private static final String PARAM_COOKIE_SIGN = "cookiesign";
	private static final String PARAM_COOKIE_SIGN_KEY = "cookiesignkey";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		Cookie cookie = MemSessionUtil.getCookie(httpRequest);
		if (cookie != null) {
			String id = cookie.getValue();
			MemSessionUtil.setCookie(httpRequest, httpResponse, id);
		}

		chain.doFilter(httpRequest, httpResponse);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		String conf_cookieName = filterConfig
				.getInitParameter(PARAM_COOKIE_NAME);
		if (StringUtils.isNotEmpty(conf_cookieName))
			MemSessionConfig.COOKIE_NAME = conf_cookieName;

		String conf_cookiepath = filterConfig
				.getInitParameter(PARAM_COOKIE_PATH);
		if (StringUtils.isNotEmpty(conf_cookiepath))
			MemSessionConfig.COOKIE_PATH = conf_cookiepath;

		String conf_cookiedomain = filterConfig
				.getInitParameter(PARAM_COOKIE_DOMAIN);
		if (StringUtils.isNotEmpty(conf_cookiedomain))
			MemSessionConfig.COOKIE_DOMAIN = conf_cookiedomain;

		int conf_cookiemaxage = NumberUtils.toInt(
				filterConfig.getInitParameter(PARAM_COOKIE_MAX_AGE), 0);
		if (conf_cookiemaxage != 0)
			MemSessionConfig.COOKIE_MAXAGE = conf_cookiemaxage;

		String conf_cookiesign = filterConfig
				.getInitParameter(PARAM_COOKIE_SIGN);
		if (StringUtils.isNotEmpty(conf_cookiesign)) {
			MemSessionConfig.COOKIE_SIGN = Boolean.valueOf(conf_cookiesign);
		}

		String conf_cookiesignkey = filterConfig
				.getInitParameter(PARAM_COOKIE_SIGN_KEY);
		if (StringUtils.isNotEmpty(conf_cookiesignkey))
			MemSessionConfig.COOKIE_SIGN_KEY = conf_cookiesignkey;
	}

	@Override
	public void destroy() {

	}

}
