package com.ufgov.ip.web.system;

import com.ufgov.ip.utils.IMAPIHelperUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import uap.iweb.exception.WebRuntimeException;
import uap.iweb.icontext.IWebViewContext;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.entity.system.IpUser;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/imaccess")
public class IMAccessController {

	//日志
	private final Logger logger = LoggerFactory.getLogger(getClass());

	//工具类 （获取token和发送http请求）IMInitGroupControllerIMInitGroupController
	private IMAPIHelperUtil imHelperUtil=IMAPIHelperUtil.getSingletonIMAPIHelperUtil();

	 @Autowired
	protected UserAccountServiceI userAccountService;
	
	 
	private HashMap<String, String> getParam() {

			HashMap<String, String> map = new HashMap<String, String>();
			try {
				Resource resource = new ClassPathResource("application.properties");
				Properties props = PropertiesLoaderUtils.loadProperties(resource);
			
				String eptId = (String) props.get("im.eptid");
				String appId = (String) props.get("im.appid");
				String clientID = (String) props.get("im.clientid");
				String clientSecret = (String) props.get("im.clientsecret");
				String imClient = "http://"+(String) props.get("im.client")+"/sysadmin/rest/";
				String servlet= "http://"+(String) props.get("im.client")+"/";
				String safeServlet= "https://"+(String) props.get("im.client")+"/";
				String wsport = (String) props.get("im.wsport");
				String hbport = (String) props.get("im.hbport");
				String address = (String) props.get("im.address");
			
				map.put("eptId", eptId);
				map.put("appId", appId);
				map.put("clientID", clientID);
				map.put("clientSecret", clientSecret);
				map.put("imClient", imClient);
				map.put("servlet", servlet);
				map.put("safeServlet", safeServlet);
				map.put("wsport",wsport);
				map.put("hbport",hbport);
				map.put("address",address);
				
		

				// System.out.println(user +"====="+ password);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			return map;
		}
		Map<String, String> paramMap = getParam();
		// 企业ID
		private final String eptId = paramMap.get("eptId");
		// 应用ID
		private final String appId = paramMap.get("appId");
		// Client ID
		private final String clientID = paramMap.get("clientID");
		// Client Secret
		private final String clientSecret = paramMap.get("clientSecret");
		// im Client
		private final String imClient = paramMap.get("imClient");

		/**
		 * 获取用户的Token
		 * 
		 * @param request
		 * @param response
		 * @param model
		 * @return
		 * @throws IOException
		 */
		@RequestMapping(method = RequestMethod.POST,value="getImParam")
		public  @ResponseBody Map<String,Object> getVouList(HttpServletRequest request){
			try {
				
				Map<String,Object> imParamMap = new HashMap<String, Object>();
				imParamMap.put("eptId",paramMap.get("eptId"));	
				imParamMap.put("appId",paramMap.get("appId"));	
				imParamMap.put("servlet",paramMap.get("servlet"));
				imParamMap.put("safeServlet",paramMap.get("safeServlet"));
				imParamMap.put("wsport",paramMap.get("wsport"));
				imParamMap.put("hbport",paramMap.get("hbport"));
				imParamMap.put("address",paramMap.get("address"));

				return imParamMap;
			} catch (Exception e) {
				logger.error(e.getMessage());
				IWebViewContext.getResponse().write("false");
				throw new WebRuntimeException("获取配置失败!");
			}		
		}
	 
	/**
	 * 获取用户的Token
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/getUserToken")
	public @ResponseBody Map<String, Object> showMenu(
			HttpServletRequest request, HttpServletResponse response,
			Model model) throws IOException {
		String username = request.getParameter("username");
		IpUser ipUser=userAccountService.findUserByUserEmailOrLoginNameOrCellphoneNo(username); //普通用户
		
		//为了保证唯一性 现改为使用手机号作为账号的唯一标识
		username=ipUser.getLoginName();
		
		System.out.println(username);
		String url = imClient+ eptId + "/" + appId
				+ "/token";
		String[] params = new String[] { "clientId=" + clientID,
				"clientSecret=" + clientSecret, "username=" + username };
		Map<String, Object> res = new HashMap<String, Object>();
		JSONObject jsStr = null;
		res.put("result", "success");
		try {
			String resStr = imHelperUtil.postJson(url, "POST",null,params);
			jsStr = JSONObject.fromObject(resStr);
			
			JSONObject appToken=imHelperUtil.getAPPToken(eptId, appId, clientID, clientSecret);
			String token=appToken.getString("token");
			updateUserInfo(username,token,ipUser);
			
		} catch (Exception e) {
			logger.error("获取token失败", e);
			res.put("result", "fail");
		}
		res.put("data", jsStr);
		return res;
	}
	
	/**
	 * 更新用户资料
	 * @param username
	 * @param appToken
	 * @param user
	 */
	public void updateUserInfo(String username,String appToken,IpUser user)
	{
		String url=imClient+"remotevcard/"+eptId+"/"+appId+"/updateVcard?token="+appToken;
		String[] params = new String[] { "username=" + user.getLoginName(),
				"email=" + user.getUserEmail(),"mobile="+user.getCellphoneNo(),"nickname="+user.getUserName()};
		imHelperUtil.postJson(url,"PUT",null,params);
	}
	

	/**
	 * 获取用户信息
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getUserInfo")
	public @ResponseBody Map<String, Object> getUserInfo(
			HttpServletRequest request, HttpServletResponse response,
			Model model) throws IOException {
		String username = request.getParameter("username");
		IpUser ipUser=null;
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("result", "success");
		try {
	         ipUser=userAccountService.findUserByUserEmailOrLoginNameOrCellphoneNo(username); //普通用户
		} catch (Exception e) {
			logger.error("获取头像失败", e);
			res.put("result", "fail");
		}
		res.put("data",ipUser);
		return res;
	}
	
}
