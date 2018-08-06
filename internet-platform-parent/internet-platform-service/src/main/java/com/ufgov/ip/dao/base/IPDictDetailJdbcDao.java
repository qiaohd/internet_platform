package com.ufgov.ip.dao.base;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ufgov.ip.entity.base.IpDictionaryDetail;


public interface IPDictDetailJdbcDao {
	public List<IpDictionaryDetail> findDicAndDetail(String dicType,String dicName);
}
