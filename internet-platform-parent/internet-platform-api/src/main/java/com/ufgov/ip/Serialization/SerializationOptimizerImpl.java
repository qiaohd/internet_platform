package com.ufgov.ip.Serialization;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;
import com.ufgov.ip.entity.base.IpDictionary;
import com.ufgov.ip.entity.base.IpDictionaryDetail;
import com.ufgov.ip.entity.base.IpRegion;
import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.sysmanager.IpRole;
import com.ufgov.ip.entity.sysmanager.IpRoleMenu;
import com.ufgov.ip.entity.sysmanager.IpUserAndCompanyEntity;
import com.ufgov.ip.entity.sysmanager.IpUserRole;
import com.ufgov.ip.entity.sysmanager.RegionCode;
import com.ufgov.ip.entity.system.ApproveStatus;
import com.ufgov.ip.entity.system.AskForLeave;
import com.ufgov.ip.entity.system.IPUserJDBC;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.entity.system.IpMenu;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.IpUserCompany;
import com.ufgov.ip.entity.system.IpUserDTO;
import com.ufgov.ip.entity.system.MenuTree;
import com.ufgov.ip.entity.system.TicketDetail;
import com.ufgov.ip.entity.system.UserCheck;
import com.yonyou.iuap.iweb.entity.DataTable;

public class SerializationOptimizerImpl implements SerializationOptimizer {

	@Override
	public Collection<Class> getSerializableClasses() {
		// TODO 自动生成的方法存根
		List<Class> classes = new LinkedList<Class>();
		/*classes.add(IpDictionary.class);
		classes.add(IpDictionaryDetail.class);
		classes.add(IpRegion.class);
		classes.add(IpCompany.class);
		classes.add(IpRole.class);
		classes.add(IpRoleMenu.class);
		classes.add(IpUserAndCompanyEntity.class);
		classes.add(IpUserRole.class);
		classes.add(RegionCode.class);
		classes.add(ApproveStatus.class);
		classes.add(AskForLeave.class);		
		classes.add(IpHirer.class);
		classes.add(IpMenu.class);
		classes.add(IpUser.class);
		classes.add(IpUserCompany.class);
		classes.add(IpUserDTO.class);
		classes.add(IPUserJDBC.class);
		classes.add(MenuTree.class);
		classes.add(TicketDetail.class);
		classes.add(UserCheck.class);*/
		classes.add(Map.class);
		classes.add(DataTable.class);
		classes.add(Sort.class);
        return classes;
	}

}
