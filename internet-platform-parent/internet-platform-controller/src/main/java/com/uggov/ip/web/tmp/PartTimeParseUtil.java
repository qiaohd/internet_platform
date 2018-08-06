package com.uggov.ip.web.tmp;

import java.util.ArrayList;
import java.util.List;

import com.ufgov.ip.entity.sysmanager.IpUserRole;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.IpUserCompany;
import com.ufgov.ip.utils.UUIDTools;

public class PartTimeParseUtil {

	public static List getList(String str,IpUser ipUserEntity){
		String substring = str.substring(1, str.length()-2);
		substring=substring.substring(1);
		String[] str1=substring.split("\\}\\,"+"\\{");
		List<IpUserRole> partList=new ArrayList<IpUserRole>();
		List<IpUserCompany> partList2=new ArrayList<IpUserCompany>();
		List allList=new ArrayList();
		for (String string : str1) {
			IpUserRole ipUserRole=new IpUserRole();
			IpUserCompany ipUserCompany=new IpUserCompany();
			String[] str2=string.split(",");
			for (int i=0;i<str2.length;i++) {
				  if(i%3!=0 || i==0){
					  String[] split = str2[i].split(":");
					  if(split[0].equals("\"p_dept_id\"")){
						  String deptId = (String) split[1].subSequence(1,split[1].length()-1);
						  ipUserRole.setCoId(deptId);
						  ipUserCompany.setCoId(deptId);
					  }else if(split[0].equals("\"p_role_id\"")){
						  String roleId= (String) split[1].subSequence(1,split[1].length()-1);
						  ipUserRole.setRoleId(roleId);
					      
					  }else{
						  String item = (String) split[1].subSequence(1,split[1].length()-1);
						  ipUserRole.setIsPartTime(item);
					  }
				   }
				  ipUserRole.setUserId(ipUserEntity.getUserId());
				  ipUserCompany.setUserId(ipUserEntity.getUserId());
				  ipUserRole.setTheId(UUIDTools.uuidRandom());
				  ipUserCompany.setTheId(UUIDTools.uuidRandom());
			}
			partList.add(ipUserRole);
			partList2.add(ipUserCompany);
		}
		allList.add(partList);
		allList.add(partList2);
		return allList;
	}
	
	
	
	
	
}
