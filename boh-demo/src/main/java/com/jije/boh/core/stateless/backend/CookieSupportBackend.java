package com.jije.boh.core.stateless.backend;

import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jije.boh.core.stateless.ISessionBackend;
import com.jije.boh.core.stateless.ISessionData;
import com.jije.boh.core.utils.CookieUtils;

/**
 * 需要cookie支持的session 处理后端
 * 
 * @author eric.zhang
 * 
 */
public abstract class CookieSupportBackend implements ISessionBackend {

	private static Logger logger = LoggerFactory
			.getLogger(CookieSupportBackend.class);

	// 配置cookie的keys
	private static final String PARAM_COOKIE_DOMAIN = "cookiedomain";
	private static final String PARAM_COOKIE_MAX_AGE = "cookiemaxage";
	private static final String PARAM_COOKIE_NAME = "cookiename";
	private static final String PARAM_COOKIE_PATH = "cookiepath";

	// 单个cookie最大长度
	private static final int COOKIE_MAX_SIZE = 3000;
	// request的attribute中存放cookie个数的key
	private String REQ_ATTR_COUNT = "stateless.session.count";

	protected String cookieName = "session";
	protected String domain = null;
	protected Integer maxAge = null;
	protected String path = "/";

	public abstract ISessionData get(HttpServletRequest request,
			HttpServletResponse response);

	public abstract void save(ISessionData session, HttpServletRequest request,
			HttpServletResponse response);

	@Override
	public void init(Map<String, String> config) {

		logger.info("backend init config ...");

		String name = config.get(PARAM_COOKIE_NAME);
		if (!StringUtils.isEmpty(name)) {
			setCookieName(name);
		}

		String path = config.get(PARAM_COOKIE_PATH);
		if (!StringUtils.isEmpty(path)) {
			this.path = path;
		}
		String domain = config.get(PARAM_COOKIE_DOMAIN);
		if (!StringUtils.isEmpty(domain)) {
			this.domain = domain;
		}
		String maxAge = config.get(PARAM_COOKIE_MAX_AGE);
		if (!StringUtils.isEmpty(maxAge)) {
			this.maxAge = new Integer(Integer.parseInt(maxAge));
		}
	}

	protected void setCookieName(String name) {
		this.cookieName = name;
		REQ_ATTR_COUNT = "stateless." + cookieName + ".count";
	}

	protected Cookie createCookie(String name, String content) {
		try {
			return CookieUtils
					.createCookie(name, content, domain, path, maxAge);
		} catch (SignatureException e) {
			logger.error("Error creating cookie", e);
		}
		return null;
	}

	protected Cookie getCookie(HttpServletRequest request, String name) {
		try {
			return CookieUtils.getCookie(request, name);
		} catch (SignatureException e) {
			logger.error("Error getting cookie", e);
		}
		return null;
	}

	protected byte[] getCookieData(HttpServletRequest request,
			HttpServletResponse response) {
		int i = 0;
		Cookie c = null;
		StringBuilder data = new StringBuilder();

		while ((c = getCookie(request, cookieName + i)) != null) {
			data.append(c.getValue());
			i++;
		}

		request.setAttribute(REQ_ATTR_COUNT, new Integer(i));

		String dataString = data.toString();
		if (dataString.length() == 0) {
			return null;
		}
		return Base64.decodeBase64(dataString);
	}

	protected void setCookieData(HttpServletRequest request,
			HttpServletResponse response, byte[] data) {
		String encoded = "";
		if (data != null) {
			encoded = new String(Base64.encodeBase64(data));
		}

		ArrayList<String> splittedData = new ArrayList<String>();
		while (encoded.length() > COOKIE_MAX_SIZE) {
			splittedData.add(encoded.substring(0, COOKIE_MAX_SIZE));
			encoded = encoded.substring(COOKIE_MAX_SIZE);
		}
		if (encoded.length() > 0) {
			splittedData.add(encoded);
		}

		int i = 0;
		Cookie c = null;
		for (String datapart : splittedData) {
			c = createCookie(cookieName + i, datapart);
			response.addCookie(c);
			i++;
		}

		// Clear no longer used segments.
		int previousCount = ((Integer) request.getAttribute(REQ_ATTR_COUNT))
				.intValue();
		while (i < previousCount) {
			c = createCookie(cookieName + i, StringUtils.EMPTY);
			response.addCookie(c);
			i++;
		}
	}

	protected void deleteCookieData(HttpServletRequest request,
			HttpServletResponse response) {

		int previousCount = ((Integer) request.getAttribute(REQ_ATTR_COUNT))
				.intValue();

		int i = 0;
		Cookie c = null;
		while (i < previousCount) {
			c = createCookie(cookieName + i, StringUtils.EMPTY);
			response.addCookie(c);
			i++;
		}
	}

}
