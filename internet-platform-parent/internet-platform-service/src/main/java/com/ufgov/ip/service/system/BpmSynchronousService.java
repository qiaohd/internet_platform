package com.ufgov.ip.service.system;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import uap.web.httpsession.cache.SessionCacheManager;
import yonyou.bpm.rest.BpmRest;
import yonyou.bpm.rest.exception.RestException;
import yonyou.bpm.rest.request.identity.OrgQueryParam;
import yonyou.bpm.rest.request.identity.OrgResourceParam;
import yonyou.bpm.rest.request.identity.TenantQueryParam;
import yonyou.bpm.rest.request.identity.TenantResourceParam;
import yonyou.bpm.rest.request.identity.UserGroupQueryParam;
import yonyou.bpm.rest.request.identity.UserGroupResourceParam;
import yonyou.bpm.rest.request.identity.UserLinkQueryParam;
import yonyou.bpm.rest.request.identity.UserLinkResourceParam;
import yonyou.bpm.rest.request.identity.UserQueryParam;
import yonyou.bpm.rest.request.identity.UserResourceParam;
import yonyou.bpm.rest.response.identity.OrgResponse;
import yonyou.bpm.rest.response.identity.TenantResponse;
import yonyou.bpm.rest.response.identity.UserGroupResponse;
import yonyou.bpm.rest.response.identity.UserLinkResponse;
import yonyou.bpm.rest.response.identity.UserResponse;
import yonyou.bpm.rest.utils.BaseUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ufgov.ip.api.system.BpmSynchronousServiceI;
import com.ufgov.ip.dao.sysmanager.ICompanyDao;
import com.ufgov.ip.dao.sysmanager.IPRoleDao;
import com.ufgov.ip.dao.sysmanager.IpUserCompanyDao;
import com.ufgov.ip.dao.sysmanager.IpUserRoleDao;
import com.ufgov.ip.dao.system.IpCompanySynMapper;
import com.ufgov.ip.dao.system.IpHirerSynMapper;
import com.ufgov.ip.dao.system.IpRoleSynMapper;
import com.ufgov.ip.dao.system.IuapUserDao;
import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.sysmanager.IpRole;
import com.ufgov.ip.entity.sysmanager.IpUserRole;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.IpUserCompany;
@Service(version = "0.0.1")
public class BpmSynchronousService implements BpmSynchronousServiceI {

	@Autowired
	private ProcessService processService;

	@Autowired
	private JsonResultService jsonResultService;

	@Autowired
	private ICompanyDao coDao;

	@Autowired
	private IpUserCompanyDao ipUserCompanyDao;

	@Autowired
	private IpUserRoleDao ipUserRoleDao;

	@Autowired
	private IPRoleDao iPRoleDao;
	
	@Autowired
    private SessionCacheManager sessionCacheManager;
	
	@Autowired
	private IpHirerSynMapper ipHirerSynMapper;
	
	@Autowired
	private IuapUserDao userDao;
	
	@Autowired
	private IpCompanySynMapper ipCompanySynMapper;
	
	@Autowired
	private IpRoleSynMapper ipRoleSynMapper;
	
	private static boolean SynFlag=true;

	public JsonResultService getJsonResultService() {
		return jsonResultService;
	}

	// 根据这新信息在bpm里面查询，遍历按照code查询
	public boolean doSynchronousService(IpHirer ipHirer,
			List<IpUser> findUserByHirerId,
			List<IpCompany> findCompanyByHirerId, List<IpRole> roleInfoByHirerId) throws RestException {
		BpmRest bpmRestServices = processService.getBpmRestServices(
				ipHirer.getHirerId(), ipHirer.getHirerId());
		
			// 同步租户
			doSynchronousHirer(ipHirer, bpmRestServices);
			// 同步角色
			doSynchronousRole(roleInfoByHirerId, bpmRestServices);
			// 同步公司
			doSynchronousOrg(bpmRestServices, findCompanyByHirerId);
			// 同步用户
			doSynchronousUser(bpmRestServices, findUserByHirerId);

		    return SynFlag;

	}

