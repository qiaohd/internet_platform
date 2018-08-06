package com.ufgov.ip.web.system;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ufgov.ip.api.base.IPDataTableServiceI;
import com.ufgov.ip.api.sysmanager.IPRoleServiceI;
import com.ufgov.ip.api.sysmanager.IpUserRoleServiceI;
import com.ufgov.ip.api.system.HirerAccountServiceI;
import com.ufgov.ip.api.system.IpMenuIconServiceI;
import com.ufgov.ip.api.system.MenuServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.entity.sysmanager.IpUserRole;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.entity.system.IpMenu;
import com.ufgov.ip.entity.system.IpMenuIcon;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.MenuTree;
import com.yonyou.iuap.iweb.entity.DataTable;
import com.yonyou.iuap.iweb.exception.WebRuntimeException;




@Component("menu.MenuController")
@Scope("prototype") 
@RequestMapping(value ="/menuShow")
public class MenuController {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	 @Autowired
	private MenuServiceI menuService;
	 @Autowired
	protected UserAccountServiceI userAccountService;
	 @Autowired
	IPDataTableServiceI iPDataTableService;
	
	DataTable<IpMenu> menuDataTable;
	
	 @Autowired
	protected HirerAccountServiceI hirerAccountService;
	 
	 @Autowired
	 protected IpMenuIconServiceI ipMenuIconService;
	public void setMenuService(MenuServiceI menuService) {
		this.menuService = menuService;
	}
	
