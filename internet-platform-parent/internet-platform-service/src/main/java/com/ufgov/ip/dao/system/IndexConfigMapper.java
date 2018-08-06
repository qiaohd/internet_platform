package com.ufgov.ip.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ufgov.ip.entity.system.IndexConfigEntity;
import com.yonyou.iuap.persistence.mybatis.anotation.MyBatisRepository;

@MyBatisRepository
public interface IndexConfigMapper {
	public IndexConfigEntity getIndexConfig(@Param(value = "catalog")String catalog);

	public void saveConfig(IndexConfigEntity indexConfigEntity);

	public void updateConfig(IndexConfigEntity indexConfigEntity);

	public IndexConfigEntity backShowIndexConfig(String configId);

	public void deleteIndexConfig(String configId);

	public List<IndexConfigEntity> getAllIndexConfig();

	public List<IndexConfigEntity> getAllIndexConfigByIsUse(String isUse);
	
}
