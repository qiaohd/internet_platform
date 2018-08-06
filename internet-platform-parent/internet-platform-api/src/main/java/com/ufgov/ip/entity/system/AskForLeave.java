package com.ufgov.ip.entity.system;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.*;


/**
 * The persistent class for the ask_for_leave database table.
 * 
 */
@Entity
@Table(name="ask_for_leave")
@NamedQuery(name="AskForLeave.findAll", query="SELECT a FROM AskForLeave a")
public class AskForLeave implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="end_date")
	private String endDate;

	private String proposer;

	private String reason;

	@Column(name="start_date")
	private String startDate;

	private int status;

	private String type;

	private String usid;
	
	@Column(name="cur_processinstance_id")
	private String curProcessinstanceId;
	
	
	@Column(name="task_id")
	private String taskId;
	
	/*@Column(name="dept_result")
	private String deptResult;
	
	@Column(name="hr_result")
	private String hrResult;*/
	
	@Transient
	private String result;
	
	@Column(name="create_time")
	private Timestamp createTime;
	

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public AskForLeave() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEndDate() {
		return this.endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getProposer() {
		return this.proposer;
	}

	public void setProposer(String proposer) {
		this.proposer = proposer;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsid() {
		return this.usid;
	}

	public void setUsid(String usid) {
		this.usid = usid;
	}

	public String getCurProcessinstanceId() {
		return curProcessinstanceId;
	}

	public void setCurProcessinstanceId(String curProcessinstanceId) {
		this.curProcessinstanceId = curProcessinstanceId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	

	/*public String getDeptResult() {
		return deptResult;
	}

	public void setDeptResult(String deptResult) {
		this.deptResult = deptResult;
	}

	public String getHrResult() {
		return hrResult;
	}

	public void setHrResult(String hrResult) {
		this.hrResult = hrResult;
	}*/

	
	
	
	
	

	
	
	

}