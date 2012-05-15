package com.jije.boh.core.memcached;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.MemcachedClient;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

/**
 * 对SpyMemcached Client的二次封装,提供常用的Get/GetBulk/Set/Delete函数的同步与异步操作封装.
 * 
 * 未提供封装的函数可直接调用getMemcachedClient()取出Spy的原版MemcachedClient来使用.
 * 
 * @author eric.zhang
 * 
 */
@SuppressWarnings("unchecked")
public class SpyMemcachedClient implements DisposableBean {

	private static Logger logger = LoggerFactory
			.getLogger(SpyMemcachedClient.class);

	private MemcachedClient memcachedClient;

	private long shutdownTimeout = 2500;

	private long updateTimeout = 2500;

	/**
	 * Get方法, 转换结果类型并屏蔽异常, 仅返回Null.
	 */
	public <T> T get(String key) {

		logger.debug("get " + key + " from memcached.");

		try {
			return (T) memcachedClient.get(key);
		} catch (RuntimeException e) {
			handleException(e, key);
			return null;
		}
	}

	/**
	 * GetBulk方法, 转换结果类型并屏蔽异常.
	 */
	public <T> Map<String, T> getBulk(Collection<String> keys) {
		try {
			return (Map<String, T>) memcachedClient.getBulk(keys);
		} catch (RuntimeException e) {
			handleException(e, StringUtils.join(keys, ","));
			return null;
		}
	}

	/**
	 * 异步Set方法, 不考虑执行结果.
	 */
	public void set(String key, int expiredTime, Object value) {
		memcachedClient.set(key, expiredTime, value);
	}

	/**
	 * 安全的Set方法,在updateTimeout秒内返回结果, 否则返回false并取消操作.
	 */
	public boolean safeSet(String key, int expiredTime, Object value) {

		Future<Boolean> future = memcachedClient.set(key, expiredTime, value);
		try {
			return future.get(updateTimeout, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			logger.warn(
					"spymemcached client receive an exception in safeSet with key:"
							+ key, e);
			future.cancel(false);
		}
		return false;
	}

	/**
	 * 异步 Delete方法, 不考虑执行结果.
	 */
	public void delete(String key) {
		memcachedClient.delete(key);
	}

	/**
	 * 安全的Delete方法,在updateTimeout秒内返回结果, 否则返回false并取消操作.
	 */
	public boolean safeDelete(String key) {
		Future<Boolean> future = memcachedClient.delete(key);
		try {
			return future.get(updateTimeout, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			logger.warn(
					"spymemcached client receive an exception in safeDelete with key:"
							+ key, e);
			future.cancel(false);
		}
		return false;
	}

	private void handleException(Exception e, String key) {
		logger.warn("spymemcached client receive an exception with key:" + key,
				e);
	}

	@Override
	public void destroy() throws Exception {
		if (memcachedClient != null)
			memcachedClient.shutdown(shutdownTimeout, TimeUnit.MILLISECONDS);
	}

	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}

	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	public void setShutdownTimeout(long shutdownTimeout) {
		this.shutdownTimeout = shutdownTimeout;
	}

	public void setUpdateTimeout(long updateTimeout) {
		this.updateTimeout = updateTimeout;
	}

}
