package com.ufgov.ip.web.system;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ufgov.ip.api.system.HirerAccountServiceI;
import com.ufgov.ip.entity.system.IpHirer;


@Controller
@RequestMapping(value ="sys_manager")
public class SysController {
	 @Autowired
	protected HirerAccountServiceI hirerAccountService;
	
	public HirerAccountServiceI getHirerAccountService() {
		return hirerAccountService;
	}

	public void setHirerAccountService(HirerAccountServiceI hirerAccountService) {
		this.hirerAccountService = hirerAccountService;
	}

	@RequestMapping(method = RequestMethod.GET,value="repeat_pwd")
	public void repeatPwd(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		
          String cusername = request.getParameter("cusername");
          model.addAttribute("cusername", cusername);
		
          response.sendRedirect("repeat_pwd_page?cusername=cusername");
          
		//return "#/ip/sys_manager/sys";
	}
	
	

	@RequestMapping(method = RequestMethod.GET,value="sysInfo")
	public String sysInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {
		
		String username = request.getParameter("username");
		IpHirer findHirerByEmailOrLoginNameOrCellphoneNo = hirerAccountService.findHirerByEmailOrLoginNameOrCellphoneNo(username);
		
		String hirerId = findHirerByEmailOrLoginNameOrCellphoneNo.getHirerId();
		model.addAttribute("username", username);
		model.addAttribute("hirerId", hirerId);
		
		return "tenantinfo";
	}
	
	
	
	
	
}
