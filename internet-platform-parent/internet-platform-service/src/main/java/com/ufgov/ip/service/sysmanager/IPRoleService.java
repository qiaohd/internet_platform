package com.ufgov.ip.service.sysmanager;

import java.util.ArrayList;
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
import org.springside.modules.persistence.SearchFilter;

import yonyou.bpm.rest.BpmRest;
import yonyou.bpm.rest.exception.RestException;
import yonyou.bpm.rest.request.identity.UserGroupQueryParam;
import yonyou.bpm.rest.request.identity.UserGroupResourceParam;
import yonyou.bpm.rest.response.identity.UserGroupResponse;
import yonyou.bpm.rest.utils.BaseUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ufgov.ip.api.base.IPDataTableServiceI;
import com.ufgov.ip.api.sysmanager.IPRoleServiceI;
import com.ufgov.ip.dao.sysmanager.ICompanyDao;
import com.ufgov.ip.dao.sysmanager.IPRoleDao;
import com.ufgov.ip.dao.sysmanager.IPRoleMenuMapper;
import com.ufgov.ip.dao.sysmanager.IpUserCompanyDao;
import com.ufgov.ip.dao.sysmanager.IpUserRoleDao;
import com.ufgov.ip.dao.sysmanagerimpl.IPRoleJdbcDaoImp;
import com.ufgov.ip.dao.system.IMenuMapper;
import com.ufgov.ip.dao.system.IpRoleSynMapper;
import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.sysmanager.IpRole;
import com.ufgov.ip.entity.sysmanager.IpRoleMenu;
import com.ufgov.ip.entity.sysmanager.IpUserAndCompanyEntity;
import com.ufgov.ip.entity.sysmanager.IpUserRole;
import com.ufgov.ip.entity.system.IpMenu;
import com.ufgov.ip.entity.system.IpUserCompany;
import com.ufgov.ip.service.system.JsonResultService;
import com.ufgov.ip.service.system.ProcessService;
import com.ufgov.ip.utils.ProduceCodeUtil;
import com.ufgov.ip.utils.PropertyUtilSys;
import com.ufgov.ip.utils.UUIDTools;


/**
 * 用户管理服务层 书写readonly是说明没有特殊标识服务层只有只读事物
 *
 */
@Service(version = "0.0.1")
public class IPRoleService implements IPRoleServiceI{

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;

	private static Logger logger = LoggerFactory
			.getLogger(IPRoleService.class);

	@Autowired
	private IPRoleDao iPRoleDao;
	@Autowired
//	private IMenuDao iMenuDao;
	private IMenuMapper iMenuMapper;
	@Autowired
	private IPDataTableServiceI ipDataTableService;
	@Autowired
//	private IPRoleMenuDao iPRoleMenuDao;
	private IPRoleMenuMapper iPRoleMenuMapper;
	@Autowired
	private IPRoleJdbcDaoImp ipRoleJdbcDao;
	@Autowired
	private ICompanyDao ipCompanyDao;
	@Autowired
	private IpUserRoleDao ipUserRoleDao;
	@Autowired
	private IpUserCompanyDao ipUserCompanyDao;
	
	@Autowired
	private IpRoleSynMapper ipRoleSynMapper;
	
	@Autowired
	private JsonResultService jsonResultService;

	public JsonResultService getJsonResultService() {
		return jsonResultService;
	}
	
	@Autowired
	private ProcessService processService;

	public ProcessService getProcessService() {
		return processService;
	}


	public IPRoleDao getiPRoleDao() {
		return iPRoleDao;
	}

	public void setiPRoleDao(IPRoleDao iPRoleDao) {
		this.iPRoleDao = iPRoleDao;
	}
	
	
	public List<IpRole> getRoleInfo(){
		return iPRoleDao.findRoleAll();
	}
	
	// 通过角色名称获取角色 
	public IpRole findIpRoleByName(String roleName,String hirerId){
		
		return iPRoleDao.findByRoleNameAndHirerId(roleName,hirerId);
	}
	
