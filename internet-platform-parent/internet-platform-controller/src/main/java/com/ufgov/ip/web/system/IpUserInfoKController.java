package com.ufgov.ip.web.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.apache.shiro.SecurityUtils;

import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.entity.system.IpUserInfoK;
import com.ufgov.ip.api.system.IpUserInfoKServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.api.system.HirerAccountServiceI;
import com.ufgov.ip.entity.system.IpUser;

import java.util.HashMap;
import java.util.Map;
import java.util.List;


/**
 * aaaaaController
 * @author winner
 * @version 2016-10-26
 */
@Controller
@RequestMapping(value = "finaltest/ipUserInfoK")
public class IpUserInfoKController{

	@Autowired
	private IpUserInfoKServiceI ipUserInfoKService;
	
	@Autowired
	protected UserAccountServiceI userAccountService;
    
    @Autowired
	protected HirerAccountServiceI hirerAccountService;
	
	
	@RequestMapping(method = RequestMethod.POST,value = "list")
	public @ResponseBody List<IpUserInfoK> list(IpUserInfoK ipUserInfoK, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<IpUserInfoK> page = ipUserInfoKService.findList(ipUserInfoK); 
		return page;
	}


	@RequestMapping(method = RequestMethod.POST,value = "save")
	public @ResponseBody Map<String,Object> save(IpUserInfoK ipUserInfoK, Model model,HttpServletRequest request, HttpServletResponse response) {
	   Map<String,Object> reg=new HashMap<String,Object>();
	   try{
	   
		   	  ipUserInfoKService.save(ipUserInfoK);
			reg.put("result", "true");
			reg.put("reason", "保存成功");
			return reg;
	   
	   }catch(Exception e){
			e.printStackTrace();
			reg.put("result", "false");
			reg.put("reason", "保存失败");
			return reg;
		}
	    
	}
	
	@RequestMapping(method = RequestMethod.POST,value = "update")
	public @ResponseBody Map<String,Object> update(IpUserInfoK ipUserInfoK, Model model,HttpServletRequest request, HttpServletResponse response) {
	   Map<String,Object> reg=new HashMap<String,Object>();
	   try{
			ipUserInfoKService.update(ipUserInfoK);
			reg.put("result", "true");
			reg.put("reason", "更新成功");
			return reg;
	   }catch(Exception e){
			e.printStackTrace();
			reg.put("result", "false");
			reg.put("reason", "更新失败");
			return reg;
		}
	    
	}
	
	@RequestMapping(method = RequestMethod.POST,value = "delete")
	public @ResponseBody Map<String,Object> delete(@RequestBody IpUserInfoK ipUserInfoK, HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> reg=new HashMap<String,Object>();
			try{
		         ipUserInfoKService.delete(ipUserInfoK);
			     reg.put("result", "true");
			     reg.put("reason", "删除成功");
			     return reg;
			}catch(Exception e){
			    e.printStackTrace();
				reg.put("result", "false");
				reg.put("reason", "删除失败");
				return reg;
			}
	}

}