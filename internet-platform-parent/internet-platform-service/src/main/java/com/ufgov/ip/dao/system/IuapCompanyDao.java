package com.ufgov.ip.dao.system;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ufgov.ip.entity.sysmanager.IpCompany;


public interface IuapCompanyDao extends PagingAndSortingRepository<IpCompany, Long>,JpaSpecificationExecutor<IpCompany>{
	
	
}
