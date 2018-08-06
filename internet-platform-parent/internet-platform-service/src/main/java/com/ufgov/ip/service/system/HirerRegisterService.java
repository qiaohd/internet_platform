package com.ufgov.ip.service.system;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.Clock;

import yonyou.bpm.rest.BpmRest;
import yonyou.bpm.rest.request.identity.OrgResourceParam;
import yonyou.bpm.rest.request.identity.TenantResourceParam;
import yonyou.bpm.rest.request.identity.UserResourceParam;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.ufgov.ip.api.system.HirerRegisterServiceI;
import com.ufgov.ip.dao.sysmanager.ICompanyDao;
import com.ufgov.ip.dao.system.IpHirerMapper;
import com.ufgov.ip.dao.system.IpHirerSynMapper;
import com.ufgov.ip.dao.system.IuapUserDao;
import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.serviceutils.PropertyUtilSys;
import com.ufgov.ip.utils.UUIDTools;

/**
 * 用户管理服务层 书写readonly是说明没有特殊标识服务层只有只读事物
 *
 */
@Service(version = "0.0.1")
public class HirerRegisterService implements HirerRegisterServiceI{

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;

	private static Logger logger = LoggerFactory
			.getLogger(HirerRegisterService.class);

	@Autowired
	private IpHirerMapper ipHirerMapper;

	/*@Autowired
	private IuapCompanyDao coDao;*/
	@Autowired
	private IuapUserDao iuapUserDao;

	@Autowired
	private ICompanyDao coDao;
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	private IpHirerSynMapper ipHirerSynMapper;

	public ProcessService getProcessService() {
		return processService;
	}

	

	public void setCoDao(ICompanyDao coDao) {
		this.coDao = coDao;
	}
	

