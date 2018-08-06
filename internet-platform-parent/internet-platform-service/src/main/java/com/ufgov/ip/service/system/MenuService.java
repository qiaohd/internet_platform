package com.ufgov.ip.service.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.base.IPDataTableServiceI;
import com.ufgov.ip.api.system.MenuServiceI;
import com.ufgov.ip.dao.sysmanager.IPRoleMenuMapper;
import com.ufgov.ip.dao.system.IMenuMapper;
import com.ufgov.ip.dao.systemimpl.IMenuJdbcDaoImpl;
import com.ufgov.ip.entity.sysmanager.IpRoleMenu;
import com.ufgov.ip.entity.system.IpMenu;
import com.ufgov.ip.serviceutils.QueryCombineUtil;
import com.ufgov.ip.utils.UUIDTools;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Service(version = "0.0.1")
public class MenuService implements MenuServiceI{

	
	@Autowired
//	private IMenuDao iMenuDao;
	private IMenuMapper iMenuMapper;
	@Autowired
//	private IPRoleMenuDao iPRoleMenuDao;
	private IPRoleMenuMapper iPRoleMenuMapper;
	@Autowired
	private IMenuJdbcDaoImpl iMenuJdbcDao;
	@Autowired 
	IPDataTableServiceI iPDataTableService;

	public void setiMenuMapper(IMenuMapper iMenuMapper) {
		this.iMenuMapper = iMenuMapper;
	}
	
	
	public List<IpMenu> findAll(){
		return iMenuMapper.findAll();
	}
	
	//查询所有的一级菜单
	public List<IpMenu> findMenuByLevelNum(String levelNum){
		return iMenuMapper.findMenuByLevelNum(levelNum);
		
	}
	
	//查询所有的一级并且需要显示的菜单
		public List<IpMenu> findMenuByLevelNumAndIsShow(String levelNum,String isShow){
			return iMenuMapper.findMenuByLevelNumAndIsShow(levelNum,isShow);
			
		}
		
	 //更新菜单的显示
		public void updateMenuByMenuId(String menuId,String isShow){
			
			HashMap<String,String> map = new HashMap<String, String>();
			map.put(menuId, isShow);
			iMenuMapper.updateMenuByMenuId(map);
			
		}
	
		
		
		//菜单的新增
		@Transactional
		public IpMenu saveEntity(IpMenu entity) throws Exception{
			
			 if(null == entity.getMenuId()||("").equals(entity.getMenuId())){
				entity.setMenuId(UUIDTools.uuidRandom());
				if("".equals(entity.getParentMenuName())){
					entity.setParentMenuName("无");
				}
				if(null==entity.getParentMenuId()){
					entity.setParentMenuId("0");
				}
			}else{
			 if(!entity.getParentMenuName().equals("无")){
				 if(null == entity.getParentMenuId()){
					 String parentMenuName = entity.getParentMenuName();
					 IpMenu ipMenu=iMenuMapper.findMenuByMenuName(parentMenuName);
					 String menuId = ipMenu.getMenuId();
					 entity.setParentMenuId(menuId);
				 }
			 }
			}
			 //先保存,再根据ID值进行查询
//			return iMenuMapper.save(entity);
			 
			 //现将原有的entity删除掉,再进行插入
			iMenuMapper.delete(entity.getMenuId());
			iMenuMapper.save(entity);
			return iMenuMapper.findByMenuId(entity.getMenuId());
		}
	
	
		//删除菜单
		public void deleteUser(String id) {
			iMenuMapper.delete(id);
		}


		//查询子级菜单
		public List<IpMenu> findByParentMenuId(String parentMenuId) {
			return iMenuMapper.findByParentMenuId(parentMenuId);
			
			
		}


		public IpMenu findMenuByMenuName(String menuName) {

			return iMenuMapper.findByMenuName(menuName);
		}

        //模糊查询
		public List<IpMenu> findMenuByMenuNameLike(String menuName) {
			return iMenuMapper.findMenuByMenuNameLike(menuName);
			
		}

		/**
		 * 通过排序disp_order 查询菜单集合
		 */
		public List<IpMenu> findMenuAll() {
			return iMenuMapper.findMenuAll();
		}


		public IpMenu findByMenuId(String menuId) {
			return iMenuMapper.findByMenuId(menuId);
			
		}
		
