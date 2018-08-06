package com.ufgov.ip.entity.sysmanager;

import java.io.Serializable;

public class IpUserAndCompanyEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5569983933671733965L;
	// id 
	private String staffId; 
	// 名称
	private String staffName;
	// 父节点
	private String staffPid;
	// 是否是人员 
	private String isUser;
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public String getStaffPid() {
		return staffPid;
	}
	public void setStaffPid(String staffPid) {
		this.staffPid = staffPid;
	}
	public String getIsUser() {
		return isUser;
	}
	public void setIsUser(String isUser) {
		this.isUser = isUser;
	}
	
	

}
