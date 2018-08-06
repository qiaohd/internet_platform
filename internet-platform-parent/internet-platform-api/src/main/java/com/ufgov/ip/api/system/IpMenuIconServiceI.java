package com.ufgov.ip.api.system;

import java.util.List;

import com.ufgov.ip.entity.system.IpMenuIcon;

public interface IpMenuIconServiceI {
	public void insert(IpMenuIcon ipMenuIcon);	
	public List<IpMenuIcon> findMenuInfo(IpMenuIcon ipMenuIcon);
	public IpMenuIcon selectByPk(IpMenuIcon ipMenuIcon);
	public void update(IpMenuIcon ipMenuIcon);
	public void delByPk(IpMenuIcon ipMenuIcon);
}
