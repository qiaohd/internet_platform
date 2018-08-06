package com.ufgov.ip.web.system;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import com.ufgov.ip.utils.CopyPropertiesUtil;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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

import com.ufgov.ip.utils.CookieUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.ufgov.ip.api.message.phoneverity.SendMessageServiceI;
import com.ufgov.ip.api.system.HirerAccountServiceI;
import com.ufgov.ip.api.system.MenuServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.utils.MD5;
import com.ufgov.ip.utils.ProduceCodeUtil;
import com.ufgov.ip.utils.PropertyUtil;
import com.ufgov.ip.utils.RSAUtils;
import com.ufgov.ip.utils.TokenGenerator;
import com.yonyou.iuap.auth.shiro.AuthConstants;
import com.yonyou.iuap.auth.token.ITokenProcessor;
import com.yonyou.iuap.auth.token.TokenParameter;
import com.yonyou.uap.entity.response.MessageResponse;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)，
 * 
 * 真正登录的POST请求由Filter完成,
 */
@Controller
@RequestMapping(value = "/login")
public class UserLoginController {
	
    private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final int HASH_INTERATIONS = 1024;
	// 默认一天
	public static final int COOKIES_MAXAGE = 60 * 60 * 24;
	private static final int SALT_SIZE = 8;
	//是否显示验证码
	private final String isAuthcodeShow = PropertyUtil.getPropertiesKey("application.properties","login.isAuthcodeShow");

	
    @Autowired
    private SessionCacheManager sessionCacheManager;

    @Autowired
	protected UserAccountServiceI userAccountService;
	
    @Autowired
	protected HirerAccountServiceI hirerAccountService;
	
	//为网页版本的登录Controller指定webTokenProcessor 相应的移动的指定为maTokenProcessor
	@Autowired
	protected ITokenProcessor webTokenProcessor;
		
	
	@Autowired
	private SendMessageServiceI sendMessageService;
	
	
	
	public SendMessageServiceI getSendMessageService() {
		return sendMessageService;
	}

	public void setSendMessageService(SendMessageServiceI sendMessageService) {
		this.sendMessageService = sendMessageService;
	}

	//菜单
	@Autowired
	private MenuServiceI menuService;

