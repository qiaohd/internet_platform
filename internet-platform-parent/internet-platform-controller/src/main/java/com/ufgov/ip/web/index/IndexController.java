package com.ufgov.ip.web.index;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ufgov.ip.api.system.HirerAccountServiceI;
import com.ufgov.ip.api.system.MenuServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.utils.PropertyUtilSys;
import com.yonyou.iuap.context.InvocationInfoProxy;

import uap.web.auth.Constants;
import uap.web.httpsession.cache.SessionCacheManager;

@Controller
@RequestMapping(value = "/")
public class IndexController {

	public static final int HASH_INTERATIONS = 1024;
	@Autowired
	protected UserAccountServiceI userAccountService;
	
	@Autowired
	protected HirerAccountServiceI hirerAccountService;
	
	@Autowired
	private MenuServiceI menuService;
	
	@Autowired
    private SessionCacheManager sessionCacheManager;

	public void setMenuService(MenuServiceI menuService) {
		this.menuService = menuService;
	}
	
	public void setUserAccountService(UserAccountServiceI userAccountService) {
		this.userAccountService = userAccountService;
	}

	public void setHirerAccountService(HirerAccountServiceI hirerAccountService) {
		this.hirerAccountService = hirerAccountService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		String cuser = null;
        String operUser = PropertyUtilSys.getOperPropertyByKey("username");
		if (SecurityUtils.getSubject().getPrincipal() != null)
			cuser = (String) SecurityUtils.getSubject().getPrincipal();
		    if(cuser!=null){
		    	model.addAttribute("cusername", cuser);
		    	if(cuser.equals(operUser)){
					return "menuIndex";
		    	}else{
				    	return "index";
		    	}
		    }
			
		    return null;
	}
	
	
	
		@RequestMapping(method = RequestMethod.POST,value="pages/ip/sys_manager/repeat_pwd_page")
		public void repeatPwdPage(HttpServletRequest request, HttpServletResponse response,ModelMap model) {		
			try {
			String value="";
			String pwd = request.getParameter("pwd");
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
	        	String cookieName = cookie.getName();
	        	if("username".equals(cookieName)){
	        		value = cookie.getValue();
	        		value=URLDecoder.decode(value,"utf-8");
	        		model.addAttribute("username", value);
	        		break;
	        	}
	        	
			}
			
			value=InvocationInfoProxy.getUserid();
			IpHirer ipHirer = hirerAccountService.findHirerByEmailOrLoginNameOrCellphoneNo(value);//租户
			String password = ipHirer.getPassword();
			String enStr=Encodes.encodeHex(Digests.sha1(pwd.getBytes(), Encodes.decodeHex(ipHirer.getSalt()), HASH_INTERATIONS));
			
			sessionCacheManager.cacheUser("hirerId", ipHirer.getHirerId());
			Cookie cookie=new Cookie("hirerId",ipHirer.getHirerId());
	        cookie.setPath(Constants.COOKIES_PATH);
            //浏览器关闭时候删除cookie
            cookie.setMaxAge(-1);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
			
			if(ipHirer==null){
				response.getWriter().print("false");
			}else if(!enStr.endsWith(password)){
			    response.getWriter().print("wrong_pwd");
			}else{
				response.getWriter().print(ipHirer.getHirerId());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
}
