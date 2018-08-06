package com.ufgov.ip.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.ufgov.ip.entity.base.IpDictionaryDetail;

@Service
public interface IPDictDetailDao extends PagingAndSortingRepository<IpDictionaryDetail, String>,JpaSpecificationExecutor<IpDictionaryDetail>{
	
	@Query("from IpDictionaryDetail idetail where dicId=:dicId")
	List<IpDictionaryDetail> findIpDictionaryDetailByDicId(@Param("dicId")String dicId);
	
	
	IpDictionaryDetail findIpDictionaryDetailByDicIdAndDetailKey(String dicId,String detailKey);
	
	IpDictionaryDetail findIpDictionaryDetailByDicIdAndDetailInfo(String dicId,String detailinfo);
	
}
