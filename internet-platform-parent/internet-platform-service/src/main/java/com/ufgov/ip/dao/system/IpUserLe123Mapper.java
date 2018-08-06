package com.ufgov.ip.dao.system;

import com.ufgov.ip.entity.system.IpUserLe123;
import com.yonyou.iuap.persistence.mybatis.anotation.MyBatisRepository;

import java.util.List;
@MyBatisRepository
public interface IpUserLe123Mapper{
	public IpUserLe123 get(String id);
	
	public List<IpUserLe123> findList(IpUserLe123 ipUserLe123);
	
	public List<IpUserLe123> findAllList(IpUserLe123 ipUserLe123);
	
	public void update(IpUserLe123 ipUserLe123);
	
	public void save(IpUserLe123 ipUserLe123);
	
	public void delete(IpUserLe123 ipUserLe123);
}