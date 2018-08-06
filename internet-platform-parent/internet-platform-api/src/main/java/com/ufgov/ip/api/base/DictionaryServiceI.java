package com.ufgov.ip.api.base;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.ufgov.ip.entity.base.IpDictionary;
import com.ufgov.ip.entity.base.IpDictionaryDetail;


public interface DictionaryServiceI {

	public List<IpDictionaryDetail> getDicPage(
			Map<String, Object> searchParams, Sort sort) ;
	
	
	public List<IpDictionary> getDicInfo();

	@Transactional
	public void saveEntity(IpDictionaryDetail ipDictionaryDetail,
			Map<String, String> resultMap);
	
	public void getErrorMsg(Map<String, String> resultMap,String flg, String reason);

	public void checkDicType(String dicId, String dicType,
			Map<String, String> resultMap);

	public void checkDetailInfo(String theId,String dicId, String detailInfo,
			Map<String, String> resultMap);

	public void checkDetailKey(String theId, String dicId, String detailKey,
			Map<String, String> resultMap);

	@Transactional
	public void delDicInfo(String theId, String dicId,
			Map<String, String> resultMap);

}
