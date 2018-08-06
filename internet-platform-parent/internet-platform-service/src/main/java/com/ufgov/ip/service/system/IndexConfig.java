package com.ufgov.ip.service.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.system.IndexConfigI;
import com.ufgov.ip.dao.system.IndexConfigMapper;
import com.ufgov.ip.entity.system.IndexConfigEntity;

@Service(version = "0.0.1")
public class IndexConfig implements IndexConfigI {

	@Autowired
	private IndexConfigMapper indexConfigMapper;
	
	
	@Override
	public IndexConfigEntity getIndexConfig(String catelog) {

		
		return null;
	}

	@Override
	public void saveIndexConfig(IndexConfigEntity indexConfigEntity) {

		if(indexConfigEntity.getConfigId()==null || "".equals(indexConfigEntity.getConfigId())){
			indexConfigEntity.preInsert();
			indexConfigMapper.saveConfig(indexConfigEntity);
		}else{
			
			indexConfigMapper.updateConfig(indexConfigEntity);
		}
	}
	
	@Override
	public IndexConfigEntity backShowIndexConfig(String configId) {
		return indexConfigMapper.backShowIndexConfig(configId);
	}
	
	
	
	@Override
	public void deleteIndexConfig(String configId) {

		indexConfigMapper.deleteIndexConfig(configId);
	}
	
	@Override
	public List<IndexConfigEntity> getAllIndexConfig() {
		return indexConfigMapper.getAllIndexConfig();
	}

	
	
	@Override
	public List<IndexConfigEntity> getAllIndexConfigByIsUse(String isUse) {
		// TODO 自动生成的方法存根
		return indexConfigMapper.getAllIndexConfigByIsUse(isUse);
	}
	
}
