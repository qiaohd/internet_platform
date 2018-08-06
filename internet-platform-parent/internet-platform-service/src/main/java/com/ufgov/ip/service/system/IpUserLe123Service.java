package com.ufgov.ip.service.system;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.ufgov.ip.utils.UUIDTools;

import yonyou.bpm.rest.request.RestVariable;
import yonyou.bpm.rest.response.historic.HistoricProcessInstanceResponse;

import com.ufgov.ip.dao.system.IpUserLe123Mapper;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.IpUserLe123;
import com.ufgov.ip.api.system.IpUserLe123ServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.api.system.ProcessServiceI;

import java.util.ArrayList;

import yonyou.bpm.rest.exception.RestException;
/**
 * aaaaService
 * @author winner
 * @version 2016-10-26
 */
@Service(version = "0.0.1")
@Transactional(readOnly = true)
public class IpUserLe123Service implements IpUserLe123ServiceI {

    @Autowired
	private IpUserLe123Mapper ipUserLe123Mapper;
	
	@Autowired
	private ProcessServiceI bpmServioce;
	
	@Autowired
	protected UserAccountServiceI userAccountService;


	public IpUserLe123 get(String id) {
		return ipUserLe123Mapper.get(id);
	}
	
	public List<IpUserLe123> findList(IpUserLe123 ipUserLe123) {
		return ipUserLe123Mapper.findList(ipUserLe123);
	}
	
	
	
	public List<IpUserLe123> findAllList(IpUserLe123 ipUserLe123){
	    return ipUserLe123Mapper.findAllList(ipUserLe123);
	}
	
	@Transactional(readOnly = false)
	public void update(IpUserLe123 ipUserLe123){
	   ipUserLe123Mapper.update(ipUserLe123);
	}
	
		  @Transactional(readOnly = false)
			public void save(IpUserLe123 ipUserLe123) {
							 	ipUserLe123.setUserId(UUIDTools.uuidRandom());
			                
				        ipUserLe123Mapper.save(ipUserLe123);
			}
		
	
	@Transactional(readOnly = false)
	public void delete(IpUserLe123 ipUserLe123) {
		ipUserLe123Mapper.delete(ipUserLe123);
	}
	
}