	public void setMenuService(MenuServiceI menuService) {
		this.menuService = menuService;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String userlogin(Model model) {
		initPubKeyParams(model);
		return "login";
	}
	
	public void initPubKeyParams(Model model) {
		RSAPublicKey publicKey = RSAUtils.getDefaultPublicKey();
    	String publicKeyExponent = publicKey.getPublicExponent().toString(16);//16进制
 	    String publicKeyModulus = publicKey.getModulus().toString(16);//16进制
 	    model.addAttribute("exponent", publicKeyExponent);
 	    model.addAttribute("modulus", publicKeyModulus);
	}
	
	@RequestMapping(method = RequestMethod.POST,value="uformLogin")
	public String uformLogin(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        String userName = request.getParameter("username");
        String encryptedPassWord = request.getParameter("password");
        String isRemember = request.getParameter("rememberMe");
        if(encryptedPassWord != null) {
        	encryptedPassWord = encryptedPassWord.replace("_encrypted", "");
        }
        String passWord = RSAUtils.decryptStringByJs(encryptedPassWord);
        
//        //将用户名存放在cookie里面
//        Cookie nameCookie=new Cookie("username",userName);
//        //浏览器关闭时候删除cookie
//        nameCookie.setMaxAge(-1);
//        nameCookie.setHttpOnly(true);
//        response.addCookie(nameCookie);
        System.out.println(isAuthcodeShow);
        if("true".equals(isAuthcodeShow)){
	        String imageCode = request.getParameter("imageCode");
	        Cookie[] cookies = request.getCookies();
	        for (Cookie cookie : cookies) {
	        	String cookieName = cookie.getName();
	        	if("piccode".equals(cookieName)){
	        		String value = cookie.getValue();
	        		if(!value.equalsIgnoreCase(imageCode)){
	        			model.addAttribute("accounterror", "你输入的验证码不正确!");
	        			initPubKeyParams(model);
	        			return "login";
	        		}
	        	}
			}
        }
        /**
         * 获得运维人员属性文件
         */
        Properties pro=new Properties();
        ClassPathResource classPathResource = new ClassPathResource("operation.properties");
        InputStream inputStream = classPathResource.getInputStream();
        pro.load(inputStream);
        
        /**
         * 判断是否为普通用户、租户、运维管理员 
         * 都是相互独立的
         * 
         */
        
        IpUser ipUser=userAccountService.findUserByUserEmailOrLoginNameOrCellphoneNo(userName); //普通用户
        // IpHirer ipHirer = hirerAccountService.findHirerByEmailOrLoginNameOrCellphoneNo(userName);//租户
        
        String op_name = pro.getProperty("username");//运维人员用户名
        String op_password=pro.getProperty("password");//运维人员用户名密码

        String path=new String();
        if(encryptedPassWord!=null){
        	
        	if(ipUser != null && Encodes.encodeHex(Digests.sha1(passWord.getBytes(), Encodes.decodeHex(ipUser.getSalt()), HASH_INTERATIONS)).equals(ipUser.getPassword())){
        		long ts = System.currentTimeMillis();
        		// 判断该用户已停用 
        		if("0".equals(ipUser.getIsEnabled())){
        			model.addAttribute("accounterror", "该用户已停用，请确认!");
        	        initPubKeyParams(model);
        	        return "login";
        		}
        		ipUser.setLoginTs(ts);
        		/*path=checkLogin(userName, response,ts);
        		//把登陆信息写入到redis缓存中
                try {
                    sessionCacheManager.cacheUser(userName, ipUser);
                } catch (Exception e) {
                    logger.error("登陆信息写入到redis缓存中失败!", e);
                }*/
        		 TokenParameter tp = new TokenParameter();
                 tp.setUserid(userName);
                 // 设置登录时间
                 tp.setLogints(String.valueOf(System.currentTimeMillis()));
                 // 租户信息,saas应用登录的时候获取用户信息，设置租户id
                 tp.getExt().put(AuthConstants.PARAM_TENANTID , ipUser.getHirerId());
                 
                 Cookie[] loginCookies = webTokenProcessor.getCookieFromTokenParameter(tp);
                 for(Cookie cookie : loginCookies){
                	 if("on".equals(isRemember)){
                		 cookie.setMaxAge(30*24*3600);
                	 }
             	   response.addCookie(cookie);
                 }
                
                if("0".equals(ipUser.getUserType())){ // 租户管理员 
                	model.addAttribute("usertype", "hirer");            	
                }else{
                	model.addAttribute("usertype", "user");               	
                }
                model.addAttribute("hirerId", ipUser.getHirerId());
        		
        	}
        	else {
                
//        		if(ipHirer!=null && Encodes.encodeHex(Digests.sha1(passWord.getBytes(), Encodes.decodeHex(ipHirer.getSalt()), HASH_INTERATIONS)).equals(ipHirer.getPassword())){
//        			long ts = System.currentTimeMillis();
//        			ipHirer.setLoginTs(ts);
//        			/*path=checkLogin(userName, response,ts);
//        			
//            		//把登陆信息写入到redis缓存中
//                    try {
//                        sessionCacheManager.cacheUser(userName, ipHirer);
//                    } catch (Exception e) {
//                        logger.error("登陆信息写入到redis缓存中失败!", e);
//                    }*/
//        			 TokenParameter tp = new TokenParameter();
//                     tp.setUserid(userName);
//                     // 设置登录时间
//                     tp.setLogints(String.valueOf(System.currentTimeMillis()));
//                     // 租户信息,saas应用登录的时候获取用户信息，设置租户id
//                     tp.getExt().put(AuthConstants.PARAM_TENANTID ,ipHirer.getHirerId());
//                     
//                     Cookie[] loginCookies = webTokenProcessor.getCookieFromTokenParameter(tp);
//                     for(Cookie cookie : loginCookies){
//                 	   response.addCookie(cookie);
//                     }
//                    model.addAttribute("usertype", "hirer");
//                    model.addAttribute("hirerId", ipHirer.getHirerId());
//        		}else{
        			
        			if(op_name!=null && (new MD5().GetMD5Code(passWord)).equals(op_password)){
        				long ts = System.currentTimeMillis();
        				pro.setProperty("ts", String.valueOf(ts));
        				// path=checkLogin(userName, response,ts);
        				inputStream.close();
                		/*//把登陆信息写入到redis缓存中
                        try {
                            sessionCacheManager.cacheUser(userName, pro);
                        } catch (Exception e) {
                            logger.error("登陆信息写入到redis缓存中失败!", e);
                        }*/
        				TokenParameter tp = new TokenParameter();
                        tp.setUserid(userName);
                        // 设置登录时间
                        tp.setLogints(String.valueOf(System.currentTimeMillis()));
                        // 租户信息,saas应用登录的时候获取用户信息，设置租户id
                        tp.getExt().put(AuthConstants.PARAM_TENANTID ,"ADMIN");
                        
                        Cookie[] loginCookies = webTokenProcessor.getCookieFromTokenParameter(tp);
                        for(Cookie cookie : loginCookies){
                        	if("on".equals(isRemember)){
                       		 cookie.setMaxAge(30*24*3600);
                       	 }
                    	   response.addCookie(cookie);
                        }
        				
        			}else{
        				model.addAttribute("accounterror", "用户名与密码不匹配，请重新输入!");
        				initPubKeyParams(model);
                        return "login";
        			}
        		}
            //}
        	//获得所有的一级菜单
        	//List<IpMenu> menuList = menuService.findAllFirstLevel(new String[]{"1"});
        	//model.addAttribute("menuList", menuList);
        	return "redirect";	
        }else {
            model.addAttribute("accounterror", "你输入的用户不存在!");
            initPubKeyParams(model);
            return "login";
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName, Model model) {
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, userName);
		initPubKeyParams(model);
		return "login";
	}
	
	public String checkLogin(String userName, HttpServletResponse response,long ts){
			
               // long ts = System.currentTimeMillis();
                String cookieValue = "";
                try {
                    cookieValue = TokenGenerator.genToken(userName, ts, sessionCacheManager.findSeed());
                } catch (Throwable e) {
                    logger.error("Fail to generate cookie!", e);
                }

                // 校验成功，写cookie
                HashMap<String, String> cookiesMap = new HashMap<String, String>();
                cookiesMap.put(Constants.PARAM_USERNAME, userName);
                cookiesMap.put(Constants.PARAM_TOKEN, cookieValue);
                for (Iterator<String> iterator = cookiesMap.keySet().iterator(); iterator.hasNext(); ) {
                    String key = iterator.next();
                    Cookie cookie = new Cookie(key, URLEncoder.encode(cookiesMap.get(key)));
                    cookie.setPath(Constants.COOKIES_PATH);
                    //浏览器关闭时候删除cookie
                   //  cookie.setMaxAge(-1);
                    cookie.setMaxAge(-1);
                    cookie.setHttpOnly(true);
                    response.addCookie(cookie);
                }
           
            return "redirect";
	    }
	public HirerAccountServiceI getHirerAccountService() {
		return hirerAccountService;
	}

	public void setHirerAccountService(HirerAccountServiceI hirerAccountService) {
		this.hirerAccountService = hirerAccountService;
	}

	//验证码的生成
	@RequestMapping(value="/getImage")
	public void getImage(HttpServletRequest request, HttpServletResponse response) throws Exception{
		 System.out.println("#######################生成数字和字母的验证码######(68,22),(15,3,18)#################");  
	        BufferedImage img = new BufferedImage(109,49,  
	  
	        BufferedImage.TYPE_INT_RGB);  
	  
	        // 得到该图片的绘图对象    
	  
	        Graphics g = img.getGraphics();  
	  
	        Random r = new Random();  
	  
	        Color c = new Color(200, 150, 255);  
	  
	        g.setColor(c);  
	  
	        // 填充整个图片的颜色    
	  
	        g.fillRect(0, 0, 109, 49);  
	  
	        // 向图片中输出数字和字母    
	  
	        StringBuffer sb = new StringBuffer();  
	  
	        char[] ch = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();  
	  
	        int index, len = ch.length;  
	  
	        for (int i = 0; i < 4; i++) {  
	  
	            index = r.nextInt(len);  
	  
	            g.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt  
	  
	            (255)));  
	  
	            g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 22));  
	            // 输出的  字体和大小                      
	  
	            g.drawString("" + ch[index], (i * 22) + 6, 29);  
	            //写什么数字，在图片 的什么位置画    
	  
	            sb.append(ch[index]);  
	  
	        }  
	        //把验证码的值放入session中以便于验证
	        //request.getSession().setAttribute("piccode", sb.toString()); 
	        //sessionCacheManager.cacheUser("piccode", sb.toString());
	        //request.setAttribute("piccode", sb.toString());
	        Cookie cookie=new Cookie("piccode", sb.toString());
	        cookie.setPath(Constants.COOKIES_PATH);
            //浏览器关闭时候删除cookie
            cookie.setMaxAge(-1);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
	        
	        ImageIO.write(img, "JPG", response.getOutputStream());  
	}
	
	
	//用户登出
