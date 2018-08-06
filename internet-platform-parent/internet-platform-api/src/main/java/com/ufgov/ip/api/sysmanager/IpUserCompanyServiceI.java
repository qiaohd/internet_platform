package com.ufgov.ip.api.sysmanager;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.system.IpUserCompany;

public interface IpUserCompanyServiceI {

	
	public List<IpUserCompany> findIpUserCompanyByCoId(String coId);

	public List<IpUserCompany> findUserCompanyByCoNameLike(String coNameInfo);

	public IpUserCompany findIpUserCompanyByUserIdAndCoId(String userId, String coId);
	@Transactional
	public void saveIpUserCompanyEntity(IpUserCompany ipUserCompany);
	//兼职的部门
	public List<IpCompany> showUserCompanyByUserIdAndIsPartTime(String userId,String isPartTime);

	public void deleteByUserIdAndCoId(String userId, String coId);

	public List<IpCompany> showUserCompanyByUserId(String userId);
	
	public IpUserCompany findIpUserCompanyByUserId(String userId);
	
	
}
