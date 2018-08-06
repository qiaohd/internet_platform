package com.ufgov.ip.dao.sysmanager;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public interface ICompanyJdbcDao {
	public int[] batchUpadteCompany(List list,String coPId);
}
