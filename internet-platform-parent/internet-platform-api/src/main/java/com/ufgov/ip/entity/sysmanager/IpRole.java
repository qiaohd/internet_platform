package com.ufgov.ip.entity.sysmanager;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the ip_role database table.
 * 
 */
@Entity
@Table(name="ip_role")
@NamedQuery(name="IpRole.findAll", query="SELECT i FROM IpRole i")
public class IpRole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ROLE_ID")
	private String roleId;

	@Column(name="CO_ID")
	private String coId;

	@Column(name="HIRER_ID")
	private String hirerId;

	@Column(name="ROLE_DESC")
	private String roleDesc;

	@Column(name="ROLE_NAME")
	private String roleName;

	@Column(name="ROLE_TYPE")
	private String roleType;
	
	@Column(name="DISP_ORDER")
	private int dispOrder;
	
	@Column(name="ROLE_CODE")
	private String roleCode;
	
	
	@Transient 
	private String parentRoleId;

	public IpRole() {
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getCoId() {
		return this.coId;
	}

	public void setCoId(String coId) {
		this.coId = coId;
	}

	public String getHirerId() {
		return this.hirerId;
	}

	public void setHirerId(String hirerId) {
		this.hirerId = hirerId;
	}

	public String getRoleDesc() {
		return this.roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleType() {
		return this.roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getParentRoleId() {
		return parentRoleId;
	}

	public void setParentRoleId(String parentRoleId) {
		this.parentRoleId = parentRoleId;
	}

	public int getDispOrder() {
		return dispOrder;
	}

	public void setDispOrder(int dispOrder) {
		this.dispOrder = dispOrder;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	
	
}