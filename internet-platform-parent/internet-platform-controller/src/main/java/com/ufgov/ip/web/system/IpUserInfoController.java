package com.ufgov.ip.web.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import org.apache.commons.beanutils.BeanUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.ufgov.ip.api.system.IpUserInfoServiceI;
import com.ufgov.ip.entity.system.IpUserInfo;

/**
 * aaaaController
 * @author winner
 * @version 2016-10-08
 */
@Controller
@RequestMapping(value = "gen_ppp/ipUserInfo")
public class IpUserInfoController{

	@Autowired
	private IpUserInfoServiceI ipUserInfoService;
	
	
	@RequestMapping(method = RequestMethod.POST,value = "list")
	public @ResponseBody List<IpUserInfo> list(IpUserInfo ipUserInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<IpUserInfo> page = ipUserInfoService.findList(ipUserInfo); 
		return page;
	}
	
	@RequestMapping(method = RequestMethod.GET,value = "allList")
	public @ResponseBody List<IpUserInfo> allList(HttpServletRequest request, HttpServletResponse response, Model model) {
		List<IpUserInfo> page = ipUserInfoService.findAllList(); 
		return page;
	}

     
	@RequestMapping(method = RequestMethod.POST,value = "get")
	public @ResponseBody Map<String,Object> form(@RequestBody IpUserInfo ipUserInfo,HttpServletRequest request, HttpServletResponse response,Model model) {
	    Map<String,Object> reg=new HashMap<String,Object>();
	    try {
			//BeanUtils.populate(ipUserInfo, request.getParameterMap());
			reg.put("result",ipUserInfoService.get(ipUserInfo.getUserId()));
			return reg;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reg;
	}


	@RequestMapping(method = RequestMethod.POST,value = "save")
	public @ResponseBody Map<String,Object> save(IpUserInfo ipUserInfo, Model model,HttpServletRequest request, HttpServletResponse response) {
	   Map<String,Object> reg=new HashMap<String,Object>();
	   try{
			ipUserInfoService.save(ipUserInfo);
			reg.put("result", "true");
			reg.put("reason", "保存aaaa成功");
			return reg;
	   
	   }catch(Exception e){
			e.printStackTrace();
			reg.put("result", "false");
			reg.put("reason", "保存aaaa成功");
			return reg;
		}
	    
	}
	
	@RequestMapping(method = RequestMethod.POST,value = "update")
	public @ResponseBody Map<String,Object> update(IpUserInfo ipUserInfo, Model model,HttpServletRequest request, HttpServletResponse response) {
	   Map<String,Object> reg=new HashMap<String,Object>();
	   try{
			ipUserInfoService.update(ipUserInfo);
			reg.put("result", "true");
			reg.put("reason", "更新aaaa成功");
			return reg;
	   }catch(Exception e){
			e.printStackTrace();
			reg.put("result", "false");
			reg.put("reason", "更新aaaa成功");
			return reg;
		}
	    
	}
	
	
	
	@RequestMapping(method = RequestMethod.POST,value = "delete")
	public @ResponseBody Map<String,Object> delete(@RequestBody IpUserInfo ipUserInfo, HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> reg=new HashMap<String,Object>();
			try{
					   ipUserInfoService.delete(ipUserInfo.getUserId());
			     reg.put("result", "true");
			     reg.put("reason", "删除生成方案失败");
			     return reg;
			}catch(Exception e){
			    e.printStackTrace();
				reg.put("result", "false");
				reg.put("reason", "删除生成方案失败");
				return reg;
			}
	}

}