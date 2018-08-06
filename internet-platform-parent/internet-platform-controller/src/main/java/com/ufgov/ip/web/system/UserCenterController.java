package com.ufgov.ip.web.system;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;
import uap.web.httpsession.cache.SessionCacheManager;
import com.ufgov.ip.facade.EnumType;
import com.alibaba.dubbo.config.annotation.Reference;
import com.ufgov.ip.api.base.IPCommonServiceI;
import com.ufgov.ip.api.sysmanager.IPRoleServiceI;
import com.ufgov.ip.api.sysmanager.IpUserCompanyServiceI;
import com.ufgov.ip.api.sysmanager.IpUserRoleServiceI;
import com.ufgov.ip.api.system.HirerAccountServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.sysmanager.IpRole;
import com.ufgov.ip.entity.sysmanager.IpUserRole;
import com.ufgov.ip.entity.base.IpDictionary;
import com.ufgov.ip.entity.base.IpDictionaryDetail;
import com.ufgov.ip.entity.system.IpUserCompany;
import com.ufgov.ip.utils.CopyPropertiesUtil;
import com.ufgov.ip.utils.UploadFileURLUtils;
import com.ufgov.ip.utils.ImageCutTool;
import com.ufgov.ip.utils.PathUtil;


@Controller
@RequestMapping(value = "/userset")
public class UserCenterController {
public static final int HASH_INTERATIONS = 1024;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
    private SessionCacheManager sessionCacheManager;

	 @Autowired
	private IpUserCompanyServiceI ipUserCompanyService;
	
	 @Autowired
	private IpUserRoleServiceI ipUserRoleService;

	 @Autowired
	private IPRoleServiceI iPRoleService;
	
	 @Autowired
	private IPCommonServiceI iPCommonService;
	
	 @Autowired
	protected UserAccountServiceI userAccountService;
	
	 @Autowired
	protected HirerAccountServiceI hirerAccountService;
	
	public IPRoleServiceI getiPRoleService() {
		return iPRoleService;
	}
	
	public IPCommonServiceI getiPCommonService() {
		return iPCommonService;
	}

	public void setiPCommonService(IPCommonServiceI iPCommonService) {
		this.iPCommonService = iPCommonService;
	}
	
	public UserAccountServiceI getUserAccountService() {
		return userAccountService;
	}

	public void setUserAccountService(UserAccountServiceI userAccountService) {
		this.userAccountService = userAccountService;
	}

	public void setiPRoleService(IPRoleServiceI iPRoleService) {
		this.iPRoleService = iPRoleService;
	}
	
	public void setIpUserRoleService(IpUserRoleServiceI ipUserRoleService) {
		this.ipUserRoleService = ipUserRoleService;
	}
	
	public SessionCacheManager getSessionCacheManager() {
		return sessionCacheManager;
	}

	public void setSessionCacheManager(SessionCacheManager sessionCacheManager) {
		this.sessionCacheManager = sessionCacheManager;
	}

	public IpUserCompanyServiceI getIpUserCompanyService() {
		return ipUserCompanyService;
	}

