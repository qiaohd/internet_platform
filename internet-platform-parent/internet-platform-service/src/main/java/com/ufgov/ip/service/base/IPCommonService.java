package com.ufgov.ip.service.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import yonyou.bpm.rest.BpmRest;
import yonyou.bpm.rest.exception.RestException;
import yonyou.bpm.rest.request.identity.OrgQueryParam;
import yonyou.bpm.rest.request.identity.UserGroupQueryParam;
import yonyou.bpm.rest.request.identity.UserLinkQueryParam;
import yonyou.bpm.rest.request.identity.UserLinkResourceParam;
import yonyou.bpm.rest.request.identity.UserResourceParam;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.ufgov.ip.api.base.IPCommonServiceI;
import com.ufgov.ip.dao.base.IPCommonDAO;
import com.ufgov.ip.dao.base.IPDictDao;
import com.ufgov.ip.dao.base.IPDictDetailDao;
import com.ufgov.ip.dao.base.IPRegionDao;
import com.ufgov.ip.dao.sysmanager.ICompanyDao;
import com.ufgov.ip.dao.sysmanager.IPRoleDao;
import com.ufgov.ip.dao.sysmanager.IpUserCompanyDao;
import com.ufgov.ip.dao.sysmanager.IpUserRoleDao;
import com.ufgov.ip.dao.system.IuapUserDao;
import com.ufgov.ip.entity.base.IpDictionary;
import com.ufgov.ip.entity.base.IpDictionaryDetail;
import com.ufgov.ip.entity.base.IpRegion;
import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.sysmanager.IpRole;
import com.ufgov.ip.entity.sysmanager.IpUserRole;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.IpUserCompany;
import com.ufgov.ip.service.system.ProcessService;
import com.ufgov.ip.utils.PropertyUtilSys;
@Service(version = "0.0.1")
@Transactional(readOnly=true)
public class IPCommonService implements IPCommonServiceI{

	@Autowired
	private IPDictDao iPDictDao;
	
	@Autowired
	private IPCommonDAO ipCommonDAO; //字段查询DAO
	
	public IPCommonDAO getIpCommonDAO() {
		return ipCommonDAO;
	}
	public void setIpCommonDAO(IPCommonDAO ipCommonDAO) {
		this.ipCommonDAO = ipCommonDAO;
	}

	@Autowired
	private IPDictDetailDao iPDictDetailDao;
	
	@Autowired
	private IPRegionDao iPRegionDao;
	
	@Autowired
	private IuapUserDao userDao;
	
	@Autowired
	private IpUserCompanyDao ipUserCompanyDao;
	
	@Autowired
	private IpUserRoleDao ipUserRoleDao;
	
	@Autowired
	private ICompanyDao coDao;
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	private IPRoleDao iPRoleDao;
	
	
	public IuapUserDao getUserDao() {
		return userDao;
	}
	public IpUserCompanyDao getIpUserCompanyDao() {
		return ipUserCompanyDao;
	}
	public IpUserRoleDao getIpUserRoleDao() {
		return ipUserRoleDao;
	}
	public IPRegionDao getiPRegionDao() {
		return iPRegionDao;
	}
	public void setiPRegionDao(IPRegionDao iPRegionDao) {
		this.iPRegionDao = iPRegionDao;
	}
	public IPDictDetailDao getiPDictDetailDao() {
		return iPDictDetailDao;
	}
	public void setiPDictDetailDao(IPDictDetailDao iPDictDetailDao) {
		this.iPDictDetailDao = iPDictDetailDao;
	}
	

	public IPDictDao getiPDictDao() {
		return iPDictDao;
	}
	public void setiPDictDao(IPDictDao iPDictDao) {
		this.iPDictDao = iPDictDao;
	}
	
	public IpDictionary findIpDictionaryByDicType(String dicType){
		return iPDictDao.findIpDictionaryByDicType(dicType);
	}
	
	public List<IpDictionaryDetail> findIpDictionaryDetailByDicId(String dicId){
		return iPDictDetailDao.findIpDictionaryDetailByDicId(dicId);
	}
	
	public List<IpRegion> findIpRegionByTheCodeLike(String theCode){
		return iPRegionDao.findIpRegionByTheCodeLike(theCode);
	}
	
	public IpRegion findIpRegionByTheCode(String theCode){
		return iPRegionDao.findIpRegionByTheCode(theCode);
	}
	
