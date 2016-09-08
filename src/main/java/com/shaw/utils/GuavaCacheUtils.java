package com.shaw.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class GuavaCacheUtils {

	public static final int CACHE_EXPIRE_TIME = 2 * 60 - 1;
	private LoadingCache<String, String> cache;

	public GuavaCacheUtils() {
		init();
	}

	private void init() {
		cache = CacheBuilder.newBuilder().softValues() // 使用SoftReference对象封装value,
				.expireAfterWrite(CACHE_EXPIRE_TIME, TimeUnit.MINUTES).build(new CacheLoader<String, String>() {
					@Override
					public String load(String key) throws Exception {
						return "";
					}
				});
	}

	public LoadingCache<String, String> getCache() {
		return cache;
	}

}