		@Transactional(rollbackFor=Exception.class)
		public IpMenu  updateMenuEntity(IpMenu entity) throws Exception {
			
			try {
				String MenuId=entity.getMenuId();
				String parentMenuName=entity.getMenuName();
				// 更新保存当前菜单
				entity = saveEntity(entity);
				// 更新子菜单的父节点名称
				updateMenuByparentMenuId(MenuId,parentMenuName);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				throw new Exception("更新菜单失败",e);
			}
			
			return entity;
			
		}
		
		/**
		 * 删除菜单
		 * @param items
		 * @param resultMap
		 * @throws Exception 
		 * 0908修改 删除菜单提示传回给调用方法
		 */
		public Map<String, String> deleteMenu(String[] items, Map<String, String> resultMap) throws Exception {
			try {
				for (String item : items) {
					 List<IpMenu> childList = findByParentMenuId(item);//该菜单下的所有子菜单
					 if(childList.size()!=0){
						 resultMap.put("result", "fail");
						 resultMap.put("reason", "所选菜单包含子菜单，请逐级删除！");
						 return resultMap;
					 }
					 List<IpRoleMenu> ipRoleMenus =iPRoleMenuMapper.findByMenuId(item);
					 if(ipRoleMenus.size()>0){
						 resultMap.put("result", "fail");
						 resultMap.put("reason", "所选菜单已赋予职务权限，请确认！");
						 return resultMap;
					 }
				}
				deleteMenu(items);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				throw e;
			}
			return resultMap;
		}
		
		@Transactional(rollbackFor=Exception.class)
		public void deleteMenu(String[] items) throws Exception {
			// TODO 自动生成的方法存根
			try {
				for (String item : items) {
					iMenuMapper.delete(item);
				}
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				throw e;
			}
		}


		/**
		 * 通过父节点ID 更新父节点名称
		 * @param parentMenuId
		 * @param parentMenuName
		 */
		public void updateMenuByparentMenuId(String parentMenuId,String parentMenuName){
			
			HashMap<String,String> map = new HashMap<String, String>();
			map.put("parentMenuId", parentMenuId);
			map.put("parentMenuName", parentMenuName);
			iMenuMapper.updateMenuByparentMenuId(map);
			
		}
		
		
		public List<IpMenu> getIpMenuListByUser(String userId) {
			List<IpMenu> ipMenus =iMenuJdbcDao.getIpMenuListByUser(userId);
			return ipMenus;			
		}
		
		public List<IpMenu> getIpMenuListByHirer(String hirerId) {
			List<IpMenu> ipMenus=iMenuJdbcDao.getIpMenuListByHirer(hirerId);
			return ipMenus;
			
		}
		
		public List<IpMenu> getIpHideMenuListByUser(String userId) {
			List<IpMenu> ipMenus =iMenuJdbcDao.getIpHideMenuListByUser(userId);
			return ipMenus;			
		}
		
		public List<IpMenu> getIpHideMenuListByHirer(String hirerId) {
			List<IpMenu> ipMenus=iMenuJdbcDao.getIpHideMenuListByHirer(hirerId);
			return ipMenus;
			
		}


		public List<IpMenu> getIpMenuListByparentMenuId(String userId) {
			// TODO 自动生成的方法存根
			List<IpMenu> ipMenus =new ArrayList<IpMenu>();
			
			ipMenus=iMenuJdbcDao.getMenuByParentId(userId);
			
			return ipMenus;
		}

		/**
		 * 将菜单从ip_user_hide_menu 移除
		 * @param userId
		 * @param menuId
		 */
		public void deleteUserMenu(String userId, String menuId) {
			// TODO 自动生成的方法存根
			iMenuJdbcDao.deleteUserMenu(userId,menuId);
		}
		
		public void saveUserMenu(String userId, String menuId) {
			// TODO 自动生成的方法存根
			iMenuJdbcDao.saveUserMenu(userId,menuId);
		}
		/**
		 * 菜单查询
		 * @param searchParams  条件参数
		 * @param sort  排序参数 
		 * @return
		 */
		public List<IpMenu> getInitMenuTree(Map<String, Object> searchParams,
				Sort sort) {			
			// 只取一级或者二级或者菜单
			return iMenuMapper.findMenuForLevelNum( "1","2","3");
			
			
		}


		@Override
		public List<IpMenu> getUserMenuTree(Map<String, Object> searchParams,
				Sort sort) {
			// TODO 自动生成的方法存根
			String conditionSql=QueryCombineUtil.QueryConditionCombine(searchParams);
			return iMenuMapper.findMenuByConditionSql(conditionSql);
		}


		@Override
		public List<IpMenu> getIpUserMngMenuList() {
			// TODO 自动生成的方法存根
			return iMenuMapper.findIpUserMngMenuList();
		}
			
	
	
}