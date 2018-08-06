package com.ufgov.ip.service.sysmanager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.sysmanager.IpRoleMenuServiceI;
import com.ufgov.ip.dao.sysmanager.IPRoleMenuMapper;
import com.ufgov.ip.dao.system.IMenuMapper;
import com.ufgov.ip.entity.sysmanager.IpRoleMenu;
import com.ufgov.ip.entity.system.IpMenu;
@Service(version = "0.0.1")
public class IpRoleMenuService implements IpRoleMenuServiceI{
	@Autowired
	private IPRoleMenuMapper iPRoleMenuMapper;
	@Override
	public List<IpRoleMenu> findMenuByRole(String RoleId) {
		// TODO 自动生成的方法存根
		return iPRoleMenuMapper.findAll(RoleId);
	}

}
