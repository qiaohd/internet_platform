package com.ufgov.ip.web.sysmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uap.web.httpsession.cache.SessionCacheManager;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ufgov.ip.api.base.IPDataTableServiceI;
import com.ufgov.ip.api.sysmanager.IPRoleServiceI;
import com.ufgov.ip.entity.sysmanager.IpRole;
import com.ufgov.ip.entity.sysmanager.IpUserAndCompanyEntity;
import com.ufgov.ip.entity.sysmanager.IpUserRole;
import com.ufgov.ip.entity.system.IpMenu;
import com.ufgov.ip.entity.system.IpUser;
import com.yonyou.iuap.iweb.entity.DataTable;
import com.yonyou.iuap.iweb.entity.Row;
import com.yonyou.iuap.iweb.exception.WebRuntimeException;
import com.yonyou.iuap.iweb.icontext.IWebViewContext;
import com.yonyou.iuap.utils.CookieUtil;



/**
 * 
 * @author qiaohd
 *
 */
@Component("role.RoleController")
@Scope("prototype") 
@RequestMapping(value = "permission")
public class RoleController {
	 @Autowired 
	IPDataTableServiceI iPDataTableService;
	 @Autowired
	IPRoleServiceI ipRoleService;
	@Autowired
    private SessionCacheManager sessionCacheManager;
	DataTable<IpMenu> menuDataTable;
	DataTable<IpUserAndCompanyEntity> companyDataTable;
	DataTable<IpUser> dataTableStaff;
	@RequestMapping(method = RequestMethod.GET,value="dutyPermission")
	public String dutyPermission(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
		// COOKIE中获取hirerId
		String hirerId = CookieUtil.findCookieValue(request.getCookies(), "hirerId");	
		model.addAttribute("hirerId", hirerId);
		return "sys_manager/dutyPermission";		
	}
	/**
	 * 初始化当前租户下的职务
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value="getRoleInfobyHirerId")
	public @ResponseBody List<IpRole> initRole(HttpServletRequest request, HttpServletResponse response){
		String hirerId = CookieUtil.findCookieValue(request.getCookies(), "hirerId");	
		List<IpRole> roleInfo = ipRoleService.getRoleInfoByHirerId(hirerId);
		return roleInfo;		
	}
		
	
	
	/**
	 * 新增部门初始化
	 * 			 查询参数需要前端配置：search_条件类型_参数字段  如search_EQ_hirerID
	 * 			 菜单查询按照菜单级别，菜单序号 顺序排列
	 */
	public void addPermission(){
		try {
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams = iPDataTableService.createSearchParamsStartingWith("search_", menuDataTable);
			Sort sort = iPDataTableService.buildSortRequest(Direction.ASC,"levelNum","dispOrder");
			List<IpMenu> categoryPage = ipRoleService.getInitMenu(searchParams, sort);
			menuDataTable.remove(menuDataTable.getAllRow());
			menuDataTable.set(categoryPage.toArray(new IpMenu[0]));			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebRuntimeException("查询数据失败!",e);
		}
	}
	
	/**
	 * 初始化权限 
	 * 		参数：search_EQ_roleId
	 */
	public void initPermissionMenu(){
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams = iPDataTableService.createSearchParamsStartingWith("search_", menuDataTable);
		List<IpMenu> categoryPage = ipRoleService.getInitPermissionMenu(searchParams);
		menuDataTable.remove(menuDataTable.getAllRow());
		menuDataTable.set(categoryPage.toArray(new IpMenu[0]));
	}
	
	/**
	 * 初始化权限的人员信息
	 * 			参数roleId：search_EQ_roleId
	 */
	public void initPermissionUser(){
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams = iPDataTableService.createSearchParamsStartingWith("search_", dataTableStaff);
		int pageNumber = 0;
		// Map<String, Object> parameters = IWebViewContext.getEventParameter();
		if (dataTableStaff.getPageIndex()!= null) {
			pageNumber = dataTableStaff.getPageIndex();
		}
		int pageSize = dataTableStaff.getPageSize();
		
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, "auto");
		Page<IpUserRole> categoryPage = ipRoleService.getUserPage(searchParams, pageRequest);
		dataTableStaff.remove(dataTableStaff.getAllRow());
		dataTableStaff.set(categoryPage.getContent().toArray(new IpUserRole[0]));
		