	@Transactional
	public void saveExcelInfo(IpUser ipUser,
			IpUserCompany ipUserCompany, IpUserRole ipUserRole) {
		String coCode = ipUserCompany.getCoCode();
		String coName = ipUserCompany.getCoName();
		IpCompany ipCompany = new IpCompany();
		ipCompany.setCoCode(coCode);
		ipCompany.setCoName(coName);
		ipCompany.setCoId(ipUserCompany.getCoId());
		
		String getbpmEnabledByKey = PropertyUtilSys.getbpmEnabledByKey("bpm.enabled");//获得工作流开/关状态值
		BpmRest bpmRestServices = processService.getBpmRestServices(ipUser.getHirerId(),ipUser.getHirerId());
		if("true".equals(getbpmEnabledByKey)){//工作流开启
			//同步用户信息
			   String userId = InsertOrUpdateBpmUser(ipUser, ipCompany);
			//同步关联关系
			   //同步关联部门
				  String tenantLimitId = bpmRestServices.getIdentityService().getBaseParam().getTenantLimitId();
				  UserLinkResourceParam userLinkResourceParam = getUserLinkParam(
						userId, bpmRestServices, ipUserCompany,
						tenantLimitId,"org");
				  try {
					
					 //先查询再删除冗余数据，再插入
					  UserLinkQueryParam userLinkQueryParam = new UserLinkQueryParam();
					  userLinkQueryParam.setType("org");
					  userLinkQueryParam.setUserId(userId);
					  userLinkQueryParam.setTenantId(tenantLimitId);
					  JsonNode jsonNode = (JsonNode)bpmRestServices.getIdentityService().queryUserLinks(userLinkQueryParam);
					  List<JsonNode> findValues = jsonNode.findValues("id");
					  for (JsonNode jsonNode2 : findValues) {
						  String jsonInfo_id=jsonNode2.toString();
		    			  String linkId = jsonInfo_id.substring(1, jsonInfo_id.length()-1);
		    			//按linkId删除冗余数据
		    			  bpmRestServices.getIdentityService().deleteUserLink(linkId);
					}
					
					  //插入新数据
	    			  bpmRestServices.getIdentityService().saveUserLink(userLinkResourceParam);
				} catch (RestException e) {
					e.printStackTrace();
				}
				  
				  
			   //同步关联角色
			      String tenantId = bpmRestServices.getIdentityService().getBaseParam().getTenantLimitId();
			      UserLinkResourceParam userLinkRole = getBpmRoleparam(
						userId, bpmRestServices,
						ipUserRole, tenantId,"usergroup");
				  
				  try {
					//先查询再删除冗余数据，再插入
					  UserLinkQueryParam userLinkQueryParam = new UserLinkQueryParam();
					  userLinkQueryParam.setType("usergroup");
					  userLinkQueryParam.setUserId(userId);
					  userLinkQueryParam.setTenantId(tenantLimitId);
					  JsonNode jsonNode = (JsonNode)bpmRestServices.getIdentityService().queryUserLinks(userLinkQueryParam);
					  List<JsonNode> findValues = jsonNode.findValues("id");
					  for (JsonNode jsonNode2 : findValues) {
						  String jsonInfo_id=jsonNode2.toString();
		    			  String linkId = jsonInfo_id.substring(1, jsonInfo_id.length()-1);
		    			//按linkId删除冗余数据
		    			  bpmRestServices.getIdentityService().deleteUserLink(linkId);
					}
					  //插入新数据
	    			  bpmRestServices.getIdentityService().saveUserLink(userLinkRole);
				} catch (RestException e) {
					e.printStackTrace();
				}
		   }
		
		
		userDao.save(ipUser);
		ipUserCompanyDao.save(ipUserCompany);
		ipUserRoleDao.save(ipUserRole);
		
		
		
	}
	
	
	private String InsertOrUpdateBpmUser(IpUser ipUserEntity, IpCompany ic) {
		String userId;
		//开启工作流
		   String coId = ic.getCoId();
		   IpCompany findByCoId = coDao.findByCoId(coId);
		    BpmRest bpmRestServices = processService.getBpmRestServices(ipUserEntity.getHirerId(),ipUserEntity.getHirerId());
		    String tenantLimitId = bpmRestServices.getIdentityService().getBaseParam().getTenantLimitId();
		    
		    //判断当前所保存的角色是否为业务管理员，如果是就设置为登录bpm流程中的超级管理员
		      //2.设置超级管理员
		    UserResourceParam userResourceParam = getBpmUserParam(ipUserEntity,
					findByCoId, bpmRestServices, tenantLimitId);
		    JsonNode jsonNode=null;
		    try {
		    	jsonNode = (JsonNode)bpmRestServices.getIdentityService().saveUser(userResourceParam);
			} catch (RestException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		    String jsonInfo_id=jsonNode.findValues("id").get(0).toString();
			userId = jsonInfo_id.substring(1, jsonInfo_id.length()-1);
		return userId;
	}
	
	public Map<String, String> getDictDetailByEnumtype(String dicType)
	{
		List<Map<String, String>> maplist_DictDetails = ipCommonDAO.getDictDetailByEnumtype(dicType);
		
		Map<String, String> resultMap = new HashMap<String, String>();
		for (Map<String, String> map : maplist_DictDetails) {
			String key = null;
			String value = null;
			for (Map.Entry<String, String> entry : map.entrySet()) {
				if ("detail_key".equals(entry.getKey())) {
					key = (String) entry.getValue();
				} else if ("detail_info".equals(entry.getKey())) {
					value = (String) entry.getValue();
				}
			}
			resultMap.put(key, value);
		}
		return resultMap;
		
	}
	
	private UserResourceParam getBpmUserParam(IpUser ipUserEntity,
			IpCompany findByCoId, BpmRest bpmRestServices, String tenantLimitId) {
		UserResourceParam userResourceParam = new UserResourceParam();
		userResourceParam.setCode(ipUserEntity.getLoginName());//bpm的code具有唯一性，这边的用户名称没有唯一性，会导致异常，所以换成登录名
		userResourceParam.setName(ipUserEntity.getUserName());
		userResourceParam.setOrgCode(findByCoId.getCoCode());
		userResourceParam.setPassword(ipUserEntity.getPassword());
		userResourceParam.setSalt(ipUserEntity.getSalt());
		userResourceParam.setEnable(true);
		userResourceParam.setPhone(ipUserEntity.getCellphoneNo());
		userResourceParam.setMail(ipUserEntity.getUserEmail());
		userResourceParam.setSource("source");
		
		  OrgQueryParam orgQueryParam = new OrgQueryParam();
		  orgQueryParam.setTenantId(tenantLimitId);
		  orgQueryParam.setName(findByCoId.getCoName());
		  orgQueryParam.setCode(findByCoId.getCoCode());
		try {
			JsonNode org_jsonNode = (JsonNode)bpmRestServices.getIdentityService().queryOrgs(orgQueryParam);
			String co_id=org_jsonNode.findValues("id").get(0).toString();
			co_id = co_id.substring(1, co_id.length()-1);
			userResourceParam.setOrg(co_id);
		} catch (RestException e1) {
			e1.printStackTrace();
		}
		return userResourceParam;
	}
	
	
	private UserLinkResourceParam getUserLinkParam(String userId,
			BpmRest bpmRestServices, IpUserCompany setProperties,
			String tenantLimitId,String type) {
		UserLinkResourceParam userLinkResourceParam = new UserLinkResourceParam();
		  userLinkResourceParam.setType(type);
		  userLinkResourceParam.setUserId(userId);
		
		  //获得bpm中的部门id
		  IpCompany findByCoId = coDao.findByCoId(setProperties.getCoId());
		  OrgQueryParam orgQueryParam = new OrgQueryParam();
		  orgQueryParam.setTenantId(tenantLimitId);
		  orgQueryParam.setName(findByCoId.getCoName());
		  orgQueryParam.setCode(findByCoId.getCoCode());
		  try {
			JsonNode org_jsonNode = (JsonNode)bpmRestServices.getIdentityService().queryOrgs(orgQueryParam);
			String co_id=org_jsonNode.findValues("id").get(0).toString();
			co_id = co_id.substring(1, co_id.length()-1);
			userLinkResourceParam.setTargetId(co_id);
		} catch (RestException e1) {
			e1.printStackTrace();
		}
		return userLinkResourceParam;
	}
	
	
	
	private UserLinkResourceParam getBpmRoleparam(String userId,
			BpmRest bpmRestServices, IpUserRole setRoleProperties,
			String tenantLimitId,String type) {
		UserLinkResourceParam userLinkResourceParam = new UserLinkResourceParam();
		  userLinkResourceParam.setType(type);
		  userLinkResourceParam.setUserId(userId);
		  
		  IpRole findByRoleId = iPRoleDao.findByRoleId(setRoleProperties.getRoleId());
		  if(findByRoleId==null){
			  userLinkResourceParam.setTargetId("未分配");
			  return userLinkResourceParam;
		  }else{
			  UserGroupQueryParam userGroupQueryParam = new UserGroupQueryParam();
			  userGroupQueryParam.setTenantId(tenantLimitId);
			  userGroupQueryParam.setName(findByRoleId.getRoleName());
			  try {
				  JsonNode userGroup_jsonNode = (JsonNode)bpmRestServices.getIdentityService().queryUserGroups(userGroupQueryParam);
				  String role_id=userGroup_jsonNode.findValues("id").get(0).toString();
				  role_id = role_id.substring(1, role_id.length()-1);
				  userLinkResourceParam.setTargetId(role_id);
			} catch (RestException e1) {
				e1.printStackTrace();
			}
			return userLinkResourceParam;
		  }
	}
	
}
