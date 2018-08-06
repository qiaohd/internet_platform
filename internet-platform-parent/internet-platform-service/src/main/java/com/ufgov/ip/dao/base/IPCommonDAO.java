package com.ufgov.ip.dao.base;

import java.util.List;
import java.util.Map;

import com.yonyou.iuap.persistence.mybatis.anotation.MyBatisRepository;

@MyBatisRepository
public interface IPCommonDAO {
	
	List<Map<String, String>> getDictDetailByEnumtype(String dicType);
}
