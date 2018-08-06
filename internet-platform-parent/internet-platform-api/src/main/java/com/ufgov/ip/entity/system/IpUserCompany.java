package com.ufgov.ip.entity.system;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the ip_user_company database table.
 * 
 */
@Entity
@Table(name="ip_user_company")
@NamedQuery(name="IpUserCompany.findAll", query="SELECT i FROM IpUserCompany i")
public class IpUserCompany implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="THE_ID")
	private String theId;

	@Column(name="CO_CODE")
	private String coCode;

	@Column(name="CO_ID")
	private String coId;

	@Column(name="CO_NAME")
	private String coName;

	@Column(name="USER_ID")
	private String userId;

	public IpUserCompany() {
	}

	public String getTheId() {
		return this.theId;
	}

	public void setTheId(String theId) {
		this.theId = theId;
	}

	public String getCoCode() {
		return this.coCode;
	}

	public void setCoCode(String coCode) {
		this.coCode = coCode;
	}

	public String getCoId() {
		return this.coId;
	}

	public void setCoId(String coId) {
		this.coId = coId;
	}

	public String getCoName() {
		return this.coName;
	}

	public void setCoName(String coName) {
		this.coName = coName;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}