	public List<IpRole> getRoleInfoByHirerId(String hirerId){
		return iPRoleDao.findByHirerId(hirerId);
	}
	
	/**
	 * 菜单查询
	 * @param searchParams  条件参数
	 * @param sort  排序参数 
	 * @return
	 */
	public List<IpMenu> getInitMenu(Map<String, Object> searchParams, Sort sort) {
		
		//之前的
		Specification<IpMenu> spec = ipDataTableService.buildSpecification(searchParams,IpMenu.class);
		//return iMenuDao.findAll(spec, sort);
				
		List<IpMenu> ipMenus =iMenuMapper.findMenuByAuthLevel();
		return ipMenus;
	}
	
	/**
	 * 初始化角色-菜单
	 * @param searchParams
	 * @return
	 */
	public List<IpMenu> getInitPermissionMenu(Map<String, Object> searchParams) {
		 String roleId=getRoleIdBySearchParams(searchParams);
		 List ipMenuList= ipRoleJdbcDao.getInitPermissionMenu(roleId);
		 List<IpMenu> ipMenus= buildIpMenuEntityList(ipMenuList);
		 return ipMenus;
	}
	
	/**
	 * 编辑权限时，初始化已选择的角色
	 * @param searchParams
	 * @return
	 */
	public List<String> getEditPermissionMenu(Map<String, Object> searchParams) {
		List<String> roleMenuList = new ArrayList<String>();
		Specification<IpRoleMenu> spec = ipDataTableService.buildSpecification(searchParams,IpRoleMenu.class);
//		List<IpRoleMenu> ipRoleMenus= iPRoleMenuDao.findAll(spec);
		
		//获得map的v
		String roleId = (String)searchParams.get("EQ_roleId");
		List<IpRoleMenu> ipRoleMenus= iPRoleMenuMapper.findAll(roleId);
	
		for (IpRoleMenu ipRoleMenu : ipRoleMenus) {
			String menuId=ipRoleMenu.getMenuId();
			roleMenuList.add(menuId);
		}
		return roleMenuList;
	}
	
	/**
	 * 
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	public List<IpUserAndCompanyEntity> initCompanyDataTable(Map<String, Object> searchParams, Sort sort) {
		Specification<IpCompany> spec = ipDataTableService.buildSpecification(searchParams,IpCompany.class);
		List<IpCompany> ipCompanies= ipCompanyDao.findAll(spec, sort);
		List<IpUserAndCompanyEntity> ipUserAndCompanyEntities= buildUserAndCompany(ipCompanies);
		return ipUserAndCompanyEntities;
	}
	
	/**
	 * 部门人员树 
	 * 		setIsUser 1：表示人员   0：表示部门
	 * 		人员的父节点为其部门ID
	 * @param ipCompanies
	 * @return
	 */
	public List<IpUserAndCompanyEntity> buildUserAndCompany(
			List<IpCompany> ipCompanies) {
		// TODO 自动生成的方法存根
		List<IpUserAndCompanyEntity> upList= new ArrayList<IpUserAndCompanyEntity>();
		IpUserAndCompanyEntity ipUserAndCompanyEntity;
		for (IpCompany ipCompany : ipCompanies) {
			ipUserAndCompanyEntity= new IpUserAndCompanyEntity();
			String coIdString=ipCompany.getCoId().toString();
			ipUserAndCompanyEntity.setStaffId(coIdString);
			ipUserAndCompanyEntity.setStaffName(ipCompany.getCoName());
			ipUserAndCompanyEntity.setStaffPid(ipCompany.getParentCoId());
			// 是否是用户 0：部门 
			ipUserAndCompanyEntity.setIsUser("0"); 
			upList.add(ipUserAndCompanyEntity);
			List userList =ipRoleJdbcDao.getUserByCoId(coIdString);
			if(userList.size()>0){
				buildipUserAndCompanyEntityList(upList,userList,coIdString);
			}
			
		}
		return upList;
	}

