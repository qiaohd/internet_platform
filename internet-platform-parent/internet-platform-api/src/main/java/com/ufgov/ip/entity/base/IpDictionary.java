package com.ufgov.ip.entity.base;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ip_dictionary database table.
 * 
 */
@Entity
@Table(name="ip_dictionary")
@NamedQuery(name="IpDictionary.findAll", query="SELECT i FROM IpDictionary i")
public class IpDictionary implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="dic_id")
	private String dicId;

	@Column(name="dic_name")
	private String dicName;

	@Column(name="dic_type")
	private String dicType;

	public IpDictionary() {
	}

	public String getDicId() {
		return this.dicId;
	}

	public void setDicId(String dicId) {
		this.dicId = dicId;
	}

	public String getDicName() {
		return this.dicName;
	}

	public void setDicName(String dicName) {
		this.dicName = dicName;
	}

	public String getDicType() {
		return this.dicType;
	}

	public void setDicType(String dicType) {
		this.dicType = dicType;
	}

}