package com.ufgov.ip.service.system;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.system.IpMenuIconServiceI;
import com.ufgov.ip.dao.system.IpMenuIconMapper;
import com.ufgov.ip.entity.system.IpMenuIcon;
@Service(version="0.0.1")
public class IpMenuIconService implements IpMenuIconServiceI{
    @Autowired
    private IpMenuIconMapper ipMenuIconMapper;
	@Override
	public void insert(IpMenuIcon ipMenuIcon) {
		 ipMenuIconMapper.insert(ipMenuIcon);
	}
	@Override
	public List<IpMenuIcon> findMenuInfo(IpMenuIcon ipMenuIcon) {
		return ipMenuIconMapper.query(ipMenuIcon);
	}
	@Override
	public IpMenuIcon selectByPk(IpMenuIcon ipMenuIcon) {
		return ipMenuIconMapper.selectByPk(ipMenuIcon);
	}
	@Override
	public void update(IpMenuIcon ipMenuIcon) {
		// TODO 自动生成的方法存根
		 ipMenuIconMapper.update(ipMenuIcon);
	}
	@Override
	public void delByPk(IpMenuIcon ipMenuIcon) {
		 ipMenuIconMapper.delete(ipMenuIcon);		
	}

}
