package com.ufgov.ip.api.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ufgov.ip.entity.system.IpUserInfoK;

public interface IpUserInfoKServiceI{

	public IpUserInfoK get(String id);
	
	public List<IpUserInfoK> findList(IpUserInfoK ipUserInfoK);
	
	public List<IpUserInfoK> findAllList(IpUserInfoK ipUserInfoK);
	
	public void update(IpUserInfoK ipUserInfoK);
	
	public void delete(IpUserInfoK ipUserInfoK);
	
		public void save(IpUserInfoK ipUserInfoK);
	
}