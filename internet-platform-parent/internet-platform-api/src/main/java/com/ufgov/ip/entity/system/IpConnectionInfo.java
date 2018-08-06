package com.ufgov.ip.entity.system;

import java.io.Serializable;

public class IpConnectionInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1599731301295481730L;

	private String name;

	private String host;
	private String schemaname;
	private int port;
	private String username;
	private String password;
	public String getSchemaname() {
		return schemaname;
	}
	public void setSchemaname(String schemaname) {
		this.schemaname = schemaname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
