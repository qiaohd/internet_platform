package com.ufgov.ip.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.system.IpHirer;
import com.yonyou.iuap.persistence.mybatis.anotation.MyBatisRepository;

@MyBatisRepository
public interface IpCompanySynMapper {
	public List<IpCompany> findCompanySynByCondition(IpCompany ipCompany);
	void insert(IpCompany ipCompany);
	void delete(String hirerId);
}
