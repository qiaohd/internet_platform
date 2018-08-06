package com.ufgov.ip.web.sysmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ufgov.ip.api.base.IPDataTableServiceI;
import com.ufgov.ip.api.sysmanager.IpCompanyServiceI;
import com.ufgov.ip.api.sysmanager.IpOrgSortServiceI;
import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.sysmanager.IpRole;
import com.ufgov.ip.entity.sysmanager.IpUserRole;
import com.yonyou.iuap.iweb.entity.DataTable;
import com.yonyou.iuap.utils.CookieUtil;


@Component("org.OrgSort")
@Scope("prototype")
@RequestMapping(value="/organizationSort")
public class OrgSortController {
	
	 @Autowired
	IpCompanyServiceI ipCompanyService;
	 @Autowired
	IpOrgSortServiceI ipOrgSortService;
	 @Autowired
	IPDataTableServiceI iPDataTableService;
	
	DataTable<IpRole> roleDataTable;

	@RequestMapping(method = RequestMethod.GET,value="/sortPage")
	public String organizationSort(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
		// COOKIE中获取hirerId
		String hirerId = CookieUtil.findCookieValue(request.getCookies(), "hirerId");	
		model.addAttribute("hirerId", hirerId);
		return "sys_manager/organizationSort";
	}
	/**
	 * 通过节点ID查询子节点集合
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "childOrg")
	public @ResponseBody List<IpCompany> childOrg(HttpServletRequest request,HttpServletResponse response) {
		String coId = request.getParameter("coId");
		if("".equals(coId) || coId==null){
			return null;
		}
		List<IpCompany> findCompanyByParentCoId = new ArrayList<IpCompany>();
		try {
			findCompanyByParentCoId = ipCompanyService.findCompanyByParentCoId(coId);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return null;
		}
		return findCompanyByParentCoId;
	}
	/**
	 * 保存排序
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "orgSort")
	public @ResponseBody Map<String, String> orgSort(HttpServletRequest request,HttpServletResponse response){
		String coIds=request.getParameter("coIds");
		String coPId=request.getParameter("coPId");
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "success");
		try {
			String[] coIdArray = coIds.split(",");		
			ipOrgSortService.updateOrgSort(coIdArray,coPId);
			resultMap.put("reason", "更新成功！");
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			resultMap.put("result", "fail");
			resultMap.put("reason", "服务端繁忙，请稍后重试！");
		}
		return resultMap;
		
	}
	
	/**
	 * 加载datatable数据 前端定义roleDataTable，并做一些初始化信息
	 */
	public void loadRoleDataTable() {
		try {
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams = iPDataTableService.createSearchParamsStartingWith("search_",roleDataTable);
			Sort sort = iPDataTableService.buildSortRequest(Direction.ASC, "dispOrder");
			List<IpRole> categoryPage = ipOrgSortService.getRolePage(searchParams, sort);
			roleDataTable.remove(roleDataTable.getAllRow());
			roleDataTable.set(categoryPage.toArray(new IpRole[0]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getIpRoles")
	public @ResponseBody List<IpRole> getIpRoles(HttpServletRequest request,HttpServletResponse response){
		String hirerId=request.getParameter("hirerId");
		if("".equals(hirerId) || hirerId==null){
			return null;
		}
		List<IpRole> findIpRoleByHirerId = new ArrayList<IpRole>();
		try {
			findIpRoleByHirerId = ipOrgSortService.findIpRoleByHirerId(hirerId);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return null;
		}
		return findIpRoleByHirerId;
	}
	
	/**
	 * 保存排序
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "roleSort")
	public @ResponseBody Map<String, String> roleSort(HttpServletRequest request,HttpServletResponse response){
		String roleIds=request.getParameter("roleIds");
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "success");
		try {
			String[] roleIdArray = roleIds.split(",");		
			ipOrgSortService.updateRoleSort(roleIdArray);
			resultMap.put("reason", "更新成功！");
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			resultMap.put("result", "fail");
			resultMap.put("reason", "服务端繁忙，请稍后重试！");
		}
		return resultMap;
		
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "getRoleUser")
	public @ResponseBody List<IpUserRole> getRoleUser(HttpServletRequest request,HttpServletResponse response){
		String roleId=request.getParameter("roleId");
		if("".equals(roleId) || roleId==null){
			return null;
		}
		List<IpUserRole> findUserRoleByRoleId = new ArrayList<IpUserRole>();
		try {
			findUserRoleByRoleId = ipOrgSortService.getRoleUserByRoleId(roleId);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return null;
		}
		return findUserRoleByRoleId;
	}
	
	
	/**
	 * 保存排序
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "roleUserSort")
	public @ResponseBody Map<String, String> roleUserSort(HttpServletRequest request,HttpServletResponse response){
		
		String userIds=request.getParameter("userIds");
		String roleId=request.getParameter("roleId");
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "success");
		try {
			String[] userIdArray = userIds.split(",");		
			ipOrgSortService.updateRoleUserSort(userIdArray,roleId);
			resultMap.put("reason", "更新成功！");
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			resultMap.put("result", "fail");
			resultMap.put("reason", "服务端繁忙，请稍后重试！");
		}
		return resultMap;
		
	}
	
}
