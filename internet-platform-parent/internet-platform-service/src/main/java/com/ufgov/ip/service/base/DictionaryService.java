package com.ufgov.ip.service.base;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.SearchFilter;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.base.DictionaryServiceI;
import com.ufgov.ip.dao.base.IPDictDao;
import com.ufgov.ip.dao.base.IPDictDetailDao;
import com.ufgov.ip.dao.baseimpl.IPDictDetailJdbcDaoImpl;
import com.ufgov.ip.entity.base.IpDictionary;
import com.ufgov.ip.entity.base.IpDictionaryDetail;
import com.ufgov.ip.utils.UUIDTools;

@Service(version = "0.0.1")
public class DictionaryService implements DictionaryServiceI{
	@Autowired
	IPDictDetailJdbcDaoImpl iPDictDetailJdbcDao;
	@Autowired
	IPDictDao ipDictDao;
	@Autowired
	IPDictDetailDao ipDictDetailDao;

	public List<IpDictionaryDetail> getDicPage(
			Map<String, Object> searchParams, Sort sort) {
		
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		SearchFilter searchFilterBydicType =filters.get("EQ_dicType");
		SearchFilter searchFilterBydicName =filters.get("LIKE_dicName");
		String dicType = "";
		String dicName = "";
		if (searchFilterBydicType!=null) {
			dicType = (String) searchFilterBydicType.value;
		}
		if (searchFilterBydicName!=null) {
			dicName = (String) searchFilterBydicName.value;
		}
		
		 List<IpDictionaryDetail> ipDictionaryDetailList= iPDictDetailJdbcDao.findDicAndDetail(dicType,dicName);
		return ipDictionaryDetailList;
	}
	
	
	public List<IpDictionary> getDicInfo() {
		// TODO 自动生成的方法存根
		List<IpDictionary>  ipDictionaries=ipDictDao.findAllDictionaries();
		return ipDictionaries;
	}

	@Transactional
	public void saveEntity(IpDictionaryDetail ipDictionaryDetail,
			Map<String, String> resultMap) {
		// TODO 自动生成的方法存根
		String flg="0";// 检查通过，可以保存 
		String theId=ipDictionaryDetail.getTheId();
		String dicId=ipDictionaryDetail.getDicId();
		String detailInfo=ipDictionaryDetail.getDetailInfo();
		String detailKey=ipDictionaryDetail.getDetailKey();
		IpDictionary ipDictionary =new IpDictionary();
		ipDictionary.setDicId(dicId);
		ipDictionary.setDicType(ipDictionaryDetail.getDicType());
		ipDictionary.setDicName(ipDictionaryDetail.getDicName());
		IpDictionary ipDictionaryByDicType= ipDictDao.findIpDictionaryByDicType(ipDictionaryDetail.getDicType());
		IpDictionaryDetail ipDictionaryDetailByKey = ipDictDetailDao.findIpDictionaryDetailByDicIdAndDetailKey(dicId, detailKey);
// 		IpDictionaryDetail ipDictionaryDetailByInfo = ipDictDetailDao.findIpDictionaryDetailByDicIdAndDetailInfo(dicId, detailInfo);
		if(("").equals(theId)||theId==null){// 新增枚举详细 
			if(("").equals(dicId)||dicId==null){//新增枚举类型 
				if(ipDictionaryByDicType!=null){
					ipDictionary.setDicId(ipDictionaryByDicType.getDicId());
					ipDictionaryDetail.setDicId(ipDictionaryByDicType.getDicId());
					IpDictionaryDetail ipDicDetailByKey = ipDictDetailDao.findIpDictionaryDetailByDicIdAndDetailKey(ipDictionaryByDicType.getDicId(), detailKey);
					if(ipDicDetailByKey!=null){
						getErrorMsg(resultMap,"detailKey","该枚举类型下存在相同的键值！");
						flg="1";
					}
				}else{		
					String newDicId =UUIDTools.uuidRandom();
					ipDictionary.setDicId(newDicId);				
					ipDictionaryDetail.setDicId(newDicId);
					ipDictionaryDetail.setTheId(UUIDTools.uuidRandom());
				}
				
			}else{// 修改枚举类型
				if(ipDictionaryByDicType!=null){
					if(!(ipDictionaryByDicType.getDicId()).equals(dicId)){
						getErrorMsg(resultMap,"dicType","存在相同的枚举类型！");
						flg="1";
					}
				}
				if(ipDictionaryDetailByKey!=null){
					getErrorMsg(resultMap,"detailKey","该枚举类型下存在相同的键值！");
					flg="1";
				}				
				ipDictionary.setDicId(dicId);
//				if(ipDictionaryDetailByInfo!=null){
//					getErrorMsg(resultMap,"该枚举类型下存在相同的标签！");
//					return;
//				}
			}
			ipDictionaryDetail.setTheId(UUIDTools.uuidRandom());
		}else{
			if(ipDictionaryDetailByKey!=null){
				if(!(ipDictionaryDetailByKey.getTheId()).equals(theId)){
					getErrorMsg(resultMap,"detailKey","该枚举类型下存在相同的键值！");
					flg="1";
				}
			}
//			if(ipDictionaryDetailByInfo!=null){
//				if(!(ipDictionaryDetailByInfo.getTheId()).equals(theId)){
//					getErrorMsg(resultMap,"该枚举类型下存在相同的标签！");
//					return;
//				}
//			}
			if(ipDictionaryByDicType!=null){
				if(!(ipDictionaryByDicType.getDicId()).equals(dicId)){
					getErrorMsg(resultMap,"dicType","存在相同的枚举类型！");
					flg="1";
				}
			}
		}
		if(flg=="1" || "1".equals(flg)){
			return;
		}
		ipDictDao.save(ipDictionary);
		ipDictDetailDao.save(ipDictionaryDetail);
				
	}
	
