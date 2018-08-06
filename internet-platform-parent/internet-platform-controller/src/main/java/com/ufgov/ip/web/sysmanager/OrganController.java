package com.ufgov.ip.web.sysmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ufgov.ip.api.base.IPCommonServiceI;
import com.ufgov.ip.api.sysmanager.IPRoleServiceI;
import com.ufgov.ip.api.sysmanager.IpCompanyServiceI;
import com.ufgov.ip.api.sysmanager.IpUserCompanyServiceI;
import com.ufgov.ip.api.sysmanager.IpUserRoleServiceI;
import com.ufgov.ip.api.system.GenTableServiceI;
import com.ufgov.ip.api.system.HirerAccountServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.entity.base.IpDictionary;
import com.ufgov.ip.entity.base.IpDictionaryDetail;
import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.sysmanager.IpRole;
import com.ufgov.ip.entity.sysmanager.IpUserRole;
import com.ufgov.ip.entity.system.ErrorInfo;
import com.ufgov.ip.entity.system.GenTable;
import com.ufgov.ip.entity.system.GenTableColumn;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.IpUserCompany;
import com.ufgov.ip.entity.system.IpUserDTO;
import com.ufgov.ip.entity.system.UserCheck;
import com.ufgov.ip.facade.EnumType;
import com.ufgov.ip.utils.BathImportUtil;
import com.ufgov.ip.utils.CopyPropertiesUtil;
import com.ufgov.ip.utils.JsonDateValueProcessor;
import com.ufgov.ip.utils.UUIDTools;
import com.ufgov.ip.utils.UploadFileURLUtils;
import com.uggov.ip.web.tmp.ObjectSerialize;
import com.uggov.ip.web.tmp.PartTimeParseUtil;
import com.uggov.ip.web.tmp.SettingPropertiesUtil;
import com.yonyou.iuap.utils.CookieUtil;
import com.yonyou.iuap.iweb.entity.DataTable;

import uap.iweb.exception.WebRuntimeException;
import uap.iweb.icontext.IWebViewContext;
import uap.web.auth.Constants;
import uap.web.httpsession.cache.SessionCacheManager;

@Component("org.OrganController")
@Scope("prototype")
@RequestMapping(value = "organization")
public class OrganController {
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
		private GenTableServiceI genTableService;

	@Autowired
	private IPCommonServiceI ipCommonService;

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

