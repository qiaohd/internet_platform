package com.ufgov.ip.entity.base;

import java.io.Serializable;

public class SolrEntity implements Serializable{
	
	
	private static final long serialVersionUID = 1L;

    private String index_catalog;//搜索类别
    private String keywords;
    private String name;//最终将索引信息name赋予该属性
    private String content;//最终将索引content赋予该属性
    private String searchDate;
    private String searchResult;
    private String searchUrl;//查询接口地址
    private String routerUrl;//路由地址
    private String createDate;//创建日期
    private String upDateDate;//更新日期
    private String linkMan;//创建人
    private String pk;//主键
    private String allCount;//总记录数
    
    private String params;//查询参数
	public String getIndex_catalog() {
		return index_catalog;
	}
	public void setIndex_catalog(String index_catalog) {
		this.index_catalog = index_catalog;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getSearchDate() {
		return searchDate;
	}
	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}
	public String getSearchResult() {
		return searchResult;
	}
	public void setSearchResult(String searchResult) {
		this.searchResult = searchResult;
	}
	public String getSearchUrl() {
		return searchUrl;
	}
	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getRouterUrl() {
		return routerUrl;
	}
	public void setRouterUrl(String routerUrl) {
		this.routerUrl = routerUrl;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUpDateDate() {
		return upDateDate;
	}
	public void setUpDateDate(String upDateDate) {
		this.upDateDate = upDateDate;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getAllCount() {
		return allCount;
	}
	public void setAllCount(String allCount) {
		this.allCount = allCount;
	}
	
	
	
	
	
}
