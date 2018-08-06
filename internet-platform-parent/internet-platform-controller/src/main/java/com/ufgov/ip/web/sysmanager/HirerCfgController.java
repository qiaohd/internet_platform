package com.ufgov.ip.web.sysmanager;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ufgov.ip.api.base.IPCommonServiceI;
import com.ufgov.ip.api.sysmanager.IPRoleServiceI;
import com.ufgov.ip.api.system.HirerAccountServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.entity.base.IpDictionary;
import com.ufgov.ip.entity.base.IpDictionaryDetail;
import com.ufgov.ip.entity.base.IpRegion;
import com.ufgov.ip.entity.sysmanager.RegionCode;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.facade.EnumType;
import com.ufgov.ip.utils.CopyPropertiesUtil;
import com.ufgov.ip.utils.ImageCutTool;
import com.ufgov.ip.utils.PathUtil;
import com.ufgov.ip.utils.UploadFileURLUtils;

@Controller
@RequestMapping(value = "sysmanager/hirercfg")
public class HirerCfgController {

    @Autowired
	protected HirerAccountServiceI hirerAccountService;
	
    @Autowired
	private IPRoleServiceI iPRoleService;
	
    @Autowired
	private IPCommonServiceI iPCommonService;
	
    @Autowired
	protected UserAccountServiceI userAccountService;
	
	public IPCommonServiceI getiPCommonService() {
		return iPCommonService;
	}

	public void setiPCommonService(IPCommonServiceI iPCommonService) {
		this.iPCommonService = iPCommonService;
	}

	public IPRoleServiceI getiPRoleService() {
		return iPRoleService;
	}

	public void setiPRoleService(IPRoleServiceI iPRoleService) {
		this.iPRoleService = iPRoleService;
	}

	public HirerAccountServiceI getHirerAccountService() {
		return hirerAccountService;
	}

	public void setHirerAccountService(HirerAccountServiceI hirerAccountService) {
		this.hirerAccountService = hirerAccountService;
	}
	
	

	public UserAccountServiceI getUserAccountService() {
		return userAccountService;
	}

	@RequestMapping(method = RequestMethod.GET,value="getHirerById")
	public @ResponseBody Map<String,Object> getHirerById(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		
		//租户号
         String hirerId = request.getParameter("hirerId");
         // IpHirer findHirerByHirerId = hirerAccountService.findHirerByHirerId(hirerId);
         IpHirer findHirerByHirerId = hirerAccountService.findHirerByHirerIdMybatis(hirerId);
         
         String region = findHirerByHirerId.getRegion();
         RegionCode regionCodeAndName=new RegionCode();
            if(region!=null && !"".equals(region)){
            	String[] split = region.split(",");
            	if(split.length==3){
            		String proCode=split[0];
            		String cityCode=split[1];
            		String countryCode=split[2];
            		String proName="";
            		String cityName="";
            		String countryName="";
            		IpRegion proCodeAndName = iPCommonService.findIpRegionByTheCode(proCode);
            		IpRegion cityCodeAndName = iPCommonService.findIpRegionByTheCode(cityCode);
            		IpRegion countryCodeAndName = iPCommonService.findIpRegionByTheCode(countryCode);            		
            		if(proCodeAndName!=null){
            			proName = proCodeAndName.getTheName();
            			if(cityCodeAndName!=null){
            				cityName = cityCodeAndName.getTheName();
            				if(countryCodeAndName!=null){
            					countryName = countryCodeAndName.getTheName();
            				}
            			}
            		}           		
            		regionCodeAndName.setCityCode(cityCode);
            		regionCodeAndName.setCityName(cityName);
            		
            		regionCodeAndName.setProCode(proCode);
            		regionCodeAndName.setProName(proName);
            		
            		regionCodeAndName.setCountryCode(countryCode);
            		regionCodeAndName.setCountryName(countryName);
            	}
            }
			
         
         //查询职务
        // List<IpRole> roleInfo = iPRoleService.getRoleInfo();
         
         //查询租户类型
         IpDictionary findIpDictionaryByDicType = iPCommonService.findIpDictionaryByDicType(EnumType.HIRERTYPE);
         List<IpDictionaryDetail> findIpDictionaryDetailByDicType = iPCommonService.findIpDictionaryDetailByDicId(findIpDictionaryByDicType.getDicId());
         
         //省市县
         List<IpRegion> provinces=getprovinceInfo(request,response,model);
         
         Map<String,Object> hirerCfg=new HashMap<String, Object>();
         
         hirerCfg.put("hirerBaseInfo", findHirerByHirerId);
         hirerCfg.put("enumTypeInfo", findIpDictionaryDetailByDicType);
         hirerCfg.put("provinceInfo", provinces);
         hirerCfg.put("regionCodeAndName", regionCodeAndName);
         
         return hirerCfg;
         
	}
	
	
	@RequestMapping(method = RequestMethod.GET,value="getprovinceInfo")
	public List<IpRegion> getprovinceInfo(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		  String code=URLEncoder.encode("__0000%","utf-8");
		  String the_code=URLDecoder.decode(code,"utf-8");
		  return iPCommonService.findIpRegionByTheCodeLike(the_code);
	}
	
