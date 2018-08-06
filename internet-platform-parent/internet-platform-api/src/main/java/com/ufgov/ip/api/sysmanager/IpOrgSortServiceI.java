package com.ufgov.ip.api.sysmanager;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import com.ufgov.ip.entity.sysmanager.IpRole;
import com.ufgov.ip.entity.sysmanager.IpUserRole;


public interface IpOrgSortServiceI {
	public void updateOrgSort(String[] ids, String coPId);
	
	public List getListByArray(String[] ids);

	/**
	 * 组装职务权限树 租户信息作为父节点 
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	public List<IpRole> getRolePage(Map<String, Object> searchParams, Sort sort);
	
	/**
	 * 取hirerId
	 * @param searchParams
	 * @return
	 */
	public String getHirerIdBySearchParams(Map<String, Object> searchParams);

	/**
	 * 查询hirerId下的角色
	 * @param hirerId
	 * @param sort
	 * @return
	 */
	public List<IpRole> findIpRoleByHirerId(String hirerId);

	
	/**
	 * 更新角色
	 * @param roleIdArray
	 */
	public void updateRoleSort(String[] roleIdArray);



	public List<IpUserRole> getRoleUserByRoleId(String roleId);



	public void updateRoleUserSort(String[] userIdArray, String roleId);
	
		
}
