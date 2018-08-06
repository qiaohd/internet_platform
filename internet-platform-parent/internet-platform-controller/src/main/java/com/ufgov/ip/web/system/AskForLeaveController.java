package com.ufgov.ip.web.system;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.web.Servlets;

import com.yonyou.iuap.iweb.entity.DataTable;

import uap.web.httpsession.cache.SessionCacheManager;
import com.ufgov.ip.api.sysmanager.IPRoleServiceI;
import com.ufgov.ip.api.sysmanager.IpCompanyServiceI;
import com.ufgov.ip.api.sysmanager.IpUserCompanyServiceI;
import com.ufgov.ip.api.sysmanager.IpUserRoleServiceI;
import com.ufgov.ip.api.system.AskForLeaveServiceI;
import com.ufgov.ip.api.system.AskTicketListServiceI;
import com.ufgov.ip.api.system.HirerAccountServiceI;
import com.ufgov.ip.api.system.JsonResultServiceI;
import com.ufgov.ip.api.system.ProcessServiceI;
import com.ufgov.ip.api.system.UserAccountServiceI;
import com.ufgov.ip.commons.web.ControllerPermissionException;
import com.ufgov.ip.entity.sysmanager.IpRole;
import com.ufgov.ip.entity.sysmanager.IpUserRole;
import com.ufgov.ip.entity.system.AskForLeave;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.IpUserCompany;
import com.ufgov.ip.utils.PropertyUtilSys;
import com.ufgov.ip.utils.UUIDTools;
//import com.ufgov.ip.util.PropertyUtil;

/**
 * 
 * @author Administrator
 *
 */
@Component("org.AskForLeaveController")
@Scope("prototype")
@RequestMapping(value = "/askForLeave")
public class AskForLeaveController{

	
private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final int HASH_INTERATIONS = 1024;
	// 默认一天
	public static final int COOKIES_MAXAGE = 60 * 60 * 24;
	private static final int SALT_SIZE = 8;
	
    @Autowired
    private SessionCacheManager sessionCacheManager;

    /*@Autowired
	private PlatformTransactionManager transactionManager;*/
    
    @Autowired
	private ProcessServiceI bpmServioce;
    
    @Autowired
    private AskForLeaveServiceI askForLeaveService;
    
    @Autowired
	protected UserAccountServiceI userAccountService;
    
    @Autowired
	protected HirerAccountServiceI hirerAccountService;
    
    @Autowired
	private IpUserCompanyServiceI ipUserCompanyService;
    
    @Autowired
    private AskTicketListServiceI askTicketListService;
    
    @Autowired
    private IpUserRoleServiceI ipUserRoleService;
    
    @Autowired
    private IPRoleServiceI iPRoleService;
    
    @Autowired
    private IpCompanyServiceI ipCompanyService;
    
    @Autowired
	private JsonResultServiceI jsonResultService;

	public JsonResultServiceI getJsonResultService() {
		return jsonResultService;
	}
    
    
    
    public IpCompanyServiceI getIpCompanyService() {
		return ipCompanyService;
	}

	public IPRoleServiceI getiPRoleService() {
		return iPRoleService;
	}

	public IpUserRoleServiceI getIpUserRoleService() {
		return ipUserRoleService;
	}

	public AskTicketListServiceI getAskTicketListService() {
		return askTicketListService;
	}

	DataTable<AskForLeave> leaveDataTable;//请假单dataTable
    
    public IpUserCompanyServiceI getIpUserCompanyService() {
		return ipUserCompanyService;
	}

	public AskForLeaveServiceI getAskForLeaveService() {
		return askForLeaveService;
	}


