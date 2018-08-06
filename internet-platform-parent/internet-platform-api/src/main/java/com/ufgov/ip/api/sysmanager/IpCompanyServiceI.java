package com.ufgov.ip.api.sysmanager;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.ufgov.ip.entity.sysmanager.IpCompany;

/**
 * 用户管理服务层
 * 书写readonly是说明没有特殊标识服务层只有只读事物
 * @author guangsa
 *
 */
public interface IpCompanyServiceI {
	public boolean registercompany(IpCompany co);
	
	public IpCompany findIpCompanyByName(String companyName);
	
	//begin_查询当前部门及所有子部门
	public List<IpCompany> findAllCurDeptAndAllChild(IpCompany ipComapny);
	//end_查询当前部门及所有子部门
	
	
	
	public IpCompany findIpCompanyByNameAndparentCoId(String companyName,String pCoId);
	
	public IpCompany findByCoCodeAndhirerId(String coCode, String hirerId);
	
	public Map<String, String> saveIpCompanyEntity(IpCompany ipCompanyEntity, Map<String, String> resultMap);
	/**
	 * 设置部门详细字段 
	 * @param ipCompanyEntity
	 */
	public void setDeptDetailForIpCompany(IpCompany ipCompanyEntity);
	
	public Map<String, String> updateIpCompanyEntity(IpCompany ipCompanyEntity, Map<String, String> resultMap);
	
	public void setIpCompanyLevelNum(IpCompany ipCompanyEntity);
	
	/**
	 * 验证
	 * @param entity
	 * @param resultMap
	 * @return
	 */
	public Boolean checkIpCompany(IpCompany ipCompanyEntity, Map<String, String> resultMap);
	
	public Boolean checkIpCompanyForUpdate(IpCompany ipCompanyEntity, Map<String, String> resultMap);
	
	
	/**
	 * 通过部门ID查询部门信息 
	 * @param coId 部门ID
	 * @return
	 */
	public IpCompany findIpCompanyByCoId(String coId);
	
	public List<IpCompany> findCompanyByParentCoId(String parentCoId);
	public void getErrorMsg(Map<String, String> resultMap,String reason);
		

	public List<IpCompany> getCompanyPage(Map<String, Object> searchParams);
	/**
     * 创建动态查询条件组合.
     */
    public Specification<IpCompany> buildSpecification(Map<String, Object> searchParams);

	public IpCompany findByCoId(String coId);

	public void updateCompanyBycoId(String coId, String parentCoId);
    
	public String delete(IpCompany findByCoId);

	public List<IpCompany> findCompanyByDeptDetailLike(String deptDetail,String hirerId);
	
	public List<IpCompany> findAll();

	public List<IpCompany> findCompanyByHirerId(String hirerId);
	
	public void deletePart(IpCompany findByCoId);
	
	public List<IpCompany> buildIpMenuEntityList(List coIdMapLists);
}