	/**
	 * 组装用户数据，设置用户的父节点为其部门
	 * @param upList
	 * @param userList
	 * @param coId
	 */
	public void buildipUserAndCompanyEntityList(List<IpUserAndCompanyEntity> upList,List userList,String coId) {
		IpUserAndCompanyEntity ipUserAndCompanyEntity;
		Iterator iterator = userList.iterator();
		 while (iterator.hasNext()) {
			 Map map4ipMenu = (Map) iterator.next();
			 ipUserAndCompanyEntity =new IpUserAndCompanyEntity();
			 ipUserAndCompanyEntity.setStaffId((String)map4ipMenu.get("userId"));
			 ipUserAndCompanyEntity.setStaffName((String)map4ipMenu.get("userName"));
			 ipUserAndCompanyEntity.setStaffPid(coId);
			// 是否是用户 1：用户   
			 ipUserAndCompanyEntity.setIsUser("1");
			 upList.add(ipUserAndCompanyEntity);
		 }
	}
	
	/**
	 * 权限添加人员时，已添加人员的LIST
	 * @param searchParams
	 * @return
	 */
	public List<String> getUserList(Map<String, Object> searchParams) {
		List<String> getUserList = new ArrayList<String>();
		Specification<IpUserRole> spec = ipDataTableService.buildSpecification(searchParams,IpUserRole.class);
		List<IpUserRole> ipUserRoles= ipUserRoleDao.findAll(spec);
		for (IpUserRole ipUserRole : ipUserRoles) {			
			if("0".equals(ipUserRole.getIsPartTime())){
				String userId=ipUserRole.getUserId();
				getUserList.add(userId);
			}
		}
		return getUserList;
	}

	/**
	 * 通过jdbc查询的结果构建 list<实体>
	 * @param ipRoleMenuList
	 * @return
	 */
	public List<IpMenu> buildIpMenuEntityList(List ipRoleMenuList) {
		// TODO 自动生成的方法存根
		List<IpMenu> ipMenus = new ArrayList<IpMenu>();
		IpMenu ipMenu;
		Iterator iterator = ipRoleMenuList.iterator();
		 while (iterator.hasNext()) {
			 Map map4ipMenu = (Map) iterator.next();
			 ipMenu = new IpMenu();
			 ipMenu.setMenuId((String)map4ipMenu.get("menuId"));
			 ipMenu.setMenuName((String)map4ipMenu.get("menuName"));
			 ipMenu.setMenuDesc((String)map4ipMenu.get("menuDesc"));
			 ipMenu.setParentMenuId((String)map4ipMenu.get("parentMenuId"));
			 ipMenu.setParentMenuName((String)map4ipMenu.get("parentMenuName"));
			 ipMenu.setLevelNum((String)map4ipMenu.get("levelNum"));
			 ipMenu.setDispOrder(Integer.parseInt(map4ipMenu.get("dispOrder").toString()));
			 ipMenu.setIsLeaf((String)map4ipMenu.get("isLeaf"));
			 ipMenu.setUrl((String)map4ipMenu.get("url"));
			 ipMenu.setAuthLevel((String)map4ipMenu.get("authLevel"));
			 ipMenu.setIsShow((String)map4ipMenu.get("isShow"));
			 ipMenu.setIsJump((String)map4ipMenu.get("isJump"));			 
			 ipMenus.add(ipMenu);
		}
		return ipMenus;
	}
	
	/**
	 * 取roleID 
	 * @param searchParams
	 * @return
	 */
	public String getRoleIdBySearchParams(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		SearchFilter searchFilterByRoleId =filters.get("EQ_roleId");
		String roleId = "";
		if (searchFilterByRoleId!=null) {
			roleId = (String) searchFilterByRoleId.value;
		}
		return roleId;
	}