	public void getErrorMsg(Map<String, String> resultMap,String flg, String reason) {
		resultMap.put("result", "fail");
		resultMap.put(flg, reason);
		resultMap.put("reason", "保存失败！");
	}

	public void checkDicType(String dicId, String dicType,
			Map<String, String> resultMap) {
		// TODO 自动生成的方法存根
		IpDictionary ipDictionaryByDicType= ipDictDao.findIpDictionaryByDicType(dicType);
		if(ipDictionaryByDicType!=null){
			if("".equals(dicId) || dicId==null){
				getErrorMsg(resultMap,"dicType","存在相同的枚举类型，请确认！");
			}else{
				if(!(dicId).equals(ipDictionaryByDicType.getDicId())){
					getErrorMsg(resultMap,"dicType","存在相同的枚举类型，请确认！");
				}
			}
		}
		
	}

	public void checkDetailInfo(String theId,String dicId, String detailInfo,
			Map<String, String> resultMap) {
		// TODO 自动生成的方法存根
		IpDictionaryDetail ipDictionaryDetailByInfo = ipDictDetailDao.findIpDictionaryDetailByDicIdAndDetailInfo(dicId, detailInfo);
		//IpDictionaryDetail ipDictionaryDetailByInfo = ipDictDetailDao.findIpDictionaryDetailByDicIdAndDetailKey(dicId, detailKey);
		if(ipDictionaryDetailByInfo!=null){
			if("".equals(theId) || theId==null){
				getErrorMsg(resultMap,"detailInfo","该枚举类型下存在相同的标签！");
				return;
			}else{
				if(!(ipDictionaryDetailByInfo.getTheId()).equals(theId)){
					getErrorMsg(resultMap,"detailInfo","该枚举类型下存在相同的标签！");
					return;
				}
				
			}
		}
		
	}

	public void checkDetailKey(String theId, String dicId, String detailKey,
			Map<String, String> resultMap) {
		IpDictionaryDetail ipDictionaryDetailByKey = ipDictDetailDao.findIpDictionaryDetailByDicIdAndDetailKey(dicId, detailKey);
		if(ipDictionaryDetailByKey!=null){
			if("".equals(theId) || theId==null){
				getErrorMsg(resultMap,"detailKey","该枚举类型下存在相同的键值！");
				return;
			}else{
				if(!(ipDictionaryDetailByKey.getTheId()).equals(theId)){
					getErrorMsg(resultMap,"detailKey","该枚举类型下存在相同的键值！");
					return;
				}
				
			}
		}
		
	}

	@Transactional
	public void delDicInfo(String theId, String dicId,
			Map<String, String> resultMap) {
		// TODO 自动生成的方法存根
		if(theId!="" && theId!=null){
			ipDictDetailDao.delete(theId);
		}
		
		List<IpDictionaryDetail> ipDictionaryDetails =ipDictDetailDao.findIpDictionaryDetailByDicId(dicId);
		if(ipDictionaryDetails.size()==0){
			ipDictDao.delete(dicId);
		}
			
	}

	

}
