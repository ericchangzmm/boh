package com.jije.boh.core.stateless.backend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jije.boh.core.memcached.MemcachedObjectType;
import com.jije.boh.core.memcached.SpyMemcachedClient;
import com.jije.boh.core.stateless.ISessionData;
import com.jije.boh.core.stateless.session.CookieSupportData;

@Component("memcacheBackend")
public class MemcacheBackend extends CookieSupportBackend {

	private static Logger logger = LoggerFactory
			.getLogger(MemcacheBackend.class);

	private static final String SESSION_ID = "stateless.memcache.id";

	@Autowired
	private SpyMemcachedClient spyMemcachedClient;

	@Override
	public ISessionData get(HttpServletRequest request,
			HttpServletResponse response) {

		logger.info("memcachebackend get session ...");

		byte[] data = getCookieData(request, null);

		if (data != null) {
			String id = new String(data);
			logger.info("memcachebackend gotted session id from cookie : " + id);
			request.setAttribute(SESSION_ID, id);

			CookieSupportData s = (CookieSupportData) spyMemcachedClient
					.get(MemcachedObjectType.SESSION.getPrefix() + id);

			if (s != null
					&& s.getRemoteAddress().equals(request.getRemoteAddr())) {
				if (s.isValid())
					return s;
				else {
					deleteCookieData(request, response);
				}
			}
		}
		return null;
	}

	@Override
	public void save(ISessionData session, HttpServletRequest request,
			HttpServletResponse response) {

		logger.info("memcachebackend save session ...");

		if (session != null) {
			logger.info("memcachebackend save session and session is not null");
			CookieSupportData cookieData = new CookieSupportData(session);
			cookieData.setRemoteAddress(request.getRemoteAddr());

			spyMemcachedClient.safeSet(MemcachedObjectType.SESSION.getPrefix()
					+ session.getId(),
					MemcachedObjectType.SESSION.getExpiredTime(), cookieData);

			if (request.getAttribute(SESSION_ID) == null) {
				setCookieData(request, response, session.getId().getBytes());
			}
		} else {
			logger.info("memcachebackend save session and session is null");
			deleteCookieData(request, response);
		}
	}

	@Override
	public void destroy() {

	}

}
