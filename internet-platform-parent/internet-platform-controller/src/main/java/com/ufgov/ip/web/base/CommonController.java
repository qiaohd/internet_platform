package com.ufgov.ip.web.base;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ufgov.ip.api.base.IPCommonServiceI;

@Component("base.commonController")
@Scope("prototype")
@RequestMapping(value = "ipcommon")
public class CommonController {	
	
	@Autowired
	IPCommonServiceI ipCommonService;
	@RequestMapping(method = RequestMethod.GET,value="getDictionaryDetailsByEnumerationType")
	public  @ResponseBody Map<String, Object> saveDic(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", "success");
		String enumerationType=request.getParameter("enumerationType");
		try
		{
			Map<String, String> dataMap=ipCommonService.getDictDetailByEnumtype(enumerationType);
			resultMap.put("data",dataMap);
		}
		catch(Exception e)
		{
			resultMap.put("result", "fail");
			e.printStackTrace();
		}
		return resultMap;
	}
}
