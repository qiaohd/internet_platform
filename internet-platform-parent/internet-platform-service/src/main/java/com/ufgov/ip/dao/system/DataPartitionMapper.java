package com.ufgov.ip.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ufgov.ip.entity.system.IpDataPartition;
import com.ufgov.ip.entity.system.IpHirer;
import com.yonyou.iuap.mybatis.type.PageResult;
import com.yonyou.iuap.persistence.mybatis.anotation.MyBatisRepository;

@MyBatisRepository
public interface DataPartitionMapper {
	
	public void insert(IpDataPartition ipDataPartition);

	public IpDataPartition selectByUrl(String dbUrl);

	public List<IpDataPartition> getMainPage();

	public List<IpHirer> getHirerInfo();
	
	public PageResult<IpHirer> getHirerInforetrievePage(PageRequest pageRequest);

	public IpDataPartition getDataPartitionByCondition(IpDataPartition ipDataPartition);
		
    public int getHirerInfoCounts(@Param("isVaild") String isVaild); //查询条数
    
    public List<IpHirer> getHirerInfoMy(@Param("startRow") int startRow,@Param("pageSize") int pageSize,@Param("isVaild") String isVaild); //查询记录
}
