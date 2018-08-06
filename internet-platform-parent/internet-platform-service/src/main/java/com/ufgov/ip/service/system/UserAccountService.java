package com.ufgov.ip.service.system;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import yonyou.bpm.rest.BpmRest;
import yonyou.bpm.rest.exception.RestException;
import yonyou.bpm.rest.request.identity.OrgQueryParam;
import yonyou.bpm.rest.request.identity.UserGroupQueryParam;
import yonyou.bpm.rest.request.identity.UserLinkQueryParam;
import yonyou.bpm.rest.request.identity.UserLinkResourceParam;
import yonyou.bpm.rest.request.identity.UserQueryParam;
import yonyou.bpm.rest.request.identity.UserResourceParam;
import yonyou.bpm.rest.request.task.TaskQueryParam;
import yonyou.bpm.rest.response.identity.UserLinkResponse;
import yonyou.bpm.rest.response.identity.UserResponse;
import yonyou.bpm.rest.utils.BaseUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ufgov.ip.api.sysmanager.IpCompanyServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.dao.sysmanager.ICompanyDao;
import com.ufgov.ip.dao.sysmanager.IPRoleDao;
import com.ufgov.ip.dao.sysmanager.IpUserCompanyDao;
import com.ufgov.ip.dao.sysmanager.IpUserRoleDao;
import com.ufgov.ip.dao.system.IpHirerMapper;
import com.ufgov.ip.dao.system.IuapUserDao;
import com.ufgov.ip.dao.systemimpl.IuapUserJdbcDaoImpl;
import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.sysmanager.IpRole;
import com.ufgov.ip.entity.sysmanager.IpUserRole;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.IpUserCompany;
import com.ufgov.ip.service.base.IPDataTableService;
import com.ufgov.ip.utils.CopyPropertiesUtil;
import com.ufgov.ip.utils.ProduceCodeUtil;
import com.ufgov.ip.utils.PropertyUtilSys;
import com.ufgov.ip.utils.UUIDTools;

/**
 * 用户管理服务层
 * 书写readonly是说明没有特殊标识服务层只有只读事物
 * @author guangsa
 *
 */
@Service(version = "0.0.1")
@Transactional(readOnly=true)
public class UserAccountService implements UserAccountServiceI{

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;

	private static Logger logger = LoggerFactory.getLogger(UserAccountService.class);

	@Autowired
	private IuapUserDao userDao;
	
	@Autowired
	private IPRoleDao iPRoleDao;
	
	@Autowired
	private ICompanyDao coDao;
	
	@Autowired
	private IpUserCompanyDao ipUserCompanyDao;
	
	@Autowired
	private IpUserRoleDao ipUserRoleDao;
	
	
	@Autowired
    private IuapUserJdbcDaoImpl iuapUserJdbcDao;
	
	
	@Autowired
	private IPDataTableService ipDataTableService;
	
	
	@Autowired
	private IpHirerMapper hirerDao;
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	private JsonResultService jsonResultService;
	
	@Autowired
	private IpCompanyServiceI ipCompanyService;

	public JsonResultService getJsonResultService() {
		return jsonResultService;
	}

	public ProcessService getProcessService() {
		return processService;
	}
	
	
	
	public void setHirerDao(IpHirerMapper hirerDao) {
		this.hirerDao = hirerDao;
	}
	
	
	/*@Autowired
	private IuapHirerDao hirerDao;*/
	
	
	
	/*public void setHirerDao(IuapHirerDao hirerDao) {
		this.hirerDao = hirerDao;
	}*/

	public IpUserCompanyDao getIpUserCompanyDao() {
		return ipUserCompanyDao;
	}


	public IpUserRoleDao getIpUserRoleDao() {
		return ipUserRoleDao;
	}


	public IPRoleDao getiPRoleDao() {
		return iPRoleDao;
	}


	public void setiPRoleDao(IPRoleDao iPRoleDao) {
		this.iPRoleDao = iPRoleDao;
	}


	public ICompanyDao getCoDao() {
		return coDao;
	}


	public void setCoDao(ICompanyDao coDao) {
		this.coDao = coDao;
	}


	public IuapUserDao getUserDao() {
		return userDao;
	}