	/**
	 * 职务添加
	 * @param roleMenu
	 * @param ipMenuList
	 */
	@Transactional
	public void saveRoleAndRoleMenu(IpRole ipRoleEntity, List<IpMenu> ipMenuList,Map<String, Object> resultMap) {
		Boolean checkBoolean=checkIpRoleName(ipRoleEntity,resultMap);
		if(checkBoolean){
			String roleCode = ProduceCodeUtil.getCode();//生成角色code
			if (ipRoleEntity.getRoleId()==null || ipRoleEntity.getRoleId()=="") {
				ipRoleEntity.setRoleId(UUIDTools.uuidRandom());
				ipRoleEntity.setRoleCode(roleCode);//设置角色code
			}
			
			
			else{
				String roleId=ipRoleEntity.getRoleId();
				// 赋值远排序号 
				IpRole ipRole=iPRoleDao.findByRoleId(roleId);
				ipRoleEntity.setRoleCode(ipRole.getRoleCode());
				ipRoleEntity.setDispOrder(ipRole.getDispOrder());
//				iPRoleMenuDao.deleteByRoleId(ipRoleEntity.getRoleId());
				iPRoleMenuMapper.deleteByRoleId(ipRoleEntity.getRoleId());
			}
			List<IpRoleMenu> ipRoleMenus=getIpRoleMenuList(ipRoleEntity,ipMenuList);
			for (IpRoleMenu ipRoleMenu : ipRoleMenus) {
//				iPRoleMenuDao.save(ipRoleMenu);
				iPRoleMenuMapper.save(ipRoleMenu);
			}
			
			
			 //begin_角色code更新时不是同一个bug_20161215
			   /* String roleId = ipRoleEntity.getRoleId();
			    if(roleId==null){
			    	
			    }else{
			    	String curRoleCode = iPRoleDao.findRoleByRoleId(ipRoleEntity.getRoleId()).getRoleCode();
			    	ipRoleEntity.setRoleCode(curRoleCode);
			    }*/
			 //end_角色code更新时不是同一个bug_20161215
			
			//begin_restful_同步用户组表
			List<UserGroupResponse> list=new ArrayList<UserGroupResponse>();
			String getbpmEnabledByKey = PropertyUtilSys.getbpmEnabledByKey("bpm.enabled");
		      if("true".equals(getbpmEnabledByKey)){//开启工作流
			    String hirerId = ipRoleEntity.getHirerId();
			    BpmRest bpmRestServices=processService.getBpmRestServices(hirerId,hirerId);
				UserGroupResourceParam bpmRoleParam = getBpmRoleParam(ipRoleEntity, roleCode);
				try {
					IpRole findRoleByRoleId = iPRoleDao.findRoleByRoleId(ipRoleEntity.getRoleId());
					if(findRoleByRoleId==null){//保存
						bpmRestServices.getIdentityService().saveUserGroup(bpmRoleParam);
						
					}else{//更新
						
						//获得当前角色的code,根据code获得bpm的用户组信息
						String roleCode2 = findRoleByRoleId.getRoleCode();
						UserGroupQueryParam userGroupQueryParam = new UserGroupQueryParam();
						userGroupQueryParam.setCode(roleCode2);
						JsonNode jsonNode = (JsonNode)bpmRestServices.getIdentityService().queryUserGroups(userGroupQueryParam);
						 ArrayNode arrayNode = BaseUtils.getData(jsonNode);
						 for (int i = 0; arrayNode != null && i < arrayNode.size(); i++) {
							 UserGroupResponse resp =jsonResultService.toObject(arrayNode.get(i).toString(), UserGroupResponse.class);
					            list.add(resp);
					        }
						 //更新bpm的用户组
						 for (UserGroupResponse userGroupResponse : list) {
							 bpmRoleParam.setId(userGroupResponse.getId());
							 bpmRoleParam.setRevision(userGroupResponse.getRevision());
							 bpmRestServices.getIdentityService().saveUserGroup(bpmRoleParam);
						 }
						
					}
					
					
				} catch (RestException e) {
					e.printStackTrace();
				}
		      }
			//end_restful_同步用户组表
			
			
			
			iPRoleDao.save(ipRoleEntity);
		}
	}
	
