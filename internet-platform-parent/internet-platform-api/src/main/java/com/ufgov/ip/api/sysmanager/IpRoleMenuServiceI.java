package com.ufgov.ip.api.sysmanager;

import java.util.List;

import com.ufgov.ip.entity.sysmanager.IpRoleMenu;
import com.ufgov.ip.entity.system.IpMenu;

public interface IpRoleMenuServiceI {
	public List<IpRoleMenu> findMenuByRole(String RoleId);
}
