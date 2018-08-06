package com.ufgov.ip.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ufgov.ip.entity.system.IpUser;

public interface IuapUserDao extends PagingAndSortingRepository<IpUser, Long>,JpaSpecificationExecutor<IpUser> {
	
	IpUser findByLoginName(String loginName);

	IpUser findUserByCellphoneNo(String cellphoneNo);

	IpUser findUserByUserEmail(String userEmail);
	
	@Query("from IpUser ih where hirerId=:hirerId and userType=:userType")
	IpUser findUserByHirerIdAndUserType(@Param("hirerId")String hirerId,@Param("userType")String userType);
	
	@Query("from IpUser ih where userEmail=:loginName or loginName=:loginName or cellphoneNo=:loginName")
	IpUser findUserByUserEmailOrLoginNameOrCellphoneNo(@Param("loginName")String loginName);

	@Query("from IpUser order by employeeNo")
	List<IpUser> findUserAll();

	IpUser findIpUserByEmployeeNo(String employeeNo);
    
	
	@Modifying(clearAutomatically = true) @Query("update IpUser ipUser set ipUser.isEnabled=:isEnabled where ipUser.userId=:userId")
	void updateUserByUserId(@Param("userId")String userId, @Param("isEnabled")String isEnabled);
	
	@Modifying(clearAutomatically = true) @Query("update IpUser ipUser set ipUser.userSex=:userSex,ipUser.userEmail=:userEmail,ipUser.phoneNo=:phoneNo,ipUser.cellphoneNo=:cellphoneNo,ipUser.duty=:duty where ipUser.hirerId=:hirerId and ipUser.userType=:userType")
	void saveIpUserInfo(@Param("hirerId")String hirerId,@Param("userType")String userType, @Param("userSex")String userSex,
			@Param("userEmail")String userEmail, @Param("phoneNo")String phoneNo, @Param("cellphoneNo")String cellphoneNo, @Param("duty")String duty);
	
	@Query("from IpUser where userId=:userId and isEnabled=:isEnabled")
	IpUser findByUserIdAndisEnabled(@Param("userId")String userId, @Param("isEnabled")String isEnabled);

	@Modifying(clearAutomatically = true) @Query("update IpUser ipUser set ipUser.userPicUrl=:userPicUrl where ipUser.userId=:userId")
	void updateUserHeaderimageByUserId(@Param("userId")String userId, @Param("userPicUrl")String userPicUrl);
	
	IpUser findUserByUserId(String userId);
	
	@Query("from IpUser ih where (userEmail like:loginName or loginName like:loginName or cellphoneNo like:loginName) and isEnabled=:isEnabled")
	List<IpUser> findUserByUserEmailOrLoginNameOrCellphoneNoLike(@Param("loginName")String loginName, @Param("isEnabled")String isEnabled);

	@Query("from IpUser ih where ih.hirerId=:hirerId")
	List<IpUser> findUserByHirerId(@Param("hirerId")String hirerId);
	
	@Modifying
    @Query("delete from IpUser where userId = :userId")
	void deleteByUserId(@Param("userId")String userId);
	
	@Modifying(clearAutomatically = true) @Query("update IpUser ipUser set ipUser.password=:password,ipUser.salt=:salt where ipUser.userId=:userId")
	void updatePwdByUserId(@Param("userId")String userId,@Param("password")String password,@Param("salt")String salt);
}
