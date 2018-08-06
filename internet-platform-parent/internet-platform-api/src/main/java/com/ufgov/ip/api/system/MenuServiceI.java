package com.ufgov.ip.api.system;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import com.ufgov.ip.entity.system.IpMenu;

public interface MenuServiceI {
	   public List<IpMenu> findAll();
	   public List<IpMenu> findMenuByLevelNum(String levelNum);
	   public List<IpMenu> findMenuByLevelNumAndIsShow(String levelNum,String isShow);
	   public void updateMenuByMenuId(String menuId,String isShow);
		public IpMenu saveEntity(IpMenu entity) throws Exception;
		public void deleteUser(String id);
		public List<IpMenu> findByParentMenuId(String parentMenuId);
		public IpMenu findMenuByMenuName(String menuName);
		public List<IpMenu> findMenuByMenuNameLike(String menuName);

		/**
		 * 通过排序disp_order 查询菜单集合
		 */
		public List<IpMenu> findMenuAll();


		public IpMenu findByMenuId(String menuId);
		
		public IpMenu  updateMenuEntity(IpMenu entity) throws Exception;
		
		/**
		 * 删除菜单
		 * @param items
		 * @param resultMap
		 * @throws Exception 
		 */
		public Map<String, String> deleteMenu(String[] items, Map<String, String> resultMap) throws Exception;
		public void deleteMenu(String[] items) throws Exception;


		/**
		 * 通过父节点ID 更新父节点名称
		 * @param parentMenuId
		 * @param parentMenuName
		 */
		public void updateMenuByparentMenuId(String parentMenuId,String parentMenuName);
		
		
		public List<IpMenu> getIpMenuListByUser(String userId);
		
		public List<IpMenu> getIpMenuListByHirer(String hirerId);
		
		public List<IpMenu> getIpHideMenuListByUser(String userId);
		
		public List<IpMenu> getIpHideMenuListByHirer(String hirerId);


		public List<IpMenu> getIpMenuListByparentMenuId(String userId);

		/**
		 * 将菜单从ip_user_hide_menu 移除
		 * @param userId
		 * @param menuId
		 */
		public void deleteUserMenu(String userId, String menuId);
		
		public void saveUserMenu(String userId, String menuId);
		/**
		 * 菜单查询
		 * @param searchParams  条件参数
		 * @param sort  排序参数 
		 * @return
		 */
		public List<IpMenu> getInitMenuTree(Map<String, Object> searchParams,
				Sort sort);	
		/**
		 * 查询菜单
		 * @param searchParams
		 * @param sort
		 * @return
		 */
		public List<IpMenu> getUserMenuTree(Map<String, Object> searchParams,
				Sort sort);	
		/**
		 * 获取租户管理元的所有菜单
		 */
		public List<IpMenu> getIpUserMngMenuList();
}