	private void doSynchronousUser(BpmRest bpmRestServices,
			List<IpUser> findUserByHirerId) throws RestException {

		  if(findUserByHirerId.size()==0){
			  return;
		  }
		
		// 如果bpm没有同步，那么关联表也没有同步
		for (IpUser ipUser : findUserByHirerId) {
			List<UserResponse> user_list = new ArrayList<UserResponse>();
			String tenantLimitId = bpmRestServices.getIdentityService()
					.getBaseParam().getTenantLimitId();
			UserQueryParam userQueryParam = new UserQueryParam();
			userQueryParam.setCode(ipUser.getUserCode());
			userQueryParam.setTenantId(tenantLimitId);
			JsonNode jsonNode = null;
			
				jsonNode = (JsonNode) bpmRestServices.getIdentityService()
						.queryUsers(userQueryParam);
				ArrayNode arrayNode = BaseUtils.getData(jsonNode);
				for (int i = 0; arrayNode != null && i < arrayNode.size(); i++) {
					UserResponse resp = jsonResultService.toObject(arrayNode
							.get(i).toString(), UserResponse.class);
					user_list.add(resp);
				}
				if(ipUser.getUserType().equals("0")){
					continue;
				}
				IpUserCompany findIpUserCompanyByUserId = ipUserCompanyDao
						.findIpUserCompanyByUserId(ipUser.getUserId());
				IpUserRole findIpUserRoleByUserIdAndIsPartTime = ipUserRoleDao
						.findIpUserRoleByUserIdAndIsPartTime(
								ipUser.getUserId(), "0");
				String coId = findIpUserCompanyByUserId.getCoId();
				IpCompany findByCoId = coDao.findByCoId(coId);
				if (user_list.size() == 0) {
					// 没有同步

					UserResourceParam bpmUserParam = getBpmUserParam(ipUser,
							findByCoId, bpmRestServices, tenantLimitId);
					JsonNode bpm_user_id = (JsonNode) bpmRestServices
							.getIdentityService().saveUser(bpmUserParam);
					/*ArrayNode bpm_user_arrayNode = BaseUtils
							.getData(bpm_user_id);
					UserResponse resp = jsonResultService.toObject(
							bpm_user_arrayNode.get(0).toString(),
							UserResponse.class);*/
					String jsonInfo_id=bpm_user_id.findValues("id").get(0).toString();
					jsonInfo_id = jsonInfo_id.substring(1, jsonInfo_id.length()-1);
					saveOrgLink(bpmRestServices, tenantLimitId,
							findIpUserCompanyByUserId, jsonInfo_id);

					saveUserGroupLink(bpmRestServices, tenantLimitId,
							findIpUserRoleByUserIdAndIsPartTime, jsonInfo_id);
				} else {
					for (UserResponse userResponse : user_list) {
						UserResourceParam bpmUserParam = getBpmUserParam(
								ipUser, findByCoId, bpmRestServices,
								tenantLimitId);
						bpmUserParam.setId(userResponse.getId());
						bpmUserParam.setRevision(userResponse.getRevision());
						bpmRestServices.getIdentityService().saveUser(
								bpmUserParam);
						
						//begin_更新userCode为最新_zhangbch
						  ipUser.setUserCode(ipUser.getLoginName());
						  userDao.save(ipUser);
						//end_更新userCode为最新_zhangbch

						saveOrgChange(bpmRestServices, tenantLimitId,
								userResponse, findIpUserCompanyByUserId);

						saveUserGroupChange(bpmRestServices, tenantLimitId,
								userResponse,
								findIpUserRoleByUserIdAndIsPartTime);
					}
				}

			
		}

	}

