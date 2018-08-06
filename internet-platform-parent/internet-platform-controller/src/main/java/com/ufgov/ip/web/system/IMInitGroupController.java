package com.ufgov.ip.web.system;

import com.ufgov.ip.api.system.IPUserCompanyQueryServiceI;
import com.ufgov.ip.utils.IMAPIHelperUtil;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/iminitgroup")
public class IMInitGroupController {

	@Autowired
	private IPUserCompanyQueryServiceI ipUserCompanyQueryService;
	
	public IPUserCompanyQueryServiceI getIpUserCompanyQueryService() {
		return ipUserCompanyQueryService;
	}
	public void setIpUserCompanyQueryService(
			IPUserCompanyQueryServiceI ipUserCompanyQueryService) {
		this.ipUserCompanyQueryService = ipUserCompanyQueryService;
	}
	private IMAPIHelperUtil imHelperUtil=IMAPIHelperUtil.getSingletonIMAPIHelperUtil();
	private static int groupId=0;

	private HashMap<String, String> getParam() {

		HashMap<String, String> map = new HashMap<String, String>();
		try {

			Resource resource = new ClassPathResource("application.properties");
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
		
			String eptId = (String) props.get("im.eptid");
			String appId = (String) props.get("im.appid");
			String clientID = (String) props.get("im.clientid");
			String clientSecret = (String) props.get("im.clientsecret");
			String imClient = "http://"+(String) props.get("im.client")+"/sysadmin/rest/";//(String) props.get("im.client");
			
			map.put("eptId", eptId);
			map.put("appId", appId);
			map.put("clientID", clientID);
			map.put("clientSecret", clientSecret);
			map.put("imClient", imClient);

			// System.out.println(user +"====="+ password);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return map;
	}
	Map<String, String> paramMap = getParam();
	// 企业ID
	private final String eptId = paramMap.get("eptId");
	// 应用ID
	private final String appId = paramMap.get("appId");
	// Client ID
	private final String clientID = paramMap.get("clientID");
	// Client Secret
	private final String clientSecret = paramMap.get("clientSecret");
	// im Client
	private final String imClient = paramMap.get("imClient");
	/**
	 * 创建群组功能
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST,value="/createGroup")
	public @ResponseBody Map<String,String> initGroup(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("result", "success");
		String hirerId = request.getParameter("hirerId");
		Map<String,List<String>> mapList=ipUserCompanyQueryService.getChargeCompanyCode(hirerId);
		
		Iterator<Entry<String, List<String>>> it = mapList.entrySet().iterator();
		  while (it.hasNext()) {
			  Entry<String, List<String>> entry = it.next();
			  String operator=entry.getKey();
			  List<String> li=entry.getValue();
			 // List<String> liResult=imHelperUtil.handleOldUserLoginName(li);
			  String[] operand=li.toArray(new String[li.size()]);
			  createGroup(operator,operand,operator+"群"+groupId,200,"群组");
			  SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			  System.out.println(df.format(new Date())+"创建群成功 群主:"+operator+" 群名称:"+operator+"群"+groupId);
			  groupId++;
		  }
        return resultMap;
	}
	/**
	 * 创建群组 并添加成员
	 * @param operator 群组的群主,创建人	
	 * @param operand 被邀请人的用户名列表
	 * @param naturalLanguageName 群组的昵称
	 * @param maxUsers 	群组的最大人数
	 * @param etpId 企业ID
	 * @param appId 应用ID
	 * @param tags 群类型字段,String类型的数组
	 * @return
	 */
	public String createGroup(String operator,String[] operand,String naturalLanguageName,int maxUsers,String tags)
	{
		JSONObject appToken=imHelperUtil.getAPPToken(eptId, appId, clientID, clientSecret);
		String token=appToken.getString("token");
		//创建群之前先创建群里要添加的用户 否则报错
		for(int i=0;i<operand.length;i++)
		{
			createUser(token,operand[i]);
		}
		String url=imClient+"remote/room/create?token="+token;
		String method="POST";
		String[] params=new String[]{"operator="+operand[0],"naturalLanguageName="+naturalLanguageName,"maxUsers="+maxUsers,"etpId="+eptId,"appId="+appId,"tags="+tags};
		String result=imHelperUtil.postJson(url, method, operand,params);
		return result;
	}
	
	
	/**
	 * 创建用户
	 * @param appToken
	 * @param username
	 */
	private void createUser(String appToken,String username)
	{
		String[] params=new String[]{"username="+username};
		String url=imClient+"remote/user/"+eptId+"/"+appId+"/create?token="+appToken;
		String result=imHelperUtil.postJson(url, "POST",null,params);
	}
}
