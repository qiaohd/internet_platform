package com.ufgov.ip.entity.database;

import java.io.Serializable;


/**
 * 数据库配置信息 
 * @author qiaohd
 *
 */
public class ConnectionInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4139524122290566855L;
	private String name;
	private String host;
	private String dbname;
	private String port;
	private String username;
	private String password;
	private String dbUrl;

	public ConnectionInfo() {

	}
	
	public ConnectionInfo(ConnectionInfo info){
		if(info != null){
			this.name = info.name;
			this.host = info.host;
			this.dbname = info.dbname;
			this.username = info.username;
			this.password = info.password;
			this.port = info.port;
		}
	}

	public ConnectionInfo(String name, String host, String port, String dbname, String username, String password) {
		super();
		this.name = name;
		this.host = host;
		this.dbname = dbname;
		this.port = port;
		this.username = username;
		this.password = password;
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

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
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

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof ConnectionInfo
				&& (this == o || (this.host.equals(((ConnectionInfo) o).host) && this.port == ((ConnectionInfo) o).port
						&& this.dbname.equals(((ConnectionInfo) o).dbname) && this.username.equals(((ConnectionInfo) o).username) && this.password
							.equals(((ConnectionInfo) o).password)))) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public String getContent() {
		return new StringBuilder().append(username).append('@').append(host).append(':').append(port).append('\n').append(dbname).toString();
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
}
