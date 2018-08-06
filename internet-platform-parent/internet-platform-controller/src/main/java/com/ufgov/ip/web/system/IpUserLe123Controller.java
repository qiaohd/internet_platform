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
import com.ufgov.ip.entity.system.IpUserLe123;
import com.ufgov.ip.api.system.IpUserLe123ServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.api.system.HirerAccountServiceI;
import com.ufgov.ip.entity.system.IpUser;

import java.util.HashMap;
import java.util.Map;
import java.util.List;


/**
 * aaaaController
 * @author winner
 * @version 2016-10-26
 */
@Controller
@RequestMapping(value = "le_code/ipUserLe123")
public class IpUserLe123Controller{

	@Autowired
	private IpUserLe123ServiceI ipUserLe123Service;
	
	@Autowired
	protected UserAccountServiceI userAccountService;
    
    @Autowired
	protected HirerAccountServiceI hirerAccountService;
	
	
	@RequestMapping(method = RequestMethod.POST,value = "list")
	public @ResponseBody List<IpUserLe123> list(IpUserLe123 ipUserLe123, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<IpUserLe123> page = ipUserLe123Service.findList(ipUserLe123); 
		return page;
	}


	@RequestMapping(method = RequestMethod.POST,value = "save")
	public @ResponseBody Map<String,Object> save(IpUserLe123 ipUserLe123, Model model,HttpServletRequest request, HttpServletResponse response) {
	   Map<String,Object> reg=new HashMap<String,Object>();
	   try{
	   
		   	  ipUserLe123Service.save(ipUserLe123);
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
	public @ResponseBody Map<String,Object> update(IpUserLe123 ipUserLe123, Model model,HttpServletRequest request, HttpServletResponse response) {
	   Map<String,Object> reg=new HashMap<String,Object>();
	   try{
			ipUserLe123Service.update(ipUserLe123);
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
	public @ResponseBody Map<String,Object> delete(@RequestBody IpUserLe123 ipUserLe123, HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> reg=new HashMap<String,Object>();
			try{
		         ipUserLe123Service.delete(ipUserLe123);
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