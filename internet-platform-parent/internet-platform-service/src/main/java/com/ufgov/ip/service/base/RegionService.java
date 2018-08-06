package com.ufgov.ip.service.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.base.RegionServiceI;
import com.ufgov.ip.dao.base.IPRegionDao;
import com.ufgov.ip.dao.system.IpHirerMapper;
import com.ufgov.ip.entity.base.IpRegion;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.serviceutils.QueryCombineUtil;
import com.ufgov.ip.utils.UUIDTools;

@Service(version = "0.0.1")
public class RegionService implements RegionServiceI{
	
	@Autowired 
	IPDataTableService iPDataTableService;
	@Autowired
	IPRegionDao iPRegionDao;
	@Autowired
	private IpHirerMapper ipHirerMapper;

	public List<IpRegion> getRegionPage(Map<String, Object> searchParams,
			Sort sort) {
		// Specification<IpRegion> spec = iPDataTableService.buildSpecification(searchParams,IpRegion.class);
		// String theCode="____00";
		
		
		String theCode="%00";
		List<IpRegion> ipRegions= iPRegionDao.findIpRegionByTheCodeLike(theCode);
		return ipRegions;
	}
	
	
public List<IpRegion> getRegionPageByHire(Map<String, Object> searchParams,Sort sort) {
		
		List<IpRegion> ipRegions =new ArrayList<IpRegion>();
		Specification<IpHirer> spec = iPDataTableService.buildSpecification(searchParams,IpHirer.class);
		// String theCode="____00";
		if(searchParams.isEmpty()){
			return ipRegions;
		}
		String conditionSql=QueryCombineUtil.QueryConditionCombine(searchParams);
		List<IpHirer> ipHirerList=ipHirerMapper.findAll(conditionSql);
		if(ipHirerList.size()>0){
			IpHirer ipHirer=ipHirerList.get(0);
			String region = ipHirer.getRegion();
			if(region!=null && region!=""){
				String[] regionArray  = region.split(",");
				String proCode="";
				if (regionArray.length>0) {
					proCode=regionArray[0];
					if(proCode.length()>2){
						  proCode=proCode.substring(0, 2);
					  }
					ipRegions= iPRegionDao.findIpRegionByTheCodeLike(proCode+"%");
				}
			}
		}
		return ipRegions;
	}
	
	
	
	
	public List<IpRegion> getRegionProAndCity(Map<String, Object> searchParams) {
		String theCode="%00";
		List<IpRegion> ipRegions= iPRegionDao.findIpRegionByTheCodeLike(theCode);
		return ipRegions;
	}
	
	public List<IpRegion> getRegionByParentId(String theId) {
		// TODO 自动生成的方法存根
		List<IpRegion> ipRegions= iPRegionDao.findByParentId(theId);
		return ipRegions;
	}

	/**
	 * 保存区划 
	 * @param ipRegion
	 * @param resultMap
	 */
	public Map<String, String> saveEntity(IpRegion ipRegion,Map<String, String> resultMap) {
		// TODO 自动生成的方法存根
		boolean chceck =checkRegionCode(ipRegion, resultMap);
		if(chceck){
			if (ipRegion.getTheId()==null || ipRegion.getTheId()=="") {
				ipRegion.setTheId(UUIDTools.uuidRandom());
				ipRegion.setCreateDate(new Date());
			}
			ipRegion.setUpdateDate(new Date());
			iPRegionDao.save(ipRegion);
		}
		return resultMap;
	}

	public boolean checkRegionCode(IpRegion ipRegion,
			Map<String, String> resultMap) {
		IpRegion ipRegionByCode= iPRegionDao.findIpRegionByTheCode(ipRegion.getTheCode());
		if(ipRegionByCode!=null){
			//新增
			if(("").equals(ipRegion.getTheId()) || ipRegion.getTheId()==null){
				resultMap.put("result", "fail");
				resultMap.put("reason", "区划编码已存在，请确认！");
				 return false;
			}else{//编辑
				if(!ipRegion.getTheId().equals(ipRegionByCode.getTheId())){
					resultMap.put("result", "fail");
					resultMap.put("reason", "区划编码已存在，请确认！");
					 return false;
				}
					
			}
			
		}
		return true;
	}

	public Map<String, String> delEntity(String theId, Map<String, String> resultMap) {
		// TODO 自动生成的方法存根
		List<IpRegion> childList = iPRegionDao.findByParentId(theId);//该菜单下的所有子菜单
		 if(childList.size()!=0){
			 resultMap.put("result", "fail");
			 resultMap.put("reason", "所选菜单包含子菜单，请逐级删除！");
			 return resultMap;
		 }
		 iPRegionDao.delete(theId);
		 return resultMap;
	}


	public void checkRegion(String theId,String theCode, Map<String, String> resultMap) {
		// TODO 自动生成的方法存根
		IpRegion ipRegion= iPRegionDao.findIpRegionByTheCode(theCode);
		if(ipRegion!=null){
			if(!theId.equals(ipRegion.getTheId())){
				
			}
		}
	}




	

}
