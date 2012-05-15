package com.jije.boh.core.mybatis;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.jije.boh.core.entity.IdEntity;

@SuppressWarnings("unchecked")
public abstract class BaseMybatisDao<T extends IdEntity> extends
		SqlSessionDaoSupport {

	protected Class<T> entityClass;

	public BaseMybatisDao() {
		entityClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected abstract String getNamespace();

	protected static final String PAGE_OFFSET = "pageOffset";
	protected static final String PAGE_SIZE = "pageSize";
	protected static final String ORDER_STRING = "orderString";

	/**
	 * 插入记录 xxxmapper.xml id="insert"
	 * 
	 * @param t
	 * @return
	 */
	public Long insert(T t) {
		getSqlSession().insert(getStatement("insert"), t);
		return t.getId();
	}

	/**
	 * 修改记录 xxxmapper.xml id="update"
	 * 
	 * @param t
	 */
	public void update(T t) {
		getSqlSession().update(getStatement("update"), t);
	}

	/**
	 * 根据id删除记录 xxxmapper.xml id="delete"
	 * 
	 * @param id
	 */
	public void delete(long id) {
		getSqlSession().delete(getStatement("delete"), id);
	}

	/**
	 * 根据id查找记录记录，返回单条记录 xxxmapper.xml id="selectById"
	 * 
	 * @param id
	 * @return
	 */
	public T selectById(long id) {
		return getSqlSession().selectOne(getStatement("selectById"), id);
	}

	/**
	 * 根据ids批量查找记录 xxxmapper.xml id="batchSelectById"
	 * 
	 * @param ids
	 * @return
	 */
	public List<T> batchSelectById(List<Long> ids) {
		if (ids == null || ids.size() == 0)
			return null;
		return getSqlSession().selectList(getStatement("batchSelectById"), ids);
	}

	/**
	 * 统计总条数 xxxmapper.xml id="count"
	 * 
	 * @param parameter
	 * @return
	 */
	public long count(Map<String, Object> parameter) {
		return (Long) getSqlSession().selectOne(getStatement("count"),
				parameter);
	}

	/**
	 * 查询并分页 xxxmapper.xml id="selectPage"
	 * 
	 * @param pageable
	 * @param parameter
	 * @return
	 */
	public Page<T> selectPage(final Pageable pageable,
			Map<String, Object> parameter) {

		Long count = count(parameter);
		List<T> list = getSqlSession().selectList(getStatement("selectPage"),
				applyPagination(parameter, pageable));

		return new PageImpl<T>(list, pageable, count);
	}

	/**
	 * 查询 xxxmapper.xml id="select"
	 * 
	 * @param parameter
	 * @return
	 */
	public Iterable<T> select(Map<String, Object> parameter) {

		return getSqlSession().selectList(getStatement("select"), parameter);
	}

	/**
	 * 查询并排序 xxxmapper.xml id="select"
	 * 
	 * @param parameter
	 * @param sort
	 * @return
	 */
	public Iterable<T> select(Map<String, Object> parameter, Sort sort) {

		return getSqlSession().selectList(getStatement("select"),
				applySorting(parameter, sort));
	}

	protected Map<String, Object> applySorting(Map<String, Object> parameter,
			Sort sort) {

		if (sort == null)
			return parameter;

		StringBuilder sb = new StringBuilder();
		for (Order order : sort) {
			sb.append("," + order.getProperty() + " " + order.getDirection());
		}
		parameter.put(ORDER_STRING, sb.substring(1));

		return parameter;
	}

	protected Map<String, Object> applyPagination(
			Map<String, Object> parameter, Pageable pageable) {

		if (pageable == null)
			return parameter;

		parameter.put(PAGE_OFFSET, pageable.getOffset());
		parameter.put(PAGE_SIZE, pageable.getPageSize());

		return applySorting(parameter, pageable.getSort());
	}

	protected String getStatement(String id) {
		return getNamespace() + "." + id;
	}
}
