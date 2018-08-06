package com.ufgov.ip.api.system;

import java.util.List;

import com.ufgov.ip.entity.system.IpUserInfo;

public interface IpUserInfoServiceI{

	public IpUserInfo get(String id);
	
	public List<IpUserInfo> findList(IpUserInfo ipUserInfo);
	
	public List<IpUserInfo> findAllList();
	
	public void update(IpUserInfo ipUserInfo);
	
	public void save(IpUserInfo ipUserInfo);
	
	public void delete(String id);
	
}