package com.ufgov.ip.entity.system;

import java.io.Serializable;
import java.util.Date;

import com.ufgov.ip.apiUtils.UUIDTools;
import com.ufgov.ip.entity.base.SolrEntity;

public class IpNotice implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String noticeId; //公告id
	private String hirerId;  //租户id
	private String attachmentUrl;//'附件路径'
	private String noticeTitle; // '公告标题'
	private String noticeContent;//'公告内容'
	private String isPublish; //'是否发布(0-未发布，只保存,1-发布)'
	private Date createDate;  //创建时间
	private Date updateDate;  //更新时间
	private String linkman;
	private Integer count;
	private String keywords;//关键字
	private Integer startNum;//分页查询的第一条记录数
	private Integer endNum;//分页查询的最后一条记录数
	private String pageNo;//当前页数
	private String isPage;//是否分页
	public static final String NOTICE_ISPUBLISH = "1";
	
	public String getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	public String getHirerId() {
		return hirerId;
	}
	public void setHirerId(String hirerId) {
		this.hirerId = hirerId;
	}
	public String getAttachmentUrl() {
		return attachmentUrl;
	}
	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}
	public String getNoticeTitle() {
		return noticeTitle;
	}
	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}
	public String getNoticeContent() {
		return noticeContent;
	}
	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}
	public String getIsPublish() {
		return isPublish;
	}
	public void setIsPublish(String isPublish) {
		this.isPublish = isPublish;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	
	
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public void preInsert(){
		setNoticeId(UUIDTools.uuidRandom());
		this.createDate = new Date();
		this.updateDate = this.createDate;
	}
	
	public void preUpdate(){
		this.updateDate = new Date();
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public Integer getStartNum() {
		return startNum;
	}
	public void setStartNum(Integer startNum) {
		this.startNum = startNum;
	}
	public Integer getEndNum() {
		return endNum;
	}
	public void setEndNum(Integer endNum) {
		this.endNum = endNum;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public String getIsPage() {
		return isPage;
	}
	public void setIsPage(String isPage) {
		this.isPage = isPage;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
