package com.ufgov.ip.api.system;

import java.util.List;

import org.apache.solr.common.SolrDocumentList;

import com.ufgov.ip.entity.base.SolrEntity;
import com.ufgov.ip.entity.system.IpNotice;
import com.yonyou.iuap.search.query.pojo.SearchResult;

public interface NoticeServiceI {

	/**
	 * 保存公告
	 */
	
     public void saveNotice(IpNotice ipNotice);
     
     
     /**
      * 编辑公告
      * @param ipNotice
      */
     public void updateNotice(IpNotice ipNotice);


     /**
      * 获得所有公告
      */
     public List<IpNotice> getAllNotice(IpNotice ipNotice);

     
     
    /**
     * 获得公告详情
     * @param noticeId
     * @return
     */
	public IpNotice getNoticeDetail(String noticeId);


	/**
	 * 删除当前公告
	 * @param noticeId
	 */
	public void deleteNotice(String noticeId);
	
	
	/**
	 * 根据solr索引库的查询结果，重组结果集
	 * @param string
	 * @param keywords 
	 * @param linkman 
	 * @param isPublish 
	 * @param hirerId 
	 * @return
	 */
	public List<SolrEntity> getSolrNoticeList(String catelog, IpNotice ipNotice,boolean isPage);


	/**
	 * 通过关键字查找公告
	 * @param keywords
	 * @param isPublish 
	 * @param hirerId 
	 * @return
	 */
	public List<IpNotice> findBykeywords(IpNotice ipNotice);


	public Integer findRecordNum(IpNotice ipNotice);


	public List<IpNotice> getAllNoticeWithSP(IpNotice ipNotice);
	
	/**
	 * 获得solr索引中的公告数量(当前租户)
	 * @param index_catalog
	 * @param copyClass1
	 * @param searchResult
	 * @return
	 */
	public <T> Integer getAllSolrNoticeResult(String index_catalog, T copyClass1);
     
     
     
     
	
}
