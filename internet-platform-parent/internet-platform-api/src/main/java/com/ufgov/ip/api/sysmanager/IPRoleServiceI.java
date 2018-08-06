package com.ufgov.ip.api.sysmanager;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import yonyou.bpm.rest.exception.RestException;

import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.sysmanager.IpRole;
import com.ufgov.ip.entity.sysmanager.IpRoleMenu;
import com.ufgov.ip.entity.sysmanager.IpUserAndCompanyEntity;
import com.ufgov.ip.entity.sysmanager.IpUserRole;
import com.ufgov.ip.entity.system.IpMenu;

public interface IPRoleServiceI {	
	// 通过角色名称获取角色 
	public IpRole findIpRoleByName(String roleName,String hirerId);
	
	public List<IpRole> getRoleInfoByHirerId(String hirerId);
	
	/**
	 * 菜单查询
	 * @param searchParams  条件参数
	 * @param sort  排序参数 
	 * @return
	 */
	public List<IpMenu> getInitMenu(Map<String, Object> searchParams, Sort sort);
	
	/**
	 * 初始化角色-菜单
	 * @param searchParams
	 * @return
	 */
	public List<IpMenu> getInitPermissionMenu(Map<String, Object> searchParams);
	/**
	 * 编辑权限时，初始化已选择的角色
	 * @param searchParams
	 * @return
	 */
	public List<String> getEditPermissionMenu(Map<String, Object> searchParams);
	/**
	 * 
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	public List<IpUserAndCompanyEntity> initCompanyDataTable(Map<String, Object> searchParams, Sort sort);
	
	/**
	 * 部门人员树 
	 * 		setIsUser 1：表示人员   0：表示部门
	 * 		人员的父节点为其部门ID
	 * @param ipCompanies
	 * @return
	 */
	public List<IpUserAndCompanyEntity> buildUserAndCompany(
			List<IpCompany> ipCompanies);

	/**
	 * 组装用户数据，设置用户的父节点为其部门
	 * @param upList
	 * @param userList
	 * @param coId
	 */
	public void buildipUserAndCompanyEntityList(List<IpUserAndCompanyEntity> upList,List userList,String coId);
	
	/**
	 * 权限添加人员时，已添加人员的LIST
	 * @param searchParams
	 * @return
	 */
	public List<String> getUserList(Map<String, Object> searchParams);

	/**
	 * 通过jdbc查询的结果构建 list<实体>
	 * @param ipRoleMenuList
	 * @return
	 */
	public List<IpMenu> buildIpMenuEntityList(List ipRoleMenuList);
	
	/**
	 * 取roleID 
	 * @param searchParams
	 * @return
	 */
	public String getRoleIdBySearchParams(Map<String, Object> searchParams);

	/**
	 * 职务添加
	 * @param roleMenu
	 * @param ipMenuList
	 */
	@Transactional
	public void saveRoleAndRoleMenu(IpRole ipRoleEntity, List<IpMenu> ipMenuList,Map<String, Object> resultMap);
	
	/**
	 * ipRoleMenu 数据LIST 
	 * @param ipRoleEntity
	 * @param ipMenuList
	 * @return
	 */
	public List<IpRoleMenu> getIpRoleMenuList(IpRole ipRoleEntity,
			List<IpMenu> ipMenuList);

	/**
	 * 验证职务名称是否存在 
	 * @param entity
	 * @param resultMap
	 * @return
	 */
	public Boolean checkIpRoleName(IpRole ipRoleEntity, Map<String, Object> json);
	
	/**
	 * 删除职务权限 
	 * @param roleId
	 * @param resultMap
	 */
	public Map<String,String> delPermission(String roleId) throws RestException;
	
	/**
	 * 判断职务权限是否可以删除 
	 * @param roleId
	 * @param resultMap
	 * @return
	 */
	public Boolean checkIpUserRole(String roleId);
	
   public List<IpRole> findAllRole();

/**
 * 保存权限人员信息 
 * @param ipRoleByreq
 * @param ipUserCompanyEntityList
 * @param json
 * @throws Exception
 */
@Transactional
public void saveRoleUser(IpRole ipRoleByreq,
		List<IpUserAndCompanyEntity> ipUserCompanyEntityList,List userIdForSaveList) throws Exception;

/**
 * 职务权限下的人员信息 
 * @param searchParams
 * @param pageRequest
 * @return
 */
public Page<IpUserRole> getUserPage(Map<String, Object> searchParams,
		PageRequest pageRequest);
public List<IpUserRole> bulidUserRoleAndOrder(String roleId);



public List<IpUserRole> buildIpUserRoleEntityList(List userRoleList);

public IpRole findIpRoleByRoleId(String roleId);

}
