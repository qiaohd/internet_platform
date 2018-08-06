package com.ufgov.ip.dao.system;

import com.ufgov.ip.entity.system.IpUserInfoK;
import com.yonyou.iuap.persistence.mybatis.anotation.MyBatisRepository;

import java.util.List;
@MyBatisRepository
public interface IpUserInfoKMapper{
	public IpUserInfoK get(String id);
	
	public List<IpUserInfoK> findList(IpUserInfoK ipUserInfoK);
	
	public List<IpUserInfoK> findAllList(IpUserInfoK ipUserInfoK);
	
	public void update(IpUserInfoK ipUserInfoK);
	
	public void save(IpUserInfoK ipUserInfoK);
	
	public void delete(IpUserInfoK ipUserInfoK);
}