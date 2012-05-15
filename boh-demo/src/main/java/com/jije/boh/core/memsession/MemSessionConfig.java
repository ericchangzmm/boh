package com.jije.boh.core.memsession;

import com.jije.boh.core.memcached.MemcachedObjectType;

public class MemSessionConfig {

	private MemSessionConfig() {
	}

	public static String COOKIE_NAME = "session";
	public static String COOKIE_DOMAIN = null;
	public static int COOKIE_MAXAGE = MemcachedObjectType.SESSION.getExpiredTime();
	public static String COOKIE_PATH = "/";
	public static boolean COOKIE_SIGN = false;
	public static String COOKIE_SIGN_KEY = "";
}
