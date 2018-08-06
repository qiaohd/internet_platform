package com.ufgov.ip.api.system;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;

import com.ufgov.ip.entity.system.IpDataPartition;
import com.ufgov.ip.entity.system.IpHirer;

public interface DatapartitionServiceI {
	/**
	 * 测试数据库连接 
	 * @param reqMap
	 * @return
	 */
	public Map<String, String> testConn(Map reqMap);
	/**
	 * 保存分区 
	 * @param reqMap
	 * @return
	 */
	public Map<String, String> savePartition(Map reqMap);
	
	/**
	 * 查询信息 
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	public List<IpDataPartition> getMainPage();
	
	/**
	 * 查询租户信息
	 * @return
	 */
	public List<IpHirer> getHirerInfo(PageRequest pageRequest);
	
	public Map<String, String> auditHirerInfo(String hirerId);

	public List<IpHirer> getHirerInfo(int pageNumber, int pageSize,String isVaild);
	
	public int getHirerInfoCounts(String isVaild);
	
}
