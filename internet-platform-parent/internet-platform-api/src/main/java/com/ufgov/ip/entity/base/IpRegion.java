package com.ufgov.ip.entity.base;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the ip_region database table.
 * 
 */
@Entity
@Table(name="ip_region")
@NamedQuery(name="IpRegion.findAll", query="SELECT i FROM IpRegion i")
public class IpRegion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="THE_ID")
	private String theId;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATE_DATE")
	private Date createDate;

	@Column(name="IS_VALID")
	private String isValid;

	@Column(name="PARENT_ID")
	private String parentId;

	@Column(name="THE_CODE")
	private String theCode;

	@Column(name="THE_NAME")
	private String theName;
	
	@Temporal(TemporalType.DATE)
	@Column(name="UPDATE_DATE")
	private Date updateDate;

	public IpRegion() {
	}

	public String getTheId() {
		return this.theId;
	}

	public void setTheId(String theId) {
		this.theId = theId;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getTheCode() {
		return this.theCode;
	}

	public void setTheCode(String theCode) {
		this.theCode = theCode;
	}

	public String getTheName() {
		return this.theName;
	}

	public void setTheName(String theName) {
		this.theName = theName;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	

}