package com.ufgov.ip.dao.sysmanager;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ufgov.ip.entity.sysmanager.IpUserRole;

public interface IpUserRoleDao extends PagingAndSortingRepository<IpUserRole, Long>,JpaSpecificationExecutor<IpUserRole>{

	IpUserRole findIpUserRoleByTheIdAndRoleId(String theId, String roleId);

	IpUserRole findIpUserRoleByUserIdAndRoleIdAndIsPartTime(String userId,
			String roleId, String isPartTime);
	// 通过权限ID查询 
	List<IpUserRole> findIpUserRoleByRoleId(String roleId);

	IpUserRole findIpUserRoleByUserIdAndIsPartTime(String userId, String isPartTime);
	
	@Modifying  
	@Query("DELETE FROM  IpUserRole  WHERE roleId=:roleId and isPartTime='0' ")
	void deleteUserRoleByRoleId(@Param("roleId") String roleId);
	
	@Modifying  
	@Query("DELETE FROM  IpUserRole  WHERE userId=:userId and isPartTime='0' ")
	void deleteUserRoleByUserId(@Param("userId") String userId);
	
	@Query("from IpUserRole iur where iur.userId=:userId")
	List<IpUserRole> showUserRoleByUserId(@Param("userId") String userId);
	//查询当前员工的兼职的职务
	/*@Query("select ir.roleId,ir.roleName from IpRole ir where ir.roleId in (select iur.roleId from IpUserRole iur where iur.userId=:userId and iur.isPartTime=:isPartTime)")
	List<IpRole> showUserRoleByUserIdAndIsPartTime(@Param("userId") String userId,@Param("isPartTime") String isPartTime);*/
	@Query("from IpUserRole iur where iur.userId=:userId and iur.isPartTime=:isPartTime")
	List<IpUserRole> showUserRoleByUserIdAndIsPartTime(@Param("userId") String userId,@Param("isPartTime") String isPartTime);

	@Modifying
    @Query("delete from IpUserRole where userId = :userId and roleId = :roleId and isPartTime = :isPartTime")
	void deleteByUserIdAndRoleId(@Param("userId")String userId, @Param("roleId")String roleId, @Param("isPartTime")String isPartTime);
	
	@Modifying
    @Query("delete from IpUserRole where userId = :userId and isPartTime = :isPartTime")
	void deleteUserRoleByUserIdAndIsPartTime(@Param("userId")String userId, @Param("isPartTime")String isPartTime);
	
	List<IpUserRole> findByroleIdAndIsPartTime(String roleId,String isPartTime);
	
	@Modifying
    @Query("delete from IpUserRole where coId = :coId")
	void deleteByCoId(@Param("coId")String coId);
}
