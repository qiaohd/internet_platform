package com.ufgov.ip.api.system;

import java.util.List;
import java.util.Map;

public interface IPUserCompanyQueryServiceI {
	
	public Map<String,List<String>> getChargeCompanyCode(String hireId);
}
