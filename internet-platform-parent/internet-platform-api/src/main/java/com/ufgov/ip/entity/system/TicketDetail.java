package com.ufgov.ip.entity.system;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the ticket_detail database table.
 * 
 */
@Entity
@Table(name="ticket_detail")
@NamedQuery(name="TicketDetail.findAll", query="SELECT t FROM TicketDetail t")
public class TicketDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="business_id")
	private String businessId;

	private int status;

	private String suggestion;

	private String usid;
	
	@Column(name="bpm_uid")
	private String bpmUid;
	
	@Column(name="create_time")
	private Timestamp createTime;

	public TicketDetail() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessId() {
		return this.businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSuggestion() {
		return this.suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public String getUsid() {
		return this.usid;
	}

	public void setUsid(String usid) {
		this.usid = usid;
	}

	public String getBpmUid() {
		return bpmUid;
	}

	public void setBpmUid(String bpmUid) {
		this.bpmUid = bpmUid;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	
	
	

}