	@RequestMapping(method = RequestMethod.GET,value="getcityInfo")
	public @ResponseBody List<IpRegion> getcityInfo(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		  
		  String proCode = request.getParameter("proCode");
		  if(proCode.length()>2){
			  proCode=proCode.substring(0, 2);
		  }
		  
		  String cityCode=URLEncoder.encode(proCode+"__00%","utf-8");
		  cityCode=URLDecoder.decode(cityCode,"utf-8");
		  List<IpRegion> findIpRegionByTheCodeLike = iPCommonService.findIpRegionByTheCodeLike(cityCode);
		  Iterator<IpRegion> iterator = findIpRegionByTheCodeLike.iterator();
			 
		  while(iterator.hasNext()){
			 
			  IpRegion ipRegion = iterator.next();
			  if(ipRegion.getTheCode().equals(proCode+"0000")){
				  iterator.remove();
			  }
		  }
		  
		  
		  return findIpRegionByTheCodeLike;
	}
	
	@RequestMapping(method = RequestMethod.GET,value="getcountyInfo")
	public @ResponseBody List<IpRegion> getcountyInfo(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		  String cityCode = request.getParameter("cityCode");
		  if(cityCode.length()>4){
			  cityCode=cityCode.substring(0, 4);
		  }
		  
		  String countryCode=URLEncoder.encode(cityCode+"%","utf-8");
		  countryCode=URLDecoder.decode(countryCode,"utf-8");
		  List<IpRegion> findIpRegionByTheCodeLike = iPCommonService.findIpRegionByTheCodeLike(countryCode);
		  
		  
		  Iterator<IpRegion> iterator = findIpRegionByTheCodeLike.iterator();
		  while(iterator.hasNext()){
			 
			  IpRegion ipRegion = iterator.next();
			  if(ipRegion.getTheCode().equals(cityCode+"00")){
				  iterator.remove();
			  }
		  }
		  return findIpRegionByTheCodeLike;
	}
	
	
	//图片上传
	@RequestMapping(method = RequestMethod.POST,value="uploadIogo")
	public String uploadIogo(HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException{
		
		
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
        
		Cookie[] cookies = request.getCookies();
		String value="";
		for (Cookie cookie : cookies) {
        	String cookieName = cookie.getName();
        	if("hirerId".equals(cookieName)){
        		value = cookie.getValue();
        		value=URLDecoder.decode(value,"utf-8");
        		//model.addAttribute("username", value);
        	}
		}
		
		String contextPath = request.getContextPath();
//		IpHirer findHirerByHirerId = hirerAccountService.findHirerByHirerId(value);
		IpHirer findHirerByHirerId = hirerAccountService.findHirerByHirerIdMybatis(value);
		String fileName = findHirerByHirerId.getHirerId() + originalFilename.substring(originalFilename.lastIndexOf("."));
		
		//保存图片
		String url2 = UploadFileURLUtils.getUrl("images", "imagesFilePath");
				//File file=new File(url2);
				
		        //begin_iternet_platform1_图片上传路径问题修改_zhangbch 
					String path_pro = request.getServletContext().getRealPath("/");
					path_pro=path_pro+"\\"+url2;
					path_pro=path_pro.replace("\\", "/");
					File file = new File(path_pro);
					if (!file.exists()) {
						file.mkdirs();
					}
			   //end_iternet_platform1_图片上传路径问题修改_zhangbch 
				
				
				File[] listFiles = file.listFiles();
				if(listFiles!=null){
					for (File file2 : listFiles) {
						if(file2.isFile() && file2.getName().startsWith(findHirerByHirerId.getHirerId())){
							file2.delete();
							break;
						}
					}
				}
				
				String path = file.getAbsolutePath()+"\\"+fileName;//新图像地址
				path=path.replace("\\", "/");
				try {
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
				
	    //更新logo路径hirerLogoUrl  /tenant/tenantInfo
		hirerAccountService.updateHirerByHirerId(findHirerByHirerId.getHirerId(), url);
		//return "redirect:/tenant/tenantInfo#/tenantinfo/tenantlogo/tenantsetlogo";
		return "redirect:/tenant/tenantInfo?ishowFlag=1&uploadPicInfo=true";
		
	}
	
	
	
	@RequestMapping(method = RequestMethod.POST,value="saveHirerInfo")
	public @ResponseBody Map<String,Object> saveHirerInfo(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		
		  IpHirer ipHirer=new IpHirer();
		 //通过BeanUtils为相应的对象属性赋值
		  Map<String, String[]> parameterMap = request.getParameterMap();
		  Map<String,Object> backInfo = new HashMap<String, Object>();
		  try {
			BeanUtils.populate(ipHirer, parameterMap);
			String hirerId = ipHirer.getHirerId();
			IpHirer findHirerByHirerId = hirerAccountService.findHirerByHirerId(hirerId);
			CopyPropertiesUtil.copyProperty(findHirerByHirerId, ipHirer);
			boolean save_statue = hirerAccountService.saveHirerInfo(findHirerByHirerId);
			//同时在ipuser表中也更新一下
			String userSex=ipHirer.getSex();
			String userEmail=ipHirer.getEmail();
			String phoneNo=ipHirer.getPhoneNo();
			String cellphoneNo=ipHirer.getCellphoneNo();
			String duty=ipHirer.getDuty();
		    userAccountService.saveIpUserInfoByHirerIdAndUserType(hirerId,"0",userSex,userEmail,phoneNo,cellphoneNo,duty);
			//获得当前租户的省市县编码
			String region = ipHirer.getRegion();
			String[] split = region.split(",");
			String proCode=split[0];
			String cityCode=split[1];
			String countryCode3=split[2];
			
			if(save_statue){
				/*//获得所有的省
				  String code=URLEncoder.encode("__0000%","utf-8");
				  String the_code=URLDecoder.decode(code,"utf-8");
				  List<IpRegion> provinceInfo = iPCommonService.findIpRegionByTheCodeLike(the_code);
				
				
				  //获得市级的
				  String proCode1=proCode.substring(0, 2);
				  String cityCode1=URLEncoder.encode(proCode1+"__00%","utf-8");
				  cityCode1=URLDecoder.decode(cityCode1,"utf-8");
				  List<IpRegion> cityInfo = iPCommonService.findIpRegionByTheCodeLike(cityCode1);
				  
				  Iterator<IpRegion> iterator = cityInfo.iterator();
				  while(iterator.hasNext()){
					  IpRegion ipRegion = iterator.next();
					  if(ipRegion.getTheCode().equals(proCode+"0000")){
						  iterator.remove();
					  }
				  }
				  
				  //获得县级的
				  String cityCode2=cityCode.substring(0, 4);
				  String countryCode=URLEncoder.encode(cityCode2+"%","utf-8");
				  countryCode=URLDecoder.decode(countryCode,"utf-8");
				  List<IpRegion> countryInfo = iPCommonService.findIpRegionByTheCodeLike(countryCode);
				  Iterator<IpRegion> iterator2 = countryInfo.iterator();
				  while(iterator2.hasNext()){
					 
					  IpRegion ipRegion = iterator2.next();
					  if(ipRegion.getTheCode().equals(cityCode+"00")){
						  iterator.remove();
					  }
				  }
				  
				 
				  RegionCode regionCode=new RegionCode();
				  regionCode.setProCode(proCode);
				  regionCode.setCityCode(cityCode);
				  regionCode.setCountry(countryCode3);*/
				  backInfo.put("result", "true");
				  /*backInfo.put("provinceInfo", provinceInfo);
				  backInfo.put("cityInfo", cityInfo);
				  backInfo.put("countryInfo", countryInfo);
				  backInfo.put("regionCode", regionCode);*/
			}else{
				backInfo.put("result", "fail");
				backInfo.put("reason", "服务器忙......");
			}
		  } catch (Exception e) {
			e.printStackTrace();
		}
		  
		  return backInfo;
	}
	
	/**
	 * 手机号校验
	 * 接口：sysmanager/hirercfg/phoneCheck
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET,value="phoneCheck")
	public @ResponseBody Map<String,String> phoneCheck(@RequestParam String hirerId,@RequestParam String phone , HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		Map<String, String> reg=new HashMap<String, String>();
		reg.put("result", "success");
		
		//校验和用户表中与其他手机号是否重复
		//1.判断是否等于租户管理员（及当前用户）的电话号码,不等于才和其他租户进行比较
		IpUser findUserByHirerIdAndUserType=userAccountService.findUserByHirerIdAndUserType(hirerId,"0");			
		if(!findUserByHirerIdAndUserType.getCellphoneNo().equals(phone)){
				//2.校验与其他租户是否重复，判断
			IpUser findUserByCellphoneNo=userAccountService.findUserByCellphoneNo(phone);
			if(findUserByCellphoneNo !=null){
				reg.put("result", "PH_two");
			   }
			}
		return reg;
		
		
	/*	IpHirer findHirerByCellphoneNo = hirerAccountService.findHirerByCellphoneNo(phone);
		IpHirer findHirerByHirerId = hirerAccountService.findHirerByHirerId(hirerId);
		
		Map<String, String> putUserIntoCache = putUserIntoCache(hirerId);//再查询一遍放进缓存
		Map<String, String> checkHirerAndUser = checkHirerAndUser(phone, reg,putUserIntoCache);
		return checkUserInfo(findHirerByCellphoneNo,findHirerByHirerId,checkHirerAndUser);*/
	}
	
	
	/**
	 * 邮箱校验
	 * 接口：sysmanager/hirercfg/emailCheck
	 * @param hirerId
	 * @param userEmail
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.GET,value="emailCheck")
	public @ResponseBody Map<String,String> emailCheck(@RequestParam String hirerId,@RequestParam String userEmail , HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		Map<String, String> reg=new HashMap<String, String>();
		reg.put("result", "success");
		
		//1.判断是否等于租户管理员（及当前用户）的邮箱,不等于才和其他租户进行比较
				IpUser findUserByHirerIdAndUserType=userAccountService.findUserByHirerIdAndUserType(hirerId,"0");			
				if(!findUserByHirerIdAndUserType.getUserEmail().equals(userEmail)){
						//2.校验与其他租户是否重复，判断
					IpUser findUserByEmail=userAccountService.findUserByUserEmail(userEmail);
					if(findUserByEmail !=null){
						reg.put("result", "EM_two");
					   }
					}
				return reg;
		
	}
	
	
	
	
	public Map<String,String> putUserIntoCache(String hirerId){
			
			Map<String,String> cacheReg=new HashMap<String, String>();
			List<IpUser> findUserByHirerId = userAccountService.findUserByHirerId(hirerId);
			for (IpUser ipUser : findUserByHirerId) 
			{
			    String loginName = ipUser.getLoginName();
				String userEmail = ipUser.getUserEmail();
				String cellphoneNo = ipUser.getCellphoneNo();
				//sessionCacheManager_h.cacheUser(loginName, loginName);
				cacheReg.put(loginName, loginName);
				
				if(userEmail!=null){
					cacheReg.put(userEmail, userEmail);
				}
				
				if(cellphoneNo!=null){
					cacheReg.put(cellphoneNo, cellphoneNo);
				}
			}
			return cacheReg;
		}
	
	
	
	public Map<String,String> checkHirerAndUser(String info,Map<String,String> reg,Map<String,String> putUserIntoCache){
		if(info.equals(putUserIntoCache.get(info))){//和员工重名
			reg.put("result", "U_two");
			return reg;
		}
		return reg;
	}
	
	
	public Map<String,String> checkUserInfo(IpHirer findHirerByCellphoneNo,IpHirer findHirerByHirerId, Map<String,String> checkHirerAndUser){
		if(checkHirerAndUser.get("result").equals("U_two")){
			return checkHirerAndUser;
		}
      
		//租户校验
		if(findHirerByHirerId!=null){
		    if(findHirerByCellphoneNo!=null){
		    	if(findHirerByHirerId.getHirerId().equals(findHirerByCellphoneNo.getHirerId())){
		    		checkHirerAndUser.put("result", "current");
					  return checkHirerAndUser;
		    	}else{
		    		checkHirerAndUser.put("result", "H_two");
					return checkHirerAndUser;
		    	}
		    }
	 }else{
		 if(findHirerByCellphoneNo!=null){
			 checkHirerAndUser.put("result", "H_two");
				return checkHirerAndUser;
		 }
	 }
		return checkHirerAndUser;
	}
}
