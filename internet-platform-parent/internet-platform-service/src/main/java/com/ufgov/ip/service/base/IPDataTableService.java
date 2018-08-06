package com.ufgov.ip.service.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.base.IPDataTableServiceI;
import com.yonyou.iuap.iweb.entity.DataTable;


@Service(version = "0.0.1")
public class IPDataTableService implements IPDataTableServiceI{
	/**
	 * 检索条件拼接 
	 * @param <T>
	 * @param prefix
	 * @return
	 */
	public  <T> Map<String, Object> createSearchParamsStartingWith(String prefix,DataTable<T> dataTable1) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> m = dataTable1.getParams();
		Set<Entry<String, Object>> set = m.entrySet();
		for (Entry<String, Object> entry : set) {
			String key = entry.getKey();
			if (key.startsWith(prefix)) {
				String unprefixed = key.substring(prefix.length());
				params.put(unprefixed, entry.getValue().toString());
			}
		}
		return params;
	}
	
	/**
	 * 排序 
	 * @param sortType 排序类型 
	 * @param arguments 排序字段 
	 * @return
	 */
	public Sort buildSortRequest(Direction sortType,String... arguments ) {
		Sort sort = new Sort(sortType, arguments);
		return sort;
	}
	
	public Map buildSortMapRequest(Direction sortType,String... arguments ) {
		Sort sort = new Sort(sortType, arguments);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("sort", sort);
		return map;
	}
	
	/**
     * 创建动态查询条件组合.
	 * @param <T>
     */
    public <T> Specification<T> buildSpecification(Map<String, Object> searchParams,final Class<T> entityClazz) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<T> spec = DynamicSpecifications.bySearchFilter(filters.values(), entityClazz);
        return spec;
    }
    
    /**
     * 组装page<实体>
     * @param pageRequest
     * @param entityList
     * @return
     */
    public <T> Page<T> getPageEntity(PageRequest pageRequest,List<T> entityList) {
    	
    	int total = entityList.size();
    	List<T> pageEntityList = new ArrayList<T>();
    	// 开始行数 
    	int firstResult= pageRequest.getOffset();
    	// 每页多少数
    	int maxResults=pageRequest.getPageSize();
    	// 结束行数 
    	int endResult=firstResult+maxResults;
		 if(endResult>total){
			 endResult=total;
		 }
		 if(total>firstResult){
	    	 for(int i=firstResult; i<endResult;i++){
	    		 pageEntityList.add(entityList.get(i));
	    	 }
		 }else{
			 pageEntityList = Collections.<T> emptyList();
		 }
    	return new PageImpl<T>(pageEntityList, pageRequest, total);
    }
}
