package com.ufgov.ip.entity.system;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the ip_hirer database table.
 * 
 */
public class IpHirer implements Serializable {
	private static final long serialVersionUID = 1L;

	private String hirerId;

	private String address;

	private String cellphoneNo;

	private Date createDate;

	private String duty;

	private String email;

	private String fax;

	private String hirerLogoUrl;

	private String hirerName;

	private String hirerNo;

	private String hirerPicUrl;
	
	private String loginName;

	private String hirerShortName;

	private String hirerType;

	private String linkman;

	private Long loginTs;

	private String password;

	private String phoneNo;

	private String postcode;

	private String region;

	private String salt;

	private String sex;

	private Date updateDate;

	private String website;
	
	private String dataId;
	
	private String dbUrl;
	
	private String isValid;
	
	private String dbSchema;
	
	private String mycatSchema;
	
	private String areaName;

	public String getHirerId() {
		return hirerId;
	}

	public void setHirerId(String hirerId) {
		this.hirerId = hirerId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCellphoneNo() {
		return cellphoneNo;
	}

	public void setCellphoneNo(String cellphoneNo) {
		this.cellphoneNo = cellphoneNo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getHirerLogoUrl() {
		return hirerLogoUrl;
	}

	public void setHirerLogoUrl(String hirerLogoUrl) {
		this.hirerLogoUrl = hirerLogoUrl;
	}

	public String getHirerName() {
		return hirerName;
	}

	public void setHirerName(String hirerName) {
		this.hirerName = hirerName;
	}

	public String getHirerNo() {
		return hirerNo;
	}

	public void setHirerNo(String hirerNo) {
		this.hirerNo = hirerNo;
	}

	public String getHirerPicUrl() {
		return hirerPicUrl;
	}

	public void setHirerPicUrl(String hirerPicUrl) {
		this.hirerPicUrl = hirerPicUrl;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getHirerShortName() {
		return hirerShortName;
	}

	public void setHirerShortName(String hirerShortName) {
		this.hirerShortName = hirerShortName;
	}

	public String getHirerType() {
		return hirerType;
	}

	public void setHirerType(String hirerType) {
		this.hirerType = hirerType;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public Long getLoginTs() {
		return loginTs;
	}

	public void setLoginTs(Long loginTs) {
		this.loginTs = loginTs;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getDbSchema() {
		return dbSchema;
	}

	public void setDbSchema(String dbSchema) {
		this.dbSchema = dbSchema;
	}

	public String getMycatSchema() {
		return mycatSchema;
	}

	public void setMycatSchema(String mycatSchema) {
		this.mycatSchema = mycatSchema;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	

}