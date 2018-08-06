package com.ufgov.ip.entity.system;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * The persistent class for the ip_menu database table.
 * 
 */
//@Entity
//@Table(name="ip_menu")
//@NamedQuery(name="IpMenu.findAll", query="SELECT i FROM IpMenu i")
public class IpMenu implements Serializable {
	private static final long serialVersionUID = 1L;

//	@Id
//	@Column(name="MENU_ID")
	private String menuId;

//	@Column(name="AUTH_LEVEL")
	private String authLevel;

//	@Column(name="DISP_ORDER")
	private int dispOrder;

//	@Column(name="IS_JUMP")
	private String isJump;

//	@Column(name="IS_LEAF")
	private String isLeaf;

//	@Column(name="IS_SHOW")
	private String isShow;

//	@Column(name="LEVEL_NUM")
	private String levelNum;

//	@Column(name="MENU_DESC")
	private String menuDesc;

//	@Column(name="MENU_NAME")
	private String menuName;

//	@Column(name="PARENT_MENU_ID")
	private String parentMenuId;

//	@Column(name="PARENT_MENU_NAME")
	private String parentMenuName;

	private String url;
	
//	@Column(name="MENU_LOGO")
	private String menuLogo;
	
	private String permission;
	
	@Transient
	private List<IpMenu> children;

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public List<IpMenu> getChildren() {
		return children;
	}

	public void setChildren(List<IpMenu> children) {
		this.children = children;
	}

	public IpMenu() {
	}

	public String getMenuId() {
		return this.menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getAuthLevel() {
		return this.authLevel;
	}

	public void setAuthLevel(String authLevel) {
		this.authLevel = authLevel;
	}

	public int getDispOrder() {
		return this.dispOrder;
	}

	public void setDispOrder(int dispOrder) {
		this.dispOrder = dispOrder;
	}

	public String getIsJump() {
		return this.isJump;
	}

	public void setIsJump(String isJump) {
		this.isJump = isJump;
	}

	public String getIsLeaf() {
		return this.isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getIsShow() {
		return this.isShow;
	}

	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}

	public String getLevelNum() {
		return this.levelNum;
	}

	public void setLevelNum(String levelNum) {
		this.levelNum = levelNum;
	}

	public String getMenuDesc() {
		return this.menuDesc;
	}

	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
	}

	public String getMenuName() {
		return this.menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getParentMenuId() {
		return this.parentMenuId;
	}

	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	public String getParentMenuName() {
		return this.parentMenuName;
	}

	public void setParentMenuName(String parentMenuName) {
		this.parentMenuName = parentMenuName;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMenuLogo() {
		return menuLogo;
	}

	public void setMenuLogo(String menuLogo) {
		this.menuLogo = menuLogo;
	}
	
	

}