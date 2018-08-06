package com.ufgov.ip.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ufgov.ip.entity.base.IpDictionary;


public interface IPDictDao extends PagingAndSortingRepository<IpDictionary, String>,JpaSpecificationExecutor<IpDictionary>{
	
	@Query("from IpDictionary idic where dicType=:dicType")
	IpDictionary findIpDictionaryByDicType(@Param("dicType")String dicType);
	
	@Query("from IpDictionary order by dicType")
	List<IpDictionary> findAllDictionaries();
}
