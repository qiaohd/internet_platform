package com.ufgov.ip.web.system;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import uap.web.httpsession.cache.SessionCacheManager;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ufgov.ip.api.message.phoneverity.SendMessageServiceI;
import com.ufgov.ip.api.sysmanager.IpCompanyServiceI;
import com.ufgov.ip.api.system.HirerAccountServiceI;
import com.ufgov.ip.api.system.HirerRegisterServiceI;
import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.utils.ProduceCodeUtil;
import com.ufgov.ip.utils.PropertyUtilSys;
import com.ufgov.ip.utils.UUIDTools;
import com.yonyou.uap.entity.response.MessageResponse;

/**
 * 注册用的Controller
 * 
 * @author zhangbch
 */

@Controller
@RequestMapping("/register")
public class UserRegisterController {
	
	 @Autowired
	HirerRegisterServiceI hirerService;
	
	 @Autowired
	protected HirerAccountServiceI hirerAccountService;
	
	 @Autowired
	IpCompanyServiceI companyService;
	
	@Autowired
    private SessionCacheManager sessionCacheManager;
	
	 @Autowired
	private SendMessageServiceI sendMessageService;
	
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	public void setService(HirerRegisterServiceI hirerService) {
		this.hirerService = hirerService;
	}

	public void setCompanyService(IpCompanyServiceI companyService) {
		this.companyService = companyService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String login(Model model) {
		return "register";
	}
	
	
	public void setHirerAccountService(HirerAccountServiceI hirerAccountService) {
		this.hirerAccountService = hirerAccountService;
	}
	

	public SessionCacheManager getSessionCacheManager() {
		return sessionCacheManager;
	}

	public void setSessionCacheManager(SessionCacheManager sessionCacheManager) {
		this.sessionCacheManager = sessionCacheManager;
	}

	public SendMessageServiceI getSendMessageService() {
		return sendMessageService;
	}

	public void setSendMessageService(SendMessageServiceI sendMessageService) {
		this.sendMessageService = sendMessageService;
	}

	//注册
	@RequestMapping(method = RequestMethod.POST,value="formRegister")
	public String formRegister(HttpServletRequest request, HttpServletResponse response,Model model) {
		String phoneNumber = request.getParameter("phoneNumber");
		String password = request.getParameter("password");
		String username = request.getParameter("username");
		String sex = request.getParameter("sex");
		String companyName = request.getParameter("companyName");
		
		String dataId= request.getParameter("dbArea");
		//租户
		IpHirer ipHirer=new IpHirer();
		if(!phoneNumber.contains("@")){
			ipHirer.setCellphoneNo(phoneNumber);
			ipHirer.setEmail("");
		}else{
			ipHirer.setEmail(phoneNumber);
			ipHirer.setCellphoneNo("");
		}
		ipHirer.setPassword(password);
		ipHirer.setHirerName(companyName);//设置租户名称
		ipHirer.setHirerShortName(companyName);
		ipHirer.setLinkman(username);
		ipHirer.setSex(sex);
		ipHirer.setDataId(dataId);
		ipHirer.setCreateDate(new Date());
		//当用户注册后获得登录账户
		String code = ProduceCodeUtil.getCode();
		ipHirer.setLoginName("admin@"+code);//设置登录账户
		ipHirer.setHirerNo(code);//设置租户号
		
		//加密
		this.entryptPassword(ipHirer);
		
		//单位
		IpCompany ipCompany=new IpCompany();
		ipCompany.setCoFullname(companyName);
		ipCompany.setCoName(companyName);
		ipCompany.setCoId(UUIDTools.uuidRandom());
		ipCompany.setCoCode(code);
		ipCompany.setCoCodeTmp(code);
		try {			
			 hirerService.registerHirer(ipHirer,ipCompany);
		} catch (Exception e) {
			e.printStackTrace();
			return "register_fail";
		}
		return "register_success";
		
	}
	
	@RequestMapping(method = RequestMethod.POST,value="checkPhoneisExist")
	public void checkPhoneisExist(HttpServletRequest request, HttpServletResponse response,Model model){
		
		 String phoneNo = request.getParameter("phone");
		
		 IpHirer findHirerByCellphoneNo = hirerAccountService.findHirerByCellphoneNo(phoneNo);
		 try {
		 if(findHirerByCellphoneNo!=null){
			 if("".equals(findHirerByCellphoneNo)){
				 response.getWriter().write("请输入手机号");
			 }else{
				 response.getWriter().write("false");
			 }
		    
		 }else{
			 response.getWriter().write("true");
		 } 
		 }catch (IOException e) {
				e.printStackTrace();
			}
		 }
	
	//邮箱校验
	@RequestMapping(method = RequestMethod.POST,value="checkMailisExist")
	public void checkMailisExist(HttpServletRequest request, HttpServletResponse response,Model model){
		
		 String mailValue = request.getParameter("mailValue");
		
		 IpHirer findHirerByEmail = hirerAccountService.findHirerByEmail(mailValue);
		 try {
		  if(findHirerByEmail!=null){
			  response.getWriter().write("false");
			 }else{
			 response.getWriter().write("true");
		   } 
		 }catch (IOException e) {
				e.printStackTrace();
			}
		 }
	
	@RequestMapping(method = RequestMethod.POST,value="regCheckUserName")
	public void regCheckUserName(HttpServletRequest request, HttpServletResponse response,Model model){
		
		    String personName = request.getParameter("personName");
		    IpHirer findHirerByLoginName = hirerAccountService.findHirerByLoginName(personName);
		    
		    try {
				 if(findHirerByLoginName!=null){
				   response.getWriter().write("false");
				 }else{
					 response.getWriter().write("true");
				  } 
				 }catch (IOException e) {
						e.printStackTrace();
					}
		
	}
	
	//获得手机验证码
	@RequestMapping(method = RequestMethod.GET,value="getMobileCode")
	public void getMobileCode(HttpServletRequest request, HttpServletResponse response,Model model){
		
		String tran_info = request.getParameter("tran_info");
		
		
		//获得6位验证码
		/*Random random = new Random();
	      String str="";
	       for(int i=0;i<6;i++){
	         str+=random.nextInt(10);
		    }*/
		
		//获得随机码
		String code = ProduceCodeUtil.getCode();
		
		try {
			
			//sessionCacheManager.setSessionTimeout(360);
            sessionCacheManager.cacheUser("mobile_code", code);
            tran_info=URLDecoder.decode(tran_info,"utf-8");
            String isSend=PropertyUtilSys.getMessagePropertyByKey("isSend");
            if(("false").equals(isSend)){
            	System.out.println("*************验证码【"+code+"】******");
            	response.getWriter().print("true"); 
            	return;
            }
    		if(!tran_info.contains("@")){
    			 List<MessageResponse> phoneMessageSend = sendMessageService.phoneMessageSend(tran_info, code,sessionCacheManager.getSessionTimeout(),"op");//像手机发送验证码
             	
             	if(phoneMessageSend.size()>0){ 
     	            for (MessageResponse messageResponse : phoneMessageSend) {
     	            	String responseStatusCode = messageResponse.getResponseStatusCode();
     				     if("1000".equals(responseStatusCode)){
     				    	  response.getWriter().print("true"); 
     				     }else{
     				    	 response.getWriter().print("false"); 
     				     }
     	            }
                 }
    		}else{
   			 List<MessageResponse> phoneMessageSend = sendMessageService.emailMessageSend(tran_info, code,sessionCacheManager.getSessionTimeout(),"op");//像手机发送验证码
   			if(phoneMessageSend.size()>0){ 
 	            for (MessageResponse messageResponse : phoneMessageSend) {
 	            	String responseStatusCode = messageResponse.getResponseStatusCode();
 				     if("0".equals(responseStatusCode)){
 				    	  response.getWriter().print("true"); 
 				     }else{
 				    	 response.getWriter().print("false"); 
 				     }
 	            }
             }
    		}
           
            
           
          
        } catch (Exception e) {
        	try {
				response.getWriter().print("false");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            logger.error("登陆信息写入到redis缓存中失败!", e);
        }
		
	}
	
	
	//验证手机验证码
	@RequestMapping(method = RequestMethod.GET,value="checkAuthCode")
	public void checkAuthCode(HttpServletRequest request, HttpServletResponse response,Model model){
		try {
				String auth_code = request.getParameter("auth_code");//手机验证码
				String serializable = sessionCacheManager.getCurUser("mobile_code");
				if(auth_code.equals(serializable)){
					response.getWriter().print("true");	
				}else{
					response.getWriter().print("false");	
				}
		        	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	private void entryptPassword(IpHirer hirer) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		hirer.setSalt(Encodes.encodeHex(salt));
		byte[] hashPassword = Digests.sha1(hirer.getPassword().getBytes(), salt, HASH_INTERATIONS);
		hirer.setPassword(Encodes.encodeHex(hashPassword));
	}
}