		int totalPages = categoryPage.getTotalPages();
		dataTableStaff.setTotalPages(totalPages);
		dataTableStaff.setTotalRow(categoryPage.getTotalElements());
		dataTableStaff.setPageIndex(pageNumber);
		// dataTableStaff.setSelect(null);
	}
	
	/**
	 * 编辑部门 初始化 
	 * 		需要参数：search_BQ_roleId 
	 */
	public void editPermission(){
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams = iPDataTableService.createSearchParamsStartingWith("search_", menuDataTable);
		Sort sort = iPDataTableService.buildSortRequest(Direction.ASC,"levelNum","dispOrder");
		List<IpMenu> ipMenuAll = ipRoleService.getInitMenu(searchParams,sort);		
		List<String> roleMenuList = ipRoleService.getEditPermissionMenu(searchParams);
		menuDataTable.remove(menuDataTable.getAllRow());
		menuDataTable.set(ipMenuAll.toArray(new IpMenu[0]));
		Integer[] selectIndexs = null ;
		List<Integer> isSelectList = new ArrayList<Integer>();
		int k=0;
		for (IpMenu ipMenu : ipMenuAll) {
			String menuId = ipMenu.getMenuId();
			if(roleMenuList.contains(menuId)){
				isSelectList.add(k);
			}
			k++;
		}
		if(isSelectList.size()>0){
			selectIndexs = (Integer[])isSelectList.toArray(new Integer[isSelectList.size()]);
			menuDataTable.setSelect(selectIndexs);
		}
		
	}
	
	/**
	 * 保存权限 
	 *  权限数据以 role_ 开头 如 role_roleName 
	 */
	public void savePermissionDataTable(){
		//JSONObject json = new JSONObject();
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("flag", "success");
		json.put("msg", "保存成功!");		
		try{
			Map<String, Object> roleInfoParams = new HashMap<String, Object>();
			roleInfoParams = iPDataTableService.createSearchParamsStartingWith("role_", menuDataTable);
			IpRole ipRoleByreq = getIpRoleEntity(roleInfoParams);
			Row [] rows =menuDataTable.getSelectRow();
			
			List<IpMenu> ipMenuList =new ArrayList<IpMenu>();
			IpMenu ipMenu;
			for (Row row : rows) {
				ipMenu=new IpMenu();
				ipMenu=menuDataTable.toBean(row);
				ipMenuList.add(ipMenu);
			}
			ipRoleService.saveRoleAndRoleMenu(ipRoleByreq,ipMenuList,json);
			JSONObject json1=JSONObject.fromObject(json);
			IWebViewContext.getResponse().write(json1.toString());
		}catch (Exception e) {
			e.printStackTrace();
			json.put("flag", "fail");
			json.put("msg", "服务端繁忙，请稍后重试!");
			IWebViewContext.getResponse().write(json.toString());
		}
	}
	 
	
	/**
	 * 初始化人员树 
	 * 	hirerId前端定义：search_EQ_hirerId
	 * 	roleId 前端定义：role_EQ_roleId
	 */
	public void initCompanyDataTable(){
		Map<String, Object> searchParams = new HashMap<String, Object>();
		Map<String, Object> roleParams = new HashMap<String, Object>();
		// 通过 参数 search_EQ_hirerId 查询 部门信息
		searchParams = iPDataTableService.createSearchParamsStartingWith("search_", companyDataTable);
		Sort sort = iPDataTableService.buildSortRequest(Direction.ASC,"levelNum","dispOrder","coCode");
		// 通过 参数 role_EQ_roleId 查询 当前权限已选择的人员 
		roleParams = iPDataTableService.createSearchParamsStartingWith("role_", companyDataTable);
		List<String> UserList = ipRoleService.getUserList(roleParams);
		// 初始化部门人员树 
		List<IpUserAndCompanyEntity> categoryPage = ipRoleService.initCompanyDataTable(searchParams, sort);
		
		companyDataTable.remove(companyDataTable.getAllRow());
		companyDataTable.set(categoryPage.toArray(new IpUserAndCompanyEntity[0]));
		// Row [] allRows =companyDataTable.getAllRow();		
		Integer[] selectIndexs = null ;
		List<Integer> isSelectList = new ArrayList<Integer>();
		// IpUserAndCompanyEntity ipUserAndCompanyEntity;
		int k=0;
		// 人员
		for (IpUserAndCompanyEntity ipUserAndCompanyEntity : categoryPage) {
			String userId = ipUserAndCompanyEntity.getStaffId();
			if(UserList.contains(userId)){
				isSelectList.add(k);
			}
			k++;
		}
		if(isSelectList.size()>0){
			selectIndexs = (Integer[])isSelectList.toArray(new Integer[isSelectList.size()]);		
			companyDataTable.setSelect(selectIndexs);
		}
		
	}
	
	
	/**
	 * 保存权限-人员信息
	 * 			参数：roleId  role_EQ_roleId 
	 */
	public void savePermissionUser(){
		JSONObject json = new JSONObject();
		json.put("flag", "success");
		json.put("msg", "保存成功!");		
		try{
			Map<String, Object> roleInfoParams = new HashMap<String, Object>();
			// 取roleId
			roleInfoParams = iPDataTableService.createSearchParamsStartingWith("role_EQ_", companyDataTable);
			IpRole ipRoleByreq = getIpRoleEntity(roleInfoParams);
			// 选择的行数 
			Row [] rows =companyDataTable.getSelectRow();
			List<IpUserAndCompanyEntity> ipUserCompanyEntityList =new ArrayList<IpUserAndCompanyEntity>();
			List userIdList =new ArrayList();
			IpUserAndCompanyEntity ipUserCompanyEntity;
			for (Row row : rows) {
				ipUserCompanyEntity=new IpUserAndCompanyEntity();
				ipUserCompanyEntity=companyDataTable.toBean(row);
				if(("1").equals(ipUserCompanyEntity.getIsUser())){
					ipUserCompanyEntityList.add(ipUserCompanyEntity);
					userIdList.add(ipUserCompanyEntity.getStaffId());
				}
			}
			//ipRoleService.saveRoleAndUser(ipRoleByreq,ipUserCompanyEntityList,userIdList);
			ipRoleService.saveRoleUser(ipRoleByreq,ipUserCompanyEntityList,userIdList);
			IWebViewContext.getResponse().write(json.toString());
		}catch (Exception e) {
			e.printStackTrace();
			json.put("flag", "fail");
			json.put("msg", "服务端繁忙，请稍后重试!");
			IWebViewContext.getResponse().write(json.toString());
		}
	}
	
	
	
	public void  getpermissionByRoleId() {
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams = iPDataTableService.createSearchParamsStartingWith("search_", menuDataTable);
		Sort sort = iPDataTableService.buildSortRequest(Direction.ASC,"levelNum","menuDesc");
		List<IpMenu> categoryPage = ipRoleService.getInitMenu(searchParams, sort);
		menuDataTable.remove(menuDataTable.getAllRow());
		menuDataTable.set(categoryPage.toArray(new IpMenu[0]));		
		int totalPages = categoryPage.size();
		menuDataTable.setTotalPages(totalPages);
		
	}
	
	/**
	 * 通过客户端参数组装IProle
	 * @param roleInfoParams
	 * @return
	 */
	private IpRole getIpRoleEntity(Map<String, Object> roleInfoParams) {
		IpRole ipRole=new IpRole();	
		String roleName = (String) roleInfoParams.get("roleName");
		String roleDesc = (String) roleInfoParams.get("roleDesc");
		String hirerId = (String) roleInfoParams.get("hirerId");
		String roleId = (String) roleInfoParams.get("roleId");
		ipRole.setRoleName(roleName);
		ipRole.setHirerId(hirerId);
		ipRole.setRoleDesc(roleDesc);
		ipRole.setRoleId(roleId);
		return ipRole;
	}
	
	@RequestMapping(method=RequestMethod.POST,value="delPermission")
	public @ResponseBody Map<String, String> delPermission(HttpServletRequest request,HttpServletResponse response) {
		Map<String,String> json=new HashMap<String,String>();
		try {
			String roleId = request.getParameter("roleId");
		    json=ipRoleService.delPermission(roleId);
		}catch(Exception e){
			json.put("flag", "fail");
			json.put("msg", "服务器繁忙,请稍后再试！");
			e.printStackTrace();
		}
		return json;
		
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
//		if ("auto".equals(sortType)) {
//			sort = new Sort(Direction.DESC, "theid");
//		} else if ("title".equals(sortType)) {
//			sort = new Sort(Direction.ASC, "theid");
//		}
		return new PageRequest(pageNumber, pagzSize, sort);
	}
	
	
}
