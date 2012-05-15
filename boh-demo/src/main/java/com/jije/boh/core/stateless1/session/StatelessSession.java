package com.jije.boh.core.stateless1.session;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.NotImplementedException;

import com.jije.boh.core.stateless1.ISessionData;
import com.jije.boh.core.stateless1.wrappers.StatelessRequestWrapper;

@SuppressWarnings("deprecation")
public class StatelessSession implements HttpSession, Serializable {

	private static final long serialVersionUID = 320871471406585728L;

	private static final String SECURE_ALGORITHM = "SHA1PRNG";

	private HashMap<String, Object> content;
	// 创建时间
	private long creationTime;
	private String id;
	private final long lastAccessedTime = 0;
	// session最大空闲时间 即timeout 单位：秒
	private int maxInactiveInterval = 2 * 60;
	private boolean valid;
	private boolean isNew;
	private final StatelessRequestWrapper request;
	

	public StatelessSession(StatelessRequestWrapper request) {
		this.request = request;
	}

	public void init(boolean newSession) throws NoSuchAlgorithmException {

		if (newSession) {
			SecureRandom rand = SecureRandom.getInstance(SECURE_ALGORITHM);
			byte[] data = new byte[32];
			rand.nextBytes(data);
			creationTime = System.currentTimeMillis();
			id = new String(Hex.encodeHex(data)) + creationTime;
			valid = true;
		}

		content = new HashMap<String, Object>();
		isNew = newSession;
	}

	public void merge(ISessionData data) {
		content.putAll(data.getContent());
		id = data.getId();
		valid = data.isValid();
		creationTime = data.getCreationTime();
	}
	
	public void setNew(boolean b) {
		this.isNew = b;
	}

	public boolean isValid() {
		return valid;
	}

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
	public long getLastAccessedTime() {
		return lastAccessedTime;
	}

	@Override
	public ServletContext getServletContext() {
		return request.getSession().getServletContext();
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		this.maxInactiveInterval = interval;
	}

	@Override
	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	@Override
	@Deprecated
	public HttpSessionContext getSessionContext() {
		throw new NotImplementedException();
	}

	@Override
	public Object getAttribute(String name) {
		return content.get(name);
	}

	@Override
	@Deprecated
	public Object getValue(String name) {
		return content.get(name);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Enumeration getAttributeNames() {
		return IteratorUtils.asEnumeration(content.keySet().iterator());
	}

	@Override
	@Deprecated
	public String[] getValueNames() {
		return (String[]) content.keySet().toArray();
	}

	@Override
	public void setAttribute(String name, Object value) {
		content.put(name, value);
	}

	@Override
	@Deprecated
	public void putValue(String name, Object value) {
		content.put(name, value);
	}

	@Override
	public void removeAttribute(String name) {
		content.remove(name);
	}

	@Override
	@Deprecated
	public void removeValue(String name) {
		content.remove(name);
	}

	@Override
	public void invalidate() {
		valid = false;
		content = new HashMap<String, Object>();
	}

	@Override
	public boolean isNew() {
		return isNew;
	}

}