	/**
	 * ipRoleMenu 数据LIST 
	 * @param ipRoleEntity
	 * @param ipMenuList
	 * @return
	 */
	public List<IpRoleMenu> getIpRoleMenuList(IpRole ipRoleEntity,
			List<IpMenu> ipMenuList) {
		// TODO 自动生成的方法存根
		List<IpRoleMenu> ipRoleMenus = new ArrayList<IpRoleMenu>();
		for (IpMenu ipMenu : ipMenuList) {
			IpRoleMenu ipRoleMenu=new IpRoleMenu();
			ipRoleMenu.setMenuId(ipMenu.getMenuId());
			ipRoleMenu.setMenuName(ipMenu.getMenuName());
			ipRoleMenu.setRoleId(ipRoleEntity.getRoleId());
			ipRoleMenu.setTheId(UUIDTools.uuidRandom());
			ipRoleMenus.add(ipRoleMenu);
		}
		return ipRoleMenus;
	}

	/**
	 * 验证职务名称是否存在 
	 * @param entity
	 * @param resultMap
	 * @return
	 */
	public Boolean checkIpRoleName(IpRole ipRoleEntity, Map<String, Object> json) {
		IpRole ipRole= findIpRoleByName(ipRoleEntity.getRoleName(),ipRoleEntity.getHirerId());
		if((ipRole!=null) && (!ipRole.getRoleId().equals(ipRoleEntity.getRoleId()))){
			json.put("flag", "fail");
			json.put("msg", "职务名称已存在!");
			return false;
		}
		return true;		
	}
	
	/**
	 * 删除职务权限 
	 * @param roleId
	 * @param resultMap
	 * @throws RestException 
	 */
	@Transactional
	public Map<String,String> delPermission(String roleId) throws RestException{
		Boolean checkBoolean=checkIpUserRole(roleId);
		String getbpmEnabledByKey = PropertyUtilSys.getbpmEnabledByKey("bpm.enabled");//获得工作流开/关状态值
		Map<String,String> json=new HashMap<String,String>();
		if(!checkBoolean){			
			json.put("flag", "fail");
			json.put("msg", "该职务权限下存在人员不能删除!");
		}else{
			if("true".equals(getbpmEnabledByKey)){
				//同步删除bpm的对应角色
				  //1.先查出角色code
				IpRole findByRoleId = iPRoleDao.findByRoleId(roleId);  
	              //2.按照code查询bpm的角色,获得id
				  UserGroupQueryParam userGroupQueryParam = new UserGroupQueryParam();
				  userGroupQueryParam.setCode(findByRoleId.getRoleCode());
				  userGroupQueryParam.setTenantId(findByRoleId.getHirerId());
				  BpmRest bpmRestServices=processService.getBpmRestServices(findByRoleId.getHirerId(),findByRoleId.getHirerId());
				  JsonNode  jsonNode = (JsonNode)bpmRestServices.getIdentityService().queryUserGroups(userGroupQueryParam);
					 String bpm_roleId=jsonNode.findValues("id").get(0).toString();
					 bpm_roleId = bpm_roleId.substring(1, bpm_roleId.length()-1);
					//3.根据id删除bpm对应的用户组
					  bpmRestServices.getIdentityService().deleteUserGroup(bpm_roleId);
			}
			
			//begin_记录删除的角色信息_20161219
			  IpRole findByRoleId = iPRoleDao.findByRoleId(roleId);
		    //end_记录删除的角色信息_20161219
			
			iPRoleDao.delete(roleId);
			iPRoleMenuMapper.deleteByRoleId(roleId);
			
			//begin_记录删除的角色信息_20161219
			  ipRoleSynMapper.insert(findByRoleId);
		   //end_记录删除的角色信息_20161219
			
			
			json.put("flag", "success");
			json.put("msg", "保存成功!");	
		}
		return json;
	}
	
