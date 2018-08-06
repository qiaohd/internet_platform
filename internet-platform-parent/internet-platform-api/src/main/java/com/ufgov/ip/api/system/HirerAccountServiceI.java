package com.ufgov.ip.api.system;

import java.util.List;

import com.ufgov.ip.entity.system.IpHirer;

public interface HirerAccountServiceI {
	public IpHirer findHirerByLoginName(String loginName);
	public IpHirer findHirerByCellphoneNo(String cellphoneNo);
	public IpHirer findHirerByEmail(String email);	
	public IpHirer findHirerByEmailOrLoginNameOrCellphoneNo(String loginName);	
	public IpHirer findHirerByPassword(String pwd);
	public IpHirer findHirerByHirerId(String hirerId);
	public IpHirer findHirerByHirerIdMybatis(String hirerId);
	public boolean saveHirerInfo(IpHirer ipHirer);
	public void updateHirerByHirerId(String hirerId, String path);
	public List<IpHirer> findAll();	
	public void updateHirerHeaderimageByHirerId(String hirerId,String url);
}
