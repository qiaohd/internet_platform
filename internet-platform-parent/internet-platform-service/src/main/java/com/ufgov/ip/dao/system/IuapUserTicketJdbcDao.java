package com.ufgov.ip.dao.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public interface IuapUserTicketJdbcDao {
	
	public List getUserTicketByHirerIdAndCoId(String hirerId, String coId,
			String isEnabled,String rolename);

}