	//菜单的查询
	@RequestMapping(method = RequestMethod.GET,value="menuList")
	public @ResponseBody List<IpMenu> mailFormLogin(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		try {		
			List<IpMenu> menuListsIpMenus= menuService.findMenuAll();
			List<IpMenu> menuList=new ArrayList<IpMenu>();
			searchMenu(menuListsIpMenus,menuList);
			return menuList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 新增部门初始化
	 * 			 查询参数需要前端配置：search_条件类型_参数字段  如search_EQ_hirerID
	 * 			 菜单查询按照菜单级别，菜单序号 顺序排列
	 */
	public void loadMenuTree(){
		try {
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams = iPDataTableService.createSearchParamsStartingWith("search_", menuDataTable);
			 //Map sort = iPDataTableService.buildSortMapRequest(Direction.ASC,"levelNum","dispOrder");
			 Sort sort2 = new Sort(Direction.ASC,"levelNum","dispOrder");
			List<IpMenu> categoryPage = menuService.getInitMenuTree(searchParams, sort2);
			IpMenu ipMenu =new IpMenu();
			ipMenu.setMenuId("00");
			ipMenu.setMenuName("无");
			ipMenu.setLevelNum("-1");
			
			categoryPage.add(0, ipMenu);
			menuDataTable.remove(menuDataTable.getAllRow());
			menuDataTable.set(categoryPage.toArray(new IpMenu[0]));			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebRuntimeException("查询数据失败!",e);
		}
	}
	/**
	 * 查询用户的菜单
	 * (过滤掉按钮资源级别的)
	 */
	public void loadMenuTreeNoIcon(){
		try {
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams = iPDataTableService.createSearchParamsStartingWith("search_", menuDataTable);
			 //Map sort = iPDataTableService.buildSortMapRequest(Direction.ASC,"levelNum","dispOrder");
			 Sort sort2 = new Sort(Direction.ASC,"dispOrder");
			 String userId=searchParams.get("EQ_userId").toString();
			 IpUser ipuser=userAccountService.findUserByUserId(userId);
			 List<IpMenu> menuList=null;
			 List <IpMenu>delList = new ArrayList<IpMenu>();
			 if("0".equals(ipuser.getUserType())){
				  menuList= menuService.getIpUserMngMenuList();	
				  for(IpMenu ipMenu:menuList){
						 String authLevel=ipMenu.getAuthLevel();
						 if("1".equals(authLevel)){
							 delList.add(ipMenu);
						 }
				  }
			 }else{			 
				  menuList= menuService.getIpMenuListByparentMenuId(userId);
				 for(IpMenu ipMenu:menuList){
					 String permission=ipMenu.getPermission();
					 if(!"".equals(permission)){
						 delList.add(ipMenu);
					 }
			 }
			}
			menuList.removeAll(delList);
			menuDataTable.remove(menuDataTable.getAllRow());
			menuDataTable.set(menuList.toArray(new IpMenu[0]));			
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebRuntimeException("查询数据失败!",e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST,value="show")
	public void showMenu(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		
		   String menuId = request.getParameter("menuId");
		   String isShow = request.getParameter("isShow");
		   menuService.updateMenuByMenuId(menuId, isShow);
		   //return "index";
		
	}
	
	//返回所有隐藏的一级菜单
	@RequestMapping(method = RequestMethod.GET,value="showhideMenu")
	public void showHideMenu(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {
		List<IpMenu> menuList_display = menuService.findMenuByLevelNumAndIsShow("1","0");
		if(menuList_display.size()==0){
			 response.getWriter().write("false");
		}
	}
	
	
	
	//菜单的新增
	@RequestMapping(value="create", method=RequestMethod.POST)  
    public @ResponseBody Map create(@RequestBody IpMenu entity, HttpServletRequest resq, HttpServletResponse response) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "success");
    	try {
    		Boolean checkIsBoolean=checkCreateMenu(entity, resultMap);
    		if(checkIsBoolean){
    			entity = menuService.saveEntity(entity);
    		}
		} catch (Exception e) {
			//记录日志
			logger.error("菜单新增出错!",e);
			getErrorMsg(resultMap,"服务端繁忙，请稍后重试！");
		}
        return resultMap;  
    }
	
	/**
	 * 验证
	 * @param entity
	 * @param resultMap
	 * @return
	 */
	private Boolean checkCreateMenu(IpMenu entity, Map<String, String> resultMap) {
		IpMenu menu= menuService.findMenuByMenuName(entity.getMenuName());
		if(menu!=null){
			  getErrorMsg(resultMap,"菜单名称已存在！");
			  return false;
		}
		if(null!=entity.getParentMenuId()&&!("").equals(entity.getParentMenuId())&&!("0").equals(entity.getParentMenuId())){
			IpMenu parentMenu =menuService.findByMenuId(entity.getParentMenuId());
			if(parentMenu==null){
				getErrorMsg(resultMap,"父菜单已不存在，请确认！");
				return false;
			}
		}
		return true;
		
	}

	/**
	 * 错误信息 
	 * @param resultMap
	 * @param reason
	 */
	private void getErrorMsg(Map<String, String> resultMap,String reason) {
		resultMap.put("result", "fail");
		resultMap.put("reason", reason);
	}  
	
	
	
 
	 
	//菜单更新
	 @RequestMapping(method = RequestMethod.POST,value = "update")
		public @ResponseBody IpMenu update(@RequestBody IpMenu entity) {
	    	try {
	    		
	    		//按名称查找菜单
	    		if(entity.getParentMenuName().equals("无")){
	    			entity.setParentMenuId("0");
	    		}else{
	    			IpMenu menu=menuService.findMenuByMenuName(entity.getParentMenuName());
	    			entity.setParentMenuId(menu.getMenuId());
	    		} 		
	    		entity = menuService.updateMenuEntity(entity);
			} catch (Exception e) {
				logger.error("菜单更新出错!",e);
			}
			return entity;
		}
	
	@RequestMapping(method = RequestMethod.GET,value = "delete")
	public @ResponseBody Map delete(HttpServletRequest request, HttpServletResponse response,Model model) {
	 String menuIdItems = request.getParameter("menuIds");//获得菜单id
	 String[] items = menuIdItems.split(",");
	 Map<String, String> resultMap = new HashMap<String, String>();
	 resultMap.put("result", "success");
	 resultMap.put("reason", "删除成功！");
	 try {
		 resultMap=menuService.deleteMenu(items,resultMap);
	} catch (Exception e) {
		// TODO 自动生成的 catch 块
		resultMap.put("result", "success");
		resultMap.put("reason", "服务器繁忙，请稍后重试！");
	}
		return resultMap;
	}
	 
	 //删除该菜单下的所有子菜单
	 public void deleteChile(List<IpMenu> childList){
		   for (IpMenu ipMenu : childList) {//遍历出所有的菜单
				 String menuId = ipMenu.getMenuId();
				 List<IpMenu> findByParentMenuId = menuService.findByParentMenuId(menuId);
				 if(findByParentMenuId.size()!=0){
					 deleteChile(findByParentMenuId);
					 menuService.deleteUser(menuId);
				 }else{
					 menuService.deleteUser(menuId);
				 }
			}
	   }

	 
	//菜单的查询
		/*@RequestMapping(method = RequestMethod.GET,value="selChildMenus")
		public @ResponseBody List<MenuTree> selChildMenus(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException{
			List<IpMenu> findByParentMenuId = menuService.findByParentMenuId(Long.valueOf(request.getParameter("menuId")));
		    List<MenuTree> menus=new ArrayList<MenuTree>();
			for (IpMenu ipMenu : findByParentMenuId) {
				MenuTree tree=new MenuTree();
				tree.setId(ipMenu.getMenuId());
				tree.setPid(ipMenu.getParentMenuId());
				tree.setName(ipMenu.getMenuName());
				tree.setUrl(ipMenu.getUrl());
				menus.add(tree);
			}
			return menus;
		}*/
	 
	 //begin_internet_platform_模糊查询的递归优化_zhangbch_20160421
	 @RequestMapping(method = RequestMethod.GET,value="likeMenuName")
		public @ResponseBody  List<IpMenu> likeMenuName_new(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException{
			
		 String menuName = request.getParameter("menuName");
		 if (StringUtils.isNotBlank(menuName)) {
			 menuName = URLDecoder.decode(menuName, "UTF-8");
		 }
		//List<List<IpMenu>> menuList=new ArrayList<List<IpMenu>>();
		List<IpMenu> menuList=new ArrayList<IpMenu>();
	    List<IpMenu> findMenuByMenuNameLike = menuService.findMenuByMenuNameLike("%"+menuName+"%");
		 
		  searchMenu(findMenuByMenuNameLike,menuList);
		  return menuList;
	 }
	 
	 public Integer searchMenu(List<IpMenu> findMenuByMenuNameLike,List<IpMenu> menuList){
		 
		boolean isExist=false;
		 if(findMenuByMenuNameLike.size()==0){
			 return null;
		 }else{
			 
			 for (IpMenu ipMenu : findMenuByMenuNameLike) {
				 isExist = isExistIpMenu(findMenuByMenuNameLike,isExist, ipMenu);
	    			if(isExist){
	    				isExist=false;
	    				continue;
	    			}
	    			menuList.add(ipMenu);
	    			List<IpMenu> findByParentMenuId = menuService.findByParentMenuId(ipMenu.getMenuId());//当前菜单下的子菜单（1）
	    			Integer flag=searchMenu(findByParentMenuId, menuList);
	    			
			 }
		 }
		return null;
	  }
	//end_internet_platform_模糊查询的递归优化_zhangbch_20160421
	 
		/**
		 * 判断是否有重复的实体存在list中
		 * @param findMenuByMenuNameLike
		 * @param isExist
		 * @param ipMenu
		 * @return
		 */
		private Boolean isExistIpMenu(List<IpMenu> findMenuByMenuNameLike,
				Boolean isExist, IpMenu ipMenu) {
			for (IpMenu ipMenuCheck : findMenuByMenuNameLike){
				if(ipMenu.getParentMenuId().equals(ipMenuCheck.getMenuId())){
					isExist=true;
				}
			}
			return isExist;
		}
		
		//查询当前菜单的子菜单
		@RequestMapping(method = RequestMethod.GET,value="selectChildMenu")
		public @ResponseBody List<MenuTree> selectChildMenu(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException{
			List<IpMenu> menuList=new ArrayList<IpMenu>();
			List<MenuTree> menus=new ArrayList<MenuTree>();
			String menuId_input = request.getParameter("input_notice");
			List<IpMenu> findByParentMenuId = menuService.findByParentMenuId(menuId_input);//当前菜单下的子菜单（2）
			 //menuList.addAll(findByParentMenuId);
			if(findByParentMenuId.size()!=0)
    			for (IpMenu ipMenu2 : findByParentMenuId) {//(2)
    				menuList.add(ipMenu2);
    				String menuId2 = ipMenu2.getMenuId();//菜单id
    				List<IpMenu> findByParentMenuId2 = menuService.findByParentMenuId(menuId2);//当前菜单下的子菜单（2）
    				if(findByParentMenuId2.size()!=0){
    					
    					for (IpMenu ipMenu3 : findByParentMenuId2) {
    						menuList.add(ipMenu3);
						}
    				}
				}
			
			for (IpMenu ipMenu : menuList) {
				MenuTree tree=new MenuTree();
				tree.setId(ipMenu.getMenuId());
				tree.setName(ipMenu.getMenuName());
				tree.setPId(ipMenu.getParentMenuId());
				tree.setUrl(ipMenu.getUrl());
				menus.add(tree);
			}
			return menus;
		}
		
		
		
	/**
	 * 用户进入系统菜单初始化 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value="/userMenu")
	public @ResponseBody Map<String, Object> initUserMenu(HttpServletRequest request, HttpServletResponse response){
		logger.info("读取权限开始!");
		String cuser = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (SecurityUtils.getSubject().getPrincipal() != null)
			cuser = (String) SecurityUtils.getSubject().getPrincipal();
		if (cuser == null) {
			return null;
		}
		try {
			// IpHirer ipHirer = hirerAccountService.findHirerByEmailOrLoginNameOrCellphoneNo(cuser);//租户
			IpUser ipUser=userAccountService.findUserByUserEmailOrLoginNameOrCellphoneNo(cuser); //普通用户
			cuser=ipUser.getUserName();
			 if(ipUser!=null){
				IpHirer ipHirerByUser = hirerAccountService.findHirerByHirerId(ipUser.getHirerId());
				if("0".equals(ipUser.getUserType())){ // 租户 
					List<IpMenu> menuList= menuService.getIpMenuListByHirer(ipHirerByUser.getHirerId());						
					String hirerLogoUrl = ipHirerByUser.getHirerLogoUrl();
			    	String hirerCompanyName = ipHirerByUser.getHirerName();
			    	String headerLogoUrl=ipUser.getUserPicUrl();
			    	String hirerId = ipHirerByUser.getHirerId();
			    	String loginname = ipHirerByUser.getLoginName();
			    	resultMap.put("hirerPicUrl",headerLogoUrl);
			    	resultMap.put("hirerId",hirerId);
			    	resultMap.put("hirerLogoUrl",hirerLogoUrl);
			    	resultMap.put("hirerCompanyName", hirerCompanyName);
			    	resultMap.put("cusername", cuser);
			    	resultMap.put("menuList", menuList);
			    	resultMap.put("loginname", loginname);
			    	
				}else{ // 用户
				   List<IpMenu> menuList= menuService.getIpMenuListByUser(ipUser.getUserId());			   
				   String hirerLogoUrl = ipHirerByUser.getHirerLogoUrl();
				   String hirerCompanyName = ipHirerByUser.getHirerName();
				   String hirerId = ipHirerByUser.getHirerId();
			       String headerLogoUrl=ipUser.getUserPicUrl();
			       String loginname=ipUser.getLoginName();
			       resultMap.put("hirerPicUrl",headerLogoUrl);
				   resultMap.put("hirerId",hirerId);
				   resultMap.put("hirerLogoUrl",hirerLogoUrl);
				   resultMap.put("hirerCompanyName", hirerCompanyName);
				   resultMap.put("cusername", cuser);
				   resultMap.put("menuList", menuList);
			       resultMap.put("loginname", loginname);
			   }
			}
			 resultMap.put("uesrId", ipUser.getUserId());
			
		} catch (Exception e) {
			logger.error("获取用户权限失败!",e);
		}
		logger.info("读取权限完成!");
		
		return resultMap;
	}
	
	
	/**
	 * 用户进入系统菜单初始化 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value="/userHideMenu")
	public @ResponseBody Map<String, Object> initUserHideMenu(HttpServletRequest request, HttpServletResponse response){
		logger.info("读取权限开始!");
		String cuser = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (SecurityUtils.getSubject().getPrincipal() != null)
			cuser = (String) SecurityUtils.getSubject().getPrincipal();
		if (cuser == null) {
			return null;
		}
		try {
			IpHirer ipHirer = hirerAccountService.findHirerByEmailOrLoginNameOrCellphoneNo(cuser);//租户
			if(ipHirer!=null){
				List<IpMenu> menuList= menuService.getIpHideMenuListByHirer(ipHirer.getHirerId());
		    	resultMap.put("menuHideList", menuList);
			}else{
			   IpUser ipUser=userAccountService.findUserByUserEmailOrLoginNameOrCellphoneNo(cuser); //普通用户
			   if(ipUser!=null){
				   List<IpMenu> menuList= menuService.getIpHideMenuListByUser(ipUser.getUserId());
				   resultMap.put("menuHideList", menuList);
			   }
			}
			
		} catch (Exception e) {
			logger.error("获取用户权限失败!",e);
		}
		logger.info("读取权限完成!");
		
		return resultMap;
	}
	
	
	
	
	/**
	 * 通过父菜单Id获取子菜单 （点击一级菜单请求子菜单）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value="/getChildMenus")
	public  @ResponseBody IpMenu getChildMenus(HttpServletRequest request, HttpServletResponse response){
		String cuser = null;
		List<IpMenu> ipMenus=new ArrayList<IpMenu>(); 
		if (SecurityUtils.getSubject().getPrincipal() != null)
			cuser = (String) SecurityUtils.getSubject().getPrincipal();
		if (cuser == null) {
			return null;
		}
		String parentMenuId = request.getParameter("menuPId");
		IpHirer ipHirer = hirerAccountService.findHirerByEmailOrLoginNameOrCellphoneNo(cuser);//租户
		// 得到父节点的菜单信息 
		IpMenu ipMenu = menuService.findByMenuId(parentMenuId);
		if(ipMenu==null){
			return null;
		}
		if(ipHirer!=null){
			ipMenus= menuService.findMenuAll();				
		}else{
			 IpUser ipUser=userAccountService.findUserByUserEmailOrLoginNameOrCellphoneNo(cuser); //普通用户
			   if(ipUser!=null){
				   ipMenus= menuService.getIpMenuListByparentMenuId(ipUser.getUserId()); 
			   }
		}
		initMenuTree(ipMenu, ipMenus);
		return ipMenu;
		
	}
	/**
	 * 菜单构建为树形结构 
	 * @param root
	 * @param allMenus
	 */
	 private void initMenuTree(IpMenu root, List<IpMenu> allMenus) {
	    List childList = getMenusByParent(root, allMenus);
	    root.setChildren(childList);
	    for (int i = 0; i < childList.size(); i++)
	    	initMenuTree((IpMenu)childList.get(i), allMenus);
	  }
		 
	 /**
	  * 通过父节点查找子节点集合 
	  * @param pMenu
	  * @param allMenus
	  * @return
	  */
	 private List<IpMenu> getMenusByParent(IpMenu pMenu, List<IpMenu> allMenus)
	  {
	    List childrenList = new ArrayList();
	    for (int i = 0; i < allMenus.size(); i++) {
	      if (pMenu.getMenuId().equals(((IpMenu)allMenus.get(i)).getParentMenuId())) {
	        childrenList.add(allMenus.get(i));
	      }
	    }
	    return childrenList;
	  }
		 
		 
 	/**
 	 * 显示菜单
 	 * @param request
 	 * @param response
 	 * @throws IOException
 	 */
	@RequestMapping(method = RequestMethod.POST,value="showMenu")
	public void showMenu(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String cuser = null;
		boolean isShow=true;
		try {
			String menuId = request.getParameter("menuId");
			if (SecurityUtils.getSubject().getPrincipal() != null)
				cuser = (String) SecurityUtils.getSubject().getPrincipal();
			if (cuser == null) {
				response.getWriter().write("false");
			}else{
				saveUserMenu(menuId, cuser, isShow);
				response.getWriter().write("true");
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			response.getWriter().write("false");
		}
		
		
	}

	
	/**
	 * 隐藏菜单 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST,value="hideMenu")
	public void hideMenu(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String cuser = null;
		boolean isShow=false;
		try {
			String menuId = request.getParameter("menuId");
			if (SecurityUtils.getSubject().getPrincipal() != null)
				cuser = (String) SecurityUtils.getSubject().getPrincipal();
			if (cuser == null) {
				response.getWriter().write("false");
			}else{
				saveUserMenu(menuId, cuser, isShow);
				response.getWriter().write("true");
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			response.getWriter().write("false");
		}
	}
	/**
	 * 保存ip_user_hide_menu 
	 * @param menuId
	 * @param cuser
	 * @param isShow 是否显示的表示：
	 * 					true：显示隐藏表（ip_user_hide_menu）中删除记录
	 * 					false：显示隐藏表（ip_user_hide_menu）中增加记录
	 * @throws Exception
	 */
	public void saveUserMenu(String menuId,String cuser, boolean isShow)
			throws Exception {
		String userId="";
		try {
			IpHirer ipHirer = hirerAccountService.findHirerByEmailOrLoginNameOrCellphoneNo(cuser);//租户
			if(ipHirer!=null){
				userId=ipHirer.getHirerId();
			}else{
				 IpUser ipUser=userAccountService.findUserByUserEmailOrLoginNameOrCellphoneNo(cuser); //普通用户
				   if(ipUser!=null){
					   userId=ipUser.getUserId();
				   }
			}
			if(isShow){
				menuService.deleteUserMenu(userId,menuId);
				
			}else{
				menuService.saveUserMenu(userId,menuId);
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			throw e;
		}
	}
	/**
	 * 保存菜单按钮图标信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("finally")
	@RequestMapping(method = RequestMethod.POST,value="saveMenuIcon")
	public @ResponseBody Map<String,Object> saveMenuIcon(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String,Object> map=new HashMap<String,Object>();
		try{
		    String menuId = request.getParameter("menuId");
		    String menuName = request.getParameter("menuName");
		    String userId = request.getParameter("userId");
		    String iconId = request.getParameter("iconId");
		    String iconPath = request.getParameter("iconPath");
		    IpMenuIcon ipMenuIcon =new IpMenuIcon();
		    String url=request.getParameter("url");
		    ipMenuIcon.setUrl(url);
			ipMenuIcon.setMenu_id(menuId);
			ipMenuIcon.setMenu_name(menuName);
			ipMenuIcon.setUser_id(userId);
			ipMenuIcon.setIcon_id(iconId);
			ipMenuIcon.setIcon_path(iconPath);
		 IpMenuIcon ifIpMenuIcon =ipMenuIconService.selectByPk(ipMenuIcon);
			if(ifIpMenuIcon ==null){				
				ipMenuIconService.insert(ipMenuIcon);
			}else{
				ipMenuIconService.update(ipMenuIcon);
			}
			map.put("result","success");
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			map.put("result","fail");
		}finally{			
			return map;	
		}
	}
	
	@SuppressWarnings("finally")
	@RequestMapping(method = RequestMethod.POST,value="loadMenuIcon")
	public @ResponseBody Map<String,Object> loadMenuIcon(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map=new HashMap<String,Object>();
		try {		
		 String userId = request.getParameter("userId");
		 IpMenuIcon ipMenuIcon=new IpMenuIcon();
		 ipMenuIcon.setUser_id(userId);
		 List<IpMenuIcon> ipMenuIconList=ipMenuIconService.findMenuInfo(ipMenuIcon);
		 map.put("dataList",ipMenuIconList);
		 map.put("result", "success");		
		} catch (Exception e) {
			e.printStackTrace();
		    map.put("result", "fail");
		}finally{
			return map;
		}
	}
	
	@SuppressWarnings("finally")
	@RequestMapping(method = RequestMethod.POST,value="delMenuIcon")
	public @ResponseBody Map<String,Object> delMenuIcon(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map=new HashMap<String,Object>();
		try {		
		 String userId = request.getParameter("userId");
		 String menuId = request.getParameter("menuId");
		 IpMenuIcon ipMenuIcon=new IpMenuIcon();
		 ipMenuIcon.setUser_id(userId);
		 ipMenuIcon.setMenu_id(menuId);
		 ipMenuIconService.delByPk(ipMenuIcon);
		 map.put("result", "success");		
		} catch (Exception e) {
			e.printStackTrace();
		    map.put("result", "fail");
		}finally{
			return map;
		}
	}
	
	
	
	/**
	 * 获得所有的一级菜单
	 * 接口：menuShow/getAllBaseLevelMenu
	 * 参数：无参数
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET,value="getAllBaseLevelMenu")
	public @ResponseBody Map<String,Object> getAllBaseLevelMenu(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> reg=new HashMap<String, Object>();
		List<IpMenu> menuList_display = menuService.findMenuByLevelNum("1");
		reg.put("firstMenuList", menuList_display);
		return reg;
	}
	
}
	 
	 
	 
	 

