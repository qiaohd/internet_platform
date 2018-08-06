package com.ufgov.ip.dao.sysmanager;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.system.IpUserCompany;

public interface IpUserCompanyDao extends PagingAndSortingRepository<IpUserCompany, String>,JpaSpecificationExecutor<IpUserCompany>{

	IpUserCompany findIpUserCompanyByUserIdAndCoId(String userId, String coId);
	
	//@Query("select ipc.userId from IpUserCompany ipc where ipc.coId =:coId")
	List<IpUserCompany> findIpUserCompanyByCoId(@Param("coId") String coId);

	List<IpUserCompany> findUserCompanyByCoNameLike(String coNameInfo);
	
	IpUserCompany findIpUserCompanyByUserId(String userId);
	
	
		@Query("select ic.coId,ic.coName from IpCompany ic where ic.coId in (select iur.coId from IpUserRole iur where iur.userId=:userId and iur.isPartTime=:isPartTime)")
		List<IpCompany> showUserCompanyByUserIdAndIsPartTime(@Param("userId") String userId,@Param("isPartTime") String isPartTime);
	
		@Modifying
	    @Query("delete from IpUserCompany iuc where iuc.userId=:userId and iuc.coId=:coId and iuc.coId in (select iur.coId from IpUserRole iur where iur.isPartTime=1 and iur.userId=:userId)")
		void deleteByUserIdAndCoId(@Param("userId")String userId, @Param("coId")String coId);

		@Query("select ic.coId,ic.coName from IpCompany ic where ic.coId in (select iur.coId from IpUserCompany iur where iur.userId=:userId)")
		List<IpCompany> showUserCompanyByUserId(@Param("userId")String userId);
		
		@Modifying
	    @Query("delete from IpUserCompany iuc where iuc.coId=:coId")
		void deleteByCoId(@Param("coId")String coId);
}
