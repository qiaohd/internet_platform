package com.ufgov.ip.api.system;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.sysmanager.IpRole;
import com.ufgov.ip.entity.sysmanager.IpUserRole;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.IpUserCompany;

public interface UserAccountServiceI {

	/**
	 * 根据用户名称得到相应的用户信息
	 * @param loginName
	 * @return
	 */
	public IpUser findUserByLoginName(String loginName);

	
	/**
	 * 根据用户手机得到相应的用户信息
	 * @param loginName
	 * @return
	 */
	public IpUser findUserByCellphoneNo(String cellphoneNo);
	
	public IpUser findUserByHirerIdAndUserType(String hirerId,String userType);
	/**
	 * 根据用户邮箱得到相应的用户信息
	 * @param loginName
	 * @return
	 */
	public IpUser findUserByUserEmail(String userEmail);
	public IpUser findUserByUserEmailOrLoginNameOrCellphoneNo(String logicName);
	public String saveIpUserEntity(IpUser ipUserEntity,
			IpCompany ic, IpRole ir,List list,Map<String, String> resultMap);
		
	public IpUserCompany setCoProperties(IpCompany ic,IpUser ipUserEntity);
	public IpUserRole setRoleProperties(IpRole ir,String coId, IpUser ipUserEntity,String flag);
	
	public IpUser checkIpUser(IpUser ipUserEntity, Map<String, String> resultMap);
	
	
	public IpUserCompany checkIpUserCompany(IpUserCompany ipUserCompany, Map<String, String> resultMap);
	
	public IpUserRole checkIpUserRole(IpUserRole ipUserRole, Map<String, String> resultMap);
	
	/**
	 * 错误信息 
	 * @param resultMap
	 * @param reason
	 */
	public void getErrorMsg(Map<String, String> resultMap,String reason);

	public List<IpUser> findUserAll();
	
	public Page<IpUser> getUserAll(Map<String, Object> searchParams,
			int pageNumber,int pageSize) throws UnsupportedEncodingException;


	public boolean checkEmployeeNo(String employeeNo);


	public IpUser findUserByEmployeeNo(String employeeNo);

	public void updateUserByUserId(String userId, String isEnabled);

	public void saveIpUser(IpUser ipUser);

	public IpUser findIpUserByLoginName(String loginName);
	
	public List<IpUser> getUserPage(Map<String, Object> searchParams,
			Sort sort);
	
	public List<IpUser> getUserByDeptPage(Map<String, Object> searchParams,
			Sort sort);
	
	/**
     * 创建动态查询条件组合.
     */
    public Specification<IpUser> buildSpecification(Map<String, Object> searchParams);
    
    
    public Map<String,String> getHirerIdAndCoIdBySearchParams(Map<String, Object> searchParams);
    
    /**
	 * 通过jdbc查询的结果构建 list<实体>
	 * @param ipRoleMenuList
	 * @return
	 */
	public List<IpUser> buildIpMenuEntityList(List ipUserList);

	public IpUser findUserByUserId(String userId);
	public Page<IpUser> findUserByUserEmailOrLoginNameOrCellphoneNoLike(
			Map<String, Object> searchParams, int pageNumber,int pageSize) throws UnsupportedEncodingException;


	public Map<String, String> getUserBySearchParams(
			Map<String, Object> searchParams);


	public List<IpUser> findUserByHirerId(String hirerId);
	
	public void saveIpUserInfoByHirerIdAndUserType(String hirerId,String userType, String userSex,String userEmail,String phoneNo,String cellphoneNo,String duty);

	public void updateUserHeaderimageByUserId(String userId,String url);

	public void updatePwdByUserId(String userId,String pwd,String salt);
}