	//保存请假单并启动流程
    @RequestMapping(method = RequestMethod.POST,value="saveAndStart")
	public @ResponseBody Map<String,String> userlogin(HttpServletRequest request,
			HttpServletResponse response, Model model) {
    	 final Map<String,String> reg=new HashMap<String, String>();
		 reg.put("result", "true");
		 String getbpmEnabledByKey = PropertyUtilSys.getbpmEnabledByKey("bpm.enabled");
		 if("false".equals(getbpmEnabledByKey)){//工作流已经关闭
			 reg.put("result", "Enabled");
			 return reg;
		 }
    	 
		final AskForLeave askforleave=new AskForLeave();
		final IpUser user=new IpUser();
		try {
			BeanUtils.copyProperties(askforleave, request.getParameterMap());
			String type=URLEncoder.encode(askforleave.getType(),"utf-8");
			String reason=URLEncoder.encode(askforleave.getReason(),"utf-8");
			type=URLDecoder.decode(type,"utf-8");
			reason=URLDecoder.decode(reason,"utf-8");
			askforleave.setReason(reason);
			askforleave.setType(type);
			askforleave.setStatus(1);
			
			 IpUser findUserByUserId = userAccountService.findUserByUserId(askforleave.getUsid());
			 String hirerId = findUserByUserId.getHirerId();
			 user.setHirerId(hirerId);
			
		} catch (Exception ex) {
			throw new RuntimeException("获得表单参数时报错了",ex);
		}
			askforleave.setId(UUIDTools.uuidRandom());
			askforleave.setCreateTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()))); 
			//启动流程
			Map<String, String> reg22=askForLeaveService.submitTicketAndRun(reg, askforleave, user);
			 return reg22;
	      }
    
    
    //获得查询参数
    @RequestMapping(method = RequestMethod.GET,value="/getQueryParamInfo")
	public @ResponseBody Map<String, Object> getQueryParamInfo(HttpServletRequest request, HttpServletResponse response){
		
    	String cuser = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (SecurityUtils.getSubject().getPrincipal() != null)
			cuser = (String) SecurityUtils.getSubject().getPrincipal();
		if (cuser == null) {
			return null;
		}
		 IpUser ipUser=userAccountService.findUserByUserEmailOrLoginNameOrCellphoneNo(cuser); //普通用户
		 
		 if(ipUser!=null){
			  String hirerId = ipUser.getHirerId();//获得租户id
		      IpUserCompany ipUserCompany=ipUserCompanyService.findIpUserCompanyByUserId(ipUser.getUserId());
			  String coId = ipUserCompany.getCoId();//获得coId
			  List<IpUserRole> showUserRoleByUserIdAndIsPartTime = ipUserRoleService.showUserRoleByUserIdAndIsPartTime(ipUser.getUserId(), "0");
			  IpUserRole ipUserRole = showUserRoleByUserIdAndIsPartTime.get(0);
			  String roleId = ipUserRole.getRoleId();
			  IpRole findIpRoleByRoleId = iPRoleService.findIpRoleByRoleId(roleId);
			  String roleName = findIpRoleByRoleId.getRoleName();
			 
			  resultMap.put("roleName",roleName);
			  resultMap.put("hirerId",hirerId);
			  resultMap.put("coId",coId);
			  resultMap.put("userId",ipUser.getUserId());
			  
		   }
    	return resultMap;
    }
    
    //获得当前部门已审批的请假单
    @RequestMapping(value = "/getApproved" ,method = RequestMethod.GET)
    public @ResponseBody Object getCurDeptCheckedTicket(@RequestParam(value = "pageIndex", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10")int pageSize, @RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model, ServletRequest request){
    		Map<String,Object> result = new HashMap<String,Object>();
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams = Servlets.getParametersStartingWith(request, "search_");
			PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
			try {
				Page<AskForLeave> accountPage = askTicketListService.getCheckedTicketlistAll(
						searchParams, pageRequest);
				result.put("data", accountPage);
				result.put("flag", "success");
				result.put("msg", "查询数据成功!");
			} catch (Exception e) {
				String errMsg = "查询数据详情失败!";
				result.put("flag", "fail");
				result.put("msg", errMsg);
				logger.error(errMsg, e);
			}
			return result;
    }
    
    
    
    //获得当前部门未审批的请假单
    @RequestMapping(value = "/getNotApproved" ,method = RequestMethod.GET)
    public @ResponseBody Object getCurDeptTicket(@RequestParam(value = "pageIndex", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model, ServletRequest request){
			Map<String,Object> result = new HashMap<String,Object>();
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams = Servlets.getParametersStartingWith(request, "search_");
			PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
			try {
				Page<AskForLeave> accountPage = askTicketListService.getTicketlistAll(
						searchParams, pageRequest);
				result.put("data", accountPage);
				result.put("flag", "success");
				result.put("msg", "查询数据成功!");
			} catch (Exception e) {
				String errMsg = "查询数据详情失败!";
				result.put("flag", "fail");
				result.put("msg", errMsg);
				logger.error(errMsg, e);
			}
			return result;
    }
    
    
    
    /**
     * 消息提醒获得待办事项列表
     * 接口：askForLeave/getNoticeList
     * @param pageNumber
     * @param pageSize
     * @param sortType
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/getNoticeList" ,method = RequestMethod.GET)
    public @ResponseBody Map<String,Object> getCurTaskList(HttpServletRequest request, HttpServletResponse response, Model model){
    	Map<String,Object> result = new HashMap<String,Object>();
		IpUser curUserId = getCurUserId();
		String userId = curUserId.getUserId();
		String hirerId = curUserId.getHirerId();
		try {
			List<AskForLeave> curTaskList = askTicketListService.getCurTaskList(hirerId,userId);
			result.put("data", curTaskList);
			result.put("flag", "success");
			result.put("msg", "查询数据成功!");
		} catch (Exception e) {
			String errMsg = "查询数据详情失败!";
			result.put("flag", "fail");
			result.put("msg", errMsg);
			logger.error(errMsg, e);
		}
		return result;
    }
    
    /**
     * 获得当前请假单详情
     * 接口：askForLeave/getTicketDetail
     * 参数：aflId
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getTicketDetail" ,method = RequestMethod.GET)
    public @ResponseBody Map<String,Object> getTicketDetail(HttpServletRequest request, HttpServletResponse response, Model model){
		
    	Map<String, Object> resultMap = new HashMap<String, Object>();
	    resultMap.put("result","success");
    	String aflId=request.getParameter("aflId");
		AskForLeave afl=askForLeaveService.findByBusinessId(aflId);	
	    resultMap.put("data",afl);
    	return resultMap;
    }
    
    
    //获得当前用户id
    @RequestMapping(method = RequestMethod.GET,value="/getProposer")
	public @ResponseBody Map<String, Object> getProposer(HttpServletRequest request, HttpServletResponse response){
		String cuser = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (SecurityUtils.getSubject().getPrincipal() != null)
			cuser = (String) SecurityUtils.getSubject().getPrincipal();
		if (cuser == null) {
			return null;
		}
		
		try {
			IpHirer ipHirer = hirerAccountService.findHirerByEmailOrLoginNameOrCellphoneNo(cuser);//租户
			if(ipHirer!=null){
		    	String hirerId = ipHirer.getHirerId();
		    	resultMap.put("hirerId",hirerId);
		    	resultMap.put("cusername", ipHirer.getHirerName());
			}else{
				 IpUser ipUser=userAccountService.findUserByUserEmailOrLoginNameOrCellphoneNo(cuser); //普通用户
				   if(ipUser!=null){
					   resultMap.put("userId",ipUser.getUserId());
					   resultMap.put("cusername", ipUser.getUserName());
				   }
			}
			
		}catch(Exception ex){
			logger.error("获取用户权限失败!",ex);
		}
    	
    	return resultMap;
    }
    
  //审批当前任务
    @RequestMapping(method = RequestMethod.POST,value="/doApprove")
   	public @ResponseBody Map<String, Object> doApprove(HttpServletRequest request, HttpServletResponse response){
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	String getbpmEnabledByKey = PropertyUtilSys.getbpmEnabledByKey("bpm.enabled");
		 if("false".equals(getbpmEnabledByKey)){//工作流已经关闭
			 resultMap.put("result", "Enabled");
			 return resultMap;
		 }
		 
    	String taskId = request.getParameter("taskId");
    	String userId = request.getParameter("userId");//IP里面的userId
    	String check=request.getParameter("check");
    	String hirerId=request.getParameter("hirerId");//租户id
    	String businessId = request.getParameter("businessId");
    	String suggestion = request.getParameter("suggestion");//审批意见
    	
    	IpUser findUserByUserId = userAccountService.findUserByUserId(userId);

    	return askForLeaveService.doApproveService(resultMap, taskId, userId, check, hirerId,
				businessId, suggestion, findUserByUserId);
    }



/*private Map<String, Object> doApproveService(Map<String, Object> resultMap,
		String taskId, String userId, String check, String hirerId,
		String businessId, String suggestion, IpUser findUserByUserId) {
	BpmRest bpmRestServices = bpmServioce.getBpmRestServices(userId, hirerId);
	UserQueryParam userQueryParam = new UserQueryParam();
	userQueryParam.setMail(findUserByUserId.getUserEmail());
	userQueryParam.setName(findUserByUserId.getUserName());
	
	AskForLeave findByBusinessId2 = askForLeaveService.findByBusinessId(businessId);
	String curProcessinstanceId = findByBusinessId2.getCurProcessinstanceId();
	
	
	JsonNode jsonNode=null;
	try {
		jsonNode = (JsonNode)bpmRestServices.getIdentityService().queryUsers(userQueryParam);
	} catch (RestException e1) {
		e1.printStackTrace();
	}
	String jsonInfo_id=jsonNode.findValues("id").get(0).toString();
	String bpm_userId = jsonInfo_id.substring(1, jsonInfo_id.length()-1);
	
		try {
						    List<RestVariable> variables=new ArrayList<RestVariable> ();
				        	RestVariable restVariable=new RestVariable();
				        	restVariable.setName("check");
				        	restVariable.setValue(check);
				        	variables.add(restVariable);
				        	
				        	//设置当前用户所属的组织
				   		 IpUserCompany findIpUserCompanyByUserId = ipUserCompanyService.findIpUserCompanyByUserId(userId);
				   		 String coId = findIpUserCompanyByUserId.getCoId();
				   		 IpCompany findByCoId = ipCompanyService.findByCoId(coId);
				   		   //按照部门Code查询bpm的部门记录
				   		    OrgQueryParam orgQueryParam = new OrgQueryParam();
				   			orgQueryParam.setCode(findByCoId.getCoCode());
				   			//BpmRest bpmRestServices2 = getBpmRestServices(findByCoId.getHirerId(),findByCoId.getHirerId());
				   			List<OrgResponse> list = new ArrayList<OrgResponse>();
				   			JsonNode jsonNode1 = (JsonNode)bpmRestServices.getIdentityService().queryOrgs(orgQueryParam);
				   			 ArrayNode arrayNode = BaseUtils.getData(jsonNode1);
				   			 for (int i = 0; arrayNode != null && i < arrayNode.size(); i++) {
				   				 OrgResponse resp =jsonResultService.toObject(arrayNode.get(i).toString(), OrgResponse.class);
				   		            list.add(resp);
				   		        }
				   	         String org_id = list.get(0).getId();
				   	         BpmRest bpmRestServices22 = bpmServioce.getBpmRestServices_start(userId, hirerId,org_id);
				   	        bpmRestServices22.getTaskService().claim(taskId,bpm_userId);
				        	Object node=bpmRestServices22.getTaskService().completeWithComment(taskId, variables,null,null,suggestion);
				        	String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date());  
				    		Timestamp time = Timestamp.valueOf(current);
				        	 //根据流程实例ID查询当前任务Id
				        	TaskQueryParam taskQueryParam = new TaskQueryParam();
						    taskQueryParam.setProcessInstanceId(curProcessinstanceId);
						    JsonNode task_jsonNode = (JsonNode)bpmRestServices.getTaskService().queryTasks(taskQueryParam);
						    List<JsonNode> findValues2 = task_jsonNode.findValues("data");
						    String bpm_task_Id="";
							if("[]".equals(findValues2.get(0).toString())){
								 bpm_task_Id ="";
							}else{
								String task_jsonInfo_id=task_jsonNode.findValues("id").get(0).toString();
								bpm_task_Id = task_jsonInfo_id.substring(1, task_jsonInfo_id.length()-1);//得到任务id
							}
						    
				        	//执行完任务之后，需要更新基本信息表及详情表
				        	if (node != null) {
				        		int status=0;
				        		if("0".equals(check)){//不同意
				        			 status=ApproveStatus.DIS_APPROVE;
				        			 askForLeaveService.updateTicketById(businessId,bpm_task_Id,status,time); 
				        		   }else{//同意
				        			   status=ApproveStatus.APPROVE;
				        			   askForLeaveService.updateTicketById(businessId,bpm_task_Id,status,time);
				        			   
				        		   }
				        	    
								 //新增请假详情
								   TicketDetail ticketDetail=new TicketDetail();
								   ticketDetail.setId(UUIDTools.uuidRandom());
								   ticketDetail.setBusinessId(businessId);
								   ticketDetail.setStatus(status);
								   ticketDetail.setUid(findByBusinessId2.getUid());
								   ticketDetail.setSuggestion(suggestion);
								   ticketDetail.setBpmUid(bpm_userId);
								   ticketDetail.setCreateTime(time);
								   askForLeaveService.saveTicketDetail(ticketDetail);
								   resultMap.put("result", "true");
								   return resultMap;
				        	 }else{
				        		 resultMap.put("result", "false");
						         return resultMap;
				        	 }
		} catch (RestException e) {
		e.printStackTrace();
	}
	return null;
}*/
    
    
    
    //获得当前人员的请假单
    @RequestMapping(value = "/getCuruserForm" ,method = RequestMethod.GET)
    public @ResponseBody Object getCurSubmitTicket(@RequestParam(value = "pageIndex", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortType", defaultValue = "auto") String sortType, Model model, ServletRequest request){
    	Map<String,Object> result = new HashMap<String,Object>();
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams = Servlets.getParametersStartingWith(request, "search_");
		//PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		try {
			Page<AskForLeave> accountPage = askTicketListService.getCurSubmitTicket(
					searchParams,pageNumber,pageSize);
			result.put("data", accountPage);
			result.put("flag", "success");
			result.put("msg", "查询数据成功!");
		} catch (Exception e) {
			String errMsg = "查询数据详情失败!";
			result.put("flag", "fail");
			result.put("msg", errMsg);
			logger.error(errMsg, e);
		}
		return result;    	
    }
    
	
	
    private Map<String, Object> buildSearchParamsStartingWith(String prefix) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> m = leaveDataTable.getParams();
		Set<Entry<String, Object>> set = m.entrySet();
		for (Entry<String, Object> entry : set) {
			String key = entry.getKey();
			if (key.startsWith(prefix)) {
				String unprefixed = key.substring(prefix.length());
				params.put(unprefixed, entry.getValue().toString());
			}
		}
		return params;
	}
    
	
    
    private PageRequest buildPageRequest(int pageNumber, int pagzSize,
			String sortType) {
		Sort sort = null;
		return new PageRequest(pageNumber, pagzSize, sort);
	}
	
    //查询详细信息
    @RequestMapping(method = RequestMethod.GET,value="/getquerydetails")
	public @ResponseBody Map<String, Object> getQueryDetails(HttpServletRequest request, HttpServletResponse response){
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
	    resultMap.put("result","success");

    	String userId=request.getParameter("userId");
    	String aflId=request.getParameter("aflId");
    	
		IpUser ipUser=userAccountService.findUserByUserId(userId);
		AskForLeave afl=askForLeaveService.findByBusinessId(aflId);	
		
		IpUserCompany ipUserCompany=ipUserCompanyService.findIpUserCompanyByUserId(ipUser.getUserId());
		String coName = ipUserCompany.getCoName();
		  
		if(ipUser != null && afl !=null ){
			  resultMap.put("data",afl);
			  resultMap.put("coName",coName);
		   }
		 else
		 {
			 resultMap.put("result","fail");
		 }
    	return resultMap;
    }

    
    private IpUser getCurUserId() {
		String cuser = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
			cuser = (String) SecurityUtils.getSubject().getPrincipal();
			IpUser ipUser=userAccountService.findUserByUserEmailOrLoginNameOrCellphoneNo(cuser); //普通用户
		return ipUser;
	}
    
}