	private Clock clock = Clock.DEFAULT;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean registerHirer(IpHirer hirer,IpCompany ipCompany) throws Exception {
			 if (hirer.getHirerId()==null || hirer.getHirerId()=="") {
				hirer.setHirerId(UUIDTools.uuid());
			}
			 
			 /**
			   * 参数	参数类型	必须	说明
				code	String	是	租户编码
				name	String	是	租户名称
				parent	String	否	父租户id，默认为当前业务租户id
				admin	String	否	租户管理员id
				address	String	否	地址
				enable	Boolean	否	是否启用，默认为true
				revision	Integer	否	保存时revision=0，更新时revision>0，更新时id不能为空

			   */
			  //begin_restful_同步租户表
			      BpmRest bpmRestServices = processService.getBpmRestServices(hirer.getHirerId(),hirer.getHirerId());
			      String getbpmEnabledByKey = PropertyUtilSys.getbpmEnabledByKey("bpm.enabled");
			      if("true".equals(getbpmEnabledByKey)){//开启工作流
					  TenantResourceParam tenantResourceParam = new TenantResourceParam();
					  tenantResourceParam.setCode(hirer.getHirerNo());
					  tenantResourceParam.setName(hirer.getHirerName());
					  tenantResourceParam.setAddress(hirer.getAddress());
					  tenantResourceParam.setEnable(true);
					  tenantResourceParam.setParent("test");
					  //tenantResourceParam.setId(hirer.getHirerId());
					  JsonNode jsonNode = (JsonNode)bpmRestServices.getIdentityService().saveTenant(tenantResourceParam);
					  //begin_向租户同步表插入hirerId及cellphone，bpm不支持多租户，此表用来判断租户是否已经同步_20161215_zhangbch
					     ipHirerSynMapper.insert(hirer);
					  //end_向租户同步表插入hirerId及cellphone，bpm不支持多租户，此表用来判断租户是否已经同步_20161215_zhangbch
			      
					 //begin_需要建一个租户管理员管理员
			        UserResourceParam userResourceParam = new UserResourceParam();
					userResourceParam.setCode(hirer.getLoginName());
					userResourceParam.setName(hirer.getHirerName());
					//userResourceParam.setOrgCode(findByCoId.getCoCode());
					userResourceParam.setPassword(hirer.getPassword());
					userResourceParam.setSalt(hirer.getSalt());
					userResourceParam.setEnable(true);
					userResourceParam.setPhone(hirer.getCellphoneNo());
					userResourceParam.setMail(hirer.getEmail());
					userResourceParam.setSource("source");
					userResourceParam.setSysadmin(true);
					userResourceParam.setOrg("空");
					userResourceParam.setOrgCode("空");
					bpmRestServices.getIdentityService().saveUser(userResourceParam);
					   //begin_还需要将基本信息添加到ip_user里面_IP待改造_先不向ip_user里面插入数据
                       /* IpUser ipUser = new IpUser();
                        ipUser.setUserId(UUIDTools.uuidRandom());
                        ipUser.setCellphoneNo(hirer.getCellphoneNo());
                        ipUser.setHirerId(hirer.getHirerId());
                        ipUser.setUserSex(hirer.getSex());
                        ipUser.setUserName(hirer.getHirerName());
                        ipUser.setUserPicUrl(hirer.getHirerLogoUrl());
                        ipUser.setUserEmail(hirer.getEmail());
                        ipUser.setUserCode(hirer.getHirerCode());
                        ipUser.setPassword(hirer.getPassword());
                        ipUser.setSalt(hirer.getSalt());
                        ipUser.setLoginName(hirer.getLoginName());*/
					  //end_还需要将基本信息添加到ip_user里面_IP待改造_先不向ip_user里面插入数据
			      //end_需要建一个租户管理员管理员
			  //end_restful_同步租户表
			}
			  IpUser ipUser = setUserInfo(hirer);
			  ipHirerMapper.deleteByPk(hirer);
			  ipHirerMapper.insert(hirer);	
			  iuapUserDao.save(ipUser);
			 //begin_iuap_zhangbch_调试_20160313
			  IpHirer iphirer1=new IpHirer();
			  iphirer1.setHirerName(hirer.getHirerName());
			 IpHirer findByHirerName = ipHirerMapper.findHirerByCondition(iphirer1);
			 ipCompany.setHirerId((findByHirerName.getHirerId()));
			 ipCompany.setLevelNum(1);
			//begin_此时需要将公司信息同步到bpm
		    if("true".equals(getbpmEnabledByKey)){//开启工作流
				    OrgResourceParam orgResourceParam = new OrgResourceParam();
					orgResourceParam.setEnable(true);
					orgResourceParam.setCode(ipCompany.getCoCode());
					orgResourceParam.setName(ipCompany.getCoName());
					orgResourceParam.setCreator(ipCompany.getHirerId());
				    bpmRestServices.getIdentityService().saveOrg(orgResourceParam);
			 }
			 //end_此时需要将公司信息同步到bpm
			 coDao.save(ipCompany);
			 //end_iuap_zhangbch_调试_20160313				
		return true;
	}

	/**
	 * 根据租户信息设置用户信息 
	 * @param hirer
	 * @return
	 */
	private IpUser setUserInfo(IpHirer hirer) {
		IpUser ipUser = new IpUser();
		 ipUser.setUserId(UUIDTools.uuid());
		 ipUser.setHirerId(hirer.getHirerId());
		 ipUser.setLoginName(hirer.getLoginName());
		 ipUser.setUserName("管理员");
		 ipUser.setUserSex(hirer.getSex());
		 ipUser.setPassword(hirer.getPassword());
		 ipUser.setUserEmail(hirer.getEmail());
		 ipUser.setPhoneNo(hirer.getPhoneNo());
		 ipUser.setCellphoneNo(hirer.getCellphoneNo());
		 ipUser.setIsEnabled("1");
		 ipUser.setSalt(hirer.getSalt());
		 ipUser.setUserType("0");//  用户类型 0：租户管理员  1：普通用户
		 return ipUser;
	}

	public void setipHirerMapper(IpHirerMapper ipHirerMapper) {
		this.ipHirerMapper = ipHirerMapper;
	}

	/*public void setCoDao(IuapCompanyDao coDao) {
		this.coDao = coDao;
	}*/

	/*
	 * private void entryptPassword(IpHirer hirer) {
	 * 
	 * String password=Encodes.encodeHex(hirer.getPassword().getBytes());
	 * hirer.setPassword(password); }
	 */

}
