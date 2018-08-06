package com.ufgov.ip.commons.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class ControllerPermissionException {
	/**
	 * 授权登录异常
	 */
	@ExceptionHandler({AuthorizationException.class})
	@ResponseBody
    public Map<String, Object> authorizationException() {
		Map<String, Object> resultMap=new HashMap<String, Object>();
		resultMap.put("flag", "false");
		return resultMap;
    }
}
