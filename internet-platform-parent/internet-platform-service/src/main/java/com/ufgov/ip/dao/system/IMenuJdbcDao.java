package com.ufgov.ip.dao.system;

import java.util.List;

import com.ufgov.ip.entity.system.IpMenu;

public interface IMenuJdbcDao {
	public List<IpMenu> getIpMenuListByUser(String userId);

	public List<IpMenu> getMenuByParentId(String userId);

	public List<IpMenu> getIpMenuListByHirer(String hirerId);
	
	public List<IpMenu> getIpHideMenuListByUser(String userId);

	public List<IpMenu> getIpHideMenuListByHirer(String hirerId);

	public void deleteUserMenu(String userId, String menuId);

	public void saveUserMenu(String userId, String menuId);
}