	private void saveUserGroupChange(BpmRest bpmRestServices,
			String tenantLimitId, UserResponse userResponse,
			IpUserRole findIpUserRoleByUserIdAndIsPartTime) {
		List<UserLinkResponse> user_link_list = new ArrayList<UserLinkResponse>();
		UserLinkQueryParam userLinkQueryParam = new UserLinkQueryParam();
		userLinkQueryParam.setUserId(userResponse.getId());
		JsonNode jsonNode = null;
		try {
			jsonNode = (JsonNode) bpmRestServices.getIdentityService()
					.queryUserLinks(userLinkQueryParam);
			ArrayNode arrayNode = BaseUtils.getData(jsonNode);
			for (int i = 0; arrayNode != null && i < arrayNode.size(); i++) {
				UserLinkResponse resp = jsonResultService.toObject(arrayNode
						.get(i).toString(), UserLinkResponse.class);
				user_link_list.add(resp);
			}

			for (UserLinkResponse userLinkResponse : user_link_list) {
				if ("usergroup".equals(userLinkResponse.getType())) {
					UserLinkResourceParam userLinkResourceParam = getBpmRoleparam(
							userLinkResponse.getUserId(), bpmRestServices,
							findIpUserRoleByUserIdAndIsPartTime, tenantLimitId,
							"usergroup");
					userLinkResourceParam.setId(userLinkResponse.getId());
					userLinkResourceParam.setRevision(userLinkResponse
							.getRevision());
					userLinkResourceParam.setUserId(userLinkResponse
							.getUserId());
					bpmRestServices.getIdentityService().saveUserLink(
							userLinkResourceParam);
				}
			}
		} catch (RestException e) {
			e.printStackTrace();
		}

	}

	private void saveOrgChange(BpmRest bpmRestServices, String tenantLimitId,
			UserResponse userResponse, IpUserCompany findIpUserCompanyByUserId)
			throws RestException {
		List<UserLinkResponse> user_link_list = new ArrayList<UserLinkResponse>();
		UserLinkQueryParam userLinkQueryParam = new UserLinkQueryParam();
		userLinkQueryParam.setUserId(userResponse.getId());
		JsonNode jsonNode = (JsonNode) bpmRestServices.getIdentityService()
				.queryUserLinks(userLinkQueryParam);
		ArrayNode arrayNode = BaseUtils.getData(jsonNode);
		for (int i = 0; arrayNode != null && i < arrayNode.size(); i++) {
			UserLinkResponse resp = jsonResultService.toObject(arrayNode.get(i)
					.toString(), UserLinkResponse.class);
			user_link_list.add(resp);
		}

		for (UserLinkResponse userLinkResponse : user_link_list) {
			if ("org".equals(userLinkResponse.getType())) {
				UserLinkResourceParam userLinkResourceParam = getUserLinkParam(
						userLinkResponse.getUserId(), bpmRestServices,
						findIpUserCompanyByUserId, tenantLimitId, "org");
				userLinkResourceParam.setId(userLinkResponse.getId());
				userLinkResourceParam.setRevision(userLinkResponse
						.getRevision());
				userLinkResourceParam.setUserId(userLinkResponse.getUserId());
				bpmRestServices.getIdentityService().saveUserLink(
						userLinkResourceParam);
			}
		}
	}

	private void saveUserGroupLink(BpmRest bpmRestServices,
			String tenantLimitId,
			IpUserRole findIpUserRoleByUserIdAndIsPartTime, String bpm_uid) {
		UserLinkResourceParam userLinkResourceParam = getBpmRoleparam(
				bpm_uid, bpmRestServices,
				findIpUserRoleByUserIdAndIsPartTime, tenantLimitId, "usergroup");

		try {
			// 先查询再删除冗余数据，再插入
			UserLinkQueryParam userLinkQueryParam = new UserLinkQueryParam();
			userLinkQueryParam.setType("usergroup");
			userLinkQueryParam.setUserId(bpm_uid);
			userLinkQueryParam.setTenantId(tenantLimitId);
			JsonNode jsonNode = (JsonNode) bpmRestServices.getIdentityService()
					.queryUserLinks(userLinkQueryParam);
			List<JsonNode> findValues = jsonNode.findValues("id");
			for (JsonNode jsonNode2 : findValues) {
				String jsonInfo_id = jsonNode2.toString();
				String linkId = jsonInfo_id.substring(1,
						jsonInfo_id.length() - 1);
				// 按linkId删除冗余数据
				bpmRestServices.getIdentityService().deleteUserLink(linkId);
			}

			// 插入新数据
			bpmRestServices.getIdentityService().saveUserLink(
					userLinkResourceParam);

		} catch (RestException e) {
			e.printStackTrace();
		}
	}

