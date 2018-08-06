package com.uggov.ip.web.tmp;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;

import org.apache.commons.beanutils.BeanUtils;

import com.ufgov.ip.entity.sysmanager.IpUserRole;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.IpUserCompany;

public class SettingPropertiesUtil {

	
	public static List copyProperty(List<String[]> excelData) {
		/**
		 * 待改进
		 */
		/**
		 * @Id
			@Column(name="THE_ID")
			private String theId;
		
			@Column(name="CO_ID")
			private String coId;
		
			@Column(name="IS_PART_TIME")
			private String isPartTime;
		
			@Column(name="ROLE_ID")
			private String roleId;
		
			@Column(name="USER_ID")
			private String userId;
		 */
		List<IpUser> userlist=new ArrayList<IpUser>();
		List<IpUserCompany> ipUserCompanylist=new ArrayList<IpUserCompany>();
		List<IpUserRole> ipUserRolelist=new ArrayList<IpUserRole>();
		List list=new ArrayList();
		
		for (String[] values : excelData) {
			  IpUser ipUser=new IpUser();
	      	  IpUserCompany ipUserCompany=new IpUserCompany();
	      	  IpUserRole ipUserRole=new IpUserRole();
	      	Map<String,Object> mapInfo=new HashMap<String, Object>();
	      	  
			mapInfo.put("userName", values[0]);
			mapInfo.put("userSex", values[1]);
			mapInfo.put("loginName", values[2]);
			mapInfo.put("coName", values[3]);
			mapInfo.put("duty", values[4]);
			mapInfo.put("cellphoneNo", values[5]);
			mapInfo.put("userEmail", values[6]);
			mapInfo.put("phoneNo", values[7]);
			mapInfo.put("extension", values[8]);
			try {
				BeanUtils.populate(ipUser, mapInfo);
				BeanUtils.populate(ipUserCompany, mapInfo);//此时只有coName
				BeanUtils.populate(ipUserRole, mapInfo);
				userlist.add(ipUser);
				ipUserCompanylist.add(ipUserCompany);
				ipUserRolelist.add(ipUserRole);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		list.add(userlist);
		list.add(ipUserCompanylist);
		list.add(ipUserRolelist);
		return list;
		
		
		
		
	}
	
}
	

