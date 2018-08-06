package com.ufgov.ip.api.base;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;

import com.ufgov.ip.entity.base.IpRegion;

public interface RegionServiceI {
	public List<IpRegion> getRegionPage(Map<String, Object> searchParams,
			Sort sort);
		
     public List<IpRegion> getRegionPageByHire(Map<String, Object> searchParams,Sort sort);
		
	public List<IpRegion> getRegionProAndCity(Map<String, Object> searchParams);
	
	public List<IpRegion> getRegionByParentId(String theId);

	/**
	 * 保存区划 
	 * @param ipRegion
	 * @param resultMap
	 */
	public Map<String, String> saveEntity(IpRegion ipRegion,Map<String, String> resultMap);

	public boolean checkRegionCode(IpRegion ipRegion,
			Map<String, String> resultMap);

	public Map<String, String> delEntity(String theId, Map<String, String> resultMap);


	public void checkRegion(String theId,String theCode, Map<String, String> resultMap);
}
