package com.ufgov.ip.web.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uap.web.httpsession.cache.SessionCacheManager;
import yonyou.bpm.rest.exception.RestException;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ufgov.ip.api.base.IPCommonServiceI;
import com.ufgov.ip.api.sysmanager.IPRoleServiceI;
import com.ufgov.ip.api.sysmanager.IpCompanyServiceI;
import com.ufgov.ip.api.sysmanager.IpUserCompanyServiceI;
import com.ufgov.ip.api.sysmanager.IpUserRoleServiceI;
import com.ufgov.ip.api.system.BpmSynchronousServiceI;
import com.ufgov.ip.api.system.HirerAccountServiceI;
import com.ufgov.ip.api.system.MenuServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.sysmanager.IpRole;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.utils.PropertyUtilSys;
import com.ufgov.ip.web.sysmanager.OrganController;
import com.yonyou.iuap.iweb.entity.DataTable;

@Component("org.BPMSynchronousController")
@Scope("prototype")
@RequestMapping(value = "/synchronous")
public class BPMSynchronousController {

	

	private final Logger logger = LoggerFactory
			.getLogger(OrganController.class);
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;
	 @Autowired
	private IpCompanyServiceI ipCompanyService;

	@Autowired
	private SessionCacheManager sessionCacheManager;

	@Autowired
	private SessionCacheManager sessionCacheManager_h;

	 @Autowired
	protected UserAccountServiceI userAccountService;

	 @Autowired
	private IPRoleServiceI iPRoleService;

	 @Autowired
	private IpUserCompanyServiceI ipUserCompanyService;

	 @Autowired
	private IPCommonServiceI iPCommonService;

	 @Autowired
	protected HirerAccountServiceI hirerAccountService;

	 @Autowired
	private IpUserRoleServiceI ipUserRoleService;

	 @Autowired
	private IPCommonServiceI ipCommonService;
	
	 @Autowired
	private BpmSynchronousServiceI bpmSynchronousService;
	

	DataTable<IpCompany> dataTable1;

	DataTable<IpUser> employeeDataTable;

	DataTable<IpUser> dataTableUser;

	DataTable<IpUser> searchUsers;

	DataTable<IpUser> employeeByDeptDataTable;

	public IpCompanyServiceI getIpCompanyService() {
		return ipCompanyService;
	}

	public void setIpCompanyService(IpCompanyServiceI ipCompanyService) {
		this.ipCompanyService = ipCompanyService;
	}

	public UserAccountServiceI getUserAccountService() {
		return userAccountService;
	}

	public void setUserAccountService(UserAccountServiceI userAccountService) {
		this.userAccountService = userAccountService;
	}

	public IPRoleServiceI getiPRoleService() {
		return iPRoleService;
	}

	public void setiPRoleService(IPRoleServiceI iPRoleService) {
		this.iPRoleService = iPRoleService;
	}

	public IPCommonServiceI getiPCommonService() {
		return iPCommonService;
	}

	public void setiPCommonService(IPCommonServiceI iPCommonService) {
		this.iPCommonService = iPCommonService;
	}

	public IpUserCompanyServiceI getIpUserCompanyService() {
		return ipUserCompanyService;
	}

	public void setIpUserCompanyService(
			IpUserCompanyServiceI ipUserCompanyService) {
		this.ipUserCompanyService = ipUserCompanyService;
	}

	public HirerAccountServiceI getHirerAccountService() {
		return hirerAccountService;
	}

	public void setHirerAccountService(HirerAccountServiceI hirerAccountService) {
		this.hirerAccountService = hirerAccountService;
	}

	public IpUserRoleServiceI getIpUserRoleService() {
		return ipUserRoleService;
	}

	public void setIpUserRoleService(IpUserRoleServiceI ipUserRoleService) {
		this.ipUserRoleService = ipUserRoleService;
	}

	public IPCommonServiceI getIpCommonService() {
		return ipCommonService;
	}

	// 菜单
	@Reference(version = "0.0.1")
	private MenuServiceI menuService;

	public void setMenuService(MenuServiceI menuService) {
		this.menuService = menuService;
	}

	// 批量同步bpm
	@RequestMapping(method = RequestMethod.GET, value = "bpm")
	public @ResponseBody Map<String, String> bpmSynchronous(
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		String cuser = null;
		Map<String, String> resultMap = new HashMap<String, String>();
		if (SecurityUtils.getSubject().getPrincipal() != null)
			cuser = (String) SecurityUtils.getSubject().getPrincipal();
		 
		    IpHirer ipHirer = hirerAccountService.findHirerByEmailOrLoginNameOrCellphoneNo(cuser);//租户
		    //查询当前租户下的所有信息ipUser ipHirer ipCompany ipHirer
			List<IpCompany> findCompanyByHirerId = ipCompanyService.findCompanyByHirerId(ipHirer.getHirerId());
			List<IpUser> findUserByHirerId = userAccountService.findUserByHirerId(ipHirer.getHirerId());
			List<IpRole> roleInfoByHirerId = iPRoleService.getRoleInfoByHirerId(ipHirer.getHirerId());
			
			String getbpmEnabledByKey = PropertyUtilSys.getbpmEnabledByKey("bpm.enabled");
			 if("true".equals(getbpmEnabledByKey)){
				 resultMap.put("result", "Enabled");
				 return resultMap;
			 }else{
			boolean doSynchronousService=true;
				    try {
						 doSynchronousService = bpmSynchronousService.doSynchronousService(ipHirer,findUserByHirerId,findCompanyByHirerId,roleInfoByHirerId);
						resultMap.put("result", "true");
						return resultMap;
					} catch (RestException e) {
						// TODO 自动生成的 catch 块
						if(doSynchronousService){
							resultMap.put("result", "true");
							return resultMap;
						}else{
							e.printStackTrace();
							resultMap.put("result", "false");
							return resultMap;
						}
					}
					
			 }
		
		
	}

}
