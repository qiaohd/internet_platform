package com.ufgov.ip.web.sysmanager;

import java.io.Serializable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uap.web.httpsession.cache.SessionCacheManager;

import com.yonyou.iuap.auth.shiro.AuthConstants;
import com.yonyou.iuap.context.InvocationInfoProxy;
import com.yonyou.iuap.utils.CookieUtil;

/**
 * 租户信息
 * @author zhangbch
 *
 */
@Controller
@RequestMapping(value="/tenant")
public class TenAntController {
	
	@Autowired
    private SessionCacheManager sessionCacheManager;

	@RequestMapping(method = RequestMethod.GET,value="tenantInfo")
	public String tenantInfo(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
		// cookie中获取hirerId
		// String hirerId = sessionCacheManager.getCurUser("hirerId");
		String hirerId = CookieUtil.findCookieValue(request.getCookies(), AuthConstants.PARAM_TENANTID);
		String hirerId1= InvocationInfoProxy.getTenantid();
		model.addAttribute("hirerId", hirerId);
		String ishowFlag = request.getParameter("ishowFlag");
		String uploadPicInfo = request.getParameter("uploadPicInfo");
		model.addAttribute("ishowFlag", ishowFlag);
		model.addAttribute("uploadPicInfo", uploadPicInfo);
		return "sys_manager/tenantinfo";
	}
	
	
}
