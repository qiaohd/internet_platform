package com.ufgov.ip.service.sysmanager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.sysmanager.IpUserCompanyServiceI;
import com.ufgov.ip.dao.sysmanager.IpUserCompanyDao;
import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.system.IpUserCompany;
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Service(version = "0.0.1")
public class IpUserCompanyService implements IpUserCompanyServiceI{

	@Autowired
	private IpUserCompanyDao ipUserCompanyDao;

	public IpUserCompanyDao getIpUserCompanyDao() {
		return ipUserCompanyDao;
	}

	public void setIpUserCompanyDao(IpUserCompanyDao ipUserCompanyDao) {
		this.ipUserCompanyDao = ipUserCompanyDao;
	}
	
	public List<IpUserCompany> findIpUserCompanyByCoId(String coId){
		
		
		return ipUserCompanyDao.findIpUserCompanyByCoId(coId);
		
	}

	public List<IpUserCompany> findUserCompanyByCoNameLike(String coNameInfo) {

		return ipUserCompanyDao.findUserCompanyByCoNameLike(coNameInfo);
		
	}

	public IpUserCompany findIpUserCompanyByUserIdAndCoId(String userId, String coId) {

		return ipUserCompanyDao.findIpUserCompanyByUserIdAndCoId(userId, coId);
		
	}

	@Transactional
	public void saveIpUserCompanyEntity(IpUserCompany ipUserCompany) {
		ipUserCompanyDao.save(ipUserCompany);
	}
	
	//兼职的部门
	public List<IpCompany> showUserCompanyByUserIdAndIsPartTime(String userId,String isPartTime){
		return ipUserCompanyDao.showUserCompanyByUserIdAndIsPartTime(userId, isPartTime);
	}

	@Transactional
	public void deleteByUserIdAndCoId(String userId, String coId) {
		ipUserCompanyDao.deleteByUserIdAndCoId(userId,coId);
		
	}

	public List<IpCompany> showUserCompanyByUserId(String userId) {
		// TODO 自动生成的方法存根
		return ipUserCompanyDao.showUserCompanyByUserId(userId);
	
	}
	
	public IpUserCompany findIpUserCompanyByUserId(String userId) {

		return ipUserCompanyDao.findIpUserCompanyByUserId(userId);
	}
	
	
}
