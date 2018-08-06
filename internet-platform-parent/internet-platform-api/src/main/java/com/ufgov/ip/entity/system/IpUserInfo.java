package com.ufgov.ip.entity.system;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * aaaaEntity
 * @author winner
 * @version 2016-10-08
 */
public class IpUserInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String userSex;		// 用户性别 （1男0女）
	private String password;		// 密码
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date graduatioinTime;		// 毕业时间
	private String education;		// 学历
	private String hobby;		// 爱好
	private String userId;		// 
	private String loginName;		// 登录名
	
	public IpUserInfo() {
	}
	

	@Length(min=0, max=1, message="用户性别 （1男0女）长度必须介于 0 和 1 之间")
	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	
	@Length(min=1, max=50, message="密码长度必须介于 1 和 50 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	//@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	//@JsonFormat(pattern="yyyy-MM-ddHH:mm:ss")
	public Date getGraduatioinTime() {
		return graduatioinTime;
	}

	public void setGraduatioinTime(Date graduatioinTime) {
		this.graduatioinTime = graduatioinTime;
	}
	
	@Length(min=0, max=20, message="学历长度必须介于 0 和 20 之间")
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}
	
	@Length(min=0, max=100, message="爱好长度必须介于 0 和 100 之间")
	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	
	@Length(min=1, max=60, message="长度必须介于 1 和 60 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=1, max=20, message="登录名长度必须介于 1 和 20 之间")
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
}