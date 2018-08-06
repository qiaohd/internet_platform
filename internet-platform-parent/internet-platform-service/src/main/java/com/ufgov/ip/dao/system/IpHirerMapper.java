package com.ufgov.ip.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ufgov.ip.entity.system.IpHirer;
import com.yonyou.iuap.persistence.mybatis.anotation.MyBatisRepository;

@MyBatisRepository
public interface IpHirerMapper {
	public IpHirer  findHirerByCondition(IpHirer ipHirer);
	
	IpHirer findHirerByEmailOrLoginNameOrCellphoneNo(@Param("loginName") String loginName);
	void insert(IpHirer ipHirer);
	void update(IpHirer ipHirer);
	void deleteByPk(IpHirer ipHirer);
	List<IpHirer> findHirerAll();
	List<IpHirer> findAll(@Param("conditionSql") String conditionSql );
}
