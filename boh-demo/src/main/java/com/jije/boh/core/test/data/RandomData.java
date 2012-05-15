package com.jije.boh.core.test.data;

import java.util.Random;

/**
 * 随机测试数据生成工具类
 * 
 * @author eric.zhang
 * 
 */
public class RandomData {

	private static Random random = new Random();

	private RandomData() {
	}

	/**
	 * @return 随机ID
	 */
	public static long randomId() {
		return random.nextLong();
	}

	/**
	 * @param prefix
	 * @return 随机字符串, prefix字符串+5位随机数字.
	 */
	public static String randomString(String prefix) {
		return prefix + random.nextInt(10000);
	}
}
