package com.ufgov.ip.entity.base;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the ip_dictionary_detail database table.
 * 
 */
@Entity
@Table(name="ip_dictionary_detail")
@NamedQuery(name="IpDictionaryDetail.findAll", query="SELECT i FROM IpDictionaryDetail i")
public class IpDictionaryDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="detail_info")
	private String detailInfo;

	@Column(name="dic_id")
	private String dicId;

	@Id
	@Column(name="the_id")
	private String theId;
	
	@Column(name="detail_key")
	private String detailKey;
	
	@Transient
	private String dicType;
	@Transient
	private String dicName;
	
	

	public IpDictionaryDetail() {
	}

	public String getDetailInfo() {
		return this.detailInfo;
	}

	public void setDetailInfo(String detailInfo) {
		this.detailInfo = detailInfo;
	}

	public String getDicId() {
		return this.dicId;
	}

	public void setDicId(String dicId) {
		this.dicId = dicId;
	}

	public String getTheId() {
		return this.theId;
	}

	public void setTheId(String theId) {
		this.theId = theId;
	}

	public String getDetailKey() {
		return detailKey;
	}

	public void setDetailKey(String detailKey) {
		this.detailKey = detailKey;
	}

	public String getDicType() {
		return dicType;
	}

	public void setDicType(String dicType) {
		this.dicType = dicType;
	}

	public String getDicName() {
		return dicName;
	}

	public void setDicName(String dicName) {
		this.dicName = dicName;
	}
	
	

}