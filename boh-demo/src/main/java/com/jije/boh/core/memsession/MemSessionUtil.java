package com.jije.boh.core.memsession;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jije.boh.core.utils.CookieUtils;

public class MemSessionUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(MemSessionUtil.class);

	private static final String SECURE_ALGORITHM = "SHA1PRNG";

	public static String generateId() {
		SecureRandom rand;
		try {
			rand = SecureRandom.getInstance(SECURE_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		byte[] data = new byte[32];
		rand.nextBytes(data);
		return new String(Hex.encodeHex(data)) + System.currentTimeMillis();
	}

	public static void setAttribute(String id, String key, Object value) {

		MemSession session = MemSession.getSession(id);
		session.setAttribute(key, value);
	}

	public static void setAttribute(HttpServletRequest request, String key,
			Object value) {

		Cookie cookie = getCookie(request);
		if (cookie != null) {
			String id = cookie.getValue();
			setAttribute(id, key, value);
		}
	}

	public static Object getAttribute(String id, String key) {
		MemSession session = MemSession.getSession(id);
		return session.getAttribute(key);
	}

	public static Object getAttribute(HttpServletRequest request, String key) {
		Cookie cookie = getCookie(request);
		if (cookie != null) {
			String id = cookie.getValue();
			return getAttribute(id, key);
		}
		return null;
	}

	public static void removeAttribute(String id, String key) {
		MemSession session = MemSession.getSession(id);
		session.removeAttribute(key);
	}

	public static void removeAttribute(HttpServletRequest request, String key) {
		Cookie cookie = getCookie(request);
		if (cookie != null) {
			String id = cookie.getValue();
			removeAttribute(id, key);
		}
	}

	public static void invalidate(HttpServletRequest request,
			HttpServletResponse response) {
		Cookie cookie = getCookie(request);
		if (cookie != null) {
			String id = cookie.getValue();
			MemSession session = MemSession.getSession(id);
			session.invalidate();
			setCookie(request, response, "", 0);
		}
	}

	public static Cookie getCookie(HttpServletRequest request) {
		try {
			return CookieUtils.getCookie(request, MemSessionConfig.COOKIE_NAME,
					MemSessionConfig.COOKIE_SIGN,
					MemSessionConfig.COOKIE_SIGN_KEY);
		} catch (SignatureException e) {
			logger.error("error in getCookie", e);
			return null;
		}
	}

	public static void setCookie(HttpServletRequest request,
			HttpServletResponse response, String value) {

		setCookie(request, response, value, MemSessionConfig.COOKIE_MAXAGE);
	}

	public static void setCookie(HttpServletRequest request,
			HttpServletResponse response, String value, int maxAge) {

		try {
			Cookie cookie = CookieUtils.createCookie(
					MemSessionConfig.COOKIE_NAME, value,
					MemSessionConfig.COOKIE_DOMAIN,
					MemSessionConfig.COOKIE_PATH, maxAge,
					MemSessionConfig.COOKIE_SIGN,
					MemSessionConfig.COOKIE_SIGN_KEY);
			response.addCookie(cookie);
		} catch (SignatureException e) {
			logger.error("error in setCookie[value=" + value + "]", e);
		}

	}
}
