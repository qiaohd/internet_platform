package com.ufgov.ip.api.system;

import org.springframework.stereotype.Component;

import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.system.IpHirer;

/**
 * 用户管理服务层 书写readonly是说明没有特殊标识服务层只有只读事物
 *
 */
@Component
public interface HirerRegisterServiceI {

	public boolean registerHirer(IpHirer hirer,IpCompany ipCompany) throws Exception;
	//public void setHirerDao(IuapHirerDao hirerDao);
}
