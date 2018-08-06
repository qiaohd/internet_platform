package com.ufgov.ip.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yonyou.iuap.persistence.mybatis.anotation.MyBatisRepository;

@MyBatisRepository
public interface IPUserCompanyQueryDAO {

	//查询主管部门（级别为2）的单位编码
	List<String> selectChargeCompanyCode(String hirer_id);
	// 获取各部门及部门下的所有用户(根据用户表和用户部门表查询用户login_name
	List<String> getAssignedUserInfoByCode(@Param("co_code") String co_code, @Param("dbType") String dbType);
}
