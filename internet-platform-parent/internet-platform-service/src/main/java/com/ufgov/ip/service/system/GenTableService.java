package com.ufgov.ip.service.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.system.GenTableServiceI;
import com.ufgov.ip.apiUtils.StringUtils;
import com.ufgov.ip.dao.baseimpl.IPDictDetailJdbcDaoImpl;
import com.ufgov.ip.dao.system.GenDataBaseDictMapper;
import com.ufgov.ip.dao.system.GenTableColumnMapper;
import com.ufgov.ip.dao.system.GenTableMapper;
import com.ufgov.ip.dao.system.IuapUserDao;
import com.ufgov.ip.entity.base.IpDictionaryDetail;
import com.ufgov.ip.entity.system.GenTable;
import com.ufgov.ip.entity.system.GenTableColumn;
import com.ufgov.ip.serviceutils.GenUtils;
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Service(version = "0.0.1")
public class GenTableService implements GenTableServiceI {
    @Value("${jdbc.type}")
	private String dbType;
	@Autowired
	private GenTableMapper genTableMapper;
	@Autowired
	private GenTableColumnMapper genTableColumnMapper;
	@Autowired
	private GenDataBaseDictMapper genDataBaseDictMapper;
	
	@Autowired
	private IuapUserDao userDao;
	
	@Autowired
	IPDictDetailJdbcDaoImpl iPDictDetailJdbcDao;
	
	public GenTable get(String id) {
		GenTable genTable = genTableMapper.get(id);
		GenTableColumn genTableColumn = new GenTableColumn();
		genTableColumn.setGenTable(new GenTable(genTable.getId()));
		genTable.setColumnList(genTableColumnMapper.findList(genTableColumn));
		return genTable;
	}
	
	public List<GenTable> find(GenTable genTable) {
		/*genTable.setPage(page);
		page.setList(genTableMapper.findList(genTable));*/
		return genTableMapper.findList(genTable);
	}

	public List<GenTable> findAll() {
		return genTableMapper.findAllList(new GenTable());
	}
	
	/**
	 * 获取物理数据表列表
	 * @param genTable
	 * @return
	 */
	public List<GenTable> findTableListFormDb(GenTable genTable){
		return genDataBaseDictMapper.findTableList(genTable,dbType);
	}
	
	/**
	 * 验证表名是否可用，如果已存在，则返回false
	 * @param genTable
	 * @return
	 */
	public boolean checkTableName(String tableName){
		if (StringUtils.isBlank(tableName)){
			return true;
		}
		GenTable genTable = new GenTable();
		genTable.setTableName(tableName);
		List<GenTable> list = genTableMapper.findListByName(genTable);
		return list.size() == 0;
	}
	
	/**
	 * 获取物理数据表列表
	 * @param genTable
	 * @return
	 */
	public GenTable getTableFormDb(GenTable genTable){
		// 如果有表名，则获取物理表
		if (StringUtils.isNotBlank(genTable.getTableName())){
			
			List<GenTable> list = genDataBaseDictMapper.findTableList(genTable,dbType);
			if (list.size() > 0){
				
				// 如果是新增，初始化表属性
				if (StringUtils.isBlank(genTable.getId())){
					genTable = list.get(0);
					// 设置字段说明
					if (StringUtils.isBlank(genTable.getTableComments())){
						genTable.setTableComments(genTable.getTableName());
					}
					genTable.setClassName(StringUtils.toCapitalizeCamelCase(genTable.getTableName()));
				}
				
				// 添加新列
				List<GenTableColumn> columnList = genDataBaseDictMapper.findTableColumnList(genTable,dbType);
				for (GenTableColumn column : columnList){
					boolean b = false;
					for (GenTableColumn e : genTable.getColumnList()){
						if (e.getColumnName().equals(column.getColumnName())){
							b = true;
						}
					}
					if (!b){
						genTable.getColumnList().add(column);
					}
				}
				
				// 删除已删除的列
				for (GenTableColumn e : genTable.getColumnList()){
					boolean b = false;
					for (GenTableColumn column : columnList){
						if (column.getColumnName().equals(e.getColumnName())){
							b = true;
						}
					}
					if (!b){
						e.setDelFlag(GenTableColumn.DEL_FLAG_DELETE);
					}
				}
				
				
				/*Iterator<GenTableColumn> sListIterator = genTable.getColumnList().iterator();  
				while(sListIterator.hasNext()){  
				    GenTableColumn e = sListIterator.next();  
				    if(e.getDelFlag().equals(GenTableColumn.DEL_FLAG_DELETE)){  
				    sListIterator.remove();  
				    }  
				} */
				
				// 获取主键
				genTable.setPkList(genDataBaseDictMapper.findTablePK(genTable,dbType));
				
				// 初始化列属性字段
				GenUtils.initColumnField(genTable);
				
			}
		}
		return genTable;
	}
	
	@Transactional(readOnly = false)
	public void save(GenTable genTable) {
		if (StringUtils.isBlank(genTable.getId())){
			genTable.preInsert(genTable.getCreateBy());
			genTableMapper.insert(genTable);
		}else{
			genTable.preUpdate();
			genTableMapper.update(genTable);
		}
		// 保存列
		for (GenTableColumn column : genTable.getColumnList()){
			column.setGenTable(genTable);
			if (StringUtils.isBlank(column.getId())){
				column.preInsert(genTable.getCreateBy());
				genTableColumnMapper.insert(column);
			}else{
				column.preUpdate();
				genTableColumnMapper.update(column);
			}
		}
	}
	
	
	public GenTable findByTableName(String tableName){
		
		GenTable genTable = new GenTable();
		genTable.setTableName(tableName);
	    GenTable genTableInfo = genTableMapper.findByTableName(genTable);
	    if(genTableInfo==null){
	    	return genTableInfo;
	    }
		GenTableColumn genTableColumn = new GenTableColumn();
		genTableColumn.setGenTable(new GenTable(genTableInfo.getId()));
		genTableInfo.setColumnList(genTableColumnMapper.findList(genTableColumn));
		return genTableInfo;
		
	}
	
