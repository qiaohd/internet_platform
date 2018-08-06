package com.ufgov.ip.entity.system;

import java.io.Serializable;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the ip_user database table.
 * 
 */
@Entity
@Table(name="ip_user")
@NamedQuery(name="IpUser.findAll", query="SELECT i FROM IpUser i")
public class IpUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="USER_ID")
	private String userId;

	@Column(name="CELLPHONE_NO")
	private String cellphoneNo;
	@Column(name="DUTY")
	private String duty;
	@Column(name="EDUCATION")
	private String education;

	@Column(name="EMPLOYEE_NO")
	private String employeeNo;
    
	
	@Column(name="GRADUATE_SCHOOL")
	private String graduateSchool;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@Column(name="GRADUATIOIN_TIME")
	private Date graduatioinTime;

	@Column(name="HIRER_ID")
	private String hirerId;

	@Column(name="IS_ENABLED",insertable=false,updatable=true) //insert使用mysql预设的默认值.
	private String isEnabled;

	@Column(name="LOGIN_NAME")
	private String loginName;

	@Column(name="login_ts")
	private Long loginTs;
	@Column(name="MAJOR")
	private String major;
	

	@Column(name="NATIVE_PLACE")
	private String nativePlace;

	@Column(name="PASSWORD")
	private String password;

	@Column(name="PHONE_NO")
	private String phoneNo;
	@Column(name="REMARK")
	private String remark;
	@Column(name="SALT")
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
	
	@Transient
	private String coName;
	
	@Transient
	private String roleName;
	
	@Column(name="USER_TYPE")
	private String userType;
	
	@Column(name="USER_CODE")
	private String userCode;
	

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public IpUser() {
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

	public Date getGraduatioinTime() {
		return this.graduatioinTime;
	}

	public void setGraduatioinTime(Date graduatioinTime) {
		this.graduatioinTime = graduatioinTime;
	}

	public String getHirerId() {
		return this.hirerId;
	}

	public void setHirerId(String hirerId) {
		this.hirerId = hirerId;
	}

	public String getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
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
	
	@PrePersist
    public void prePersist() {
//		this.isEnabled="1";
    }
 
    @PreUpdate
    public void preUpdate() {
    }

	public String getCoName() {
		return coName;
	}

	public void setCoName(String coName) {
		this.coName = coName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	
	
	
    
    
}