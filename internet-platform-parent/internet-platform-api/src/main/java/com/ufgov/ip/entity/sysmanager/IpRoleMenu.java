package com.ufgov.ip.entity.sysmanager;
 
 import java.io.Serializable;
 import javax.persistence.*;
 
 
 /**
  * The persistent class for the ip_role_menu database table.
  * 
  */
// @Entity
// @Table(name="ip_role_menu")
// @NamedQuery(name="IpRoleMenu.findAll", query="SELECT i FROM IpRoleMenu i")
 public class IpRoleMenu implements Serializable {
 	private static final long serialVersionUID = 1L;
 
// 	@Id
// 	@Column(name="THE_ID")
 	private String theId;
 
// 	@Column(name="MENU_ID")
 	private String menuId;
 
// 	@Column(name="MENU_NAME")
 	private String menuName;
 
// 	@Column(name="ROLE_ID")
 	private String roleId;
 
 	public IpRoleMenu() {
 	}
 
 	public String getTheId() {
 		return this.theId;
 	}
 
 	public void setTheId(String theId) {
 		this.theId = theId;
 	}
 
 	public String getMenuId() {
 		return this.menuId;
 	}
 
 	public void setMenuId(String menuId) {
 		this.menuId = menuId;
 	}
 
 	public String getMenuName() {
 		return this.menuName;
 	}
 
 	public void setMenuName(String menuName) {
 		this.menuName = menuName;
 	}
 
 	public String getRoleId() {
 		return this.roleId;
 	}
 
 	public void setRoleId(String roleId) {
 		this.roleId = roleId;
 	}
 
 }