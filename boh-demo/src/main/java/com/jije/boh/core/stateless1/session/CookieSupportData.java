package com.jije.boh.core.stateless1.session;

import java.util.HashMap;

import com.jije.boh.core.stateless1.ISessionData;

public class CookieSupportData extends SessionData {

	private static final long serialVersionUID = 9191982916370728677L;

	private String remoteAddress;

	public CookieSupportData() {
	}

	public CookieSupportData(ISessionData session) {
		content = new HashMap<String, Object>();
		content.putAll(session.getContent());

		creationTime = session.getCreationTime();
		id = session.getId();

		valid = session.isValid();
	}

	public CookieSupportData(String sessionId) {
		this.id = sessionId;
		creationTime = System.currentTimeMillis();
		content = new HashMap<String, Object>();
		valid = true;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

}
