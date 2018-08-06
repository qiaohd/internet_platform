package com.ufgov.ip.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ufgov.ip.entity.system.GenTable;
import com.ufgov.ip.entity.system.GenTableColumn;
import com.yonyou.iuap.persistence.mybatis.anotation.MyBatisRepository;
@MyBatisRepository
public interface GenDataBaseDictMapper {

	/**
	 * 获取单条数据
	 * @param id
	 * @return
	 */
	public GenTableColumn get(String id);
	
	/**
	 * 获取单条数据
	 * @param entity
	 * @return
	 */
	public GenTableColumn get(GenTableColumn entity);
	
	/**
	 * 查询数据列表，如果需要分页，请设置分页对象，如：entity.setPage(new Page<T>());
	 * @param entity
	 * @return
	 */
	public List<GenTableColumn> findList(GenTableColumn entity);
	
	/**
	 * 查询所有数据列表
	 * @param entity
	 * @return
	 */
	public List<GenTableColumn> findAllList(GenTableColumn entity);
	
	/**
	 * 查询所有数据列表
	 * @see public List<T> findAllList(T entity)
	 * @return
	 */
	@Deprecated
	public List<GenTableColumn> findAllList();
	
	/**
	 * 插入数据
	 * @param entity
	 * @return
	 */
	public int insert(GenTableColumn entity);
	
	/**
	 * 更新数据
	 * @param entity
	 * @return
	 */
	public int update(GenTableColumn entity);
	
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
	public int delete(GenTableColumn entity);
	
	


	/**
	 * 查询表列表
	 * @param genTable
	 * @return
	 */
	List<GenTable> findTableList(@Param("genTable") GenTable genTable,@Param("dbType") String dbType);

	/**
	 * 获取数据表字段
	 * @param genTable
	 * @return
	 */
	List<GenTableColumn> findTableColumnList(@Param("genTable") GenTable genTable,@Param("dbType") String dbType);
	
	/**
	 * 获取数据表主键
	 * @param genTable
	 * @return
	 */
	List<String> findTablePK(@Param("genTable") GenTable genTable,@Param("dbType") String dbType);
	

	
}
