package com.ufgov.ip.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ufgov.ip.entity.system.GenScheme;
import com.yonyou.iuap.persistence.mybatis.anotation.MyBatisRepository;
@MyBatisRepository
public interface GenSchemeMapper {

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public GenScheme get(String id);
	
	/**
	 * 获取单条数据
	 * @param entity
	 * @return
	 */
	public GenScheme get(GenScheme entity);
	
	/**
	 * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
	 * @param entity
	 * @return
	 */
	public List<GenScheme> findList(@Param("genScheme") GenScheme genScheme, @Param("dbType") String dbType);
	
	/**
	 * 查询所有数据列表
	 * @param entity
	 * @return
	 */
	public List<GenScheme> findAllList(GenScheme entity);
	
	/**
	 * 查询所有数据列表
	 * @see public List<T> findAllList(T entity)
	 * @return
	 */
	@Deprecated
	public List<GenScheme> findAllList();
	
	/**
	 * 插入数据
	 * @param entity
	 * @return
	 */
	public int insert(GenScheme entity);
	
	/**
	 * 更新数据
	 * @param entity
	 * @return
	 */
	public int update(GenScheme entity);
	
	/**
	 * 删除数据（一般为逻辑删除，更新del_flag字段为1）
	 * @param id
	 * @see public int delete(T entity)
	 * @return
	 */
	@Deprecated
	public int delete(String id);
	
	/**
	 * 删除数据（一般为逻辑删除，更新del_flag字段为1）
	 * @param entity
	 * @return
	 */
	public int delete(GenScheme entity);
	
}
