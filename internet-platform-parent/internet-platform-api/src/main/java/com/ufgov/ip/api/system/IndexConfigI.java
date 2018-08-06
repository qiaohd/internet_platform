package com.ufgov.ip.api.system;

import java.util.List;

import com.ufgov.ip.entity.system.IndexConfigEntity;

public interface IndexConfigI {

	public IndexConfigEntity getIndexConfig(String catelog);

	public void saveIndexConfig(IndexConfigEntity indexConfigEntity);

	public IndexConfigEntity backShowIndexConfig(String configId);

	public void deleteIndexConfig(String configId);

	public List<IndexConfigEntity> getAllIndexConfig();
	
	public List<IndexConfigEntity> getAllIndexConfigByIsUse(String isUser);
	
}
