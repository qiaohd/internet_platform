package com.ufgov.ip.dao.system;

import java.util.List;

import com.ufgov.ip.entity.system.GenTable;
import com.yonyou.iuap.persistence.mybatis.anotation.MyBatisRepository;
@MyBatisRepository
public interface GenTableMapper {



	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public GenTable get(String id);
	
	/**
	 * 获取单条数据
	 * @param entity
	 * @return
	 */
	public GenTable get(GenTable entity);
	
	/**
	 * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
	 * @param entity
	 * @return
	 */
	public List<GenTable> findList(GenTable entity);
	
	/**
	 * 按照名字全等查询
	 * @param entity
	 * @return
	 */
	public List<GenTable> findListByName(GenTable entity);
	
	
	public GenTable findByTableName(GenTable entity);
	
	/**
	 * 查询所有数据列表
	 * @param entity
	 * @return
	 */
	public List<GenTable> findAllList(GenTable entity);
	
	/**
	 * 查询所有数据列表
	 * @see public List<T> findAllList(T entity)
	 * @return
	 */
	@Deprecated
	public List<GenTable> findAllList();
	
	/**
	 * 插入数据
	 * @param entity
	 * @return
	 */
	public int insert(GenTable entity);
	
	/**
	 * 更新数据
	 * @param entity
	 * @return
	 */
	public int update(GenTable entity);
	
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
	public int delete(GenTable entity);

	public GenTable findByTableNameAndIsGen(GenTable genTable);

	public void insertDeptCol(GenTable genTable);

	public GenTable findByTableNameAndIsGenAndHirerId(GenTable genTable);

	public GenTable findByTableNameAndGen(GenTable genTable);

	

	
}