//	@RequestMapping(method = RequestMethod.GET,value="userLogout")
//	public String logout(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
//		/*Subject currentUser = SecurityUtils.getSubject();//获得Subject
//		Session session = currentUser.getSession();
//		session.removeAttribute("userName");*/
//		SecurityUtils.getSubject().logout();
//		initPubKeyParams(model);
//		return "login";
//	}
	
	
	
	
	
    //校验验证码
	@RequestMapping(method = RequestMethod.POST,value="/checkImage")
	public void checkImage(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String imageCode = request.getParameter("imageCode");
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
        	String cookieName = cookie.getName();
        	if("piccode".equals(cookieName)){//修复验证码区分大小写 equalsIgnoreCase
        		String value = cookie.getValue();
        		if(!value.equalsIgnoreCase(imageCode)){
        			response.getWriter().write("false");
        		}else{
        			response.getWriter().write("true");
        		}
        	}
		}
	}
	
	public Boolean checkImageCode(HttpServletRequest request, Model model) {
		String imageCode = request.getParameter("imageCode");
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
        	String cookieName = cookie.getName();
        	if("piccode".equals(cookieName)){
        		String value = cookie.getValue();
        		if(!value.equalsIgnoreCase(imageCode)){
        			model.addAttribute("accounterror", "你输入的验证码不正确!");
        			initPubKeyParams(model);
        			return false;
        		}
        	}
		}
		return true;
	}

	/**
	 * 获得手机验证码
	 * 接口：login/getPhoneCode
	 * @param request
	 * @param response
	 * @param model
	 * @return result:"true"/"userLoginName"/"userID"/"false"
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getPhoneCode")
	public @ResponseBody Map<String,String> getPhoneCode(@RequestParam String phoneNo,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws IOException {
		
		Map<String, String> reg = new HashMap<String, String>();
		reg.put("result", "success");
		// 获得随机码
		String code = ProduceCodeUtil.getCode();
		
		IpUser untaxUser=userAccountService.findUserByUserEmailOrLoginNameOrCellphoneNo(phoneNo);
		if(untaxUser==null){
			reg.put("result", "noExist");
			reg.put("reason", "不存在该手机号");
			return reg;
		}
		System.out.println("为了节省开销，模拟生成手机验证码***************************");
		System.out.println("*************验证码【"+code+"】******");
		reg.put("PhoneCode", code);
		//临时发送手机验证码
		
//		try {
//			sessionCacheManager.cacheUser("PhoneCode", code);
//			
//			//begin_为了节省开销，模拟生成手机验证码***************************
//				String isSend=PropertyUtil.getPropertyByKey("isSend");
//	            if(("false").equals(isSend)){
//	            	System.out.println("*************验证码【"+code+"】******");
//	            	reg.put("result", "success");
//	            	Cookie createCookie=CookieUtil.createCookie("userLoginName", untaxUser.getLoginName());
//	            	Cookie createCookie2=CookieUtil.createCookie("userID", untaxUser.getUserId());
//	            	response.addCookie(createCookie);
//		            response.addCookie(createCookie2);
//					return reg;
//	            }
//            //end_为了节省开销，模拟生成手机验证码***************************
//	            
//			//向手机发送验证码
//			List<MessageResponse> phoneMessageSend = sendMessageService
//					.phoneMessageSend(phoneNo, code,
//							sessionCacheManager.getSessionTimeout(), "op");
//			
//			if (phoneMessageSend.size() > 0) {
//				for (MessageResponse messageResponse : phoneMessageSend) {
//					String responseStatusCode = messageResponse
//							.getResponseStatusCode();
//					if ("1000".equals(responseStatusCode)) {
//						reg.put("result", "success");
//						Cookie createCookie = CookieUtil.createCookie("userLoginName", untaxUser.getLoginName());
//						Cookie createCookie2 = CookieUtil.createCookie("userID", untaxUser.getUserId());
//			            response.addCookie(createCookie);
//			            response.addCookie(createCookie2);
//						/*reg.put("userLoginName", untaxUser.getLoginName());//嵌入到页面，值可以放进<input>隐藏域里面，通过id取
//						reg.put("userID", untaxUser.getUserId());//嵌入到页面，值可以放进<input>隐藏域里面，通过id取*/
//			            return reg;
//					} else {
//						reg.put("result", "fail");
//					}
//				}
//			}
//		} catch (Exception e) {
//			reg.put("result", "fail");
//			logger.error("手机获得验证码失败!", e);
//		}
		return reg;
	}
	
	 /**
     * 手机验证码校验
     * 接口：login/checkAuthCode
     * @param request
     * @param response
     * @param model
     * 返回参数："true"/"false"
     */
	@RequestMapping(method = RequestMethod.GET,value="/checkAuthCode")
	public void checkAuthCode(@RequestParam String auth_code,HttpServletRequest request, HttpServletResponse response,Model model){
		try {
				String serializable = sessionCacheManager.getCurUser("PhoneCode");
				if(auth_code.equals(serializable)){
					response.getWriter().print("true");	
				}else{
					response.getWriter().print("false");	
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 进入找回密码界面 
	 * 接口：login/findPassword
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/findPassword")
	public String findPassword(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws IOException {
		return "findpassword";
	}
	@RequestMapping(method = RequestMethod.GET, value = "/findPasswordStep2")
	public String findPasswordStep2(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws IOException {
		return "findpasswordstep2";
	}
	@RequestMapping(method = RequestMethod.GET, value = "/findPasswordStep3")
	public String findPasswordStep3(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws IOException {
		return "findpasswordstep3";
	}
	@RequestMapping(method = RequestMethod.GET, value = "/findPasswordSucc")
	public String findPasswordSucc(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws IOException {
		return "findPasswordSucc";
	}
	@RequestMapping(method = RequestMethod.GET, value = "/findPasswordError")
	public String findPasswordError(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws IOException {
		return "vcodeError";
	}
	
	/**
	 * 进入正式重置密码界面
	 * 接口：login/proResetPwd
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value="/proResetPwd")
	public @ResponseBody Map<String,String> proResetPwd(HttpServletRequest request, HttpServletResponse response,Model model){
		
		String loginName = CookieUtil.findCookieValue(request.getCookies(), "userLoginName");//从cookie里面获得userId
		Map<String, String> reg = new HashMap<String, String>();
		reg.put("result", "success");
		if(loginName==null){
			reg.put("result", "fail");
			reg.put("reason", "请求超时");
			return reg;
		}else{
			reg.put("userLoginName", loginName);
			return reg;
		}
	}

	
	/**
	 * 重置密码
	 * 接口：login/restPwd
	 * @param phoneNo
	 * @param request
	 * @param response
	 * @param model
	 * 返回参数：result:"success"/"fail"
	 */
	@RequestMapping(method = RequestMethod.GET,value="/restPwd")
	public @ResponseBody Map<String,String> restPwd(@RequestParam String newPwd,@RequestParam String phoneNo,HttpServletRequest request, HttpServletResponse response,Model model){
		
		Map<String, String> reg = new HashMap<String, String>();
		reg.put("result", "success");
		IpUser findUserByUser = userAccountService.findUserByCellphoneNo(phoneNo);
		
		IpHirer findUserByHirer = hirerAccountService.findHirerByCellphoneNo(phoneNo);
		
//		String userId = CookieUtil.findCookieValue(request.getCookies(), "userID");//从cookie里面获得userId
		if(findUserByUser==null){
			reg.put("result", "noUserId");//cookie里面没有userId,前台需要重定向找回密码界面
			reg.put("reason", "请求超时");
			return reg;
		}
		// 0 是管理员  更新管理员帐户的内容
		if("0".equals(findUserByUser.getUserType()))
		{
			IpHirer tempHirer=new IpHirer();
			tempHirer.setPassword(newPwd);
			this.entryptPassword(tempHirer);//加密
			
			findUserByHirer.setPassword(tempHirer.getPassword());
			findUserByHirer.setSalt(tempHirer.getSalt());
			
			hirerAccountService.saveHirerInfo(findUserByHirer);
		}
		
		
		IpUser UntaxUserTmp=new IpUser();
		UntaxUserTmp.setPassword(newPwd);
		this.entryptPassword(UntaxUserTmp);//加密
		
//		IpUser findUserByUserId = userAccountService.findUserByUserId(userId);
		//CopyPropertiesUtil.setProperty(findUserByUser);//将null置为空字符串，为了后面的更新操作，jpa底层不允许将null更新给原来的字段
		try{
			//更新密码
			userAccountService.updatePwdByUserId(findUserByUser.getUserId(),UntaxUserTmp.getPassword(),UntaxUserTmp.getSalt());//根据id更新
			return reg;
		}catch(Exception ex){
			reg.put("result", "fail");
			logger.error("服务器忙，密码重置失败!", ex);
			return reg;
		}
	}
	/**
	 * 加密user的密码
	 * @param user
	 */
	private void entryptPassword(IpUser user) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		user.setSalt(Encodes.encodeHex(salt));
		byte[] hashPassword = Digests.sha1(user.getPassword().getBytes(), salt, HASH_INTERATIONS);
		user.setPassword(Encodes.encodeHex(hashPassword));
	}
	
	
	/**
	 * 加密user的密码
	 * @param user
	 */
	private void entryptPassword(IpHirer hirer) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		hirer.setSalt(Encodes.encodeHex(salt));
		byte[] hashPassword = Digests.sha1(hirer.getPassword().getBytes(), salt, HASH_INTERATIONS);
		hirer.setPassword(Encodes.encodeHex(hashPassword));
	}
	
}