	private void saveOrgLink(BpmRest bpmRestServices, String tenantLimitId,
			IpUserCompany findIpUserCompanyByUserId, String bpm_uid) {

		UserLinkResourceParam userLinkResourceParam_org = getUserLinkParam(
				bpm_uid, bpmRestServices, findIpUserCompanyByUserId,
				tenantLimitId, "org");
		try {

			// 先查询再删除冗余数据，再插入
			UserLinkQueryParam userLinkQueryParam = new UserLinkQueryParam();
			userLinkQueryParam.setType("org");
			userLinkQueryParam.setUserId(bpm_uid);
			userLinkQueryParam.setTenantId(tenantLimitId);
			JsonNode orgLink_jsonNode = (JsonNode) bpmRestServices
					.getIdentityService().queryUserLinks(userLinkQueryParam);
			List<JsonNode> findValues = orgLink_jsonNode.findValues("id");
			for (JsonNode jsonNode2 : findValues) {
				String jsonInfo_id = jsonNode2.toString();
				String linkId = jsonInfo_id.substring(1,
						jsonInfo_id.length() - 1);
				// 按linkId删除冗余数据
				bpmRestServices.getIdentityService().deleteUserLink(linkId);
			}
			// 插入新数据
			bpmRestServices.getIdentityService().saveUserLink(
					userLinkResourceParam_org);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private UserLinkResourceParam getUserLinkParam(String userId,
			BpmRest bpmRestServices, IpUserCompany setProperties,
			String tenantLimitId, String type) {
		UserLinkResourceParam userLinkResourceParam = new UserLinkResourceParam();
		userLinkResourceParam.setType(type);
		userLinkResourceParam.setUserId(userId);
		// userLinkResourceParam.setTenantId(tenantLimitId);//设置租户id

		// 获得bpm中的部门id
		IpCompany findByCoId = coDao.findByCoId(setProperties.getCoId());
		OrgQueryParam orgQueryParam = new OrgQueryParam();
		orgQueryParam.setTenantId(tenantLimitId);
		orgQueryParam.setName(findByCoId.getCoName());
		orgQueryParam.setCode(findByCoId.getCoCode());
		try {
			JsonNode org_jsonNode = (JsonNode) bpmRestServices
					.getIdentityService().queryOrgs(orgQueryParam);
			String co_id = org_jsonNode.findValues("id").get(0).toString();
			co_id = co_id.substring(1, co_id.length() - 1);
			userLinkResourceParam.setTargetId(co_id);
		} catch (RestException e1) {
			e1.printStackTrace();
		}
		return userLinkResourceParam;
	}

	private UserLinkResourceParam getBpmRoleparam(String userId,
			BpmRest bpmRestServices, IpUserRole setRoleProperties,
			String tenantLimitId, String type) {
		UserLinkResourceParam userLinkResourceParam = new UserLinkResourceParam();
		userLinkResourceParam.setType(type);
		userLinkResourceParam.setUserId(userId);
		// userLinkResourceParam.setTenantId(tenantLimitId);

		IpRole findByRoleId = iPRoleDao.findByRoleId(setRoleProperties
				.getRoleId());
		if (findByRoleId == null) {
			userLinkResourceParam.setTargetId("未分配");
			return userLinkResourceParam;
		} else {
			UserGroupQueryParam userGroupQueryParam = new UserGroupQueryParam();
			userGroupQueryParam.setTenantId(tenantLimitId);
			userGroupQueryParam.setName(findByRoleId.getRoleName());
			try {
				JsonNode userGroup_jsonNode = (JsonNode) bpmRestServices
						.getIdentityService().queryUserGroups(
								userGroupQueryParam);
				String role_id = userGroup_jsonNode.findValues("id").get(0)
						.toString();
				role_id = role_id.substring(1, role_id.length() - 1);
				userLinkResourceParam.setTargetId(role_id);
			} catch (RestException e1) {
				e1.printStackTrace();
			}
			return userLinkResourceParam;
		}

	}

	private UserResourceParam getBpmUserParam(IpUser ipUserEntity,
			IpCompany findByCoId, BpmRest bpmRestServices, String tenantLimitId) {
		UserResourceParam userResourceParam = new UserResourceParam();
		// userResourceParam.setCode(ipUserEntity.getUserName());
		userResourceParam.setCode(ipUserEntity.getLoginName());// bpm的code具有唯一性，这边的用户名称没有唯一性，会导致异常，所以换成登录名
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
			JsonNode org_jsonNode = (JsonNode) bpmRestServices
					.getIdentityService().queryOrgs(orgQueryParam);
			String co_id = org_jsonNode.findValues("id").get(0).toString();
			co_id = co_id.substring(1, co_id.length() - 1);
			userResourceParam.setOrg(co_id);
		} catch (RestException e1) {
			e1.printStackTrace();
		}
		return userResourceParam;
	}

	private void doSynchronousOrg(BpmRest bpmRestServices,
			List<IpCompany> findCompanyByHirerId) throws RestException {

		if(findCompanyByHirerId.size()==0){
			return;
		}
		
		 //begin_同步删除部门数据_20161219
				IpCompany ipCompany2 = new IpCompany();
				ipCompany2.setHirerId(findCompanyByHirerId.get(0).getHirerId());
				List<IpCompany> findCompanySynByCondition = ipCompanySynMapper.findCompanySynByCondition(ipCompany2);
				for (IpCompany ipCompany : findCompanySynByCondition) {
					//获得bpm的冗余部门
					OrgQueryParam orgQueryParam = new OrgQueryParam();
					 //begin_添加查询参数
					   orgQueryParam.setCode(ipCompany.getCoCodeTmp());
					   orgQueryParam.setTenantId(ipCompany.getHirerId());
					 //end_添加查询参数
					JsonNode companyJsonNode = (JsonNode) bpmRestServices
								.getIdentityService().queryOrgs(orgQueryParam);
						ArrayNode companyArrayNode = BaseUtils.getData(companyJsonNode);
						for (int i = 0; companyArrayNode != null
								&& i < companyArrayNode.size(); i++) {
							OrgResponse resp = jsonResultService.toObject(
									companyArrayNode.get(i).toString(),
									OrgResponse.class);
							//获得部门id,查询部门-人员关联
							String target_id = resp.getId();
							 UserLinkQueryParam userLinkQueryParam = new UserLinkQueryParam();
							 userLinkQueryParam.setTenantId(ipCompany.getHirerId());
							 userLinkQueryParam.setOrgId(target_id);
							 userLinkQueryParam.setType("org");
							 JsonNode jsonNodeLink = (JsonNode)bpmRestServices.getIdentityService().queryUserLinks(userLinkQueryParam);
							 ArrayNode arrayNodeLink = BaseUtils.getData(jsonNodeLink);
							 for (int k = 0; arrayNodeLink != null && k < arrayNodeLink.size(); k++) {
								 UserLinkResponse resp_1 =jsonResultService.toObject(arrayNodeLink.get(k).toString(), UserLinkResponse.class);
								 String userId = resp_1.getUserId();
								
								 //删除部门-人员关联
								 bpmRestServices.getIdentityService().deleteUserLink(resp_1.getId());
								 
								 //根据userId删除冗余用户
								 bpmRestServices.getIdentityService().deleteUser(userId);
								 
								 UserLinkQueryParam userLinkQueryParam2 = new UserLinkQueryParam();
								 userLinkQueryParam2.setTenantId(ipCompany.getHirerId());
								 userLinkQueryParam.setUserId(userId);
								 userLinkQueryParam2.setType("usergroup");
								 JsonNode jsonNodeLink2 = (JsonNode)bpmRestServices.getIdentityService().queryUserLinks(userLinkQueryParam2);
								 ArrayNode arrayNodeLink2 = BaseUtils.getData(jsonNodeLink2);
								 for (int k1 = 0; arrayNodeLink2 != null && k1 < arrayNodeLink2.size(); k1++) {
									 //删除角色-人员关联
									   bpmRestServices.getIdentityService().deleteUserLink(jsonResultService.toObject(arrayNodeLink2.get(k1).toString(), UserLinkResponse.class).getId());
								 }
							 }
							 //删除部门
							 bpmRestServices.getIdentityService().deleteOrg(resp.getId());
						}
				}
				ipCompanySynMapper.delete(ipCompany2.getHirerId());
		 //end_同步删除部门数据_20161219
		
		for (IpCompany ipCompany : findCompanyByHirerId) {
			List<OrgResponse> org_list = new ArrayList<OrgResponse>();
			OrgQueryParam orgQueryParam = new OrgQueryParam();
			orgQueryParam.setCode(ipCompany.getCoCode());
			 //begin_添加查询参数
			   orgQueryParam.setCode(ipCompany.getCoCodeTmp());
			   orgQueryParam.setTenantId(ipCompany.getHirerId());
			 //end_添加查询参数
			JsonNode companyJsonNode = null;
			
				companyJsonNode = (JsonNode) bpmRestServices
						.getIdentityService().queryOrgs(orgQueryParam);
				ArrayNode companyArrayNode = BaseUtils.getData(companyJsonNode);
				for (int i = 0; companyArrayNode != null
						&& i < companyArrayNode.size(); i++) {
					OrgResponse resp = jsonResultService.toObject(
							companyArrayNode.get(i).toString(),
							OrgResponse.class);
					org_list.add(resp);
				}
				if (org_list.size() == 0) {// 证明没有同步
					OrgResourceParam orgResourceParam = new OrgResourceParam();
					orgResourceParam.setEnable(true);
					orgResourceParam.setCode(ipCompany.getCoCode());
					orgResourceParam.setName(ipCompany.getCoName());
					orgResourceParam.setCreator(ipCompany.getHirerId());
					bpmRestServices.getIdentityService().saveOrg(
							orgResourceParam);
				} else {
					for (OrgResponse orgResponse : org_list) {
						saveOrg(bpmRestServices, ipCompany, orgResponse);
						//begin_同步更新ip_company的coCodeTmp_20161215
							ipCompany.setCoCodeTmp(ipCompany.getCoCode());
							coDao.save(ipCompany);
						//end_同步更新ip_company的coCodeTmp_20161215
						
						
					}
				}
			
		}

		// 重新设置父级id
		for (IpCompany p_company : findCompanyByHirerId) {
			for (IpCompany c_company : findCompanyByHirerId) {
				if (p_company.getCoId().equals(c_company.getParentCoId())) {
					List<OrgResponse> p_orgList = getOrgList(bpmRestServices,
							p_company);
					List<OrgResponse> c_orgList = getOrgList(bpmRestServices,
							c_company);
					for (OrgResponse orgResponse : c_orgList) {
						try {
							orgResponse.setParent(p_orgList.get(0).getId());
							orgResponse.setParentCode(p_orgList.get(0)
									.getCode());
							saveOrg(bpmRestServices, c_company, orgResponse);
						} catch (RestException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	private void saveOrg(BpmRest bpmRestServices, IpCompany ipCompany,
			OrgResponse orgResponse) throws RestException {
		OrgResourceParam orgResourceParam = new OrgResourceParam();
		orgResourceParam.setId(orgResponse.getId());
		orgResourceParam.setRevision(orgResponse.getRevision());
		orgResourceParam.setEnable(true);
		orgResourceParam.setCode(ipCompany.getCoCode());
		orgResourceParam.setName(ipCompany.getCoName());
		orgResourceParam.setCreator(orgResponse.getTenantId());
		orgResourceParam.setParent(orgResponse.getParent());
		orgResourceParam.setParentCode(orgResponse.getParentCode());
		bpmRestServices.getIdentityService().saveOrg(orgResourceParam);
	}

	private List<OrgResponse> getOrgList(BpmRest bpmRestServices,
			IpCompany p_company) {
		List<OrgResponse> reset_list = new ArrayList<OrgResponse>();
		OrgQueryParam orgQueryParam = new OrgQueryParam();
		orgQueryParam.setCode(p_company.getCoCode());
		JsonNode companyJsonNode = null;
		try {
			companyJsonNode = (JsonNode) bpmRestServices.getIdentityService()
					.queryOrgs(orgQueryParam);
			ArrayNode companyArrayNode = BaseUtils.getData(companyJsonNode);
			for (int i = 0; companyArrayNode != null
					&& i < companyArrayNode.size(); i++) {
				OrgResponse resp = jsonResultService.toObject(companyArrayNode
						.get(i).toString(), OrgResponse.class);
				reset_list.add(resp);
			}
			return reset_list;

		} catch (RestException e) {
			e.printStackTrace();
		}
		return reset_list;
	}

	

	private void doSynchronousRole(List<IpRole> roleInfoByHirerId,
			BpmRest bpmRestServices) throws RestException {
		
		//begin_同步删除冗余角色_20161219
		  IpRole ipRole2 = new IpRole();
		  String operatorID = bpmRestServices.getIdentityService().getBaseParam().getOperatorID();
		  ipRole2.setHirerId(operatorID);
		  List<IpRole> findRoleSynByCondition = ipRoleSynMapper.findRoleSynByCondition(ipRole2);
		  for (IpRole ipRole : findRoleSynByCondition) {
			  UserGroupQueryParam userGroupQueryParam = new UserGroupQueryParam();
			  userGroupQueryParam.setCode(ipRole.getRoleCode());
			  userGroupQueryParam.setTenantId(ipRole.getHirerId());
			  JsonNode  jsonNode = (JsonNode)bpmRestServices.getIdentityService().queryUserGroups(userGroupQueryParam);
				 String bpm_roleId=jsonNode.findValues("id").get(0).toString();
				 bpm_roleId = bpm_roleId.substring(1, bpm_roleId.length()-1);
				//3.根据id删除bpm对应的用户组
				  bpmRestServices.getIdentityService().deleteUserGroup(bpm_roleId);
		}
		  ipRoleSynMapper.delete(ipRole2.getHirerId());
		  
	  //end_同步删除冗余角色_20161219
		
		  if(roleInfoByHirerId.size()==0){
			  return;
		  }
		  
		  
		
		for (IpRole IpRole : roleInfoByHirerId) {
			List<UserGroupResponse> userGroup_list = new ArrayList<UserGroupResponse>();
			UserGroupQueryParam userGroupQueryParam = new UserGroupQueryParam();
			userGroupQueryParam.setCode(IpRole.getRoleCode());
			//userGroupQueryParam.setName(IpRole.getRoleName());
			JsonNode roleJsonNode = (JsonNode) bpmRestServices
					.getIdentityService().queryUserGroups(userGroupQueryParam);
			ArrayNode roleArrayNode = BaseUtils.getData(roleJsonNode);
			for (int i = 0; roleArrayNode != null && i < roleArrayNode.size(); i++) {
				UserGroupResponse resp = jsonResultService.toObject(
						roleArrayNode.get(i).toString(),
						UserGroupResponse.class);
				userGroup_list.add(resp);
			}
			// 判断和bpm的code是否一样
			if (userGroup_list.size() == 0) {//没有数据需要同步
				UserGroupResourceParam userGroupResourceParam = new UserGroupResourceParam();
				userGroupResourceParam.setName(IpRole.getRoleName());
				userGroupResourceParam.setCode(IpRole.getRoleCode());// 设置ip角色code
				bpmRestServices.getIdentityService().saveUserGroup(
						userGroupResourceParam);
			} else {// IP更新 BPM没同步
				UserGroupResourceParam userGroupResourceParam = new UserGroupResourceParam();
				userGroupResourceParam.setName(IpRole.getRoleName());
				userGroupResourceParam.setCode(IpRole.getRoleCode());// 设置ip角色code
				userGroupResourceParam.setId(userGroup_list.get(0).getId());
				userGroupResourceParam.setRevision(userGroup_list.get(0)
						.getRevision());
				bpmRestServices.getIdentityService().saveUserGroup(
						userGroupResourceParam);
			}
		}
	}

	private void doSynchronousHirer(IpHirer ipHirer,
			BpmRest bpmRestServices) throws RestException {
		/*List<TenantResponse> tenant_list = new ArrayList<TenantResponse>();
		
	    //查询bpm的租户信息
		TenantQueryParam tenantQueryParam = new TenantQueryParam();
		tenantQueryParam.setCode(ipHirer.getHirerNo());
		tenantQueryParam.setName(ipHirer.getCellphoneNo());
		tenantQueryParam.setEnable(true);
		tenantQueryParam.setTenantId(ipHirer.getHirerId());
		JsonNode TenantJsonNode = (JsonNode) bpmRestServices.getIdentityService().getTenant("test");
		
		ArrayNode arrayNode = BaseUtils.getData(TenantJsonNode);
		for (int i = 0; arrayNode != null && i < arrayNode.size(); i++) {
			TenantResponse resp = jsonResultService.toObject(arrayNode.get(i)
					.toString(), TenantResponse.class);
			tenant_list.add(resp);
		}*/
		
		
		IpHirer findHirerSynByCondition = ipHirerSynMapper.findHirerSynByCondition(ipHirer);
		if (findHirerSynByCondition ==null) {
				// 证明没有同步过去了，需要同步过去
					TenantResourceParam tenantResourceParam = new TenantResourceParam();
					tenantResourceParam.setCode(ipHirer.getHirerNo());
					tenantResourceParam.setName(ipHirer.getHirerName());
					tenantResourceParam.setAddress(ipHirer.getAddress());
					tenantResourceParam.setEnable(true);
					tenantResourceParam.setParent("test");
					JsonNode jsonNode = (JsonNode) bpmRestServices
							.getIdentityService().saveTenant(
									tenantResourceParam);
					ipHirerSynMapper.insert(ipHirer);
					
					// begin_需要建一个租户管理员管理员
					UserResourceParam userResourceParam = new UserResourceParam();
					userResourceParam.setCode(ipHirer.getLoginName());
					userResourceParam.setName(ipHirer.getHirerName());
					// userResourceParam.setOrgCode(findByCoId.getCoCode());
					userResourceParam.setPassword(ipHirer.getPassword());
					userResourceParam.setSalt(ipHirer.getSalt());
					userResourceParam.setEnable(true);
					userResourceParam.setPhone(ipHirer.getCellphoneNo());
					userResourceParam.setMail(ipHirer.getEmail());
					userResourceParam.setSource("source");
					userResourceParam.setSysadmin(true);
					userResourceParam.setOrg("空");
					userResourceParam.setOrgCode("空");
					bpmRestServices.getIdentityService().saveUser(
							userResourceParam);
					// end_需要建一个租户管理员管理员
		}
		//return null;
	}
}
