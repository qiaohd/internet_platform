package com.ufgov.ip.entity.system;

import java.io.Serializable;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Date;



public class IpUserDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="USER_ID")
	private String userId;

	@Column(name="CELLPHONE_NO")
	private String cellphoneNo;

	private String duty;

	private String education;

	@Column(name="EMPLOYEE_NO")
	private String employeeNo;
    
	
	@Column(name="GRADUATE_SCHOOL")
	private String graduateSchool;

	
	private String graduatioinTime;

	@Column(name="HIRER_ID")
	private String hirerId;

	@Column(name="IS_ENABLED")
	private BigDecimal isEnabled;

	@Column(name="LOGIN_NAME")
	private String loginName;

	@Column(name="login_ts")
	private Long loginTs;

	private String major;

	@Column(name="NATIVE_PLACE")
	private String nativePlace;

	private String password;

	@Column(name="PHONE_NO")
	private String phoneNo;

	private String remark;

	private String salt;

	@Column(name="USER_EMAIL")
	private String userEmail;

	@Column(name="USER_NAME")
	private String userName;

	@Column(name="USER_PIC_URL")
	private String userPicUrl;

	@Column(name="USER_SEX")
	private String userSex;
	
	@Column(name="EXTENSION")
	private String extension;
	

	public IpUserDTO() {
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCellphoneNo() {
		return this.cellphoneNo;
	}

	public void setCellphoneNo(String cellphoneNo) {
		this.cellphoneNo = cellphoneNo;
	}

	public String getDuty() {
		return this.duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getEducation() {
		return this.education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getEmployeeNo() {
		return this.employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}


	public String getGraduateSchool() {
		return graduateSchool;
	}

	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}

	public String getGraduatioinTime() {
		return this.graduatioinTime;
	}

	public void setGraduatioinTime(String graduatioinTime) {
		this.graduatioinTime = graduatioinTime;
	}

	public String getHirerId() {
		return this.hirerId;
	}

	public void setHirerId(String hirerId) {
		this.hirerId = hirerId;
	}

	public BigDecimal getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(BigDecimal isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Long getLoginTs() {
		return this.loginTs;
	}

	public void setLoginTs(Long loginTs) {
		this.loginTs = loginTs;
	}

	public String getMajor() {
		return this.major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getNativePlace() {
		return this.nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNo() {
		return this.phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPicUrl() {
		return this.userPicUrl;
	}

	public void setUserPicUrl(String userPicUrl) {
		this.userPicUrl = userPicUrl;
	}

	public String getUserSex() {
		return this.userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	

}