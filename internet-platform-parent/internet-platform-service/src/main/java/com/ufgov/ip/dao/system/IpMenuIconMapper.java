package com.ufgov.ip.dao.system;

import java.util.List;

import com.ufgov.ip.entity.system.IpMenuIcon;
import com.yonyou.iuap.persistence.mybatis.anotation.MyBatisRepository;

@MyBatisRepository
public interface IpMenuIconMapper {
	public void insert(IpMenuIcon ipMenuIcon);
	public List<IpMenuIcon> query(IpMenuIcon ipMenuIcon);
	public IpMenuIcon selectByPk(IpMenuIcon ipMenuIcon);
	public void update(IpMenuIcon ipMenuIcon);
	public void  delete(IpMenuIcon ipMenuIcon);
}
