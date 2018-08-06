package com.ufgov.ip.entity.system;

import java.io.Serializable;
import java.util.Date;

import com.ufgov.ip.apiUtils.UUIDTools;

public class IndexConfigEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String configId;//主键
	private String menuName;//权限菜单名称
	private String menuId;//权限菜单id
	private String catalog;//分类
	private String routerAddr;//路由地址
	private String interfaceAddr;//接口地址
	private String interParam;//参数
	private String isUse;//是否启用  Y/N
	private String className;
	private String isHirer;
	private String isUser;
	
	public String getConfigId() {
		return configId;
	}
	public void setConfigId(String configId) {
		this.configId = configId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getRouterAddr() {
		return routerAddr;
	}
	public void setRouterAddr(String routerAddr) {
		this.routerAddr = routerAddr;
	}
	public String getInterfaceAddr() {
		return interfaceAddr;
	}
	public void setInterfaceAddr(String interfaceAddr) {
		this.interfaceAddr = interfaceAddr;
	}
	public String getInterParam() {
		return interParam;
	}
	public void setInterParam(String interParam) {
		this.interParam = interParam;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getIsHirer() {
		return isHirer;
	}
	public void setIsHirer(String isHirer) {
		this.isHirer = isHirer;
	}
	public String getIsUser() {
		return isUser;
	}
	public void setIsUser(String isUser) {
		this.isUser = isUser;
	}
	
	public void preInsert(){
		setConfigId(UUIDTools.uuidRandom());
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	
	
	
	
	
}
