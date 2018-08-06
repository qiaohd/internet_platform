package com.ufgov.ip.web.system;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import uap.web.auth.Constants;
import uap.web.httpsession.cache.SessionCacheManager;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.api.system.HirerAccountServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.utils.CopyPropertiesUtil;

@Controller
@RequestMapping("reset")
public class ResetUserInfoController {
	public static final int HASH_INTERATIONS = 1024;
	// 默认一天
	public static final int COOKIES_MAXAGE = 60 * 60 * 24;
	
	public static final int OVERDUR = 60 * 60 * 24 * 1000;
	
	private static final int SALT_SIZE = 8;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SessionCacheManager sessionCacheManager;
	
	
	 @Autowired
	protected UserAccountServiceI userAccountService;
	
	 @Autowired
	protected HirerAccountServiceI hirerAccountService;
	
	
	public UserAccountServiceI getUserAccountService() {
		return userAccountService;
	}

	public HirerAccountServiceI getHirerAccountService() {
		return hirerAccountService;
	}


	/**
	 * 重置用户密码
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "resetUserPassword")
	public String resetUserPassword(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String ylUserID = request.getParameter("ylUserID");//人员id
		String ylHirerID = request.getParameter("ylHirerID");//财政id
		if(ylUserID!=null){//人员
			Cookie cookie1=new Cookie("hirerOrUser", "user");
			Cookie cookie2=new Cookie("currSubject", ylUserID);
			cookie1.setPath(Constants.COOKIES_PATH);
			cookie2.setPath(Constants.COOKIES_PATH);
            //浏览器关闭时候删除cookie
			cookie1.setMaxAge(-1);
			cookie2.setMaxAge(-1);
			cookie1.setHttpOnly(true);
			cookie2.setHttpOnly(true);
            response.addCookie(cookie1);
            response.addCookie(cookie2);
            return "resetPassword";
			
		}else{//财政
			Cookie cookie1=new Cookie("hirerOrUser", "hirer");
			Cookie cookie2=new Cookie("currSubject", ylHirerID);
			cookie1.setPath(Constants.COOKIES_PATH);
			cookie2.setPath(Constants.COOKIES_PATH);
            //浏览器关闭时候删除cookie
			cookie1.setMaxAge(-1);
			cookie2.setMaxAge(-1);
			cookie1.setHttpOnly(true);
			cookie2.setHttpOnly(true);
            response.addCookie(cookie1);
            response.addCookie(cookie2);
            return "resetPassword";
		}
	}
	
	
	
	/**
	 * 修改密码
	 * @param newPwd
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value="/restPwd")
	public @ResponseBody Map<String,String> restPwd(@RequestParam String newPwd,HttpServletRequest request, HttpServletResponse response,Model model){
		
		String ylUserID = request.getParameter("ylUserID");//人员id
		String ylHirerID = request.getParameter("ylHirerID");//财政id
		
		Map<String, String> reg = new HashMap<String, String>();
		reg.put("result", "success");
		if("".equals(ylUserID) && "".equals(ylHirerID)){
			reg.put("result", "noUserId");//cookie里面没有userId,前台需要重定向找回密码界面
			reg.put("reason", "请求超时");
			return reg;
		}
		
		if(ylUserID!=""){//登录的是人员
		
			IpUser ipUser=new IpUser();
			ipUser.setPassword(newPwd);
			this.entryptPassword(ipUser);//加密
			IpUser findUserByUserId = userAccountService.findUserByUserId(ylUserID);
			//CopyPropertiesUtil.setProperty(findUserByUserId);//将null置为空字符串，为了后面的更新操作，jpa底层不允许将null更新给原来的字段
			try{
				//更新密码
				userAccountService.updatePwdByUserId(findUserByUserId.getUserId(),ipUser.getPassword(),ipUser.getSalt());//根据id更新
				return reg;
			}catch(Exception ex){
				reg.put("result", "fail");
				logger.error("服务器忙，密码重置失败!", ex);
				return reg;
			}
		
		}else{//登录的是财政系统管理员
			
			IpHirer ipHirer=new IpHirer();
			ipHirer.setPassword(newPwd);
			this.entryptPassword(ipHirer);//加密
			IpHirer findHirerByUserId = hirerAccountService.findHirerByHirerId(ylHirerID);
			findHirerByUserId.setPassword(ipHirer.getPassword());
			findHirerByUserId.setSalt(ipHirer.getSalt());
			//CopyPropertiesUtil.setProperty(findHirerByUserId);//将null置为空字符串，为了后面的更新操作，jpa底层不允许将null更新给原来的字段
			
			//修改用户表的密码
			IpUser ipUser=new IpUser();
			ipUser.setPassword(newPwd);
			this.entryptPassword(ipUser);//加密
			IpUser findUserByUserId=userAccountService.findUserByCellphoneNo(findHirerByUserId.getCellphoneNo());
			//CopyPropertiesUtil.setProperty(findUserByUserId);
			
			try{
				//更新租户表的密码
				hirerAccountService.saveHirerInfo(findHirerByUserId);//根据id更新
				
			}catch(Exception ex){
				reg.put("result", "fail");
				logger.error("服务器忙，密码重置失败!", ex);
			}
			
			try{
				//更新用户表的密码
				userAccountService.updatePwdByUserId(findUserByUserId.getUserId(),ipUser.getPassword(),ipUser.getSalt());
			}catch(Exception ex){
				reg.put("result", "fail");
				logger.error("服务器忙，密码重置失败!", ex);
			}
          return reg;
		}
	}
	
	private void entryptPassword(IpHirer hirer) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		hirer.setSalt(Encodes.encodeHex(salt));
		byte[] hashPassword = Digests.sha1(hirer.getPassword().getBytes(), salt, HASH_INTERATIONS);
		hirer.setPassword(Encodes.encodeHex(hashPassword));
	}
	
	
	private void entryptPassword(IpUser ipUser) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		ipUser.setSalt(Encodes.encodeHex(salt));
		byte[] hashPassword = Digests.sha1(ipUser.getPassword().getBytes(), salt, HASH_INTERATIONS);
		ipUser.setPassword(Encodes.encodeHex(hashPassword));
	}
	
}