	public void setUserDao(IuapUserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * 根据用户名称得到相应的用户信息
	 * @param loginName
	 * @return
	 */
	public IpUser findUserByLoginName(String loginName) {
		return userDao.findByLoginName(loginName);
	}

	
	/**
	 * 根据用户手机得到相应的用户信息
	 * @param loginName
	 * @return
	 */
	public IpUser findUserByCellphoneNo(String cellphoneNo) {
		return userDao.findUserByCellphoneNo(cellphoneNo);
	}
	public IpUser findUserByHirerIdAndUserType(String hirerId,String userType) {
		return userDao.findUserByHirerIdAndUserType(hirerId,userType);
	}
	/**
	 * 根据用户邮箱得到相应的用户信息
	 * @param loginName
	 * @return
	 */
	public IpUser findUserByUserEmail(String userEmail) {
		return userDao.findUserByUserEmail(userEmail);
	}
	public IpUser findUserByUserEmailOrLoginNameOrCellphoneNo(String logicName) {
		return userDao.findUserByUserEmailOrLoginNameOrCellphoneNo(logicName);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public String saveIpUserEntity(IpUser ipUserEntity,
			IpCompany ic, IpRole ir,List list,Map<String, String> resultMap) {

        IpUser ipUser=checkIpUser(ipUserEntity,resultMap);
        IpUser selUser=new IpUser();
        IpUser no_cache=new IpUser();
        IpHirer ipHirer=new IpHirer();
        ipHirer.setHirerId(ipUserEntity.getHirerId());
        //IpHirer findHirerByHirerId = hirerDao.findAll(ipUserEntity.getHirerId()).get(0);
        IpHirer findHirerByHirerId = hirerDao.findHirerByCondition(ipHirer);
        String userId="";
        String getbpmEnabledByKey = PropertyUtilSys.getbpmEnabledByKey("bpm.enabled");//获得工作流开/关状态值
        List<UserResponse> user_list = new ArrayList<UserResponse>();
        BpmRest bpmRestServices = processService.getBpmRestServices(ipUserEntity.getHirerId(),ipUserEntity.getHirerId());
		if(ipUser==null){
			ipUserEntity.setUserId(UUIDTools.uuidRandom());
			CopyPropertiesUtil.setProperty(ipUserEntity);
			selUser=ipUserEntity;
			String userCode = ProduceCodeUtil.getCode();//生成用户code
			ipUserEntity.setUserCode(ipUserEntity.getLoginName());
			/**
			 *  参数        参数类型	必须	      说明
				code	 String	  是	   用户编码
				name	 String	  是	   用户名称
				org	     String	  是	   部门id(id和编码，提供一个即可)
				orgCode	 String	  是	   部门编码
				password String	  否	   密码，默认为code
				salt	 String	  否	   密码“盐度”值，默认为根据password随机生成
				enable	 Boolean 否	   是否启用，默认为true
				phone	 String	  否	   手机号
				mail	 String	  否	   电子邮件
				sysadmin Boolean 否	   是否管理员，默认为false
				source	 String	  否	   来源
				revision Integer 否	   保存时revision=0，更新时revision>0，更新时id不能为空。保存提供批量方法，更新不提供

			 */
			//begin_restful_通过rest服务同步BPM的user表
		      if("true".equals(getbpmEnabledByKey)){
		    	  userId = InsertOrUpdateBpmUser(ipUserEntity, ic, ir);
		      }
		   //end_restful_通过rest服务同步BPM的user表
				userDao.save(ipUserEntity);
		}else{
			String bpm_userCode=ipUser.getLoginName();
			CopyPropertiesUtil.copyProperty(ipUser, ipUserEntity);
			selUser=ipUser;
			
			 String coId = ic.getCoId();
			 IpCompany findByCoId = coDao.findByCoId(coId);
			//begin_restful_同步更新bpm的用户表信息
				 if("true".equals(getbpmEnabledByKey)){
				    //1.先按照usercode查询bpm的user信息
					UserQueryParam userQueryParam = new UserQueryParam();
					//目前通过后端查出当前用户的code(其实应该前端传过来(待改进))
					//IpUser findUserByUserId = userDao.findUserByUserId(ipUser.getUserId());
					userQueryParam.setCode(bpm_userCode);
					userQueryParam.setPhone(ipUser.getCellphoneNo());
					//BpmRest bpmRestServices=processService.getBpmRestServices(ipUser.getHirerId(),ipUser.getHirerId());
					String tenantLimitId = bpmRestServices.getIdentityService().getBaseParam().getTenantLimitId();
					 try {
							JsonNode jsonNode = (JsonNode)bpmRestServices.getIdentityService().queryUsers(userQueryParam);
							 ArrayNode arrayNode = BaseUtils.getData(jsonNode);
							 for (int i = 0; arrayNode != null && i < arrayNode.size(); i++) {
								 UserResponse resp =jsonResultService.toObject(arrayNode.get(i).toString(), UserResponse.class);
								 user_list.add(resp);
						        }
							 
							 //2.根据同步bpm_id_user表的公用信息
							 for (UserResponse userResponse : user_list) {
								
								 UserResourceParam bpmUserParam = getBpmUserParam(ipUserEntity, findByCoId, bpmRestServices, tenantLimitId);
								 bpmUserParam.setId(userResponse.getId());
								 bpmUserParam.setRevision(userResponse.getRevision());
								 bpmRestServices.getIdentityService().saveUser(bpmUserParam);
							 }
							
						} catch (RestException e) {
							e.printStackTrace();
						}
					
				 }
			//end_restful_同步更新bpm的用户表信息
			userDao.save(ipUser);
		}
			//添加_部门
			IpUserCompany setProperties = setCoProperties(ic,selUser);
			//添加_职务
			IpUserRole setRoleProperties = setRoleProperties(ir,setProperties.getCoId(), selUser, "0");
			
			//保存职务、部门关联信息
			IpUserCompany checkIpUserCompany = checkIpUserCompany(setProperties,resultMap);
			if(checkIpUserCompany==null){
				ipUserCompanyDao.save(setProperties);
				
				/**
				 * 
				 * UserLinkResourceParam
				 *   参数	     参数类型	必须  	说明
					type	   String	是	关联类型(org/usergroup)
					userId	   String	是	用户Id
					targetId   String	是	部门/用户组id(id和编码，提供一个即可)
					targetCode String	是	部门/用户组编码
					enable	   Boolean	否	是否启用，默认为true
					revision   Integer	否	保存时revision=0，更新时revision>0，更新时id不能为空。保存提供批量方法，更新不提供

				 */
				//begin_restful_创建用户关联关系(关联部门)
			      if("true".equals(getbpmEnabledByKey)){//开启工作流
					 // BpmRest bpmRestServices = processService.getBpmRestServices(ipUserEntity.getHirerId(),ipUserEntity.getHirerId());
					  String tenantLimitId = bpmRestServices.getIdentityService().getBaseParam().getTenantLimitId();
					  UserLinkResourceParam userLinkResourceParam = getUserLinkParam(
							userId, bpmRestServices, setProperties,
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
			   }
				//end_restful_创建用户关联关系(关联部门)
				
			}else{
				
				//begin_restful_同步更新用户关联表(关联部门)
					 if("true".equals(getbpmEnabledByKey)){
						//根据当前用户code获得bpm对应的user信息，然后获得userId，最后根据userId查询出bpmuser关联表的数据，根据type获得对应的关联信息
						   //1.获得bpm的user信息
						 List<UserLinkResponse> user_link_list = new ArrayList<UserLinkResponse>();
						 String tenantLimitId = bpmRestServices.getIdentityService().getBaseParam().getTenantLimitId();
						 for (UserResponse userResponse : user_list) {
							 String bpm_id = userResponse.getId();
							 //2.根据bpm的userId查询用户关联表
							 UserLinkQueryParam userLinkQueryParam = new UserLinkQueryParam();
							 userLinkQueryParam.setUserId(bpm_id);
							 try {
								JsonNode jsonNode = (JsonNode)bpmRestServices.getIdentityService().queryUserLinks(userLinkQueryParam);
								ArrayNode arrayNode = BaseUtils.getData(jsonNode);
								 for (int i = 0; arrayNode != null && i < arrayNode.size(); i++) {
									 UserLinkResponse resp =jsonResultService.toObject(arrayNode.get(i).toString(), UserLinkResponse.class);
									 user_link_list.add(resp);
							        }
								 
								 for (UserLinkResponse userLinkResponse : user_link_list) {
									 if("org".equals(userLinkResponse.getType())){
										 UserLinkResourceParam userLinkResourceParam = getUserLinkParam(
													userId, bpmRestServices, setProperties,
													tenantLimitId,"org");
											  userLinkResourceParam.setId(userLinkResponse.getId());
											  userLinkResourceParam.setRevision(userLinkResponse.getRevision());
											  userLinkResourceParam.setUserId(userLinkResponse.getUserId());
							    			  bpmRestServices.getIdentityService().saveUserLink(userLinkResourceParam);
									 }
								 }
								
							} catch (RestException e) {
								e.printStackTrace();
							}
						 }
					 }
				//end_restful_同步更新用户关联表(关联部门)
				
				setProperties.setTheId(checkIpUserCompany.getTheId());
				CopyPropertiesUtil.copyProperty(checkIpUserCompany, setProperties);
				ipUserCompanyDao.save(checkIpUserCompany);
			}
			
			IpUserRole checkIpUserRole = checkIpUserRole(setRoleProperties,resultMap);
			if(checkIpUserRole==null){
				//setProperties.setCoId(checkIpUserCompany.getCoId());
				ipUserRoleDao.save(setRoleProperties);
				
				//begin_restful_创建用户关联关系(关联用户组)
			      if("true".equals(getbpmEnabledByKey)){//开启工作流
					      //BpmRest bpmRestServices = processService.getBpmRestServices(ipUserEntity.getHirerId(),ipUserEntity.getHirerId());
					      String tenantLimitId = bpmRestServices.getIdentityService().getBaseParam().getTenantLimitId();
					      UserLinkResourceParam userLinkResourceParam = getBpmRoleparam(
								userId, bpmRestServices,
								setRoleProperties, tenantLimitId,"usergroup");
						  
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
			    			  bpmRestServices.getIdentityService().saveUserLink(userLinkResourceParam);
			    			  
						} catch (RestException e) {
							e.printStackTrace();
						}
			      }
				//end_restful_创建用户关联关系(关联用户组)
			}else{
				
				//begin_restful_同步更新用户关联表(关联角色)
				 if("true".equals(getbpmEnabledByKey)){
					 
					 //先查询当前用户的角色
					 IpUserRole findIpUserRoleByUserIdAndIsPartTime = ipUserRoleDao.findIpUserRoleByUserIdAndIsPartTime(ipUser.getUserId(), "0");
					 String old_roleId = findIpUserRoleByUserIdAndIsPartTime.getRoleId();
					 String new_roleId = ir.getRoleId();
					 if(!old_roleId.equals(new_roleId)){
					   try {
						 //查看当前用户是否有任务
						   UserQueryParam userQueryParam = new UserQueryParam();
						   userQueryParam.setCode(ipUser.getLoginName());
						   userQueryParam.setPhone(ipUser.getCellphoneNo());
						   JsonNode user_jsonNode;
						   user_jsonNode = (JsonNode) bpmRestServices.getIdentityService().queryUsers(userQueryParam);
						   String bpm_user_id=user_jsonNode.findValues("id").get(0).toString();
						   String real_bpm_user_id = bpm_user_id.substring(1, bpm_user_id.length()-1);
						//查询当前用户的任务
						   TaskQueryParam taskQueryParam = new TaskQueryParam();
						   taskQueryParam.setAssignee(real_bpm_user_id);
						   JsonNode jsonNode = (JsonNode)bpmRestServices.getTaskService().queryTasks(taskQueryParam);
						   List<JsonNode> findValues2 = jsonNode.findValues("data");
						   if(!"[]".equals(findValues2.get(0).toString())){
							   //证明现在有任务，有任务不能更换角色，必须处理所有的任务在更换
							   return "wrong";
							   
							}
						} catch (RestException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
						 
					 }
					 
					 
					//根据当前用户code获得bpm对应的user信息，然后获得userId，最后根据userId查询出bpmuser关联表的数据，根据type获得对应的关联信息
					   //1.获得bpm的user信息
					 List<UserLinkResponse> user_link_list = new ArrayList<UserLinkResponse>();
					 String tenantLimitId = bpmRestServices.getIdentityService().getBaseParam().getTenantLimitId();
					 for (UserResponse userResponse : user_list) {
						 String bpm_id = userResponse.getId();
						 //2.根据bpm的userId查询用户关联表
						 UserLinkQueryParam userLinkQueryParam = new UserLinkQueryParam();
						 userLinkQueryParam.setUserId(bpm_id);
						 try {
							JsonNode jsonNode = (JsonNode)bpmRestServices.getIdentityService().queryUserLinks(userLinkQueryParam);
							ArrayNode arrayNode = BaseUtils.getData(jsonNode);
							 for (int i = 0; arrayNode != null && i < arrayNode.size(); i++) {
								 UserLinkResponse resp =jsonResultService.toObject(arrayNode.get(i).toString(), UserLinkResponse.class);
								 user_link_list.add(resp);
						        }
							 
							 for (UserLinkResponse userLinkResponse : user_link_list) {
								 if("usergroup".equals(userLinkResponse.getType())){
									 UserLinkResourceParam userLinkResourceParam = getBpmRoleparam(
												userId, bpmRestServices, setRoleProperties,
												tenantLimitId,"usergroup");
										  userLinkResourceParam.setId(userLinkResponse.getId());
										  userLinkResourceParam.setRevision(userLinkResponse.getRevision());
										  userLinkResourceParam.setUserId(userLinkResponse.getUserId());
						    			  bpmRestServices.getIdentityService().saveUserLink(userLinkResourceParam);
								 }
							 }
							
						} catch (RestException e) {
							e.printStackTrace();
						}
					 }
				 }
			 //end_restful_同步更新用户关联表(关联角色)
				
				setRoleProperties.setTheId(checkIpUserRole.getTheId());
				CopyPropertiesUtil.copyProperty(checkIpUserRole, setRoleProperties);
				ipUserRoleDao.save(setRoleProperties);
			}
					
					
					
					//添加兼职
					if(list.size()!=0){
					//添加_兼职部门
					List<IpUserRole> ipUserRoleList = (List<IpUserRole>) list.get(0);
					List<IpUserCompany> ipUserCompanyList = (List<IpUserCompany>) list.get(1);
					int show_p_coId=0;
					//在保存兼职的时候删除原来的兼职
					ipUserRoleDao.deleteUserRoleByUserIdAndIsPartTime(selUser.getUserId(),"1");
					//添加_兼职的职务
					for (IpUserRole ipUserRole : ipUserRoleList) {//添加的都是最新的
						ipUserRole.setUserId(selUser.getUserId());
							String roleId = ipUserRole.getRoleId();
							IpRole findByRoleId = iPRoleDao.findByRoleId(roleId);
							setRoleProperties(findByRoleId,ipUserCompanyList.get(show_p_coId).getCoId(), selUser, ipUserRole.getIsPartTime());
							ipUserRoleDao.save(ipUserRole);
						    show_p_coId++;
					}
				}
	   return "right";
	
	}
	
	
	
	public IpUserCompany setCoProperties(IpCompany ic,IpUser ipUserEntity){
		IpUserCompany userCo=new IpUserCompany();
		String coId = ic.getCoId();
		IpCompany findByCoId = coDao.findByCoId(coId);
		userCo.setCoCode(findByCoId.getCoCode());
		userCo.setCoId(findByCoId.getCoId());
		userCo.setCoName(findByCoId.getCoFullname());
		userCo.setTheId(UUIDTools.uuidRandom());
		userCo.setUserId(ipUserEntity.getUserId());
		return userCo;
		
	}
	public IpUserRole setRoleProperties(IpRole ir,String coId, IpUser ipUserEntity,String flag){
		IpUserRole userRole=new IpUserRole();
		String roleId = ir.getRoleId();
		if(roleId==null){
			roleId="";
		}
		IpRole findByRoleId = iPRoleDao.findByRoleId(roleId);
		/*String coId = ir.getCoId();
		findByRoleId.setCoId(coId);*/
		
		if(findByRoleId==null){	
			userRole.setRoleId(roleId);
			userRole.setCoId(coId);
		}else{
			userRole.setRoleId(findByRoleId.getRoleId());
			userRole.setCoId(coId);
		}
		userRole.setUserId(ipUserEntity.getUserId());
		userRole.setTheId(UUIDTools.uuidRandom());
		userRole.setIsPartTime(flag);
		return userRole;
	}
	
	public IpUser checkIpUser(IpUser ipUserEntity, Map<String, String> resultMap) {
		IpUser ipUser= findUserByUserId(ipUserEntity.getUserId());//按登入号查询user
		/*if(ipUser!=null){
			  getErrorMsg(resultMap,"登录号已存在！");
			  return false;
		}
		return true;*/
		
		return ipUser;
	}
	
	
	public IpUserCompany checkIpUserCompany(IpUserCompany ipUserCompany, Map<String, String> resultMap) {

		IpUserCompany findIpUserCompanyByUserIdAndCoId = ipUserCompanyDao.findIpUserCompanyByUserId(ipUserCompany.getUserId());
		/*if(findIpUserCompanyByUserIdAndCoId!=null){
			  getErrorMsg(resultMap,"已添加该部门！");
			  return false;
		}*/
		
		return findIpUserCompanyByUserIdAndCoId;
		
	}
	
	public IpUserRole checkIpUserRole(IpUserRole ipUserRole, Map<String, String> resultMap) {
		/*if(ipUserRole==null){
			return false;
		}*/
		IpUserRole findIpUserRoleByUserIdAndRoleIdAndIsPartTime = ipUserRoleDao.findIpUserRoleByUserIdAndIsPartTime(ipUserRole.getUserId(),ipUserRole.getIsPartTime());
		/*if(findIpUserRoleByUserIdAndRoleIdAndIsPartTime!=null){
			  getErrorMsg(resultMap,"已添加该职务！");
			  return false;
		}*/
		
		return findIpUserRoleByUserIdAndRoleIdAndIsPartTime;
		
	}
	
	/**
	 * 错误信息 
	 * @param resultMap
	 * @param reason
	 */
	public void getErrorMsg(Map<String, String> resultMap,String reason) {
		resultMap.put("result", "fail");
		resultMap.put("reason", reason);
	}


	public List<IpUser> findUserAll() {
		
		// TODO 自动生成的方法存根
				 return userDao.findUserAll();

	}
	
	public Page<IpUser> getUserAll(Map<String, Object> searchParams,
			int pageNumber,int pageSize) throws UnsupportedEncodingException {
				
				/* Specification<IpUser> spec = buildSpecification(searchParams);
				 return userDao.findAll(spec, pageRequest);*/
				 
				 //Specification<IpUser> spec = buildSpecification(searchParams);
			      //  return userDao.findAll(spec, pageRequest);
				 //return userDao.findUserAll();
		
		    PageRequest pageRequest = new PageRequest(pageNumber, pageSize, null);// 目前的pagesize是固定的，现在等待前台传值
		    Map<String,String> roleIdAndCoId=getHirerIdAndCoIdBySearchParams(searchParams);
			String hirerId = roleIdAndCoId.get("hirerId");
			String coId = roleIdAndCoId.get("coId");
			String isEnabled = roleIdAndCoId.get("isEnabled");
			
			String loginName = roleIdAndCoId.get("loginName");
			
			List allUserList=new ArrayList();
			List<IpUser> allCurUserList=new ArrayList<IpUser>();
			if("".equals(coId)|| coId==null){
				 allUserList = iuapUserJdbcDao.getInitUserByHirerIdAndCoId(hirerId, coId,isEnabled,loginName);
				 List<IpUser> buildIpMenuEntityList = buildIpMenuEntityList(allUserList);
				  return ipDataTableService.getPageEntity(pageRequest, buildIpMenuEntityList);
			}else{
				IpCompany ipCompany = new IpCompany();
				ipCompany.setCoId(coId);
				List<IpCompany> findAllCurDeptAndAllChild = ipCompanyService.findAllCurDeptAndAllChild(ipCompany);
				List<List<IpUser>> userListTmp=new ArrayList<List<IpUser>>();
				for (IpCompany ipCompany2 : findAllCurDeptAndAllChild) {
					 List initUserByHirerIdAndCoId = iuapUserJdbcDao.getInitUserByHirerIdAndCoId(hirerId, ipCompany2.getCoId(),isEnabled,loginName);
					 List<IpUser> buildIpMenuEntityList = buildIpMenuEntityList(initUserByHirerIdAndCoId);
					 userListTmp.add(buildIpMenuEntityList);
				}
				
				for (List<IpUser> tmpList : userListTmp) {
					for (IpUser deptUser : tmpList) {
						allCurUserList.add(deptUser);
					}
				}
				
				//List<IpUser> buildIpMenuEntityList = buildIpMenuEntityList(allUserList);
				return ipDataTableService.getPageEntity(pageRequest, allCurUserList);
				
			}
		    
		  // List<IpUser> buildIpMenuEntityList = buildIpMenuEntityList(allUserList);
		   // Page<IpUser> pageEntity = ipDataTableService.getPageEntity(pageRequest, buildIpMenuEntityList);
		   //return ipDataTableService.getPageEntity(pageRequest, buildIpMenuEntityList);
		   //return buildIpMenuEntityList;
		
	}


	public boolean checkEmployeeNo(String employeeNo) {
		// TODO 自动生成的方法存根
		IpUser ipUser= findUserByEmployeeNo(employeeNo);//按登入号查询user
		if(ipUser!=null){
			  return false;
		}
		return true;
	}


	public IpUser findUserByEmployeeNo(String employeeNo) {
		// TODO 自动生成的方法存根
		return userDao.findIpUserByEmployeeNo(employeeNo);
	}


	public void updateUserByUserId(String userId, String isEnabled) {
		// TODO 自动生成的方法存根
		
		userDao.updateUserByUserId(userId,isEnabled);
		
	}

	@Transactional
	public void saveIpUser(IpUser ipUser) {
		userDao.save(ipUser);
	}


	public IpUser findIpUserByLoginName(String loginName) {
		return userDao.findByLoginName(loginName);
	}	
	
	public List<IpUser> getUserPage(Map<String, Object> searchParams,
			Sort sort) {
		// TODO 自动生成的方法存根
		 Specification<IpUser> spec = buildSpecification(searchParams);
		 return userDao.findAll(spec, sort);
	} 
	
	public List<IpUser> getUserByDeptPage(Map<String, Object> searchParams,
			Sort sort) {
		// TODO 自动生成的方法存根
		 Specification<IpUser> spec = buildSpecification(searchParams);
		 return userDao.findAll(spec, sort);
	} 
	
	/**
     * 创建动态查询条件组合.
     */
    public Specification<IpUser> buildSpecification(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<IpUser> spec = DynamicSpecifications.bySearchFilter(filters.values(), IpUser.class);
        return spec;
    }
    
    
    public Map<String,String> getHirerIdAndCoIdBySearchParams(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		SearchFilter searchFilterByHirerId =filters.get("EQ_hirerId");
		SearchFilter searchFilterByCoId =filters.get("EQ_coId");
		SearchFilter searchFilterByIsEnabled =filters.get("EQ_isEnabled");
		SearchFilter searchFilterByLoginName =filters.get("LIKE_loginName");
		String hirerId = "";
		String coId = "";
		String isEnabled="";
		String loginName="";
		Map<String,String> dataInfo=new HashMap<String, String>();
		if (searchFilterByHirerId!=null) {
			hirerId = (String) searchFilterByHirerId.value;
		}
		if (searchFilterByCoId!=null) {
			coId = (String) searchFilterByCoId.value;
		}
		
		if (searchFilterByIsEnabled!=null) {
			isEnabled = (String) searchFilterByIsEnabled.value;
		}
		if (searchFilterByLoginName!=null) {
			loginName = (String) searchFilterByLoginName.value;
			try {
				loginName = URLEncoder.encode(loginName,"utf-8");
				loginName=URLDecoder.decode(loginName,"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
		}
		dataInfo.put("hirerId", hirerId);
		dataInfo.put("coId", coId);
		dataInfo.put("isEnabled", isEnabled);
		dataInfo.put("loginName", loginName);
		return dataInfo;
	}
    
    /**
	 * 通过jdbc查询的结果构建 list<实体>
	 * @param ipRoleMenuList
	 * @return
	 */
	public List<IpUser> buildIpMenuEntityList(List ipUserList) {
		List<IpUser> ipUsers = new ArrayList<IpUser>();
		IpUser ipUser;
		Iterator iterator = ipUserList.iterator();
		 while (iterator.hasNext()) {
			 Map map4ipMenu = (Map) iterator.next();
			 ipUser=new IpUser();
			 CopyPropertiesUtil.copyJdbcProperty(ipUser, map4ipMenu);
			 ipUsers.add(ipUser);
		}
		return ipUsers;
	}


	public IpUser findUserByUserId(String userId) {
		return userDao.findUserByUserId(userId);
	}


//	public Page<IpUser> findUserByUserEmailOrLoginNameOrCellphoneNoLike(
//			Map<String, Object> searchParams, PageRequest pageRequest) throws UnsupportedEncodingException {
//
//		    Map<String,String> userInfomation=getUserBySearchParams(searchParams);
//			String loginName = userInfomation.get("loginName");
//			String isEnabled = userInfomation.get("isEnabled");
//			
//			String loginNameLike=URLEncoder.encode("%"+loginName+"%","utf-8");
//			loginNameLike=URLDecoder.decode(loginNameLike,"utf-8");
//			List<IpUser> userInfoLike = userDao.findUserByUserEmailOrLoginNameOrCellphoneNoLike(loginNameLike,isEnabled);
//		    //List<IpUser> buildIpMenuEntityList = buildIpMenuEntityList(findUserByUserEmailOrLoginNameOrCellphoneNoLike);
//		    return ipDataTableService.getPageEntity(pageRequest, userInfoLike);
//		
//	}
	public Page<IpUser> findUserByUserEmailOrLoginNameOrCellphoneNoLike(
			Map<String, Object> searchParams, int pageNumber,int pageSize) throws UnsupportedEncodingException {
			Page<IpUser> userInfoLike = getUserAll(searchParams, pageNumber,pageSize);
		    return userInfoLike;		
	}


	public Map<String, String> getUserBySearchParams(
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		SearchFilter searchFilterByLoginName =filters.get("LIKE_loginName");
		SearchFilter searchFilterByIsEnabled =filters.get("EQ_isEnabled");
		String loginName="";
		String isEnabled="";
		Map<String,String> dataInfo=new HashMap<String, String>();
		
		if (searchFilterByLoginName!=null) {
			loginName = (String) searchFilterByLoginName.value;
		}
		
		if (searchFilterByIsEnabled!=null) {
			isEnabled = (String) searchFilterByIsEnabled.value;
		}
		
		dataInfo.put("loginName", loginName);
		dataInfo.put("isEnabled", isEnabled);
		return dataInfo;
	}


	public List<IpUser> findUserByHirerId(String hirerId) {
		return userDao.findUserByHirerId(hirerId);
	}
	
	private UserLinkResourceParam getBpmRoleparam(String userId,
			BpmRest bpmRestServices, IpUserRole setRoleProperties,
			String tenantLimitId,String type) {
		UserLinkResourceParam userLinkResourceParam = new UserLinkResourceParam();
		  userLinkResourceParam.setType(type);
		  userLinkResourceParam.setUserId(userId);
		  //userLinkResourceParam.setTenantId(tenantLimitId);
		  
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
	
	private UserResourceParam getBpmUserParam(IpUser ipUserEntity,
			IpCompany findByCoId, BpmRest bpmRestServices, String tenantLimitId) {
		UserResourceParam userResourceParam = new UserResourceParam();
		//userResourceParam.setCode(ipUserEntity.getUserName());
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
		  //userLinkResourceParam.setTenantId(tenantLimitId);//设置租户id
		
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
	
	private String InsertOrUpdateBpmUser(IpUser ipUserEntity, IpCompany ic,
			IpRole ir) {
		String userId;
		//开启工作流
		   String coId = ic.getCoId();
		   IpCompany findByCoId = coDao.findByCoId(coId);
		    BpmRest bpmRestServices = processService.getBpmRestServices(ipUserEntity.getHirerId(),ipUserEntity.getHirerId());
		    String tenantLimitId = bpmRestServices.getIdentityService().getBaseParam().getTenantLimitId();
		    
		    //判断当前所保存的角色是否为业务管理员，如果是就设置为登录bpm流程中的超级管理员
		      //1.判断角色
		          /*String roleId = ir.getRoleId();
		          IpRole findByRoleId = iPRoleDao.findByRoleId(roleId);
		          String roleName = findByRoleId.getRoleName();*/
		      //2.设置超级管理员
		    UserResourceParam userResourceParam = getBpmUserParam(ipUserEntity,
					findByCoId, bpmRestServices, tenantLimitId);
		    
		    //begin_去掉判断
			    /*if("业务管理员".equals(roleName)){//待改进
			    	userResourceParam.setSysadmin(true);
			      }*/
		    //end_去掉判断
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

	public void saveIpUserInfoByHirerIdAndUserType(String hirerId,String userType, String userSex,
			String userEmail, String phoneNo, String cellphoneNo, String duty) {
		// TODO 自动生成的方法存根
		userDao.saveIpUserInfo(hirerId,userType,userSex,userEmail,phoneNo,cellphoneNo,duty);
	}
	

	public void updateUserHeaderimageByUserId(String userId,String url){
		
		userDao.updateUserHeaderimageByUserId(userId, url);
	}

	public void updatePwdByUserId(String userId, String pwd, String salt) {
		userDao.updatePwdByUserId(userId, pwd,salt);		
	}
}
