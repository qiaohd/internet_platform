package com.ufgov.ip.service.system;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.ufgov.ip.utils.UUIDTools;

import yonyou.bpm.rest.request.RestVariable;
import yonyou.bpm.rest.response.historic.HistoricProcessInstanceResponse;

import com.ufgov.ip.dao.system.IpUserInfoKMapper;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.IpUserInfoK;
import com.ufgov.ip.api.system.IpUserInfoKServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.api.system.ProcessServiceI;

import java.util.ArrayList;

import yonyou.bpm.rest.exception.RestException;
/**
 * aaaaaService
 * @author winner
 * @version 2016-10-26
 */
@Service(version = "0.0.1")
@Transactional(readOnly = true)
public class IpUserInfoKService implements IpUserInfoKServiceI {

    @Autowired
	private IpUserInfoKMapper ipUserInfoKMapper;
	
	@Autowired
	private ProcessServiceI bpmServioce;
	
	@Autowired
	protected UserAccountServiceI userAccountService;


	public IpUserInfoK get(String id) {
		return ipUserInfoKMapper.get(id);
	}
	
	public List<IpUserInfoK> findList(IpUserInfoK ipUserInfoK) {
		return ipUserInfoKMapper.findList(ipUserInfoK);
	}
	
	
	
	public List<IpUserInfoK> findAllList(IpUserInfoK ipUserInfoK){
	    return ipUserInfoKMapper.findAllList(ipUserInfoK);
	}
	
	@Transactional(readOnly = false)
	public void update(IpUserInfoK ipUserInfoK){
	   ipUserInfoKMapper.update(ipUserInfoK);
	}
	
		  @Transactional(readOnly = false)
			public void save(IpUserInfoK ipUserInfoK) {
							 	ipUserInfoK.setUserId(UUIDTools.uuidRandom());
			                
				        ipUserInfoKMapper.save(ipUserInfoK);
			}
		
	
	@Transactional(readOnly = false)
	public void delete(IpUserInfoK ipUserInfoK) {
		ipUserInfoKMapper.delete(ipUserInfoK);
	}
	
}