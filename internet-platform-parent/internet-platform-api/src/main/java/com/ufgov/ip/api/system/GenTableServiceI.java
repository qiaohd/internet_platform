package com.ufgov.ip.api.system;

import java.util.List;

import com.ufgov.ip.entity.base.IpDictionaryDetail;
import com.ufgov.ip.entity.system.GenTable;
import com.ufgov.ip.entity.system.GenTableColumn;

public interface GenTableServiceI {

	public GenTable get(String id);
	
	public List<GenTable> find(GenTable genTable);

	public List<GenTable> findAll();
	
	/**
	 * 获取物理数据表列表
	 * @param genTable
	 * @return
	 */
	public List<GenTable> findTableListFormDb(GenTable genTable);
	
	/**
	 * 验证表名是否可用，如果已存在，则返回false
	 * @param genTable
	 * @return
	 */
	public boolean checkTableName(String tableName);
	
	/**
	 * 获取物理数据表列表
	 * @param genTable
	 * @return
	 */
	public GenTable getTableFormDb(GenTable genTable);
	public void save(GenTable genTable);
	public void delete(GenTable genTable);
	
	public GenTable findByTableName(String tableName);

	public List<IpDictionaryDetail> findDicAndDetail(String dicType, String dicName);

	public GenTable findByTableNameAndIsGen(GenTable genTable);

	public GenTable findByTableNameAndIsGenAndIsUse(GenTable genTable);

	public void saveRestColumn(GenTable genTable);

	public GenTable findByTableNameAndIsGenAndAllUseAndHirerId(GenTable genTable);

	public GenTable findByTableNameAndIsGenAndHirerId(GenTable genTable);
	
	public void updateDeptRestCol(List<GenTableColumn> restColumnList);

	public GenTable findByTableNameAndIsGenAndHirerIdWitoutSort(
			GenTable genTable);

	public GenTable findByTableNameAndGen(GenTable genTable);
	
	
}
