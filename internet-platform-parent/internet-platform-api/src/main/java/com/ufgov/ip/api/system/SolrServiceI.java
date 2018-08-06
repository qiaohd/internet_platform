package com.ufgov.ip.api.system;

import java.util.List;










import java.util.Map;

import org.apache.solr.common.SolrDocumentList;

import com.ufgov.ip.entity.base.SolrEntity;
import com.ufgov.ip.entity.system.IpMenu;
import com.ufgov.ip.entity.system.IpNotice;
import com.yonyou.iuap.search.query.pojo.SearchResult;

public interface SolrServiceI {
	public <T> List<SolrEntity> getSolrResult(String index_catalog,T copyClass1);
	public <T> Map<String,Object> getSolrResultInfo(String index_catalog, T copyClass1,SearchResult searchResult,boolean isPage);
	public <T> Map<String,Object> getAllSolrNoticeResult(String index_catalog, T copyClass1,SearchResult searchResult);
	public Map<String,Object> findSolrResult(String index_catalog,
			String keywords, String searchDate, String searchResult, String pageNo, String userId, String hirerId);
	
	public Integer findSolrResultNum(String index_catalog,
			String keywords, String searchDate, String searchResult, String pageNo, String userId, String hirerId, boolean isPage);
	
	/**
	 * 根据id删除索引库记录
	 * @param id
	 */
	public void deleteSolrIndex(String id);
	
	
	
}
