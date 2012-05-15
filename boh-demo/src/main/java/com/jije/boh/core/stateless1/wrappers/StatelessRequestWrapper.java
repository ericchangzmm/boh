package com.jije.boh.core.stateless1.wrappers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jije.boh.core.stateless1.ISessionBackend;
import com.jije.boh.core.stateless1.ISessionData;
import com.jije.boh.core.stateless1.session.SessionData;
import com.jije.boh.core.stateless1.session.StatelessSession;

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

		if (session == null) {
			logger.info("StatelessRequestWrapper session is null");
			try {
				session = createSession();
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}
		return session;
	}

	@Override
	public HttpSession getSession(boolean create) {
		if (create) {
			return getSession();
		}

		if (session == null) {
			try {
				session = restoreSession();
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}

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
	private StatelessSession restoreSession() throws NoSuchAlgorithmException {
		StatelessSession s = null;

		ISessionData data = backend.get(originalRequest, originalResponse);

		if (data != null) {
			s = new StatelessSession(this);
			s.init(false);
			s.merge(data);
		}

		return s;
	}

	/**
	 * 从backend中获取session, 若无则新建一个
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private StatelessSession createSession() throws NoSuchAlgorithmException {

		StatelessSession s = restoreSession();

		if (s == null) {
			s = new StatelessSession(this);
			s.init(true);
		}

		return s;
	}
}
