package com.ufgov.ip.api.base;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.ufgov.ip.entity.base.IpDictionary;
import com.ufgov.ip.entity.base.IpDictionaryDetail;
import com.ufgov.ip.entity.base.IpRegion;
import com.ufgov.ip.entity.sysmanager.IpUserRole;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.IpUserCompany;

public interface IPCommonServiceI {
	@Transactional
	public void saveExcelInfo(IpUser ipUser,
			IpUserCompany ipUserCompany, IpUserRole ipUserRole);

	public List<IpRegion> findIpRegionByTheCodeLike(String cityCode);

	public IpRegion findIpRegionByTheCode(String proCode);

	public IpDictionary findIpDictionaryByDicType(String hirertype);

	public List<IpDictionaryDetail> findIpDictionaryDetailByDicId(String dicId); 
	
	public Map<String, String> getDictDetailByEnumtype(String dicType);
}
