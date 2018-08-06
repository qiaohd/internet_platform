package com.ufgov.ip.web.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.bouncycastle.ocsp.Req;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.web.Servlets;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ufgov.ip.api.base.IPDataTableServiceI;
import com.ufgov.ip.api.base.RegionServiceI;
import com.ufgov.ip.commons.web.ControllerPermissionException;
import com.ufgov.ip.entity.base.IpRegion;
import com.yonyou.iuap.iweb.entity.DataTable;
import com.yonyou.iuap.iweb.exception.WebRuntimeException;



@Component("base.regionController")
@Scope("prototype")
@RequestMapping(value = "region")
public class RegionController{
	private final Logger logger = LoggerFactory.getLogger(RegionController.class);
	
	DataTable<IpRegion> regionDataTable;
	DataTable<IpRegion> fatherRegionDataTable;
	@Autowired
	IPDataTableServiceI iPDataTableService;
	@Autowired
	RegionServiceI regionService;

	@RequestMapping(value = "/region" ,method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> loadRegionData(Model mode,ServletRequest request) {
		try {
			Map<String, Object> result = new HashMap<String,Object>();
			Map<String, Object> searchParams = new HashMap<String, Object>();
//			searchParams = iPDataTableService.createSearchParamsStartingWith("search_",regionDataTable);
			searchParams = Servlets.getParametersStartingWith(request, "search_");
			Sort sort = iPDataTableService.buildSortRequest(Direction.ASC, "theCode");

			List<IpRegion> categoryPage = regionService.getRegionPage(searchParams, sort);
			//regionDataTable.remove(regionDataTable.getAllRow());
		 	//regionDataTable.set(categoryPage.toArray(new IpRegion[0]));
			result.put("cata", categoryPage);
			return result;
		} catch (Exception e) {
			logger.error("查询数据失败!", e);
			throw new WebRuntimeException("查询数据失败!");
		}
	}
	//@RequiresPermissions("baseData:region:query")
	@RequestMapping(value = "/regionByHiredId" ,method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> loadRegionByHirer(Model mode,ServletRequest request) {
		try {
			Map<String, Object> result = new HashMap<String,Object>();
			Map<String, Object> searchParams = new HashMap<String, Object>();
//			searchParams = iPDataTableService.createSearchParamsStartingWith("search_",regionDataTable);
			Sort sort = iPDataTableService.buildSortRequest(Direction.ASC, "theCode");
			searchParams = Servlets.getParametersStartingWith(request, "search_");
			List<IpRegion> categoryPage = regionService.getRegionPageByHire(searchParams, sort);
//			regionDataTable.remove(regionDataTable.getAllRow());
//			regionDataTable.set(categoryPage.toArray(new IpRegion[0]));
			result.put("cata", categoryPage);
			return result;
		} catch (Exception e) {
			logger.error("查询数据失败!", e);
			throw new WebRuntimeException("查询数据失败!");
		}
	}
	
	public void loadRegionDataByFather() {
		try {
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams = iPDataTableService.createSearchParamsStartingWith("search_",regionDataTable);
			Sort sort = iPDataTableService.buildSortRequest(Direction.ASC, "theCode");
			
			List<IpRegion> categoryPage = regionService.getRegionPage(searchParams, sort);
			String theId=getTheIdBySearchParams(searchParams);
			List<IpRegion> childRegions=regionService.getRegionByParentId(theId);
			categoryPage.addAll(childRegions);
			regionDataTable.remove(regionDataTable.getAllRow());
			regionDataTable.set(categoryPage.toArray(new IpRegion[0]));
		} catch (Exception e) {
			logger.error("查询数据失败!", e);
			throw new WebRuntimeException("查询数据失败!");
		}
	}
	/**
	 * 取theID 
	 * @param searchParams
	 * @return
	 */
	private String getTheIdBySearchParams(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		SearchFilter searchFilterByRoleId =filters.get("EQ_theId");
		String theId = "";
		if (searchFilterByRoleId!=null) {
			theId = (String) searchFilterByRoleId.value;
		}
		return theId;
	}
	

	
	public void loadFatherRegion() {
		try {
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams = iPDataTableService.createSearchParamsStartingWith("search_",fatherRegionDataTable);
			List<IpRegion> categoryPage = regionService.getRegionProAndCity(searchParams);
			IpRegion ipRegion =new IpRegion();
			ipRegion.setTheId("0");
			ipRegion.setTheCode("0");
			ipRegion.setTheName("无");
			fatherRegionDataTable.remove(fatherRegionDataTable.getAllRow());
			categoryPage.add(0, ipRegion);
			fatherRegionDataTable.set(categoryPage.toArray(new IpRegion[0]));
		} catch (Exception e) {
			logger.error("查询数据失败!", e);
			throw new WebRuntimeException("查询数据失败!");
		}
	}
	//@RequiresPermissions("baseData:region:edit")
	@RequestMapping(method = RequestMethod.POST,value="saveRegion")
	public  @ResponseBody Map<String, String> saveRegion(@RequestBody IpRegion ipRegion,HttpServletRequest request, HttpServletResponse response){
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "success");
		resultMap.put("reason", "保存成功！");
		try {
			resultMap=regionService.saveEntity(ipRegion,resultMap);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			resultMap.put("result", "fail");
			resultMap.put("reason", "服务端繁忙，请稍后重试！");
		}
		return resultMap;
	}
	//@RequiresPermissions("baseData:region:delete")
	@RequestMapping(method = RequestMethod.POST,value="delRegion")
	public  @ResponseBody Map<String, String> delRegion(HttpServletRequest request, HttpServletResponse response){
		String theId = request.getParameter("theId");//获得菜单id
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "success");
		resultMap.put("reason", "删除成功！");
		try {
			resultMap=regionService.delEntity(theId,resultMap);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			resultMap.put("result", "fail");
			resultMap.put("reason", "服务端繁忙，请稍后重试！");
		}
		return resultMap;
	}
	
	
	
	
	@RequestMapping(method = RequestMethod.POST,value="checkRegion")
	public void checkRegion(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "success");
		String theCode = request.getParameter("theCode");
		String theId = request.getParameter("theId");
		regionService.checkRegion(theId,theCode,resultMap);
	}
}
