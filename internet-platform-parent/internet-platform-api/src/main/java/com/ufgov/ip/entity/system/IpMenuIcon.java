package com.ufgov.ip.entity.system;

import java.io.Serializable;

public class IpMenuIcon implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String menu_id      ;
	private String menu_name    ;
	private String url          ;
	private String user_id      ;
	private String icon_id      ;
	private String icon_path    ;
	public String getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}
	public String getMenu_name() {
		return menu_name;
	}
	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getIcon_id() {
		return icon_id;
	}
	public void setIcon_id(String icon_id) {
		this.icon_id = icon_id;
	}
	public String getIcon_path() {
		return icon_path;
	}
	public void setIcon_path(String icon_path) {
		this.icon_path = icon_path;
	}

}
