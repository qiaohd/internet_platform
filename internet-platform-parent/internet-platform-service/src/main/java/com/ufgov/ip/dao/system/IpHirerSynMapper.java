package com.ufgov.ip.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ufgov.ip.entity.system.IpHirer;
import com.yonyou.iuap.persistence.mybatis.anotation.MyBatisRepository;

@MyBatisRepository
public interface IpHirerSynMapper {
	public IpHirer  findHirerSynByCondition(IpHirer ipHirer);
	void insert(IpHirer ipHirer);
}
