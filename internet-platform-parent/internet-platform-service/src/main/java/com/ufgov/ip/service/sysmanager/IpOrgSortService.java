package com.ufgov.ip.service.sysmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springside.modules.persistence.SearchFilter;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.sysmanager.IpOrgSortServiceI;
import com.ufgov.ip.dao.sysmanager.ICompanyJdbcDao;
import com.ufgov.ip.dao.sysmanager.IPRoleDao;
import com.ufgov.ip.dao.sysmanagerimpl.IPRoleJdbcDaoImp;
import com.ufgov.ip.entity.sysmanager.IpRole;
import com.ufgov.ip.entity.sysmanager.IpUserRole;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.service.base.IPDataTableService;
import com.ufgov.ip.service.system.HirerAccountService;

@Service(version = "0.0.1")
public class IpOrgSortService implements IpOrgSortServiceI{
	
	@Autowired
	ICompanyJdbcDao iCompanyJdbcDao;
	@Autowired
	IPDataTableService ipDataTableService;
	@Autowired
	IPRoleDao ipRoleDao;
	@Autowired
	HirerAccountService hirerAccountService;
	@Autowired
	IPRoleJdbcDaoImp iPRoleJdbcDao;
	@Autowired
	IPRoleService ipRoleService;
	
	/**
	 * 更新部门排序 
	 * @param ids
	 * @param coPId
	 */
	public void updateOrgSort(String[] ids, String coPId) {
		List idList = getListByArray(ids);
		if(idList.size()>0){
			iCompanyJdbcDao.batchUpadteCompany(idList, coPId);
		}
	}
	
	

	/**
	 * 数组转换为list 
	 * @param ids
	 * @return
	 */
	public List getListByArray(String[] ids) {
		List idList = new ArrayList();
		for (String coId : ids) {
			idList.add(coId);
		}
		return idList;
	}

	/**
	 * 组装职务权限树 租户信息作为父节点 
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	public List<IpRole> getRolePage(Map<String, Object> searchParams, Sort sort) {
		// TODO 自动生成的方法存根
		Specification<IpRole> spec = ipDataTableService.buildSpecification(searchParams,IpRole.class);		
		List<IpRole> ipRoles= ipRoleDao.findAll(spec, sort);
		
		String hirerId=getHirerIdBySearchParams(searchParams);	
		IpHirer ipHirer = hirerAccountService.findHirerByHirerId(hirerId);
		
		// 组装职务权限树 租户信息作为父节点 
		List<IpRole> ipRolesList =new ArrayList<IpRole>();
		IpRole ipRoleByHirer = new IpRole();
		ipRoleByHirer.setRoleId(hirerId);
		ipRoleByHirer.setParentRoleId(null);
		ipRoleByHirer.setRoleName(ipHirer.getHirerShortName());
		ipRoleByHirer.setDispOrder(0);
		ipRolesList.add(ipRoleByHirer);
		for (IpRole ipRole : ipRoles) {
			ipRole.setParentRoleId(hirerId);
			ipRolesList.add(ipRole);
		}	
		return ipRolesList;
	}
	
	/**
	 * 取hirerId
	 * @param searchParams
	 * @return
	 */
	public String getHirerIdBySearchParams(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		SearchFilter searchFilterByHirerId =filters.get("EQ_hirerId");
		String hirerId = "";
		if (searchFilterByHirerId!=null) {
			hirerId = (String) searchFilterByHirerId.value;
		}
		return hirerId;
	}

	/**
	 * 查询hirerId下的角色
	 * @param hirerId
	 * @param sort
	 * @return
	 */
	public List<IpRole> findIpRoleByHirerId(String hirerId) {
		// TODO 自动生成的方法存根			
		List<IpRole> ipRoles= ipRoleService.getRoleInfoByHirerId(hirerId);
		return ipRoles;
	}

	
	/**
	 * 更新角色
	 * @param roleIdArray
	 */
	public void updateRoleSort(String[] roleIdArray) {
		// TODO 自动生成的方法存根
		List idList = getListByArray(roleIdArray);
		if(idList.size()>0){
			iPRoleJdbcDao.batchUpadteRole(idList);
		}
	}



	public List<IpUserRole> getRoleUserByRoleId(String roleId) {
		// TODO 自动生成的方法存根
		List<IpUserRole> ipRoles= ipRoleService.bulidUserRoleAndOrder(roleId);
		return ipRoles;
	}



	public void updateRoleUserSort(String[] userIdArray, String roleId) {
		// TODO 自动生成的方法存根
		List idList = getListByArray(userIdArray);
		if(idList.size()>0){
			iPRoleJdbcDao.batchUpadteRoleUser(idList, roleId);
		}
	}
	
	
	
	
}
