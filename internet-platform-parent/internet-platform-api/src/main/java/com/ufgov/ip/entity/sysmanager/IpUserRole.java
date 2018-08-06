package com.ufgov.ip.entity.sysmanager;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ip_user_role database table.
 * 
 */
@Entity
@Table(name="ip_user_role")
@NamedQuery(name="IpUserRole.findAll", query="SELECT i FROM IpUserRole i")
public class IpUserRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="THE_ID")
	private String theId;

	@Column(name="CO_ID")
	private String coId;

	@Column(name="IS_PART_TIME")
	private String isPartTime;

	@Column(name="ROLE_ID")
	private String roleId;

	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="DISP_ORDER")
	private int dispOrder;
	
	@Transient 
	private String userName;
	@Transient 
	private String coName;
	@Transient
	private String roleName;
	@Transient
	private String loginName;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCoName() {
		return coName;
	}

	public void setCoName(String coName) {
		this.coName = coName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public IpUserRole() {
	}

	public String getTheId() {
		return this.theId;
	}

	public void setTheId(String theId) {
		this.theId = theId;
	}

	public String getCoId() {
		return this.coId;
	}

	public void setCoId(String coId) {
		this.coId = coId;
	}

	public String getIsPartTime() {
		return this.isPartTime;
	}

	public void setIsPartTime(String isPartTime) {
		this.isPartTime = isPartTime;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getDispOrder() {
		return dispOrder;
	}

	public void setDispOrder(int dispOrder) {
		this.dispOrder = dispOrder;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

}