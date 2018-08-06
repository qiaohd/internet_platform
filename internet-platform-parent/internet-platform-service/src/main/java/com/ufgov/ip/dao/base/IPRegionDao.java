package com.ufgov.ip.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ufgov.ip.entity.base.IpRegion;

public interface IPRegionDao extends PagingAndSortingRepository<IpRegion, String>,JpaSpecificationExecutor<IpRegion>{
	
	@Query("from IpRegion where theCode like :theCode order by theCode asc")
	List<IpRegion> findIpRegionByTheCodeLike(@Param("theCode")String theCode);

	@Query("from IpRegion where theCode=:theCode")
	IpRegion findIpRegionByTheCode(@Param("theCode")String theCode);

	List<IpRegion> findByParentId(String theId);

}
