package com.ufgov.ip.dao.system;

import com.ufgov.ip.entity.system.IpUserInfo;
import com.yonyou.iuap.persistence.mybatis.anotation.MyBatisRepository;

import java.util.List;
@MyBatisRepository
public interface IpUserInfoMapper{
	public IpUserInfo get(String id);
	
	public List<IpUserInfo> findList(IpUserInfo ipUserInfo);
	
	public List<IpUserInfo> findAllList();
	
	public void update(IpUserInfo ipUserInfo);
	
	public void save(IpUserInfo ipUserInfo);
	
	public void delete(String id);
}