package com.ufgov.ip.service.system;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.system.IpUserInfoServiceI;
import com.ufgov.ip.dao.system.IpUserInfoMapper;
import com.ufgov.ip.entity.system.IpUserInfo;
import com.ufgov.ip.utils.UUIDTools;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * aaaaService
 * @author winner
 * @version 2016-10-08
 */
//@Service(version = "0.0.1")
@Transactional(readOnly = true)
public class IpUserInfoService implements IpUserInfoServiceI {

    @Autowired
	private IpUserInfoMapper ipUserInfoMapper;


	public IpUserInfo get(String id) {
		return ipUserInfoMapper.get(id);
	}
	
	public List<IpUserInfo> findList(IpUserInfo ipUserInfo) {
		return ipUserInfoMapper.findList(ipUserInfo);
	}
	
	
	
	public List<IpUserInfo> findAllList(){
	    return ipUserInfoMapper.findAllList();
	}
	
	@Transactional(readOnly = false)
	public void update(IpUserInfo ipUserInfo){
	   ipUserInfoMapper.update(ipUserInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(IpUserInfo ipUserInfo) {
		ipUserInfo.setUserId(UUIDTools.uuidRandom());
		ipUserInfoMapper.save(ipUserInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		ipUserInfoMapper.delete(id);
	}
	
}