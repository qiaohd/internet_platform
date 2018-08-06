package com.ufgov.ip.api.sysmanager;

import java.util.List;
import com.ufgov.ip.entity.sysmanager.IpUserRole;

public interface IpUserRoleServiceI {
	
	public IpUserRole findIpUserRoleByTheIdAndRoleId(String theId, String roleId);

	public void saveUserRoleEntity(IpUserRole ipUserRole);
	
	public List<IpUserRole> showUserRoleByUserId(String userId);
	//兼职的职务
	public List<IpUserRole> showUserRoleByUserIdAndIsPartTime(String userId,String isPartTime);
	public void deleteByUserIdAndRoleId(String userId, String roleId,
			String isPartTime);	
	
}
