package com.ufgov.ip.entity.system;

import java.io.Serializable;
import java.util.Date;
	

/**
 * The persistent class for the ip_data_partition database table.
 * 
 */

public class IpDataPartition  implements Serializable {
	
	private static final long serialVersionUID = 6787067393822729688L;
	private String data_id;
	private String host;
	private String port;
	private String user_name;
	private String password;
	private String schema_name;
	private String area_name;
	private String db_driver;
	private String url;
	private Date create_date;
	private Date update_date;

	public IpDataPartition() {
	}


	public String getData_id() {
		return this.data_id;
	}

	public void setData_id(String data_id) {
		this.data_id = data_id;
	}


	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}


	public String getPort() {
		return this.port;
	}

	public void setPort(String port) {
		this.port = port;
	}


	public String getUser_name() {
		return this.user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}


	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getSchema_name() {
		return this.schema_name;
	}

	public void setSchema_name(String schema_name) {
		this.schema_name = schema_name;
	}


	public String getArea_name() {
		return this.area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}


	public String getDb_driver() {
		return this.db_driver;
	}

	public void setDb_driver(String db_driver) {
		this.db_driver = db_driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getCreate_date() {
		return this.create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}


	public Date getUpdate_date() {
		return this.update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	
}