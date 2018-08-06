package com.ufgov.ip.api.system;

import java.util.List;

import yonyou.bpm.rest.exception.RestException;

import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.sysmanager.IpRole;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.entity.system.IpUser;

public interface BpmSynchronousServiceI {

	boolean doSynchronousService(IpHirer ipHirer, List<IpUser> findUserByHirerId,
			List<IpCompany> findCompanyByHirerId, List<IpRole> roleInfoByHirerId) throws RestException;

	
	
	
}
