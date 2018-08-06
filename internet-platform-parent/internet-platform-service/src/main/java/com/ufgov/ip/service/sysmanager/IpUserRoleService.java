package com.ufgov.ip.service.sysmanager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.sysmanager.IpUserRoleServiceI;
import com.ufgov.ip.dao.sysmanager.IpUserRoleDao;
import com.ufgov.ip.entity.sysmanager.IpUserRole;

@Transactional(propagation = Propagation.REQUIRES_NEW)
@Service(version = "0.0.1")
public class IpUserRoleService implements IpUserRoleServiceI{

	
	@Autowired
	private IpUserRoleDao ipUserRoleDao;

	public IpUserRoleDao getIpUserRoleDao() {
		return ipUserRoleDao;
	}

	public void setIpUserRoleDao(IpUserRoleDao ipUserRoleDao) {
		this.ipUserRoleDao = ipUserRoleDao;
	}
	
	public IpUserRole findIpUserRoleByTheIdAndRoleId(String theId, String roleId){
		
		
		
		
		return null;
	}

	@Transactional
	public void saveUserRoleEntity(IpUserRole ipUserRole) {
		ipUserRoleDao.save(ipUserRole);
	}

	public List<IpUserRole> showUserRoleByUserId(String userId){
		 return ipUserRoleDao.showUserRoleByUserId(userId);
	}
	
	//兼职的职务
	public List<IpUserRole> showUserRoleByUserIdAndIsPartTime(String userId,String isPartTime){
		 return ipUserRoleDao.showUserRoleByUserIdAndIsPartTime(userId, isPartTime);
	}

	@Transactional
	public void deleteByUserIdAndRoleId(String userId, String roleId,
			String isPartTime) {
		ipUserRoleDao.deleteByUserIdAndRoleId(userId,roleId,isPartTime);
		
	}

	
	
	
	
	
	
}