	public void setIpUserCompanyService(
			IpUserCompanyServiceI ipUserCompanyService) {
		this.ipUserCompanyService = ipUserCompanyService;
	}
	
	
	/**
	 * 保存登陸id(各人或租戶)
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "getLoginId")
	public @ResponseBody Map<String, Object> getLoginId(HttpServletRequest request, HttpServletResponse response,
			ModelMap model){
		String cuser = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (SecurityUtils.getSubject().getPrincipal() != null)
			cuser = (String) SecurityUtils.getSubject().getPrincipal();
		if (cuser == null) {
			return null;
		}
		try {
			IpUser ipUser = userAccountService.findUserByUserEmailOrLoginNameOrCellphoneNo(cuser);//租户
			if(ipUser!=null){
				String type=ipUser.getUserType();
				if("0".equals(type))
				{
					resultMap.put("hirerId",ipUser.getHirerId());
				}
				else
				{
					resultMap.put("userId", ipUser.getUserId());
				}
			}
		} catch (Exception e) {
			logger.error("获取用户权限失败!",e);
		}
		
		return resultMap;
	}
	
	
	
	/**
	 * 检查原密码是否输入正确
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "checkoriginpass")
	public @ResponseBody Map<String, Object> checkoriginpassword(HttpServletRequest request, HttpServletResponse response,
			ModelMap model){
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String userId = request.getParameter("userId");
		String hirerId = request.getParameter("hirerId");
		String originPassword=request.getParameter("password2");
		
		IpUser findUserByUserId=null;
		IpHirer findHirerByHirerId=null;
		
		if(!"".equals(userId))
		{
			try
			{
				findUserByUserId = userAccountService.findUserByUserId(userId);
				if(Encodes.encodeHex(Digests.sha1(originPassword.getBytes(), Encodes.decodeHex(findUserByUserId.getSalt()), HASH_INTERATIONS)).equals(findUserByUserId.getPassword()))
				{
					resultMap.put("result","success");
				}
				else
				{
					resultMap.put("result","fail");
					resultMap.put("reason","原密码输入有误！");
	
				}
			}
			catch(Exception ex)
			{
				resultMap.put("other","服务器繁忙！！");
			}
		}
		else if(!"".equals(hirerId))
		{
		try
			{
				findHirerByHirerId=hirerAccountService.findHirerByHirerId(hirerId);
				if(Encodes.encodeHex(Digests.sha1(originPassword.getBytes(), Encodes.decodeHex(findHirerByHirerId.getSalt()), HASH_INTERATIONS)).equals(findHirerByHirerId.getPassword()))
				{
					resultMap.put("result","success");
				}
				else
				{
					resultMap.put("result","fail");
					resultMap.put("reason","原密码输入有误！");
				}
			}
			catch(Exception ex)
			{
				   resultMap.put("other","服务器繁忙！！");
			}  
		 }
		return resultMap;
	}
	
	
	
	/**
	 * 个人中心->保存设置
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "saveusersetinfo")
	public @ResponseBody Map<String, Object> saveusersetinfo(
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {

		  IpUser ipUser=new IpUser();
		 //通过BeanUtils为相应的对象属性赋值
		  Map<String, String[]> parameterMap = request.getParameterMap();
		  Map<String,Object> backInfo = new HashMap<String, Object>();
		  try 
		  {
			BeanUtils.populate(ipUser, parameterMap);
			String userId = ipUser.getUserId();
			IpUser findUserByUserId = userAccountService.findUserByUserId(userId);
			CopyPropertiesUtil.copyProperty(findUserByUserId, ipUser);
			try {
				userAccountService.saveIpUser(findUserByUserId);
				backInfo.put("result", "true");
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("保存用户信息失败！",e);
				backInfo.put("result", "fail");
				backInfo.put("reason", "服务器忙......");
			}
			
			
		  }
		  catch (Exception e) 
		  {		
			e.printStackTrace();
		  }  
		  return backInfo;
	}
	
	
	
	
	/**
	 * 个人中心->个人设置
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "showusersetinfo")
	public @ResponseBody Map<String, Object> showusersetinfo(
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		
		String userId = request.getParameter("userId");
		String hirerId = request.getParameter("hirerId");
		
		Map<String, Object> usersetCfg = new HashMap<String, Object>();
		// 非兼职
		/*List<IpCompany> no_p_co = ipUserCompanyService
				.showUserCompanyByUserIdAndIsPartTime(userId, "0");*/
		
		List<IpCompany> no_p_co = ipUserCompanyService
				.showUserCompanyByUserId(userId);
		List<IpUserRole> no_p_userRole = ipUserRoleService
				.showUserRoleByUserIdAndIsPartTime(userId, "0");
		
		List<IpRole> no_p_role=new ArrayList<IpRole>();
		for (IpUserRole ipUserRole : no_p_userRole) {
			String roleId = ipUserRole.getRoleId();
			IpRole findIpRoleByRoleId = iPRoleService.findIpRoleByRoleId(roleId);
			if(findIpRoleByRoleId==null){
				IpRole ipRole=new IpRole();
				ipRole.setRoleName("请选择");
				ipRole.setRoleId("");
				no_p_role.add(ipRole);
			}else{
				no_p_role.add(findIpRoleByRoleId);
			}
		}
		// 兼职
		List<IpCompany> part_co = ipUserCompanyService
				.showUserCompanyByUserIdAndIsPartTime(userId, "1");
		List<IpUserRole> part_userRole = ipUserRoleService
				.showUserRoleByUserIdAndIsPartTime(userId, "1");
		List<IpRole> part_role=new ArrayList<IpRole>();
		for (IpUserRole ipUserRole : part_userRole) {
			String roleId = ipUserRole.getRoleId();
			IpRole findIpRoleByRoleId = iPRoleService.findIpRoleByRoleId(roleId);
			if(findIpRoleByRoleId==null){
				IpRole ipRole=new IpRole();
				ipRole.setRoleName("请选择");
				ipRole.setRoleId("");
				part_role.add(ipRole);
			}else{
				part_role.add(findIpRoleByRoleId);
			}
			
		}
		
		

		IpUser userinfo = userAccountService.findUserByUserId(userId);
		
		// 查询学历
				IpDictionary findIpDictionaryByDicType = iPCommonService
						.findIpDictionaryByDicType(EnumType.DEGREE);
				List<IpDictionaryDetail> findIpDictionaryDetailByDicType = iPCommonService
						.findIpDictionaryDetailByDicId(findIpDictionaryByDicType
								.getDicId());
				
