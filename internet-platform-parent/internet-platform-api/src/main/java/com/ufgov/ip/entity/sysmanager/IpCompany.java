package com.ufgov.ip.entity.sysmanager;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the ip_company database table.
 * 
 */
@Entity
@Table(name="ip_company")
@NamedQuery(name="IpCompany.findAll", query="SELECT i FROM IpCompany i")
public class IpCompany implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CO_ID")
	private String coId;

	@Column(name="CO_CODE")
	private String coCode;
	
	@Column(name="CO_CODE_TMP")
	private String coCodeTmp;

	@Column(name="CO_DESC")
	private String coDesc;
	
	@Column(name="IS_FINANCIAL")
	private String isFinancial;
	@Column(name="CO_NAME1")
	private String coName1;
	
	@Column(name="CO_NAME2")
	private String coName2;
	
	@Column(name="CO_NAME3")
	private String coName3;

	public String getIsFinancial() {
		return isFinancial;
	}

	public void setIsFinancial(String isFinancial) {
		this.isFinancial = isFinancial;
	}

	public String getCoName1() {
		return coName1;
	}

	public void setCoName1(String coName1) {
		this.coName1 = coName1;
	}

	public String getCoName2() {
		return coName2;
	}

	public void setCoName2(String coName2) {
		this.coName2 = coName2;
	}

	public String getCoName3() {
		return coName3;
	}

	public void setCoName3(String coName3) {
		this.coName3 = coName3;
	}

	@Column(name="CO_FULLNAME")
	private String coFullname;

	@Column(name="CO_NAME")
	private String coName;
	
	@Column(name="agcfs_dw_type")
	private String agcfsDwType;
	
	@Column(name="hold1")
	private String hold1;
	
	@Column(name="hold2")
	private String hold2;
	
	@Column(name="hold3")
	private String hold3;
	
	@Column(name="hold4")
	private String hold4;
	
	@Column(name="hold5")
	private String hold5;
	
	@Column(name="hold6")
	private String hold6;
	
	@Column(name="hold7")
	private String hold7;
	
	
	@Column(name="hold8")
	private String hold8;
	
	@Column(name="hold9")
	private String hold9;
	
	@Column(name="hold10")
	private String hold10;
	
	
	//树形展示需要显示编码和名称.
	@Transient
	private String coCodeAndName;

	public String getCoCodeAndName() {
		return this.coCode+" "+this.coName;
	}

	public void setCoCodeAndName(String coCodeAndName) {
		this.coCodeAndName = coCodeAndName;
	}

	@Column(name="HIRER_ID")
	private String hirerId;

	@Column(name="IS_ENABLED")
	private String isEnabled;

	private String linkman;

	@Column(name="PARENT_CO_ID")
	private String parentCoId;
	
	@Column(name="LEVEL_NUM")
	// 默认一级 
	private int levelNum=1;
	
	@Column(name="DEPT_DETAIL")
	private String deptDetail;
	
	@Column(name="DISP_ORDER",insertable=false,updatable=true)
	private int dispOrder=0;
	
	@Transient
	private String oldCoCode;
	

	public IpCompany() {
	}

	public String getCoId() {
		return this.coId;
	}

	public void setCoId(String coId) {
		this.coId = coId;
	}

	public String getCoCode() {
		return this.coCode;
	}

	public void setCoCode(String coCode) {
		this.coCode = coCode;
	}

	public String getCoDesc() {
		return this.coDesc;
	}

	public void setCoDesc(String coDesc) {
		this.coDesc = coDesc;
	}

	public String getCoFullname() {
		return this.coFullname;
	}

	public void setCoFullname(String coFullname) {
		this.coFullname = coFullname;
	}

	public String getCoName() {
		return this.coName;
	}

	public void setCoName(String coName) {
		this.coName = coName;
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

	public String getLinkman() {
		return this.linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getParentCoId() {
		return this.parentCoId;
	}

	public void setParentCoId(String parentCoId) {
		this.parentCoId = parentCoId;
	}

	public int getLevelNum() {
		return levelNum;
	}

	public void setLevelNum(int levelNum) {
		this.levelNum = levelNum;
	}

	public String getDeptDetail() {
		return deptDetail;
	}

	public void setDeptDetail(String deptDetail) {
		this.deptDetail = deptDetail;
	}

	public int getDispOrder() {
		return dispOrder;
	}

	public void setDispOrder(int dispOrder) {
		this.dispOrder = dispOrder;
	}

	public String getAgcfsDwType() {
		return agcfsDwType;
	}

	public void setAgcfsDwType(String agcfsDwType) {
		this.agcfsDwType = agcfsDwType;
	}

	public String getHold1() {
		return hold1;
	}

	public void setHold1(String hold1) {
		this.hold1 = hold1;
	}

	public String getHold2() {
		return hold2;
	}

	public void setHold2(String hold2) {
		this.hold2 = hold2;
	}

	public String getHold3() {
		return hold3;
	}

	public void setHold3(String hold3) {
		this.hold3 = hold3;
	}

	public String getHold4() {
		return hold4;
	}

	public void setHold4(String hold4) {
		this.hold4 = hold4;
	}

	public String getHold5() {
		return hold5;
	}

	public void setHold5(String hold5) {
		this.hold5 = hold5;
	}

	public String getHold6() {
		return hold6;
	}

	public void setHold6(String hold6) {
		this.hold6 = hold6;
	}

	public String getHold7() {
		return hold7;
	}

	public void setHold7(String hold7) {
		this.hold7 = hold7;
	}

	public String getHold8() {
		return hold8;
	}

	public void setHold8(String hold8) {
		this.hold8 = hold8;
	}

	public String getHold9() {
		return hold9;
	}

	public void setHold9(String hold9) {
		this.hold9 = hold9;
	}

	public String getHold10() {
		return hold10;
	}

	public void setHold10(String hold10) {
		this.hold10 = hold10;
	}

	public String getOldCoCode() {
		return oldCoCode;
	}

	public void setOldCoCode(String oldCoCode) {
		this.oldCoCode = oldCoCode;
	}

	public String getCoCodeTmp() {
		return coCodeTmp;
	}

	public void setCoCodeTmp(String coCodeTmp) {
		this.coCodeTmp = coCodeTmp;
	}
	
	
	
	
	

}