	@RequestMapping(method = RequestMethod.GET, value = "organInfo")
	public String organ_info(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		// COOKIE中获取hirerId
		String hirerId = CookieUtil.findCookieValue(request.getCookies(),
				"hirerId");

		String result = request.getParameter("result");
		String fileName = request.getParameter("fileName");

		model.addAttribute("result", result);
		model.addAttribute("fileName", fileName);

		String uploadExcelInfo = request.getParameter("uploadExcelInfo");
		model.addAttribute("uploadExcelInfo", uploadExcelInfo);

		IpHirer findHirerByHirerId = hirerAccountService
				.findHirerByHirerId(hirerId);
		String hirerNo = findHirerByHirerId.getHirerNo();

		model.addAttribute("hirerId", hirerId);
		model.addAttribute("hirerNo", hirerNo);

		// 获得所有的员工
		JSONArray json = new JSONArray();
		List<IpUser> allUsers = userAccountService.findUserAll();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());
		json = JSONArray.fromObject(allUsers, jsonConfig);
		model.addAttribute("userInfo", json.toString());
		return "sys_manager/organization";
	}

	/**
	 * 添加员工
	 * 
	 * @param ipUserEntity
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "saveUserInfo")
	public @ResponseBody Map<String, String> saveUserInfo(
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "success");
		try {

			//获取前端的值
			String isEditOrSave=request.getParameter("isEditOrSave");
			
			//获取
			String userId=request.getParameter("userId");
			
			
			// 部门
			IpCompany ic = new IpCompany();// 含部门编号、部门名称、简称(coName)
			BeanUtils.populate(ic, request.getParameterMap());
			// 职务
			IpRole ir = new IpRole();// 含职务编号、职务名称
			BeanUtils.populate(ir, request.getParameterMap());

			IpUser ipUserEntity = new IpUser();
			IpUserDTO ipUserDTO = new IpUserDTO();
			request.setCharacterEncoding("utf-8");
			BeanUtils.populate(ipUserDTO, request.getParameterMap());
			CopyPropertiesUtil.copyPropertyWithoutDate(ipUserEntity, ipUserDTO);
			String graduatioinTime = ipUserDTO.getGraduatioinTime();
			if ("".equals(graduatioinTime) || graduatioinTime == null) {
				ipUserEntity.setGraduatioinTime(null);
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date d1 = sdf.parse(graduatioinTime);
				ipUserEntity.setGraduatioinTime(d1);
			}
			String hirerId = CookieUtil.findCookieValue(request.getCookies(),
					"hirerId");
			ipUserEntity.setHirerId(hirerId);

			// 兼职的部门及职务
			String partTimeInfo = request.getParameter("isParttimeInfo");// 前台封装好的兼职信息包括部门、职务、及编号、是否兼职
			List list = new ArrayList();
			if (!"[]".equals(partTimeInfo)) {
				list = PartTimeParseUtil.getList(partTimeInfo, ipUserEntity);
			}

			//编辑员工信息时密码做特殊处理（不修改密码）
			if("save".equals(isEditOrSave) || isEditOrSave==null || "".equals(isEditOrSave))
			{
				// 加密
				this.entryptPassword(ipUserEntity);
			}
			else if("edit".equals(isEditOrSave))
			{
				IpUser userEdit=userAccountService.findUserByUserId(userId);
				ipUserEntity.setPassword(userEdit.getPassword());
				ipUserEntity.setSalt(userEdit.getSalt());
			}
			String saveIpUserEntity = userAccountService.saveIpUserEntity(ipUserEntity, ic, ir, list,
					resultMap);
			if("wrong".equals(saveIpUserEntity)){
				resultMap.put("result", "taskUnfinished");
				resultMap.put("reason", "当前员工还未完成待办任务，角色不能更换！");
			}
		} catch (Exception e) {
			// 记录日志
			logger.error("新增员工发生错误!", e);
			resultMap.put("result", "fail");
			resultMap.put("reason", "服务端繁忙，请稍后重试！");
		}
		return resultMap;

	}

	/**
	 * 回显员工信息
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "backToShowUser")
	public @ResponseBody Map<String, Object> backToShowUser(
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {

		String userId = request.getParameter("userId");
		Map<String, Object> hirerCfg = new HashMap<String, Object>();
		// 非兼职
		/*List<IpCompany> no_p_co = ipUserCompanyService
				.showUserCompanyByUserIdAndIsPartTime(userId, "0");*/
		
		List<IpCompany> no_p_co = ipUserCompanyService
				.showUserCompanyByUserId(userId);
		List<IpUserRole> no_p_userRole = ipUserRoleService
				.showUserRoleByUserIdAndIsPartTime(userId, "0");
		
		List<IpRole> no_p_role=new ArrayList<IpRole>();
		for (IpUserRole ipUserRole : no_p_userRole) {
			String roleId = ipUserRole.getRoleId();
			IpRole findIpRoleByRoleId = iPRoleService.findIpRoleByRoleId(roleId);
			if(findIpRoleByRoleId==null){
				IpRole ipRole=new IpRole();
				ipRole.setRoleName("请选择");
				ipRole.setRoleId("");
				no_p_role.add(ipRole);
			}else{
				no_p_role.add(findIpRoleByRoleId);
			}
		}

		// 兼职
		List<IpCompany> part_co = ipUserCompanyService
				.showUserCompanyByUserIdAndIsPartTime(userId, "1");
		List<IpUserRole> part_userRole = ipUserRoleService
				.showUserRoleByUserIdAndIsPartTime(userId, "1");
		List<IpRole> part_role=new ArrayList<IpRole>();
		for (IpUserRole ipUserRole : part_userRole) {
			String roleId = ipUserRole.getRoleId();
			IpRole findIpRoleByRoleId = iPRoleService.findIpRoleByRoleId(roleId);
			if(findIpRoleByRoleId==null){
				IpRole ipRole=new IpRole();
				ipRole.setRoleName("请选择");
				ipRole.setRoleId("");
				part_role.add(ipRole);
			}else{
				part_role.add(findIpRoleByRoleId);
			}
			
		}
		
		

		IpUser findUserByUserId = userAccountService.findUserByUserId(userId);
		
		// 查询学历
		IpDictionary findIpDictionaryByDicType = iPCommonService
						.findIpDictionaryByDicType(EnumType.DEGREE);
				List<IpDictionaryDetail> findIpDictionaryDetailByDicType = iPCommonService
						.findIpDictionaryDetailByDicId(findIpDictionaryByDicType
								.getDicId());
				
				//all_duty
				String hirerId = CookieUtil.findCookieValue(request.getCookies(),
						"hirerId");
				List<IpRole> roleInfo = iPRoleService.getRoleInfoByHirerId(hirerId);
				
		
		

		hirerCfg.put("p_role", part_role);
		hirerCfg.put("part_co", part_co);

		if (no_p_role.size() == 0) {
			hirerCfg.put("no_p_role", "");
		} else {
			hirerCfg.put("no_p_role", no_p_role.get(0));
		}
		if (no_p_co.size() == 0) {
			hirerCfg.put("no_p_co", "");
		} else {
			hirerCfg.put("no_p_co", no_p_co.get(0));
		}

		hirerCfg.put("back_user", findUserByUserId);
		
		hirerCfg.put("all_edu", findIpDictionaryDetailByDicType);
		hirerCfg.put("all_duty", roleInfo);

		return hirerCfg;
	}

	
	/**
	 * 登录号校验
	 * 接口：organization/checkLoginName
	 * @param request
	 * @param response
	 * @param model
	 * @return result:"success"/"H_two"/"U_two"
	 */
	@RequestMapping(method = RequestMethod.GET, value = "checkLoginName")
	public @ResponseBody Map<String, String> checkLoginName(@RequestParam String loginName,@RequestParam String userId,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		Map<String, String> reg=new HashMap<String, String>();
		reg.put("result", "success");
		
		IpUser findUserByLoginName = userAccountService.findUserByLoginName(loginName);
		IpUser findUserByUserId = userAccountService.findUserByUserId(userId);
		
		
		//校验租户的
		/*if(sessionCacheManager_h.exists(loginName)){//缓存中有数据
			Map<String, String> checkHirerAndUser = checkHirerAndUser(loginName, reg);
			return checkUserInfo(findUserByLoginName, checkHirerAndUser);
			
		}else{*/
			Map<String, String> putHirerIntoCache = putHirerIntoCache();//再查询一遍放进缓存
			Map<String, String> checkHirerAndUser = checkHirerAndUser(loginName, reg,putHirerIntoCache);
			return checkUserInfo(findUserByLoginName, findUserByUserId,checkHirerAndUser);
		//}
	}
	
	
	/**
	 * 校验手机号
	 * 接口：organization/checkCellphoneNo
	 * @param cellphoneNo
	 * @param request
	 * @param response
	 * @param model
	 * @return result:"success"/"fail"
	 */
	@RequestMapping(method = RequestMethod.GET, value = "checkCellphoneNo")
	public @ResponseBody Map<String, String> checkCellphoneNo(@RequestParam String cellphoneNo,@RequestParam String userId,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		Map<String, String> reg=new HashMap<String, String>();
		reg.put("result", "success");
		IpUser findUserByCellphoneNo = userAccountService.findUserByCellphoneNo(cellphoneNo);
		IpUser findUserByUserId = userAccountService.findUserByUserId(userId);
		
		/*if(sessionCacheManager_h.exists(cellphoneNo)){//缓存中有数据
			Map<String, String> checkHirerAndUser = checkHirerAndUser(cellphoneNo, reg);
			return checkUserInfo(findUserByCellphoneNo, checkHirerAndUser);
			
		}else{*/
			Map<String, String> putHirerIntoCache = putHirerIntoCache();//再查询一遍放进缓存
			Map<String, String> checkHirerAndUser = checkHirerAndUser(cellphoneNo, reg,putHirerIntoCache);
			return checkUserInfo(findUserByCellphoneNo,findUserByUserId,checkHirerAndUser);
		//}
	}
	
	
	/**
	 * 邮箱校验
	 * @param userEmail
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "checkUserEmail")
	public @ResponseBody Map<String, String> checkUserEmail(@RequestParam String userEmail,@RequestParam String userId,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		Map<String, String> reg=new HashMap<String, String>();
		reg.put("result", "success");
		IpUser findUserByUserEmail = userAccountService.findUserByUserEmail(userEmail);
		IpUser findUserByUserId = userAccountService.findUserByUserId(userId);
		/*if(sessionCacheManager_h.exists(userEmail)){//缓存中有数据
			Map<String, String> checkHirerAndUser = checkHirerAndUser(userEmail, reg);
			return checkUserInfo(findUserByUserEmail, checkHirerAndUser);
			
		}else{*/
			Map<String, String> putHirerIntoCache = putHirerIntoCache();//再查询一遍放进缓存
			Map<String, String> checkHirerAndUser = checkHirerAndUser(userEmail, reg,putHirerIntoCache);
			return checkUserInfo(findUserByUserEmail,findUserByUserId,checkHirerAndUser);
		//}
	}
	
	
	/**
	 * 删除员工兼职
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "deletePart")
	public @ResponseBody Map<String, String> deletePart(
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		Map<String, String> reg=new HashMap<String, String>();  	
		String userId = request.getParameter("userId");
		String coId = request.getParameter("coId");
		String roleId = request.getParameter("roleId");
		
		try{
		//删除兼职部门
		ipUserCompanyService.deleteByUserIdAndCoId(userId,coId);
		
		//删除兼职职务
		ipUserRoleService.deleteByUserIdAndRoleId(userId,roleId,"1");
		reg.put("result", "success");
		return reg; 
		}catch(Exception ex){
			ex.printStackTrace();
			reg.put("result", "fail");
			return reg; 
			
		}
	}
	
	
	/**
	 * none:无子部门
	 * child:有子部门
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "preventAddUser")
	public @ResponseBody Map<String,String> preventAddUser(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		
		Map<String,String> reg=new HashMap<String, String>();
		String coId = request.getParameter("coId");
		List<IpCompany> findCompanyByParentCoId = ipCompanyService.findCompanyByParentCoId(coId);
		if(findCompanyByParentCoId.size()==0){//无子部门
			reg.put("result", "none");
		}else{
			reg.put("result", "child");
		}
		     return reg;
	}
	
	
	/**
	 * 获得所有的职务
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "getRoleInfo")
	public @ResponseBody List<IpRole> getRoleInfo(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String hirerId = CookieUtil.findCookieValue(request.getCookies(),
				"hirerId");
		List<IpRole> roleInfo = iPRoleService.getRoleInfoByHirerId(hirerId);
		return roleInfo;
	}

	/**
	 * 获得学历
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "getEnumInfo")
	public @ResponseBody List<IpDictionaryDetail> getEnumInfo(
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		// 查询学历
		IpDictionary findIpDictionaryByDicType = iPCommonService
				.findIpDictionaryByDicType(EnumType.DEGREE);
		List<IpDictionaryDetail> findIpDictionaryDetailByDicType = iPCommonService
				.findIpDictionaryDetailByDicId(findIpDictionaryByDicType
						.getDicId());
		return findIpDictionaryDetailByDicType;
	}

	/**
	 * 检查员工号是否重复
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(method = RequestMethod.GET, value = "checkEmployeeNo")
	public void checkEmployeeNo(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		String employeeNo = request.getParameter("employeeNo");

		boolean isExist = userAccountService.checkEmployeeNo(employeeNo);
		try {
			if (isExist) {
				response.getWriter().print("true");
			} else {
				response.getWriter().print("false");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 停用/启用
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(method = RequestMethod.GET, value = "updateUsersIsEnable")
	public @ResponseBody Map<String, String> updateUsersIsEnable(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
        Map<String,String> res=new HashMap<String, String>();
		String userId = request.getParameter("userId");
		String isEnabled = request.getParameter("isEnabled");// 0-停用，1-启用
		try {
			userAccountService.updateUserByUserId(userId, isEnabled);
			res.put("result", "success");
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			res.put("result", "fail");
			return res;
		}
	}

	/**
	 * 保存组织结构
	 * 
	 * @param ipCompanyEntity
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "saveOrgInfo")
	public @ResponseBody Map<String, String> saveOrgInfo(
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		IpCompany ipCompany = new IpCompany();
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "success");
		try {
			Map<String, String[]> parameterMap = request.getParameterMap();
			BeanUtils.populate(ipCompany, parameterMap);
			
			updateRestColSort(request, model, ipCompany);
			
			resultMap=ipCompanyService.saveIpCompanyEntity(ipCompany, resultMap);
			System.out.println(resultMap);

		} catch (Exception e) {
			// 记录日志
			logger.error("新增部门发生错误!", e);
			resultMap.put("result", "fail");
			resultMap.put("reason", "服务端繁忙，请稍后重试！");
		}
		return resultMap;

	}

	private void updateRestColSort(HttpServletRequest request, ModelMap model,
			IpCompany ipCompany) {
		//获得排好顺序的预留字段
		String fieldSortInfo = request.getParameter("fieldSort");
		Gson gson = new Gson();
		List<String> fieldSortInfoList = (List<String>)gson.fromJson(fieldSortInfo, new TypeToken<ArrayList<String>>() {}.getType());
		//begin_如果没有加预留字段，不需要更新预留字段的顺序
			if(fieldSortInfoList==null){
				return;
			}
		//end_如果没有加预留字段，不需要更新预留字段的顺序
		//根据排序好的字段信息，更新列属性表的排序
		  //1.获得当前租户已经启用的预留字段信息
		    List<GenTableColumn> restColumnProperties = getRestColumnProperties(ipCompany.getHirerId(), "ip_company", model);
		    //begin_防止空指针，之前可能没有添加预留字段
			    if(restColumnProperties==null){
					return;
				}
		  //end_防止空指针，之前可能没有添加预留字段
		  //获得预留字段的顺序
		    List<Integer> sortList=new ArrayList<Integer>();
		    for (GenTableColumn  restColumnProperties_e: restColumnProperties) {
		    	sortList.add(restColumnProperties_e.getSort());
			}
		    
		   
		    List<GenTableColumn> restColumnTemp=new ArrayList<GenTableColumn>();
		    for(int i=0;i<fieldSortInfoList.size();i++){
		    	for (GenTableColumn genTableColumn : restColumnProperties) {
		    		  if(fieldSortInfoList.get(i).equals(genTableColumn.getJavaField())){
		    			  restColumnTemp.add(genTableColumn);
		    		  }else{/*
		    			  //在restColumnProperties中寻找该元素和当前元素的sort值互换
		    			  String javaFieldName = fieldSortInfoList.get(i);
		    			  for (GenTableColumn genTableColumn_search : restColumnProperties) {
		    				  if(genTableColumn_search.getJavaField().equals(javaFieldName)){
		    					  //互换排序
		    					  int sortTemp=genTableColumn_search.getSort();
		    					  genTableColumn_search.setSort(genTableColumn.getSort());
		    					  genTableColumn.setSort(sortTemp);
		    				  }
		    			  }
		    		  */
		    		  }
		    	}
		    }
		    
		    for(int i = 0 ; i < restColumnTemp.size() ; i++) {
		    	for (int j=i;j<sortList.size();j++ ) {
		    		restColumnTemp.get(i).setSort(sortList.get(j));
		    		break;
				}
			}
		    
		   //2.更新列属性的排序
		    genTableService.updateDeptRestCol(restColumnTemp);
	}
	
	
	public List<GenTableColumn> getRestColumnProperties(String hirerId,String tableName, ModelMap model) {
		GenTable genTable = new GenTable();
	    genTable.setTableName(tableName);
	    genTable.setIsGen("1");
	    genTable.setHirerId(hirerId);
		Map<String,Object> reg=new HashMap<String,Object>();
		genTable = genTableService.findByTableNameAndIsGenAndHirerId(genTable);
		if(genTable==null){
			return null;
		}else{
			return genTable.getColumnList();
		}
		
	}
	
	public List<GenTableColumn> getRestColumnPropertiesWitoutSort(String hirerId,String tableName, ModelMap model) {
		GenTable genTable = new GenTable();
	    genTable.setTableName(tableName);
	    genTable.setIsGen("1");
	    genTable.setHirerId(hirerId);
		Map<String,Object> reg=new HashMap<String,Object>();
		genTable = genTableService.findByTableNameAndIsGenAndHirerIdWitoutSort(genTable);
		if(genTable==null){
			return null;
		}else{
			return genTable.getColumnList();
		}
		
	}

	/**
	 * 更新部门
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "updateOrgInfo")
	public @ResponseBody Map<String, String> updateOrgInfo(
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		IpCompany ipCompany = new IpCompany();
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "success");
		try {
			Map<String, String[]> parameterMap = request.getParameterMap();
			BeanUtils.populate(ipCompany, parameterMap);
			updateRestColSort(request, model, ipCompany);
			resultMap=ipCompanyService.updateIpCompanyEntity(ipCompany, resultMap);
		} catch (Exception e) {
			// 记录日志
			logger.error("编辑部门发生错误!", e);
			resultMap.put("result", "fail");
			resultMap.put("reason", "服务端繁忙，请稍后重试！");
		}
		return resultMap;

	}

	/**
	 * 获得当前部门的信息及父级部门的信息
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(method = RequestMethod.GET, value = "getEditcompny")
	public @ResponseBody Map<String, Object> getEditcompny(
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		String coId = request.getParameter("coId");
		Map<String, Object> map = new HashMap<String, Object>();
		IpCompany childIC = ipCompanyService.findByCoId(coId);// 当前部门的信息
		
		 
		String parentCoId = childIC.getParentCoId();
		IpCompany parentIC = ipCompanyService.findByCoId(parentCoId);// 当父级部门的信息
		map.put("childIC", childIC);
		map.put("parentIC", parentIC);
		
		//获得当前租户设置的预留字段属性集合(设置好顺序的)
		 List<GenTableColumn> restColumnProperties = getRestColumnProperties(childIC.getHirerId(), "ip_company", model);
		 if(restColumnProperties==null || restColumnProperties.size()==0){
			 //map.put("columnList", null);//test
			 return map;
		 }
		 //1.获得每一列的javaField，然后通过反射获得具体的值,并拼装在一起放在集合里
		 List<String> sortFieldAndValeList=new ArrayList<String>();
		 for (GenTableColumn genTableColumn : restColumnProperties) {
			 String javaField = genTableColumn.getJavaField();
			 String javaFieldValue =(String)CopyPropertiesUtil.getValue(childIC, javaField);
			 if(javaFieldValue==null){
				 javaFieldValue="";
			 }
			 sortFieldAndValeList.add(javaField+":"+javaFieldValue);
		 }
		 
		 //2.返回具体的预留字段key:value
		 map.put("sortFieldAndValeList", sortFieldAndValeList);
		 map.put("columnList", restColumnProperties);
		
		return map;
	}

	/**
	 * 删除部门
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "delOrgInfo")
	public void delOrgInfo(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String flag=request.getParameter("flag");
		String coId = request.getParameter("coId");
		IpCompany findByCoId = ipCompanyService.findByCoId(coId);
		
		if("false".equals(flag)){		
			// 获得所有的子部门_含有子部门不能删除
			List<IpCompany> findCompanyByParentCoId = ipCompanyService
					.findCompanyByParentCoId(coId);
			if (findCompanyByParentCoId.size() != 0) {
				try {
					response.getWriter().print("has_son");
					return;
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				List<IpUserCompany> findIpUserCompanyByCoId = ipUserCompanyService
						.findIpUserCompanyByCoId(coId);
				if (findIpUserCompanyByCoId.size() != 0) {
					try {
						response.getWriter().print("has_employee");
						return;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else{
					try {
						ipCompanyService.deletePart(findByCoId);			
						response.getWriter().print("true");
					} catch (IOException e) {
						try {
							response.getWriter().print("false");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
		     }
	       }else {
				try {
					ipCompanyService.deletePart(findByCoId);			
					response.getWriter().print("true");
				} catch (Exception e) {
					try {
						response.getWriter().print("false");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
			}
		}
	}

	/**
	 * 获得模板的URL
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(method = RequestMethod.GET, value = "download")
	public void download(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		String url2 = UploadFileURLUtils.getUrl("template", "templateFilePath");
		File file = new File(url2);
		String templateName = "";
		File[] listFiles = file.listFiles();
		if (listFiles.length != 0) {
			for (File file2 : listFiles) {
				if (file2.isFile()) {
					templateName = file2.getName();
					break;
				}
			}
		}
		String path = file.getAbsolutePath();
		String url = path + "\\" + templateName;
		model.addAttribute("templateURL", url);
	}

	/**
	 * excel文件上传
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(method = RequestMethod.POST, value = "uploadUserInfo")
	public String uploadUserInfo(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String uploadExcelInfo = "";
		try {
			// 完成上传
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile pictureFile = multipartRequest.getFile("excelFile");
			String originalFilename = pictureFile.getOriginalFilename();
			String value = "";
			value = CookieUtil.findCookieValue(request.getCookies(), "hirerId");
			IpHirer findHirerByHirerId = hirerAccountService
					.findHirerByHirerId(value);
			String fileName = findHirerByHirerId.getHirerId()
					+ originalFilename.substring(originalFilename
							.lastIndexOf("."));
			String url2 = UploadFileURLUtils.getUrl("importExcel",
					"importExcelFilePath");
			// File file = new File(url2);
			String path = request.getServletContext().getRealPath("/");
			File file = new File(path + "//" + url2);
			if (!file.exists()) {
				file.mkdirs();
			}
			File[] listFiles = file.listFiles();
			if (listFiles != null) {
				for (File file2 : listFiles) {
					if (file2.isFile()
							&& file2.getName().startsWith(
									findHirerByHirerId.getHirerId())) {
						file2.delete();
						break;
					}
				}
			}
			// String path = file.getAbsolutePath();
			// File file1 = new File(path);
			// file1.mkdirs();
			String url = path + "\\" + url2+"\\"+fileName;
			url=url.replace("\\", "/");
			pictureFile.transferTo(new File(url));
			return "redirect:/organization/organInfo?result=true&fileName="
					+ fileName;

		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("result", "false");
			return "redirect:/organization/organInfo?result=false";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "showMeg")
	public @ResponseBody Map<String, Object> showMeg(
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", "true");

		String uploadExcelInfo = CookieUtil.findCookieValue(
				request.getCookies(), Constants.PARAM_TOKEN);
		String meg = CookieUtil.findCookieValue(request.getCookies(),
				uploadExcelInfo + "excel");

		if ("false".equals(meg)) {
			resultMap.put("result", "false");
		}
		return resultMap;
	}

	/**
	 * 批量导入
	 * 
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(method = RequestMethod.GET, value = "bathImportUserInfo")
	public @ResponseBody Map<String, Object> bathImportUserInfo(
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		InputStream is;
		InputStream template_is = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", "success");
		String value = CookieUtil.findCookieValue(request.getCookies(),
				"hirerId");
		IpHirer findHirerByHirerId = hirerAccountService
				.findHirerByHirerId(value);

		String url2 = UploadFileURLUtils.getUrl("importExcel",
				"importExcelFilePath");
		//File file = new File(url2);
		
		String path = request.getServletContext().getRealPath("/");
		path=path + "\\" + url2;
		path=path.replace("\\", "/");
		System.out.println("**********************************************"+path+"/"+findHirerByHirerId.getHirerId());
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		
		File[] listFiles = file.listFiles();
		String fileURL = "";
		boolean isFlag = true;
		if (listFiles != null) {
			for (File file2 : listFiles) {
				if (!file2.isFile()) {
					continue;
				} else if (file2.getName().startsWith(
						findHirerByHirerId.getHirerId())) {
					fileURL = file2.getAbsolutePath();
					isFlag = false;
					break;
				}
			}
			if (isFlag) {
				resultMap.put("result", "fileNotFound");
				return resultMap;// 文件还没上传
			}
			isFlag = true;
		} else {
			resultMap.put("result", "fileNotFound");
			return resultMap;// 文件还没上传
		}
		try {

			// 开始导入
			// String fileURL="D:\\userInfor.xlsx";
			is = new FileInputStream(fileURL);// 获得源文件地址
			BathImportUtil excelReader = new BathImportUtil();
			boolean checkFormat = BathImportUtil.checkFormat(excelReader, is,
					template_is, fileURL,request);
			if (!checkFormat) {
				resultMap.put("result", "format");
				// 上传失败_格式不正确
				return resultMap;// 返回异常页
			}

			List<String[]> readExcelContent = excelReader.readExcelContent(is,
					fileURL);
			List copyProperty = SettingPropertiesUtil
					.copyProperty(readExcelContent);// 获取要添加的信息

			List<IpUser> userlist = (List<IpUser>) copyProperty.get(0);
			List<IpUserCompany> ipUserCompanylist = (List<IpUserCompany>) copyProperty
					.get(1);
			List<IpUserRole> ipUserRolelist = (List<IpUserRole>) copyProperty
					.get(2);

			List<IpUser> userlist_b = new ArrayList<IpUser>();
			List<IpUserCompany> ipUserCompanylist_b = new ArrayList<IpUserCompany>();
			List<IpUserRole> ipUserRolelist_b = new ArrayList<IpUserRole>();

			List<ErrorInfo> errorList = new ArrayList<ErrorInfo>();
			List<UserCheck> error_List = new ArrayList<UserCheck>();

			List<IpUser> findUserAll = userAccountService.findUserAll();
			//List<IpRole> findAllRole = iPRoleService.findAllRole();
			//List<IpCompany> findAllCompany = ipCompanyService.findAll();
			
			//根据hirerId过滤部门、职责
			List<IpRole> findAllRole =iPRoleService.getRoleInfoByHirerId(findHirerByHirerId.getHirerId());
			List<IpCompany> findAllCompany=ipCompanyService.findCompanyByHirerId(findHirerByHirerId.getHirerId());
			

			if (readExcelContent.size() == 0) {
				resultMap.put("result", "empty_excel");
				return resultMap;
				// 2.Excel有数据_数据库为空/不为空
			} else {
				int excel_rowno = 2; // 记录Excel的行号，第1行是标题，从2行开始
				int pre_insert_no = 0;// 准备往数据库插入的第几笔数据

				// begin_开始将信息放进缓存

				Map<String,String> restUserCache=new HashMap<String, String>();
				//begin_导入可用人员后再次，导入excel，过滤已导入的员工_20160614
					String tokens_c = CookieUtil.findCookieValue(request.getCookies(),
							Constants.PARAM_TOKEN);
					byte[] in = sessionCacheManager.getCurUser(tokens_c + "import");
					List deserialize = ObjectSerialize.deserialize(in);
					if(deserialize.size()!=0){//上次导入有可导入的人员，将人员放进map，校验的时候进行过滤
					 List<IpUser> byte_userList = (List<IpUser>) deserialize.get(0);
						for (IpUser user : byte_userList) {
							
							String loginName = user.getLoginName();
							String userEmail = user.getUserEmail();
							String cellphoneNo = user.getCellphoneNo();
							restUserCache.put(loginName, loginName);
							restUserCache.put(userEmail, userEmail);
							restUserCache.put(cellphoneNo, cellphoneNo);
						}
						
						
					}
			   //end_导入可用人员后再次，导入excel，过滤已导入的员工_20160614
				
				
				Map<String,String> userCache=new HashMap<String, String>();
				Map<String,String> coCache=new HashMap<String, String>();
				Map<String,String> roleCache=new HashMap<String, String>();
				
				for (IpUser ipUser2 : findUserAll) {
					String loginName = ipUser2.getLoginName();
					String userEmail = ipUser2.getUserEmail();
					String cellphoneNo = ipUser2.getCellphoneNo();
					userCache.put(loginName, loginName);
					userCache.put(userEmail, userEmail);
					userCache.put(cellphoneNo, cellphoneNo);
				}
				for (IpRole ipRole : findAllRole) {
					String roleName = ipRole.getRoleName();
					roleCache.put(roleName, roleName);
					//sessionCacheManager.cacheUser(roleName, roleName);
				}
				for (IpCompany ipCompany : findAllCompany) {
					String deptDetail = ipCompany.getDeptDetail();
					coCache.put(deptDetail, deptDetail);
					//sessionCacheManager.cacheUser(deptDetail, deptDetail);
				}
				// end_开始将信息放进缓存

				for (IpUser ipUser_excel : userlist) {
					boolean flag = true; // 默认可插入数据库.
					boolean insert_flag = true; // 默认可插入数据库.
					UserCheck errorInfo = new UserCheck();
					// begin_校验开始
					if (findUserAll.size() != 0) {// 如果员工表为null，那么不需要校验登录名、邮箱、手机号，只需要校验职责、部门
						
						//姓名校验
						if("".equals(ipUser_excel.getUserName())){//用户名为空

							errorInfo.setUsername("第" + excel_rowno
									+ "行   【姓名】 为空");
							flag = false;
						}
						
						//性别校验
						if("".equals(ipUser_excel.getUserSex())){//用户名为空

							errorInfo.setUserSex("第" + excel_rowno
									+ "行   【性别】 为空");
							flag = false;
						}
						
						
						//手机号校验
						if("".equals(ipUser_excel.getCellphoneNo())){//手机号为空

							errorInfo.setVerifyCellPhoneNo("第" + excel_rowno
									+ "行   【手机号】 为空");
							flag = false;
						}else if(ipUser_excel.getCellphoneNo().equals(
								userCache.get(ipUser_excel
										.getCellphoneNo()))){
							//begin_导入可用人员后再次，导入excel，过滤已导入的员工_20160614
								if(deserialize.size()!=0 && ipUser_excel.getCellphoneNo().equals(restUserCache.get(ipUser_excel.getCellphoneNo()))){//已经导入，但不提示重复，而是后面不需要导入
									insert_flag=false;
								}else{
									// 手机号
									errorInfo.setVerifyCellPhoneNo("第" + excel_rowno
											+ "行   【手机号】:"
											+ ipUser_excel.getCellphoneNo() + "重复！");
									flag = false;
								}
							//end_导入可用人员后再次，导入excel，过滤已导入的员工_20160614
							
						}else if(!patternCheck("phone",ipUser_excel.getCellphoneNo())){
							errorInfo.setVerifyCellPhoneNo("第" + excel_rowno
									+ "行   【手机】:" + ipUser_excel.getCellphoneNo()
									+ "格式不正确！");
							flag = false;
						}else{
							
							Map<String, String> putHirerIntoCache = putHirerIntoCache();
							if(ipUser_excel.getCellphoneNo().equals(putHirerIntoCache.get(ipUser_excel.getCellphoneNo()))){//和租户手机号相同
					           
								errorInfo.setVerifyCellPhoneNo("第" + excel_rowno
										+ "行   【手机】:" + ipUser_excel.getCellphoneNo()
										+ "和租户手机号重复！");
								flag = false;
							}
							
						}
							
						//登录名校验
						String excelLoginName=ipUser_excel.getLoginName()+"@"+findHirerByHirerId.getHirerNo();
						if("".equals(ipUser_excel.getLoginName())){//邮箱为空

							errorInfo.setVerifyLoginName("第" + excel_rowno
									+ "行   【登录名】 为空");
							flag = false;
						}else if (excelLoginName.equals(
								userCache.get(ipUser_excel
										.getLoginName()))) {
							//begin_导入可用人员后再次，导入excel，过滤已导入的员工_20160614
								if(deserialize.size()!=0 && ipUser_excel.getUserName().equals(restUserCache.get(ipUser_excel.getUserName()))){//已经导入，但不提示重复，而是后面不需要导入
									insert_flag=false;
								}else{
									// 登录名
									errorInfo.setVerifyLoginName("第" + excel_rowno
											+ "行   【登录名】:" + ipUser_excel.getUserName()
											+ "重复！");
									flag = false;
								}
							//end_导入可用人员后再次，导入excel，过滤已导入的员工_20160614
						}else{
							Map<String, String> putHirerIntoCache = putHirerIntoCache();
							if(excelLoginName.equals(putHirerIntoCache.get(excelLoginName))){//和租户名称相同
					           
								errorInfo.setVerifyLoginName("第" + excel_rowno
										+ "行   【登录名】:" + ipUser_excel.getUserName()
										+ "和租户名称重复！");
								flag = false;
							}
						}
						
						//邮箱校验
						/*if("".equals(ipUser_excel.getUserEmail())){//邮箱为空

							errorInfo.setVerifyUserEmail("第" + excel_rowno
									+ "行   【邮箱】 为空");
							flag = false;
						}else */if (ipUser_excel.getUserEmail().equals(
								userCache.get(ipUser_excel
										.getUserEmail()))) {
							
							//begin_导入可用人员后再次，导入excel，过滤已导入的员工_20160614
								if(deserialize.size()!=0 && ipUser_excel.getUserEmail().equals(restUserCache.get(ipUser_excel.getUserEmail()))){//已经导入，但不提示重复，而是后面不需要导入
									insert_flag=false;
								}else{
									if(!"".equals(ipUser_excel.getUserEmail())){
										// 邮箱
										errorInfo.setVerifyUserEmail("第" + excel_rowno
												+ "行   【邮箱】:" + ipUser_excel.getUserEmail()
												+ "重复！");
										flag = false;
									}
									
								}
							//end_导入可用人员后再次，导入excel，过滤已导入的员工_20160614
						}else if(!patternCheck("mail",ipUser_excel.getUserEmail())){
							errorInfo.setVerifyUserEmail("第" + excel_rowno
									+ "行   【邮箱】:" + ipUser_excel.getUserEmail()
									+ "格式不正确！");
							flag = false;
							
						}else{
							Map<String, String> putHirerIntoCache = putHirerIntoCache();
							if(ipUser_excel.getUserEmail().equals(putHirerIntoCache.get(ipUser_excel.getUserEmail()))){//和租户名称相同
						           
								errorInfo.setVerifyLoginName("第" + excel_rowno
										+ "行   【邮箱】:" + ipUser_excel.getUserEmail()
										+ "和租户邮箱重复！");
								flag = false;
							}
						}
					}

					IpUserCompany ipUserCompany = ipUserCompanylist
							.get(pre_insert_no);
					String coName = ipUserCompany.getCoName();
					//String[] split = coName.split("/");
					//for (String deptDetailInfo : split) {
						if (coCache.get(coName) == null) {
							if("".equals(coName)){
								errorInfo.setVerifyCoName("第" + excel_rowno
										+ "行    【部门】为空！");
								flag = false;
							}else{
								errorInfo.setVerifyCoName("第" + excel_rowno
										+ "行    【部门】：不存在" + coName
										+ "这个部门！");
								flag = false;
							}
						}
					//}

					if (roleCache.get(ipUser_excel.getDuty()) == null) {
						
						if("".equals(ipUser_excel.getDuty())){
							errorInfo.setVerifyDuty("第" + excel_rowno
									+ "行    【职务】 为空！");
							flag = false;
						}else{
							errorInfo.setVerifyDuty("第" + excel_rowno
									+ "行    【职务】：不存在" + ipUser_excel.getDuty()
									+ "这个职务！");
							flag = false;
						}
					}
					// end_校验结束

					if (!flag) {// 有错误信息
						CopyPropertiesUtil.setProperty(errorInfo);
						error_List.add(errorInfo);
						break;
					} else {
						if(insert_flag){
							userlist_b.add(ipUser_excel);// 添加员工信息
							ipUserCompanylist_b.add(ipUserCompanylist
									.get(pre_insert_no));//
							ipUserRolelist_b.add(ipUserRolelist.get(pre_insert_no));

							List list = new ArrayList();
							list.add(userlist_b);
							list.add(ipUserCompanylist_b);
							list.add(ipUserRolelist_b);

							String tokens = CookieUtil.findCookieValue(
									request.getCookies(), Constants.PARAM_TOKEN);
							sessionCacheManager.cacheUser(tokens + "import",
									ObjectSerialize.serialize(list));// 将list序列化后放进缓存
						}
						
					}
					excel_rowno++;
					pre_insert_no++;
				}

				if (error_List.size() != 0) {
					resultMap.put("error_list", error_List);
					resultMap.put("valid", String.valueOf(userlist_b.size()));
					
					
					if(deserialize.size()!=0){
						List<IpUser> byte_userList = (List<IpUser>) deserialize.get(0);
						resultMap.put(
								"invalid",
								String.valueOf(readExcelContent.size()
										- userlist_b.size()-byte_userList.size()));
					}else{
						resultMap.put(
								"invalid",
								String.valueOf(readExcelContent.size()
										- userlist_b.size()));
					}
					
					
					resultMap.put("result", "data_error");
					return resultMap;
				} else {

					return importToDB(userlist_b, ipUserCompanylist_b,
							ipUserRolelist_b, request, resultMap,true);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 导入可用的员工数据
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "bathImportRestUserInfo")
	private @ResponseBody Map<String, Object> bathImportRestUserInfo(
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		String tokens = CookieUtil.findCookieValue(request.getCookies(),
				Constants.PARAM_TOKEN);

		byte[] in = sessionCacheManager.getCurUser(tokens + "import");
		List deserialize = ObjectSerialize.deserialize(in);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", "success");
		List<IpUser> byte_userList = (List<IpUser>) deserialize.get(0);
		List<IpUserCompany> byte_userCOList = (List<IpUserCompany>) deserialize
				.get(1);
		List<IpUserRole> byte_userRoleList = (List<IpUserRole>) deserialize
				.get(2);
		Map<String, Object> importToDB = importToDB(byte_userList,
				byte_userCOList, byte_userRoleList, request, resultMap,false);

		return importToDB;
	}

	/**
	 * 开始向表中导入数据
	 * 
	 * @param userlist
	 * @param ipUserCompanylist
	 * @param ipUserRolelist
	 * @param findUserAll
	 * @param request
	 */
	private Map<String, Object> importToDB(List<IpUser> userlist,
			List<IpUserCompany> ipUserCompanylist,
			List<IpUserRole> ipUserRolelist, HttpServletRequest request,
			Map<String, Object> resultMap,boolean deleteCache) {
		/*
		 * //可以全部添加 if(findUserAll.size()==0){
		 */
		for (int i = 0; i < userlist.size(); i++) {
			try {
				String hirerId = CookieUtil.findCookieValue(
						request.getCookies(), "hirerId");
				IpHirer findHirerByHirerId = hirerAccountService
						.findHirerByHirerId(hirerId);
				String hirerNo = findHirerByHirerId.getHirerNo();
				/*userlist.get(i).setLoginName(
						userlist.get(i).getLoginName() + "@" + hirerNo);*/
				//begin_IP去掉@hirerNo,直接保存前缀
					userlist.get(i).setLoginName(
							userlist.get(i).getLoginName());
			    //end_IP去掉@hirerNo,直接保存前缀
					
				IpUser findUserByLoginName = userAccountService
						.findUserByLoginName(userlist.get(i).getLoginName());
				if (findUserByLoginName == null) {
					userlist.get(i).setUserId(UUIDTools.uuidRandom());
				} else {
					userlist.get(i).setUserId(findUserByLoginName.getUserId());
				}
				CopyPropertiesUtil.setProperty(userlist.get(i));

				userlist.get(i).setHirerId(hirerId);
				userlist.get(i).setPassword("12345678");// 默认密码
				this.entryptPassword(userlist.get(i));// 加密
				if ("男".equals(userlist.get(i).getUserSex())) {
					userlist.get(i).setUserSex("1");
				} else {
					userlist.get(i).setUserSex("0");
				}
				CopyPropertiesUtil.setProperty(userlist.get(i));

				// userAccountService.saveIpUser(userlist.get(i));//添加员工表
				IpUserCompany ipUserCompany = ipUserCompanylist.get(i);
				IpUserRole ipUserRole = ipUserRolelist.get(i);
				String coName = ipUserCompany.getCoName();
				//String[] split = coName.split("/");
				//for (String coNameInfo : split) {
					// List<IpUserCompany> findUserCompanyByCoNameLike =
					// ipUserCompanyService.findUserCompanyByCoNameLike(coNameInfo);
					List<IpCompany> findCompanyByDeptDetailLike = ipCompanyService
							.findCompanyByDeptDetailLike(coName,hirerId);
					/*
					 * if(findCompanyByDeptDetailLike.size()==0){
					 * System.out.println
					 * ("该部门不存在****************************************");
					 * return;//该部门不存在 }
					 */

					IpCompany ipCompany = findCompanyByDeptDetailLike.get(0);// 唯一的部门
					ipUserCompany.setCoCode(ipCompany.getCoCode());
					ipUserCompany.setCoId(ipCompany.getCoId());
					ipUserCompany.setCoName(ipCompany.getCoName());
					ipUserCompany.setTheId(UUIDTools.uuidRandom());
					ipUserCompany.setUserId(userlist.get(i).getUserId());
					// IpUserCompany findIpUserCompanyByUserIdAndCoId =
					// ipUserCompanyService.findIpUserCompanyByUserIdAndCoId(ipUserCompany.getUserId(),ipUserCompany.getCoId());
					// ipUserCompanyService.saveIpUserCompanyEntity(ipUserCompany);

					// 开始添加角色关联表
					String roleName = userlist.get(i).getDuty();
					IpRole findIpRoleByName = iPRoleService
							.findIpRoleByName(roleName,hirerId);
					ipUserRole.setCoId(ipCompany.getCoId());
					ipUserRole.setIsPartTime("0");// 待处理
					ipUserRole.setRoleId(findIpRoleByName.getRoleId());
					ipUserRole.setTheId(UUIDTools.uuidRandom());
					ipUserRole.setUserId(userlist.get(i).getUserId());
					// ipUserRoleService.saveUserRoleEntity(ipUserRole);
					try {
						// 写在一个事务里面
						ipCommonService.saveExcelInfo(userlist.get(i),
								ipUserCompany, ipUserRole);
					} catch (Exception ex) {
						// 记录日志
						logger.error("员工导入发生错误!", ex);
						resultMap.put("result", "fail");
						resultMap.put("reason", "服务端繁忙，请稍后重试！");
						String tokens = CookieUtil.findCookieValue(
								request.getCookies(), Constants.PARAM_TOKEN);
						sessionCacheManager.disCacheUser(tokens + "import");
					}
				//}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		// 清理缓存
		String tokens = CookieUtil.findCookieValue(request.getCookies(),
				Constants.PARAM_TOKEN);
		if(deleteCache){
			sessionCacheManager.disCacheUser(tokens + "import");
		}
		
		resultMap.put("realNum", userlist.size());
		return resultMap;
		/*
		 * } return;
		 */
	}
	
	
	/**
	 * 模板下载
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "templateDownLoad")
	private void templateDownLoad(
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model){
	try {		
		//获得请求文件名  
        String filename = request.getParameter("filename");
        filename = URLDecoder.decode(filename, "UTF-8");
        //filename=new String(filename.getBytes("utf-8"), "ISO-8859-1");
        System.out.println(filename);  
          
        response.setContentType(request.getServletContext().getMimeType(filename));  
        response.setHeader("Content-Disposition", "attachment;filename="
				+ new String(filename.getBytes("utf-8"), "ISO-8859-1"));  
        String url2 = UploadFileURLUtils.getUrl("template",
				"templateFilePath");
        String path = request.getServletContext().getRealPath("/");
        
        String fullFileName =  path+"\\"+url2+"\\"+filename;
        fullFileName=fullFileName.replace("\\", "/");
        InputStream in;
        OutputStream out;
		
			in = new FileInputStream(fullFileName);
		    out = response.getOutputStream();
	        int b;  
	        while((b=in.read())!= -1)  
	        {  
	            out.write(b);  
	        }  
	          
	        in.close();  
	        out.close();  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	

	/**
	 * 加载datatable数据 前端定义datatable1，并做一些初始化信息
	 */
	public void loadData() {
		try {
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams = this.createSearchParamsStartingWith("search_");

			List<IpCompany> categoryPage = ipCompanyService.getCompanyPage(
					searchParams);
			dataTable1.remove(dataTable1.getAllRow());
			dataTable1.set(categoryPage.toArray(new IpCompany[0]));
		} catch (Exception e) {
			logger.error("查询数据失败!", e);
			throw new WebRuntimeException("查询数据失败!");
		}
	}
	
   /**
    * 按邮箱、手机、登录名查询员工
    */
	public void searchUser() {
		try {
			int pageNumber = 0;
			if (dataTableUser.getPageIndex() != null) {
				pageNumber = dataTableUser.getPageIndex();
			}
			int pageSize = dataTableUser.getPageSize();

			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams = this.buildSearchUserParamsStartingWith("searchs_");

			Page<IpUser> categoryPage = userAccountService.findUserByUserEmailOrLoginNameOrCellphoneNoLike(
					searchParams, pageNumber,pageSize);
			dataTableUser.remove(dataTableUser.getAllRow());
			dataTableUser.set(categoryPage.getContent().toArray(new IpUser[0]));

			
			 int totalPages = categoryPage.getTotalPages();
			 dataTableUser.setTotalPages(totalPages);
			 dataTableUser.setTotalRow(categoryPage.getTotalElements());
			 dataTableUser.setPageIndex(pageNumber);
		
			 

		} catch (Exception e) {
			logger.error("查询数据失败!", e);
			throw new WebRuntimeException("查询数据失败!");
		}
	}
	
	
	
	
	

	/**
	 * 初始化员工信息
	 */
	public void getUsers() {
		try {
			int pageNumber = 0;
			if (dataTableUser.getPageIndex() != null) {
				pageNumber = dataTableUser.getPageIndex();
			}
			int pageSize = dataTableUser.getPageSize();

			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams = this.buildSearchParamsStartingWith("search_");

			// Sort sort = buildSortRequest(Direction.ASC, "employee_no");
			Page<IpUser> categoryPage = userAccountService.getUserAll(
					searchParams, pageNumber,pageSize);
			dataTableUser.remove(dataTableUser.getAllRow());
			dataTableUser.set(categoryPage.getContent().toArray(new IpUser[0]));

			
			 int totalPages = categoryPage.getTotalPages();
			 dataTableUser.setTotalPages(totalPages);
			 dataTableUser.setTotalRow(categoryPage.getTotalElements());
			  dataTableUser.setPageIndex(pageNumber);
		
			 

		} catch (Exception e) {
			logger.error("查询数据失败!", e);
			throw new WebRuntimeException("查询数据失败!");
		}
	}

	/**
	 * 按照部门初始化员工
	 */
	public void initEmployeeByDept() {

		try {
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams = this.createSearchParamsStartingWith("search_");
			Sort sort = buildSortRequest(Direction.ASC, "levelNum");
			List<IpUser> categoryPage = userAccountService.getUserPage(
					searchParams, sort);
			employeeByDeptDataTable.remove(employeeByDeptDataTable.getAllRow());
			employeeByDeptDataTable.set(categoryPage.toArray(new IpUser[0]));
		} catch (Exception e) {
			logger.error("查询数据失败!", e);
			throw new WebRuntimeException("查询数据失败!");
		}
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize,
			String sortType) {
		Sort sort = null;
		/*
		 * if ("auto".equals(sortType)) { sort = new Sort(Direction.DESC,
		 * "theid"); } else if ("title".equals(sortType)) { sort = new
		 * Sort(Direction.ASC, "title"); }
		 */
		return new PageRequest(pageNumber, pagzSize, sort);
	}

	/**
	 * 检索条件拼接
	 * 
	 * @param prefix
	 * @return
	 */
	private Map<String, Object> createSearchParamsStartingWith(String prefix) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> m = dataTable1.getParams();
		Set<Entry<String, Object>> set = m.entrySet();
		for (Entry<String, Object> entry : set) {
			String key = entry.getKey();
			if (key.startsWith(prefix)) {
				String unprefixed = key.substring(prefix.length());
				params.put(unprefixed, entry.getValue().toString());
			}
		}
		return params;
	}

	private Map<String, Object> buildSearchParamsStartingWith(String prefix) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> m = dataTableUser.getParams();
		Set<Entry<String, Object>> set = m.entrySet();
		for (Entry<String, Object> entry : set) {
			String key = entry.getKey();
			if (key.startsWith(prefix)) {
				String unprefixed = key.substring(prefix.length());
				params.put(unprefixed, entry.getValue().toString());
			}
		}
		return params;
	}
	
	private Map<String, Object> buildSearchUserParamsStartingWith(String prefix) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> m = dataTableUser.getParams();
		Set<Entry<String, Object>> set = m.entrySet();
		for (Entry<String, Object> entry : set) {
			String key = entry.getKey();
			if (key.startsWith(prefix)) {
				String unprefixed = key.substring(prefix.length());
				params.put(unprefixed, entry.getValue().toString());
			}
		}
		return params;
	}

	/**
	 * 
	 */
	private Sort buildSortRequest(Direction sortType, String... arguments) {
		Sort sort = null;
		sort = new Sort(sortType, arguments);
		return sort;
	}

	// 加密
	private void entryptPassword(IpUser user) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		user.setSalt(Encodes.encodeHex(salt));
		byte[] hashPassword = Digests.sha1(user.getPassword().getBytes(), salt,
				HASH_INTERATIONS);
		user.setPassword(Encodes.encodeHex(hashPassword));
	}
	
	
	/**
	 * 将所有租户放进缓存，用于员工和租户的信息比对，如：登录名、手机、邮箱
	 */
	public Map<String,String> putHirerIntoCache(){
		
		Map<String,String> cacheReg=new HashMap<String, String>();
		List<IpHirer> findUserAll = hirerAccountService.findAll();
		for (IpHirer IpHirer : findUserAll) 
		{
		    String loginName = IpHirer.getLoginName();
			String userEmail = IpHirer.getEmail();
			String cellphoneNo = IpHirer.getCellphoneNo();
			//sessionCacheManager_h.cacheUser(loginName, loginName);
			cacheReg.put(loginName, loginName);
			
			if(userEmail!=null){
				cacheReg.put(userEmail, userEmail);
			}
			
			if(cellphoneNo!=null){
				cacheReg.put(cellphoneNo, cellphoneNo);
			}
		}
		return cacheReg;
	}
	
	public Map<String,String> checkHirerAndUser(String info,Map<String,String> reg,Map<String,String> putHirerIntoCache){
		if(info.equals(putHirerIntoCache.get(info))){//和租户重名
			reg.put("result", "H_two");
			return reg;
		}
		return reg;
	}
	
	public Map<String,String> checkUserInfo(IpUser findUserByLoginName,IpUser findUserByUserId, Map<String,String> checkHirerAndUser){
		if(checkHirerAndUser.get("result").equals("H_two")){
			return checkHirerAndUser;
		}
      
		//员工校验
		if(findUserByUserId!=null){
		    if(findUserByLoginName!=null){
		    	if(findUserByUserId.getUserId().equals(findUserByLoginName.getUserId())){
		    		checkHirerAndUser.put("result", "current");
					  return checkHirerAndUser;
		    	}else{
		    		checkHirerAndUser.put("result", "U_two");
					return checkHirerAndUser;
		    	}
		    }
	 }else{
		 if(findUserByLoginName!=null){
			 checkHirerAndUser.put("result", "U_two");
				return checkHirerAndUser;
		 }
	 }
		return checkHirerAndUser;
	}
	
	
	/**
	 * 邮箱及手机号格式校验
	 * @param flag
	 * @param info
	 * @return
	 */
	public boolean patternCheck(String flag,String info){
		if(!"".equals(info)){
			if(flag.equals("mail")){
				 String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
				 Pattern p = Pattern.compile(check);
				 Matcher m = p.matcher(info);
				 return m.matches();
			}else{
				String check = "^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
				 Pattern p = Pattern.compile(check);
				 Matcher m = p.matcher(info);
				 return m.matches();
			}
		}else{
			return true;
		}
		
		
		
		
	}
	
	
}
