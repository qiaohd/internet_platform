package com.ufgov.ip.dao.system;

import java.util.List;


public interface IuapUserJdbcDao{

	public List getInitUserByHirerIdAndCoId(String hirerId,String coId,String isEnabled,String loginNameLike);
	
}
