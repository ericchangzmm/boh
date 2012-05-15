package com.jije.boh.core.stateless.wrappers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jije.boh.core.stateless.ISessionBackend;
import com.jije.boh.core.stateless.ISessionData;
import com.jije.boh.core.stateless.session.SessionData;
import com.jije.boh.core.stateless.session.StatelessSession;

public class StatelessRequestWrapper extends HttpServletRequestWrapper {

	Logger logger = LoggerFactory.getLogger(StatelessRequestWrapper.class);

	HttpServletRequest originalRequest = null;
	HttpServletResponse originalResponse = null;
	StatelessSession session = null;
	ISessionBackend backend = null;

	public StatelessRequestWrapper(HttpServletRequest request,
			HttpServletResponse response, ISessionBackend backend) {
		super(request);
		logger.info("prepare StatelessRequestWrapper ... ");
		this.originalRequest = request;
		this.originalResponse = response;
		this.backend = backend;
	}

	public HttpSession getRealSession() {
		return super.getSession();
	}

	public HttpSession getRealSession(boolean create) {
		return super.getSession(create);
	}

	@Override
	public HttpSession getSession() {

		return getSession(true);
	}

	@Override
	public HttpSession getSession(boolean create) {

		if (session == null) {
			session = restoreSession();
		}

		if (session == null) {
			if (create)
				session = createSession(true);
		} else {
			if (!session.isValid())
				session.init(false);
		}
		
		session.setLastAccessedTime(System.currentTimeMillis());
		return session;
	}

	public void writeSession() throws IOException {

		logger.info("StatelessRequestWrapper writeSession ... ");

		if (session != null) {

			logger.info("StatelessRequestWrapper writeSession session is not null ");

			session.setNew(false);

			SessionData sessionData = new SessionData();
			sessionData.setId(session.getId());
			sessionData.setCreationTime(session.getCreationTime());
			sessionData.setValid(session.isValid());
			sessionData.setContent(session.getContent());

			backend.save(sessionData, originalRequest, originalResponse);
		}
	}

	/**
	 * 从backend中获取session, 若无则返回null
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private StatelessSession restoreSession() {

		StatelessSession s = null;

		ISessionData data = backend.get(originalRequest, originalResponse);

		if (data != null) {
			s = createSession(false);
			s.merge(data);
		}

		return s;
	}

	/**
	 * 新建一个StatelessSession
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private StatelessSession createSession(boolean newSession) {

		StatelessSession s = new StatelessSession(this);
		s.init(newSession);
		return s;
	}
}
