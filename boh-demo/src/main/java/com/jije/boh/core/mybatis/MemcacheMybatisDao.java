package com.jije.boh.core.mybatis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jije.boh.core.entity.IdEntity;
import com.jije.boh.core.mapper.JsonMapper;
import com.jije.boh.core.memcached.SpyMemcachedClient;

/**
 * 用mybatis对数据库操作 使用memcached的缓存扩展
 * 缓存设计分为两个层次
 * 	第一层：cacheKey+entity.id====>entity
 * 	第二层：cachekey+查询条件根据一定算法或者自定义产生的key=====>符合条件的ids
 * 
 * 	使用第二层缓存时先查到符合条件的ids 再用batchSelectByIdCached方法获取实体列表；
 * 	batchSelectByIdCached方法会先根据id从第一层缓存中获取，没有再从数据库获取并且添加到第一层缓存
 * 
 *  这样做的好处：我们只需要维护第一层缓存 即在用户update 或者delete时同时修改第一层缓存即可，不用维护第二层缓存
 *  
 * @author eric.zhang
 *
 * @param <T>
 */
public abstract class MemcacheMybatisDao<T extends IdEntity> extends
		BaseMybatisDao<T> {

	@Autowired
	protected SpyMemcachedClient memcachedClient;

	public void updateCached(T t, String cacheKey, int cacheExpiredTime,
			JsonMapper jsonMapper) {

		update(t);

		memcachedClient.set(cacheKey + t.getId(), cacheExpiredTime,
				jsonMapper.toJson(t));
	}

	public void deleteCached(long id, String cacheKey) {

		delete(id);

		memcachedClient.delete(cacheKey + id);
	}

	public T selectByIdCached(long id, String cacheKey, int cacheExpiredTime,
			JsonMapper jsonMapper) {

		String jsonString = memcachedClient.get(cacheKey + id);

		T t = null;
		if (StringUtils.isNotEmpty(jsonString)) {
			t = jsonMapper.fromJson(jsonString, entityClass);
		} else {
			t = selectById(id);
			if (t != null)
				memcachedClient.set(cacheKey + id, cacheExpiredTime,
						jsonMapper.toJson(t));
		}

		return t;
	}

	@SuppressWarnings("serial")
	public List<T> batchSelectByIdCached(List<Long> ids, String cacheKey,
			int cacheExpiredTime, JsonMapper jsonMapper) {

		if (ids == null || ids.size() == 0)
			return null;

		final int ids_size = ids.size();
		List<T> result = new ArrayList<T>(ids_size) {
			{
				for (int i = 0; i < ids_size; i++) {
					add(null);
				}
			}
		};
		List<Long> no_cache_ids = new ArrayList<Long>();

		for (int i = 0; i < ids_size; i++) {
			long id = ids.get(i);

			String jsonString = memcachedClient.get(cacheKey + id);

			if (StringUtils.isEmpty(jsonString)) {
				no_cache_ids.add(id);
			} else {
				result.set(i, jsonMapper.fromJson(jsonString, entityClass));
			}
		}

		if (no_cache_ids.size() > 0) {
			List<T> no_cache_prjs = batchSelectById(no_cache_ids);

			if (no_cache_prjs != null) {
				for (T t : no_cache_prjs) {
					memcachedClient.set(cacheKey + t.getId(), cacheExpiredTime,
							jsonMapper.toJson(t));
					result.set(ids.indexOf(t.getId()), t);
				}
			}

		}

		return result;
	}

}
