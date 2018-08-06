package com.ufgov.ip.dao.sysmanager;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.ufgov.ip.entity.sysmanager.IpCompany;

@Service
public interface ICompanyDao extends PagingAndSortingRepository<IpCompany, String>,JpaSpecificationExecutor<IpCompany>{

	public void saveCompany(IpCompany ipCompany);
	
	// 通过部门名称查询部门信息 
	IpCompany findByCoName(String coName);
	
	IpCompany findIpCompanyByCoNameAndParentCoId(String coName,String parentCoId);
	// 通过部门Id查询部门信息
	IpCompany findByCoId(String coId);
	
	IpCompany findCompanyByCoName(String companyName);
	
	//获得所有的子部门
	@Query(" from IpCompany where parentCoId=? order by dispOrder,coCode asc")
	List<IpCompany> findCompanyByParentCoId(String parentCoId);

	
	@Modifying(clearAutomatically = true) @Query("update IpCompany ipCompany set ipCompany.parentCoId=:parentCoId where ipCompany.coId=:coId")
	public void updateCompanyBycoId(@Param("coId")String coId, @Param("parentCoId")String parentCoId);

	@Query(" from IpCompany where 1=1 and hirerId=:hirerId and deptDetail like :deptDetail")
	public List<IpCompany> findCompanyByDeptDetailLike(@Param("deptDetail")String deptDetail,@Param("hirerId")String hirerId);
	
	@Modifying
	@Query("update IpCompany ipCompany set ipCompany.coName=:coName where ipCompany.hirerId=:hirerId and ipCompany.levelNum=:levelNum")
	public void updateCompanyByHirerIdAndLevelNum(@Param("hirerId")String hirerId, @Param("levelNum")int levelNum,@Param("coName")String coName);

	public List<IpCompany> findCompanyByHirerId(String hirerId);
	
	IpCompany findIpCompanyByCoCodeAndHirerId(String coCode,String hirerId);
	
	@Modifying
	@Query("delete from IpCompany ic where ic.coId=:coId")
	void deleteByCoId(@Param("coId") String coId);
}
