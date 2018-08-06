package com.ufgov.ip.web.system;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ufgov.ip.api.system.HirerAccountServiceI;
import com.ufgov.ip.api.system.IndexConfigI;
import com.ufgov.ip.api.system.MenuServiceI;
import com.ufgov.ip.api.system.SolrServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.entity.base.SolrEntity;
import com.ufgov.ip.entity.system.IndexConfigEntity;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.entity.system.IpMenu;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.Page;

@Controller
@RequestMapping(value = "search")
public class SolrSearchController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	 @Autowired
	   private SolrServiceI SolrService;
	
	 @Autowired
		protected UserAccountServiceI userAccountService;
	 
	 @Autowired
		protected HirerAccountServiceI hirerAccountService;
	 
	 @Autowired
		private MenuServiceI menuService;
	 
	 @Autowired
		private IndexConfigI indexConfig;
	 
	 
	 
	 
	/**
	 * 索引数据信息
	 * @param ipMenu
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value = "solrSearch")
	public @ResponseBody Map<String,Object> solrSearch(@RequestParam String index_catalog,
			                                           @RequestParam String keywords,
			                                           @RequestParam String searchDate,
			                                           @RequestParam String searchResult,
			                                           @RequestParam String pageNo,
			                                           HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String,Object> reg=new HashMap<String,Object>();
		reg.put("result", "true");
		IpUser curUserId = getCurUserId();
		IpHirer ipHirerByUser=null;
		 if(curUserId!=null){
			 ipHirerByUser = hirerAccountService.findHirerByHirerId(curUserId.getHirerId());
		}
		
		//根据索引条件,分页查询所有的记录
		                                   Map<String, Object> findSolrResult = SolrService.findSolrResult(index_catalog,
				                                                      keywords,
				                                                      searchDate,
				                                                      searchResult,
				                                                      pageNo,
				                                                      curUserId.getUserId(),
				                                                      ipHirerByUser.getHirerId());
		 
		 if(findSolrResult.get("indexList")==null){
			 reg.put("result", "false");
			 reg.put("reason", "无此记录");
		 }else{
			//获得当前条件下的所有记录数
			 Integer totalCount=(Integer) findSolrResult.get("totalElements");
			 Page page = new Page();
			 List<SolrEntity> resultList = (List<SolrEntity>) findSolrResult.get("indexList");
			 if(resultList.size()<10){
				 page.setTotalCount(resultList.size());
				 reg.put("totalCount", resultList.size());
			 }else{
				 page.setTotalCount(totalCount);
				 reg.put("totalCount", totalCount);
			 }
			 
			 page.setPageNo(Integer.valueOf(pageNo));
			
			 
			 reg.put("currentPage", page.getPageNo());
			 reg.put("pageSize", page.getPageSize());
			 reg.put("totalPages", page.getTotalPage());
			
			 reg.put("solrMenu", findSolrResult.get("indexList"));
		 }
		 return reg;
	}
	
	/**
	 * 获得右侧的分类区数据：结果类型、应用选择、更新时间
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value = "loadCategoryInfo")
	public @ResponseBody Map<String,Object> loadCategoryInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
		//获得应用选择数据
			String cuser = null;
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (SecurityUtils.getSubject().getPrincipal() != null)
				cuser = (String) SecurityUtils.getSubject().getPrincipal();
			if (cuser == null) {
				return null;
			}
			
			try {
				IpUser ipUser=userAccountService.findUserByUserEmailOrLoginNameOrCellphoneNo(cuser); //普通用户
				 if(ipUser!=null){
					IpHirer ipHirerByUser = hirerAccountService.findHirerByHirerId(ipUser.getHirerId());
					if("0".equals(ipUser.getUserType())){ // 租户 
						List<IpMenu> menuList= menuService.getIpMenuListByHirer(ipHirerByUser.getHirerId());
						 List<IpMenu> finallMenuList = getMenuCatalogInfo(menuList);
				    	 resultMap.put("menuList", finallMenuList);
					}else{ // 用户
					   List<IpMenu> menuList= menuService.getIpMenuListByUser(ipUser.getUserId());
					   List<IpMenu> finallMenuList = getMenuCatalogInfo(menuList);
					   resultMap.put("menuList", finallMenuList);
				   }
				}
				
			} catch (Exception e) {
				logger.error("获取用户权限失败!",e);
			}
		
		//获得结果类型
			//获取更新时间
			   //获得当前时间
			    String nowTime = dateTransforToString(new Date());
			  //近两周
				String twoWeeks = this.getUpdateTime(Calendar.DATE, 14);
	          //三个月内
				String threeMonth = this.getUpdateTime(Calendar.MONTH, 3);
		      //今年
				String thisYear = this.getUpdateTime(Calendar.YEAR, 0);
			  //去年以前
				String lastYear = this.getUpdateTime(Calendar.YEAR, 1);
				resultMap.put("twoWeeks", twoWeeks);
				resultMap.put("threeMonth", threeMonth);
				resultMap.put("thisYear", thisYear);
				resultMap.put("lastYear", lastYear);
				resultMap.put("nowTime", nowTime);
		 //获得结果类型
				resultMap.put("searchFile", "仅搜文件");
				resultMap.put("searchContent", "仅搜内容");
	        
		return resultMap;
	}

	private List<IpMenu> getMenuCatalogInfo(List<IpMenu> menuList) {
		//动态筛选权限菜单分类
		   List<IndexConfigEntity> allIndexConfigByIsUse = indexConfig.getAllIndexConfigByIsUse("Y");
		   List<IpMenu> finallMenuList=new ArrayList<IpMenu>();
		   if(menuList.size()!=0 && allIndexConfigByIsUse.size()!=0){
			   for (IpMenu menu : menuList) {
		    	   for (IndexConfigEntity indexConfig : allIndexConfigByIsUse){
		    		   if(menu.getMenuId().equals(indexConfig.getMenuId())){
		    			   finallMenuList.add(menu);
		    		   }
		    	   }
			   }		 
		   }
		return finallMenuList;
	}
	
	
	public String getUpdateTime(Integer dateType,Integer count){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(dateType!=Calendar.YEAR){
	        Calendar c = Calendar.getInstance();  
	        c.add(dateType, - count);  
	        Date dateTime = c.getTime();
	        String updateTime = sdf.format(dateTime);
			return updateTime;
		}else{
			Calendar currCal=Calendar.getInstance();  
	        int currentYear = currCal.get(dateType);
	        currCal.clear();
	        currCal.set(Calendar.YEAR, currentYear-count);
	        Date currYearFirst = currCal.getTime();
	        String updateTime = sdf.format(currYearFirst);
	        return updateTime;
		}
		
	}
	
	
	public String dateTransforToString(Date now){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTimeToString = sdf.format(now);
		return nowTimeToString;
	}
	
	private IpUser getCurUserId() {
		String cuser = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
			cuser = (String) SecurityUtils.getSubject().getPrincipal();
			IpUser ipUser=userAccountService.findUserByUserEmailOrLoginNameOrCellphoneNo(cuser); //普通用户
			
		return ipUser;
	}
	
	
}
