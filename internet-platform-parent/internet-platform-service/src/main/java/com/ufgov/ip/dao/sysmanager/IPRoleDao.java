package com.ufgov.ip.dao.sysmanager;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ufgov.ip.entity.sysmanager.IpRole;

public interface IPRoleDao extends PagingAndSortingRepository<IpRole, String>,JpaSpecificationExecutor<IpRole>{
    
	@Query(" from IpRole order by roleName")
	public List<IpRole> findRoleAll();
	
	IpRole findByRoleId(String roleId);
	
	public IpRole findByRoleNameAndHirerId(String roleName,String hirerId);

	@Query(" from IpRole where hirerId=:hirerId order by dispOrder")
	public List<IpRole>  findByHirerId(@Param("hirerId")String hirerId);

	public IpRole findRoleByRoleId(String roleId);
	
	//Page<IPRoleDao> findByRoleId(String  title, String brandName, String price, Pageable pageable);
	
}
