package com.ufgov.ip.api.base;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import com.yonyou.iuap.iweb.entity.DataTable;

public interface IPDataTableServiceI {
	public  <T> Map<String, Object> createSearchParamsStartingWith(String prefix,DataTable<T> dataTable1);
	
	public Sort buildSortRequest(Direction sortType,String... arguments );
    public <T> Specification<T> buildSpecification(Map<String, Object> searchParams,final Class<T> entityClazz);
    public <T> Page<T> getPageEntity(PageRequest pageRequest,List<T> entityList);
}
