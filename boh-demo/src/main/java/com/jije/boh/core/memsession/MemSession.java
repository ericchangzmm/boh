package com.jije.boh.core.memsession;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jije.boh.core.memcached.MemcachedObjectType;
import com.jije.boh.core.memcached.SpyMemcachedClient;
import com.jije.boh.core.spring.SpringContextHolder;

public class MemSession {

	private String id;
	private Map<String, Object> content;
	private static SpyMemcachedClient memcachedClient = SpringContextHolder
			.getBean("spyMemcachedClient");

	private MemSession(String id, boolean create) {
		this.id = id;
		content = memcachedClient.get(MemcachedObjectType.SESSION.getPrefix()
				+ id);
		if (content == null) {
			if (create) {
				content = new HashMap<String, Object>(5);
				memcachedClient.set(MemcachedObjectType.SESSION.getPrefix()
						+ id, MemSessionConfig.COOKIE_MAXAGE, content);
			}
		}
	}

	public static MemSession getSession(String id) {

		return new MemSession(id, true);
	}

	public static MemSession getSession(String id, boolean create) {

		return new MemSession(id, create);
	}

	public void invalidate() {
		this.content.clear();
		memcachedClient.delete(MemcachedObjectType.SESSION.getPrefix() + id);
	}

	public Object getAttribute(String arg0) {
		return this.content.get(arg0);
	}

	public void removeAttribute(String arg0) {
		if (StringUtils.isEmpty(arg0)) {
			return;
		}
		this.content.remove(arg0);
		memcachedClient.set(MemcachedObjectType.SESSION.getPrefix() + id,
				MemSessionConfig.COOKIE_MAXAGE, content);
	}

	public void setAttribute(String arg0, Object arg1) {
		if (StringUtils.isEmpty(arg0) || arg1 == null) {
			return;
		}
		this.content.put(arg0, arg1);
		memcachedClient.set(MemcachedObjectType.SESSION.getPrefix() + id,
				MemSessionConfig.COOKIE_MAXAGE, content);
	}

}
