package com.ufgov.ip.web.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ufgov.ip.api.base.DictionaryServiceI;
import com.ufgov.ip.api.base.IPDataTableServiceI;
import com.ufgov.ip.commons.web.ControllerPermissionException;
import com.ufgov.ip.entity.base.IpDictionary;
import com.ufgov.ip.entity.base.IpDictionaryDetail;
import com.yonyou.iuap.iweb.entity.DataTable;
import com.yonyou.iuap.iweb.exception.WebRuntimeException;
import com.yonyou.iuap.iweb.icontext.IWebViewContext;


@Component("base.dicController")
@Scope("prototype")
@RequestMapping(value = "dic")
public class DictionaryController extends ControllerPermissionException{
	DataTable<IpDictionaryDetail> dicDataTable;
	@Autowired
	IPDataTableServiceI iPDataTableService;
	@Autowired
	DictionaryServiceI distionaryService;
	public void loadDictionary() {
		try {
			
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams = iPDataTableService.createSearchParamsStartingWith("search_",dicDataTable);
			Sort sort = iPDataTableService.buildSortRequest(Direction.ASC, "theCode");

			List<IpDictionaryDetail> categoryPage = distionaryService.getDicPage(searchParams, sort);
			dicDataTable.remove(dicDataTable.getAllRow());
			dicDataTable.set(categoryPage.toArray(new IpDictionaryDetail[0]));
			IWebViewContext.getResponse().write("true");
		} catch (Exception e) {
			IWebViewContext.getResponse().write("false");
			throw new WebRuntimeException("查询数据失败!");
		}
	}
	@RequestMapping(method = RequestMethod.POST,value="saveDic")
	public  @ResponseBody Map<String, String> saveDic(@RequestBody IpDictionaryDetail ipDictionaryDetail,HttpServletRequest request, HttpServletResponse response){
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "success");
		resultMap.put("reason", "保存成功！");
		try {
			distionaryService.saveEntity(ipDictionaryDetail,resultMap);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			resultMap.put("result", "fail");
			resultMap.put("reason", "服务端繁忙，请稍后重试！");
		}
		return resultMap;
	}
	@RequestMapping(method = RequestMethod.GET,value="getDicInfo")
	public @ResponseBody List<IpDictionary> getDicInfo(HttpServletRequest request, HttpServletResponse response){
		List<IpDictionary> ipDictionaries =new ArrayList<IpDictionary>();
		try {
			ipDictionaries = distionaryService.getDicInfo();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return ipDictionaries;
		
	}
	
	@RequestMapping(method = RequestMethod.GET,value="checkDicType")
	public @ResponseBody Map<String, String> checkDicType(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "success");
		resultMap.put("reason", "检查通过！");
		try {
			String dicId = request.getParameter("dicId");
			String dicType = request.getParameter("dicType");
			distionaryService.checkDicType(dicId,dicType,resultMap);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			resultMap.put("result", "fail");
			resultMap.put("reason", "服务端繁忙，请稍后重试！");
		}
		return resultMap;
		
	}
	
	@RequestMapping(method = RequestMethod.GET,value="checkDetailInfo")
	public @ResponseBody Map<String, String> checkDetailInfo(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "success");
		resultMap.put("reason", "检查通过！");
		try {
			String theId = request.getParameter("theId");
			String dicId = request.getParameter("dicId");
			String detailInfo = request.getParameter("detailInfo");
			distionaryService.checkDetailInfo(theId,dicId,detailInfo,resultMap);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			resultMap.put("result", "fail");
			resultMap.put("reason", "服务端繁忙，请稍后重试！");
		}
		return resultMap;
		
	}
	
	@RequestMapping(method = RequestMethod.GET,value="checkDetailKey")
	public @ResponseBody Map<String, String> checkDetailKey(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "success");
		resultMap.put("reason", "检查通过！");
		try {
			String theId = request.getParameter("theId");
			String dicId = request.getParameter("dicId");
			String detailKey = request.getParameter("detailKey");
			distionaryService.checkDetailKey(theId,dicId,detailKey,resultMap);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			resultMap.put("result", "fail");
			resultMap.put("reason", "服务端繁忙，请稍后重试！");
		}
		return resultMap;
		
	}
	@RequestMapping(method = RequestMethod.POST,value="delDicInfo")
	public @ResponseBody Map<String, String> delDicInfo(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "success");
		resultMap.put("reason", "删除成功！");
		try {
			String theId = request.getParameter("theId");
			String dicId = request.getParameter("dicId");
			distionaryService.delDicInfo(theId,dicId,resultMap);
		} catch (Exception e) {
			// TODO 自动生成的 catch块
			resultMap.put("result", "fail");
			resultMap.put("reason", "服务端繁忙，请稍后重试！");
		}
		return resultMap;
		
	}
	
}
