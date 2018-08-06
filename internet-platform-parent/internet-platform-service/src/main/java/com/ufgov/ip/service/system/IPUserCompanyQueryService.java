package com.ufgov.ip.service.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.system.IPUserCompanyQueryServiceI;
import com.ufgov.ip.dao.system.IPUserCompanyQueryDAO;

@Service(version = "0.0.1")
public class IPUserCompanyQueryService implements IPUserCompanyQueryServiceI {

	@Value("${jdbc.type}")
	private String dbType;
	@Autowired
	IPUserCompanyQueryDAO ipUserCompanyQueryDAO;
	
	public IPUserCompanyQueryDAO getIpUserCompanyQueryDAO() {
		return ipUserCompanyQueryDAO;
	}

	public void setIpUserCompanyQueryDAO(IPUserCompanyQueryDAO ipUserCompanyQueryDAO) {
		this.ipUserCompanyQueryDAO = ipUserCompanyQueryDAO;
	}

	/**
	 * 获取各部门及部门下的所有用户(根据用户表和用户部门表查询用户login_name)
	 * @param hireId
	 * @return
	 */
	public Map<String,List<String>> getChargeCompanyCode(String hireId)
	{
		Map<String,List<String>> resultList=new HashMap<String,List<String>>();
		List<String> liCodes=new ArrayList<String>();
		//查询主管部门（级别为2）的单位编码
		liCodes= ipUserCompanyQueryDAO.selectChargeCompanyCode(hireId);
		for(String code:liCodes)
		{
			List<String> list=ipUserCompanyQueryDAO.getAssignedUserInfoByCode(code,dbType);
			if(list.size()>0)
			{
				//************由于连接查询采用友连接，可能有些部分没有对应人员，以下针对查询出来的数据做去空值处理********
				//*********************处理开始********************************************************
				Iterator<String> it=list.iterator();
				while(it.hasNext())
				{
					if(it.next()==null)
					{
						it.remove();
					}
				}
				//*********************处理结束********************************************************		
				if(list.size()>0) 
				{
					resultList.put(code, list);
				}
			}
		}
		return resultList;
	}
}
