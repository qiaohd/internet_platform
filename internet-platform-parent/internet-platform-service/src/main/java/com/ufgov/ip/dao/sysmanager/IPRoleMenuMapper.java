package com.ufgov.ip.dao.sysmanager;

import java.util.List;

import com.ufgov.ip.entity.sysmanager.IpRoleMenu;
import com.yonyou.iuap.persistence.mybatis.anotation.MyBatisRepository;

@MyBatisRepository
public interface IPRoleMenuMapper {

	List<IpRoleMenu> findByMenuId(String menuId);
	
	List<IpRoleMenu> findAll(String roleId);
	
	void deleteByRoleId(String roleId);
	void save(IpRoleMenu ipRoleMenu);
}
