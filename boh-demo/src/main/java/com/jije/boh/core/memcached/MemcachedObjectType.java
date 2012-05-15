package com.jije.boh.core.memcached;

/**
 * 统一定义Memcached中存储的各种对象的Key前缀和超时时间(单位：秒).
 * 
 * @author eric.zhang
 * 
 */
public enum MemcachedObjectType {

	SESSION("session:", 60 * 60), USER("user:", 60 * 60 * 1);

	private String prefix;

	private int expiredTime;

	private MemcachedObjectType(String prefix, int expiredTime) {
		this.prefix = prefix;
		this.expiredTime = expiredTime;
	}

	public String getPrefix() {
		return prefix;
	}

	public int getExpiredTime() {
		return expiredTime;
	}

}
