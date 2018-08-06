package com.ufgov.ip.dao.sysmanager;

import java.util.List;
import java.util.Map;

import com.ufgov.ip.entity.sysmanager.IpUserRole;

public interface IPRoleJdbcDao {
	public List getMenuForRole(String roleId);

	public List getInitPermissionMenu(String roleId);
	
	public List getUserByCoId(String coId);

	public List<IpUserRole> findRoleUserByRoleIdList(String roleId);
	
	public Map<String, String> findCountRoleUserById(String roleId);

	public void batchUpadteRole(List idList);

	public void batchUpadteRoleUser(List idList, String roleId);
}