	/**
	 * 判断职务权限是否可以删除 
	 * @param roleId
	 * @param resultMap
	 * @return
	 */
	public Boolean checkIpUserRole(String roleId) {
		List<IpUserRole> ipUserRoles= ipUserRoleDao.findIpUserRoleByRoleId(roleId);
		if(ipUserRoles.size()>0){
			return false;
		}
		return true;		
	}
	
   public List<IpRole> findAllRole(){
	   
	   return (List<IpRole>) iPRoleDao.findAll();
   }

/**
 * 保存权限人员信息 
 * @param ipRoleByreq
 * @param ipUserCompanyEntityList
 * @param json
 * @throws Exception
 */
//@Transactional
//public void saveRoleAndUser(IpRole ipRoleByreq,
//		List<IpUserAndCompanyEntity> ipUserCompanyEntityList) throws Exception {
//	try {
//		IpUserRole ipUserRoleForSave;
//		String roleId=ipRoleByreq.getRoleId();
//		
//		ipUserRoleDao.deleteUserRoleByRoleId(roleId);
//		for (IpUserAndCompanyEntity ipUserAndCompanyEntity : ipUserCompanyEntityList) {
//			String userId=ipUserAndCompanyEntity.getStaffId();
//			IpUserCompany ipUserCompany=ipUserCompanyDao.findIpUserCompanyByUserId(userId);
//			if(ipUserCompany!=null){
//				ipUserRoleForSave=new IpUserRole();
//				String coId=ipUserCompany.getCoId();
//				ipUserRoleForSave.setCoId(coId);
//				ipUserRoleForSave.setUserId(userId);
//				ipUserRoleForSave.setRoleId(roleId);
//				// 主要职务  0 主要职务 
//				ipUserRoleForSave.setIsPartTime("0");
//				// 当前人员是否存在主要职务 
//				ipUserRoleDao.deleteUserRoleByUserId(userId);
////				IpUserRole ipUserRole = ipUserRoleDao.findIpUserRoleByUserIdAndIsPartTime(userId, "0");
////				if(ipUserRole!=null){
////					ipUserRoleForSave.setTheId(ipUserRole.getTheId());
////				}else{
////					ipUserRoleForSave.setTheId(UUIDTools.uuidRandom());
////				}
//				ipUserRoleForSave.setTheId(UUIDTools.uuidRandom());
//				ipUserRoleDao.save(ipUserRoleForSave);
//			}
//		}
//	} catch (Exception e) {
//		// TODO 自动生成的 catch 块
//		throw e;
//	}
//}

/**
 * 保存权限人员信息 
 * @param ipRoleByreq
 * @param ipUserCompanyEntityList
 * @param json
 * @throws Exception
 */
@Transactional
public void saveRoleUser(IpRole ipRoleByreq,
		List<IpUserAndCompanyEntity> ipUserCompanyEntityList,List userIdForSaveList) throws Exception {
	try {
		IpUserRole ipUserRoleForSave;
		String roleId=ipRoleByreq.getRoleId();
		
		// ipUserRoleDao.deleteUserRoleByRoleId(roleId);
		// 非兼职状态 
		String isPartTime="0";
 		List<IpUserRole> ipUserRoles=ipUserRoleDao.findByroleIdAndIsPartTime(roleId, isPartTime);
 		List userIdList=new ArrayList();
 		for (IpUserRole ipUserRole : ipUserRoles) {
 			userIdList.add(ipUserRole.getUserId());
 			if(userIdForSaveList.contains(ipUserRole.getUserId())){
 				continue;
 			}else{
 				ipUserRoleDao.delete(ipUserRole);
 			}
		}
		for (IpUserAndCompanyEntity ipUserAndCompanyEntity : ipUserCompanyEntityList) {
			String userId=ipUserAndCompanyEntity.getStaffId();
			if(userIdList.contains(userId)){
				continue;
			}else{
				IpUserCompany ipUserCompany=ipUserCompanyDao.findIpUserCompanyByUserId(userId);
				if(ipUserCompany!=null){
					ipUserRoleForSave=new IpUserRole();
					String coId=ipUserCompany.getCoId();
					ipUserRoleForSave.setCoId(coId);
					ipUserRoleForSave.setUserId(userId);
					ipUserRoleForSave.setRoleId(roleId);
					// 主要职务  0 主要职务 
					ipUserRoleForSave.setIsPartTime("0");
					// 当前人员是否存在主要职务 
					ipUserRoleDao.deleteUserRoleByUserId(userId);
					ipUserRoleForSave.setTheId(UUIDTools.uuidRandom());
					ipUserRoleDao.save(ipUserRoleForSave);
				}
			}
		}
		
	} catch (Exception e) {
		// TODO 自动生成的 catch 块
		throw e;
	}
}

/**
 * 职务权限下的人员信息 
 * @param searchParams
 * @param pageRequest
 * @return
 */
public Page<IpUserRole> getUserPage(Map<String, Object> searchParams,
		PageRequest pageRequest) {
	String roleId=getRoleIdBySearchParams(searchParams);
	
	List<IpUserRole> userRoleList = bulidUserRoleAndOrder(roleId);
	// 将List 转换为实体list 
	// List<IpUserRole> ipUserRoles = buildIpUserRoleEntityList(userRoleList);
	// 组装page<IpUserRole> 
	return ipDataTableService.getPageEntity(pageRequest, userRoleList);
	
}

public List<IpUserRole> bulidUserRoleAndOrder(String roleId) {
	List<IpUserRole> userRoleList=ipRoleJdbcDao.findRoleUserByRoleIdList(roleId);
	int i=1;
	for (IpUserRole ipUserRole : userRoleList) {
		ipUserRole.setDispOrder(i);
		i++;
	}
	return userRoleList;
}



public List<IpUserRole> buildIpUserRoleEntityList(List userRoleList) {
	// TODO 自动生成的方法存根
	List<IpUserRole> ipUserRoles = new ArrayList<IpUserRole>();
	IpUserRole ipUserRole;
	Iterator iterator = userRoleList.iterator();
	 while (iterator.hasNext()) {
		 Map map4ipMenu = (Map) iterator.next();
		 ipUserRole = new IpUserRole();
		 ipUserRole.setTheId((String)map4ipMenu.get("theId"));
		 ipUserRole.setCoId((String)map4ipMenu.get("coId"));
		 ipUserRole.setCoName((String)map4ipMenu.get("coName"));
		 ipUserRole.setIsPartTime((String)map4ipMenu.get("isPartTime"));
		 ipUserRole.setRoleId((String)map4ipMenu.get("roleId"));
		 ipUserRole.setRoleName((String)map4ipMenu.get("roleName"));
		 ipUserRole.setUserId((String)map4ipMenu.get("userId"));
		 ipUserRole.setUserName((String)map4ipMenu.get("userName"));
		 		 
		 ipUserRoles.add(ipUserRole);
	}
	return ipUserRoles;
}

	public IpRole findIpRoleByRoleId(String roleId) {
		// TODO 自动生成的方法存根
		return iPRoleDao.findRoleByRoleId(roleId);
		
	}

	private UserGroupResourceParam getBpmRoleParam(IpRole ipRoleEntity, String roleCode) {
		UserGroupResourceParam userGroupResourceParam = new UserGroupResourceParam();
		userGroupResourceParam.setName(ipRoleEntity.getRoleName());
		userGroupResourceParam.setCode(roleCode);//设置ip角色code
		return userGroupResourceParam;
		
	}
	
}