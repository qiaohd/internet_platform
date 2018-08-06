package com.ufgov.ip.api.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ufgov.ip.entity.system.IpUserLe123;

public interface IpUserLe123ServiceI{

	public IpUserLe123 get(String id);
	
	public List<IpUserLe123> findList(IpUserLe123 ipUserLe123);
	
	public List<IpUserLe123> findAllList(IpUserLe123 ipUserLe123);
	
	public void update(IpUserLe123 ipUserLe123);
	
	public void delete(IpUserLe123 ipUserLe123);
	
		public void save(IpUserLe123 ipUserLe123);
	
}