	@Transactional(readOnly = false)
	public void delete(GenTable genTable) {
		genTableMapper.delete(genTable);
		genTableColumnMapper.deleteByGenTableId(genTable.getId());
	}

	
	
	@Override
	public List<IpDictionaryDetail> findDicAndDetail(String dicType,
			String dicName) {

		List<IpDictionaryDetail> ipDictionaryDetailList= iPDictDetailJdbcDao.findDicAndDetail(dicType,dicName);
		return ipDictionaryDetailList;
	}

	@Override
	public GenTable findByTableNameAndIsGen(GenTable genTable) {
		
	    GenTable genTableInfo = genTableMapper.findByTableNameAndIsGen(genTable);
	    if(genTableInfo==null){
	    	return genTableInfo;
	    }
		GenTableColumn genTableColumn = new GenTableColumn();
		genTableColumn.setGenTable(new GenTable(genTableInfo.getId()));
		genTableInfo.setColumnList(genTableColumnMapper.findList(genTableColumn));
		return genTableInfo;
	}

	@Override
	public GenTable findByTableNameAndIsGenAndIsUse(GenTable genTable) {
		 GenTable genTableInfo = genTableMapper.findByTableNameAndIsGen(genTable);
		    if(genTableInfo==null){
		    	return genTableInfo;
		    }
			GenTableColumn genTableColumn = new GenTableColumn();
			genTableColumn.setGenTable(new GenTable(genTableInfo.getId()));
			genTableInfo.setColumnList(genTableColumnMapper.findListByIsUse(genTableColumn));
			return genTableInfo;
	}
	
	
	
	@Override
	public GenTable findByTableNameAndIsGenAndAllUseAndHirerId(GenTable genTable) {
		 GenTable genTableInfo = genTableMapper.findByTableNameAndIsGenAndHirerId(genTable);
		    if(genTableInfo==null){
		    	return genTableInfo;
		    }
			GenTableColumn genTableColumn = new GenTableColumn();
			genTableColumn.setGenTable(new GenTable(genTableInfo.getId()));
			genTableInfo.setColumnList(genTableColumnMapper.findAllColumnList(genTableColumn));
			return genTableInfo;
	}
	
	
	@Override
	public GenTable findByTableNameAndIsGenAndHirerId(GenTable genTable) {
		 GenTable genTableInfo = genTableMapper.findByTableNameAndIsGenAndHirerId(genTable);
		    if(genTableInfo==null){
		    	return genTableInfo;
		    }
			GenTableColumn genTableColumn = new GenTableColumn();
			genTableColumn.setGenTable(new GenTable(genTableInfo.getId()));
			genTableInfo.setColumnList(genTableColumnMapper.findListByIsUse(genTableColumn));
			return genTableInfo;
	}
	
	
	@Transactional(readOnly = false)
	public void saveRestColumn(GenTable genTable) {
		if (StringUtils.isBlank(genTable.getId())){
			genTable.preInsert(genTable.getCreateBy());
			//genTableMapper.insert(genTable);
			genTableMapper.insertDeptCol(genTable);
		}else{
			genTable.preUpdate();
			genTableMapper.update(genTable);
			//genTableMapper.updateDeptCol(genTable);
		}
		// 保存列
		for (GenTableColumn column : genTable.getColumnList()){
			column.setGenTable(genTable);
			if (StringUtils.isBlank(column.getId())){
				column.preInsert(genTable.getCreateBy());
				genTableColumnMapper.insert(column);
			}else{
				column.preUpdate();
				genTableColumnMapper.updateByIsUse(column);
			}
		}
	}

	@Override
	public void updateDeptRestCol(List<GenTableColumn> restColumnList) {

		for (GenTableColumn genTableColumn : restColumnList) {
			   genTableColumn.preUpdate();
				genTableColumnMapper.updateByIsUse(genTableColumn);
			}
			
		}

	@Override
	public GenTable findByTableNameAndIsGenAndHirerIdWitoutSort(
			GenTable genTable) {
		 GenTable genTableInfo = genTableMapper.findByTableNameAndIsGenAndHirerId(genTable);
		    if(genTableInfo==null){
		    	return genTableInfo;
		    }
			GenTableColumn genTableColumn = new GenTableColumn();
			genTableColumn.setGenTable(new GenTable(genTableInfo.getId()));
			genTableInfo.setColumnList(genTableColumnMapper.findListByIsUseWitoutSort(genTableColumn));
			return genTableInfo;
	}

	@Override
	public GenTable findByTableNameAndGen(GenTable genTable) {
		
	    GenTable genTableInfo = genTableMapper.findByTableNameAndGen(genTable);
	    if(genTableInfo==null){
	    	return genTableInfo;
	    }
		GenTableColumn genTableColumn = new GenTableColumn();
		genTableColumn.setGenTable(new GenTable(genTableInfo.getId()));
		genTableInfo.setColumnList(genTableColumnMapper.findList(genTableColumn));
		return genTableInfo;
	}
	
}
