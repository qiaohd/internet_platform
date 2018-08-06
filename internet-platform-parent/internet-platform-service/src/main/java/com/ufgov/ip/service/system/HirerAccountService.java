package com.ufgov.ip.service.system;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.ufgov.ip.api.system.HirerAccountServiceI;
import com.ufgov.ip.dao.sysmanager.ICompanyDao;
import com.ufgov.ip.dao.system.IpHirerMapper;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.utils.CopyPropertiesUtil;

/**
 * 用户管理服务层
 * 书写readonly是说明没有特殊标识服务层只有只读事物
 * @author guangsa
 *
 */
@Service(version = "0.0.1")
//@Transactional(readOnly=true)
public class HirerAccountService implements HirerAccountServiceI{

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;

	private static Logger logger = LoggerFactory.getLogger(UserAccountService.class);

	@Autowired
	private IpHirerMapper ipHirerMapper;
	
	@Autowired
	private ICompanyDao coDao;

	public void setipHirerMapper(IpHirerMapper ipHirerMapper) {
		this.ipHirerMapper = ipHirerMapper;
	}
	
	public void setCoDao(ICompanyDao coDao) {
		this.coDao = coDao;
	}



	/**
	 * 根据租户名称得到相应的用户信息
	 * @param loginName
	 * @return
	 */
	public IpHirer findHirerByLoginName(String loginName) {
		IpHirer ipHirer=new IpHirer();
		ipHirer.setLoginName(loginName);
		return ipHirerMapper.findHirerByCondition(ipHirer);
	}
	
	/**
	 * 根据租户电话得到相应的用户信息
	 * @param loginName
	 * @return
	 */
	public IpHirer findHirerByCellphoneNo(String cellphoneNo) {
		IpHirer ipHirer=new IpHirer();
		ipHirer.setCellphoneNo(cellphoneNo);
		return ipHirerMapper.findHirerByCondition(ipHirer);
	}
	
	/**
	 * 根据租户邮箱得到相应的用户信息
	 * @param loginName
	 * @return
	 */
	public IpHirer findHirerByEmail(String email) {
		IpHirer ipHirer=new IpHirer();
		ipHirer.setEmail(email);
		return ipHirerMapper.findHirerByCondition(ipHirer);
	}
	
	public IpHirer findHirerByEmailOrLoginNameOrCellphoneNo(String loginName){
		return ipHirerMapper.findHirerByEmailOrLoginNameOrCellphoneNo(loginName);
	}
	
	public IpHirer findHirerByPassword(String pwd){
		IpHirer ipHirer=new IpHirer();
		ipHirer.setPassword(pwd);
		return ipHirerMapper.findHirerByCondition(ipHirer);
	}

	public IpHirer findHirerByHirerId(String hirerId) {
		IpHirer ipHirer=new IpHirer();
		ipHirer.setHirerId(hirerId);
		return ipHirerMapper.findHirerByCondition(ipHirer);
	}
		
	public IpHirer findHirerByHirerIdMybatis(String hirerId) {
		IpHirer ipHirer=new IpHirer();
		ipHirer.setHirerId(hirerId);
	    ipHirer= ipHirerMapper.findHirerByCondition(ipHirer);
		return ipHirer;
	}
	
	@Transactional
	public boolean saveHirerInfo(IpHirer ipHirer) {

		try {
			//IpHirer findByHirerName = ipHirerMapper.findByHirerName(ipHirer.getHirerName());
			//ipHirer.setHirerId(findByHirerName.getHirerId());
			IpHirer ipHirerQ=new IpHirer();
			ipHirerQ.setHirerId(ipHirer.getHirerId());
			IpHirer findHirerByHirerId = ipHirerMapper.findHirerByCondition(ipHirerQ);
			CopyPropertiesUtil.setProperty(findHirerByHirerId);
			CopyPropertiesUtil.copyProperty(findHirerByHirerId, ipHirer);
			ipHirerMapper.deleteByPk(findHirerByHirerId);
			ipHirerMapper.insert(findHirerByHirerId);
			String hirerId = ipHirer.getHirerId();
			coDao.updateCompanyByHirerIdAndLevelNum(hirerId,1,ipHirer.getHirerShortName());
		} catch (Exception e) {
			logger.error("注册用户失败!"+e);
			try {
				throw new Exception();
			} catch (Exception e1) {
               
				e1.printStackTrace();
			}
		}
		return true;
		
		
		
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateHirerByHirerId(String hirerId, String path) {
         IpHirer ipHirer =new IpHirer();
         ipHirer.setHirerId(hirerId);
         ipHirer.setHirerLogoUrl(path);        
		ipHirerMapper.update(ipHirer);
		
	}

	public List<IpHirer> findAll() {
		// TODO 自动生成的方法存根
		return ipHirerMapper.findHirerAll();
	}
	
	public void updateHirerHeaderimageByHirerId(String hirerId,String url){
		
		 IpHirer ipHirer =new IpHirer();
         ipHirer.setHirerId(hirerId);
         ipHirer.setHirerPicUrl(url); 
		ipHirerMapper.update(ipHirer);
	}

	
	
}
