package com.jije.boh.core.stateless.session;

import java.util.HashMap;
import java.util.Map;

import com.jije.boh.core.stateless.ISessionData;

public class SessionData implements ISessionData {

	private static final long serialVersionUID = -2346745760414323089L;

	String id;
	long creationTime;
	boolean valid;
	Map<String, Object> content = new HashMap<String, Object>();

	public SessionData() {
	}

	@Override
	public Map<String, Object> getContent() {
		return content;
	}

	@Override
	public long getCreationTime() {
		return creationTime;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public boolean isValid() {
		return valid;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public void setContent(Map<String, Object> content) {
		this.content = content;
	}

}
