package com.ufgov.ip.web.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ufgov.ip.api.system.DatapartitionServiceI;
import com.ufgov.ip.entity.system.IpDataPartition;
import com.ufgov.ip.entity.system.IpHirer;

@Component("dataPartition")
@Scope("prototype")
@RequestMapping(value = "partition")
public class DatapartitionController {
	
	@Autowired
	DatapartitionServiceI datapartitionService;
	
	
	/**
	 * 测试连接 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value="testConnection")
	public @ResponseBody Map<String, String> testConnection (HttpServletRequest request, HttpServletResponse response){

		Map<String, String> reqMap = getParameterByReq(request);
		
		Map<String, String> resultMap  = new HashMap<String, String>();
		
		try {
			resultMap = datapartitionService.testConn(reqMap);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			resultMap.put("result", "fail");
    		resultMap.put("reason", "未请求到测试服务！");  
		}
				
		return resultMap;
		
	}
	
	/**
	 * 提取参数组成map
	 * @param request
	 * @return
	 */
	private Map<String, String> getParameterByReq(HttpServletRequest request) {
		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put("host", request.getParameter("host").toString().trim());
		reqMap.put("dbDriver", request.getParameter("dbDriver").toString().trim());
		reqMap.put("port", request.getParameter("port").toString().trim());
		reqMap.put("userName", request.getParameter("userName").toString().trim());
		reqMap.put("password", request.getParameter("password").toString().trim());
		reqMap.put("areaName", request.getParameter("areaName").toString().trim());
		return reqMap;
	}
	
	/**
	 * 保存分区
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value="savePartition")
	public @ResponseBody Map<String, String> savePartition (HttpServletRequest request, HttpServletResponse response){
		Map<String, String> reqMap = getParameterByReq(request);
		Map<String, String> resultMap  = new HashMap<String, String>(); 
		try {
			resultMap  = datapartitionService.savePartition(reqMap);
		}catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			resultMap.put("result", "fail");
    		resultMap.put("reason", "未请求到测试服务！");  
		}
		return resultMap;
	}
	
	@RequestMapping(value="/page", method= RequestMethod.GET)
	public @ResponseBody Object page(@RequestParam(value = "pageIndex", defaultValue = "0") int pageNumber, 
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model, ServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		Map<String, Object> searchParams = new HashMap<String, Object>();
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		try {
			List<IpDataPartition> mainPage = datapartitionService.getMainPage();
			result.put("data", mainPage);
			result.put("flag", "success");
			result.put("msg", "查询数据成功!");
		} catch (Exception e) {
			String errMsg = "查询数据详情失败!";
			result.put("flag", "fail");
			result.put("msg", errMsg);
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 创建分页请求简单示例，业务上按照自己的需求修改.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.ASC, "area_name","host");
		} else{
			sort = new Sort(Direction.ASC, "host","area_name");
		}
		return new PageRequest(pageNumber, pagzSize, sort);
	}
	
	/**
	 * 查询所有分区 
	 * @return
	 */
	@RequestMapping(value="/getDataPartition", method= RequestMethod.GET)
	public @ResponseBody Map<String, Object> getDataPartition() {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			List<IpDataPartition> mainPage = datapartitionService.getMainPage();
			result.put("data", mainPage);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return result;
		
	}
	
	@RequestMapping(value="/getHirerInfo", method= RequestMethod.GET)
	public @ResponseBody Object getHirerInfo(@RequestParam(value = "pageIndex", defaultValue = "3") int pageNumber, 
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model, ServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
			List<IpHirer> mainPage = datapartitionService.getHirerInfo(pageRequest);
			result.put("data", mainPage);
			result.put("flag", "success");
			result.put("msg", "查询数据成功!");
		} catch (Exception e) {
			String errMsg = "查询数据详情失败!";
			result.put("flag", "fail");
			result.put("msg", errMsg);
			e.printStackTrace();
		}
		return result;
	}
	@RequestMapping(value="/auditHirerInfo", method= RequestMethod.POST)
	public @ResponseBody Map<String, String> auditHirerInfo(HttpServletRequest request, HttpServletResponse response){
		String hirerId = request.getParameter("hirerId");
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			resultMap = datapartitionService.auditHirerInfo(hirerId);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return resultMap;
		}
//	
//	/*
//	 * mybatis 分页插件
//	 */
//	@RequestMapping(value="/getHirerInfoPage",method= RequestMethod.GET)
//	public @ResponseBody PageInfo<IpHirer> queryPage(HttpServletRequest request, HttpServletResponse response){
//		String pageNoStr=request.getParameter("pageNo");
//	    int pageNo=Integer.valueOf(pageNoStr);
//	    String pageSizeStr=request.getParameter("pageSize");
//	    int pageSize=Integer.valueOf(pageSizeStr);
//		return datapartitionService.getHirerInfo(pageNo, pageSize);
//	}
	@RequestMapping(value="/getHirerInfoPage", method= RequestMethod.GET)
	public @ResponseBody Object getHirerInfoPage(@RequestParam(value = "pageIndex", defaultValue = "0") int pageNumber, 
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "isVaild") String isVaild, Model model, ServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
//			PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
//			List<IpHirer> mainPage = datapartitionService.getHirerInfo(pageRequest);
			List<IpHirer> mainPage = datapartitionService.getHirerInfo(pageNumber, pageSize,isVaild);
			int rowCounts = datapartitionService.getHirerInfoCounts(isVaild);
			int totalPages=rowCounts/pageSize==0?rowCounts/pageSize:rowCounts/pageSize+1;
			result.put("data", mainPage);
			result.put("totalRow",rowCounts);
			result.put("totalPages",totalPages);
			result.put("flag", "success");
			result.put("msg", "查询数据成功!");
		} catch (Exception e) {
			String errMsg = "查询数据详情失败!";
			result.put("flag", "fail");
			result.put("msg", errMsg);
			e.printStackTrace();
		}
		return result;
	}
	
}