				//all_duty
				//String hirerId = CookieUtil.findCookieValue(request.getCookies(),"hirerId");
				List<IpRole> roleInfo = iPRoleService.getRoleInfoByHirerId(hirerId);
				
		usersetCfg.put("p_role", part_role);
		usersetCfg.put("part_co", part_co);

		if (no_p_role.size() == 0) {
			usersetCfg.put("no_p_role", "");
		} else {
			usersetCfg.put("no_p_role", no_p_role.get(0));
		}
		if (no_p_co.size() == 0) {
			usersetCfg.put("no_p_co", "");
		} else {
			usersetCfg.put("no_p_co", no_p_co.get(0));
		}

		usersetCfg.put("back_user", userinfo);		
		usersetCfg.put("all_edu", findIpDictionaryDetailByDicType);
		usersetCfg.put("all_duty", roleInfo);

		return usersetCfg;
	}
	
	

	/**
	 * 个人中心-修改头像
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value="setuserheadIogo")
	public String uploadIogo(HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException{
		
		String userId = request.getParameter("userId");
		String hirerId = request.getParameter("hirerId");
		
		MultipartHttpServletRequest multipartRequest  =  (MultipartHttpServletRequest) request;  
        MultipartFile pictureFile = multipartRequest.getFile("imgFile");
        String originalFilename =pictureFile.getOriginalFilename();//源图像地址
 
        // 用户经过剪辑后的图片的大小  
        Integer x = Integer.parseInt(request.getParameter("x"));  
        Integer y = Integer.parseInt(request.getParameter("y"));  
        Integer w = Integer.parseInt(request.getParameter("w"));  
        Integer h = Integer.parseInt(request.getParameter("h")); 
        Integer w1 = Integer.parseInt(request.getParameter("w1"));  
        Integer h2 = Integer.parseInt(request.getParameter("h2")); 
        
		//String contextPath = request.getContextPath();
		
		IpUser findUserByUserId=null;
		IpHirer findHirerByHirerId=null;
		
		String userOrhirerId="";
		
		String fileName="";
		if(!"".equals(userId))
		{
			findUserByUserId = userAccountService.findUserByUserId(userId);
			fileName = findUserByUserId.getUserId() + "headerLogo"+originalFilename.substring(originalFilename.lastIndexOf("."));
			userOrhirerId=userId;
		}
		else if(!"".equals(hirerId))
		{
			findHirerByHirerId=hirerAccountService.findHirerByHirerId(hirerId);
			fileName=findHirerByHirerId.getHirerId()+ "headerLogo"+originalFilename.substring(originalFilename.lastIndexOf("."));
			userOrhirerId=hirerId;
		}
					
		//保存图片
		String url2 = UploadFileURLUtils.getUrl("images", "headerlogoFilePath");
		//begin_iternet_platform1_图片上传路径问题修改_zhangbch 
		String path_pro = request.getServletContext().getRealPath("/");
		path_pro=path_pro+"\\"+url2;
		path_pro=path_pro.replace("\\", "/");
		File file = new File(path_pro);
		if (!file.exists()) {
					file.mkdirs();
			}
			  		
				File[] listFiles = file.listFiles();
				if(listFiles!=null){
					for (File file2 : listFiles) {
						if(file2.isFile() && file2.getName().startsWith(userOrhirerId)){
							file2.delete();
							break;
						}
					}
				}
				
				String path = file.getAbsolutePath();//新图像地址
				try {
					path=path+"\\" + fileName;
					path=path.replace("\\", "/");
					pictureFile.transferTo(new File(path));
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/tenant/tenantInfo?ishowFlag=1&uploadPicInfo=false";
				}
				if(w!=0 && h!=0){
					ImageCutTool.abscut(pictureFile,path, x, y, w, h,w1,h2);
				}
		
		//设置存放的url
				String url = PathUtil.getURL(path);
		if(!"".equals(userId)){//登陆的是人员
			//为支持linux下对路径做处理
			url=url.replace("\\", "/");
			userAccountService.updateUserHeaderimageByUserId(findUserByUserId.getUserId(), url);
		}
		else if(!"".equals(hirerId))//登陆的是租户
		{
			//为支持linux下对路径做处理
			url=url.replace("\\", "/");
			hirerAccountService.updateHirerHeaderimageByHirerId(findHirerByHirerId.getHirerId(), url);
			//更新租户在用户表里的信息
			IpUser user=userAccountService.findUserByUserEmailOrLoginNameOrCellphoneNo(findHirerByHirerId.getCellphoneNo());
			userAccountService.updateUserHeaderimageByUserId(user.getUserId(), url);
		}
				
	   
		//头像更新成功后界面跳转至首页
		return "redirect";	
		
	}
	
}
