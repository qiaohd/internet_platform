package com.ufgov.ip.web.base;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ufgov.ip.commons.web.ControllerPermissionException;

@Component
@Scope("prototype")
@RequestMapping(value = "/PermissionAuthIP")
public class PermissionAuthController extends ControllerPermissionException{
    /*未审核请假单页面资源权限验证Begin*/
	@RequiresPermissions("askForLeave:task:approve")
    @RequestMapping(method = RequestMethod.POST,value="IfApprovePermessionAuthed")
	public  @ResponseBody Map<String, Object> GetBtnPermessionAskForLeave(HttpServletRequest request, HttpServletResponse response){
    	Map<String, Object> map=new HashMap<String, Object>();
    	map.put("flag","true");
    	return map;
    }
    /*未审核请假单页面资源权限验证End*/
}
