package com.ufgov.ip.service.system;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springside.modules.persistence.SearchFilter;

import yonyou.bpm.rest.BpmRest;
import yonyou.bpm.rest.exception.RestException;
import yonyou.bpm.rest.request.identity.UserQueryParam;
import yonyou.bpm.rest.request.task.TaskQueryParam;
import yonyou.bpm.rest.response.runtime.task.TaskResponse;
import yonyou.bpm.rest.utils.BaseUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ufgov.ip.api.system.AskTicketListServiceI;
import com.ufgov.ip.dao.system.AskForLeaveDao;
import com.ufgov.ip.dao.system.IpHirerMapper;
import com.ufgov.ip.dao.system.IuapUserDao;
import com.ufgov.ip.dao.system.TicketDetailDao;
import com.ufgov.ip.dao.systemimpl.AskForLeaveJDBCDaoImpl;
import com.ufgov.ip.dao.systemimpl.IuapUserTicketJdbcDaoImpl;
import com.ufgov.ip.entity.system.AskForLeave;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.service.base.IPDataTableService;
import com.ufgov.ip.utils.CopyPropertiesUtil;



@Service(version = "0.0.1")
public class AskTicketListService implements AskTicketListServiceI{

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;
	private static Logger logger = LoggerFactory.getLogger(AskTicketListService.class);
	
	@Autowired
    private IuapUserTicketJdbcDaoImpl iuapUserTicketJdbcDaoImpl;
	
	@Autowired
	private AskForLeaveDao askForLeaveDao;
	
	@Autowired
	private IuapUserDao userDao;
	
	@Autowired
	private TicketDetailDao ticketDetailDao;
	
	@Autowired
	private AskForLeaveJDBCDaoImpl askForLeaveJDBCDao;
	
	/*//设置redis缓存
    @Autowired
    private SessionCacheManager sessionCacheManager;*/
    
    @Autowired
    private ProcessService processService;
	
	@Autowired
	private IPDataTableService ipDataTableService;
	
	@Autowired
	private IpHirerMapper hirerDao;

	public IPDataTableService getIpDataTableService() {
		return ipDataTableService;
	}


	public IuapUserTicketJdbcDaoImpl getIuapUserJdbcDao() {
		return iuapUserTicketJdbcDaoImpl;
	}
	
	
	public ProcessService getProcessService() {
		return processService;
	}
	
	
	//获得当前部门已审批的请假单
	public  Page<AskForLeave> getCheckedTicketlistAll(Map<String, Object> searchParams,
			PageRequest pageRequest){
		 Map<String,String> roleIdAndCoId=getCheckedAskTicketBySearchParams(searchParams);
			String hirerId = roleIdAndCoId.get("hirerId");
			String userId = roleIdAndCoId.get("userId");
			
		   //获得所有的请假申请
		   
		    IpUser findUserByUserId = userDao.findUserByUserId(userId);
	    	BpmRest bpmRestServices = processService.getBpmRestServices(userId, hirerId);
	    	UserQueryParam userQueryParam = new UserQueryParam();
	    	userQueryParam.setMail(findUserByUserId.getUserEmail());
	    	userQueryParam.setName(findUserByUserId.getUserName());
		   
	    	JsonNode jsonNode=null;
	    	try {
	    		jsonNode = (JsonNode)bpmRestServices.getIdentityService().queryUsers(userQueryParam);
			} catch (RestException e1) {
				e1.printStackTrace();
			}
	    	String jsonInfo_id=jsonNode.findValues("id").get(0).toString();
			String bpm_userId = jsonInfo_id.substring(1, jsonInfo_id.length()-1);
			
			//根据当前bpm_uid基本表和详情表联查，得到当前领导审批过的单据
			List<AskForLeave> findAskForLeaveByBpmUserId = askForLeaveJDBCDao.findAskForLeaveByBpmUserId(bpm_userId);
		   
		    return ipDataTableService.getPageEntity(pageRequest, getAskForLeaveList(findAskForLeaveByBpmUserId));
	}

	public  Page<AskForLeave> getTicketlistAll(Map<String, Object> searchParams,
			PageRequest pageRequest){
		 Map<String,String> roleIdAndCoId=getHirerIdAndCoIdBySearchParams(searchParams);
			String hirerId = roleIdAndCoId.get("hirerId");
			String userId = roleIdAndCoId.get("userId");
		
		   List<AskForLeave> realTicket=new ArrayList<AskForLeave>();
		   IpUser findUserByUserId = userDao.findUserByUserId(userId);
		   String userEmail = findUserByUserId.getUserEmail();
		   
		   
		   BpmRest bpmRestServices = processService.getBpmRestServices(userId, hirerId);
		   UserQueryParam userQueryParam = new UserQueryParam();
		   userQueryParam.setMail(userEmail);
		   userQueryParam.setTenantId(hirerId);
		   userQueryParam.setPhone(findUserByUserId.getCellphoneNo());
    
		   try{
			   JsonNode user_jsonNode = (JsonNode) bpmRestServices.getIdentityService().queryUsers(userQueryParam);
			   String bpm_user_id=user_jsonNode.findValues("id").get(0).toString();
			   String real_bpm_user_id = bpm_user_id.substring(1, bpm_user_id.length()-1);
			   
			   //根据当前用户Id获得当前任务Id
			      BpmRest bpmRestServices2 = processService.getBpmRestServices(real_bpm_user_id, hirerId);
			      TaskQueryParam taskQueryParam = new TaskQueryParam();
				  taskQueryParam.setAssignee(real_bpm_user_id);
				  JsonNode jsonNode = (JsonNode)bpmRestServices2.getTaskService().queryTasks(taskQueryParam);
				  List<JsonNode> findValues2 = jsonNode.findValues("data");
				  
				  if("[]".equals(findValues2.get(0).toString())){
					  return ipDataTableService.getPageEntity(pageRequest, getAskForLeaveList(realTicket));
				  }else{
					  List<TaskResponse> list = new ArrayList<TaskResponse>();
				        ArrayNode arrayNode = BaseUtils.getData(jsonNode);
				        for (int i = 0; arrayNode != null && i < arrayNode.size(); i++) {
				            TaskResponse resp =toObject(arrayNode.get(i).toString(), TaskResponse.class);
				            list.add(resp);
				        }
				        
				        for (TaskResponse arrayNode1 : list) {
				        	AskForLeave askForLeave= askForLeaveDao.findByTaskId(arrayNode1.getId());
							if(askForLeave!=null){
								realTicket.add(askForLeave);
							}
						}
				        return ipDataTableService.getPageEntity(pageRequest, getAskForLeaveList(realTicket));
				  }
				    
				    
				  
				  
				  
				  
				  
				  /*List<JsonNode> findValues2 = jsonNode.findValues("data");
				  if("[]".equals(findValues2.get(0).toString())){
					  return ipDataTableService.getPageEntity(pageRequest, realTicket);
				  }else{
					  for (JsonNode jsonNode2 : findValues2) {
							
						  List<JsonNode> findValues = jsonNode.findValues("id");
							  String bpm_task_id = findValues.get(0).toString().substring(1, findValues.get(0).toString().length()-1);
							  //根据任务id查询请假单
							  AskForLeave askForLeave= askForLeaveDao.findByTaskId(bpm_task_id);
							  realTicket.add(askForLeave);
						   return ipDataTableService.getPageEntity(pageRequest, realTicket);
					  }
				  }*/
				  
				  /*if(jsonNode==null){
					  return ipDataTableService.getPageEntity(pageRequest, realTicket);
				  }else{
					  List<JsonNode> findValues = jsonNode.findValues("id");
					  //for (JsonNode jsonNode2 : findValues) {
						  String bpm_task_id = findValues.get(0).toString().substring(1, findValues.get(0).toString().length()-1);
						 // String bpm_task_id=jsonNode2.findValues("id").get(0).toString();
						  //根据任务id查询请假单
						  AskForLeave askForLeave= askForLeaveDao.findByTaskId(bpm_task_id);
						  realTicket.add(askForLeave);
					 //} 
					  return ipDataTableService.getPageEntity(pageRequest, realTicket); 
				  }*/
		   }catch(Exception ex){
			   ex.printStackTrace();
		   }
		return null;
		   
			 
	}
	
	
	
	public  List<AskForLeave> getCurTaskList(String hirerId,String userId){
		    List<AskForLeave> realTicket=new ArrayList<AskForLeave>();
		    IpUser findUserByUserId = userDao.findUserByUserId(userId);
		    String userEmail = findUserByUserId.getUserEmail();
		   BpmRest bpmRestServices = processService.getBpmRestServices(userId, hirerId);
		   UserQueryParam userQueryParam = new UserQueryParam();
		   userQueryParam.setMail(userEmail);
		   userQueryParam.setTenantId(hirerId);
		   userQueryParam.setPhone(findUserByUserId.getCellphoneNo());
    
		   try{
			   JsonNode user_jsonNode = (JsonNode) bpmRestServices.getIdentityService().queryUsers(userQueryParam);
			   ArrayNode user_arrayNode = BaseUtils.getData(user_jsonNode);
			   if(user_arrayNode==null){
				   return null;
			   }
			   String bpm_user_id=user_jsonNode.findValues("id").get(0).toString();
			   String real_bpm_user_id = bpm_user_id.substring(1, bpm_user_id.length()-1);
			   
			   //根据当前用户Id获得当前任务Id
			      BpmRest bpmRestServices2 = processService.getBpmRestServices(real_bpm_user_id, hirerId);
			      TaskQueryParam taskQueryParam = new TaskQueryParam();
				  taskQueryParam.setAssignee(real_bpm_user_id);
				  JsonNode jsonNode = (JsonNode)bpmRestServices2.getTaskService().queryTasks(taskQueryParam);
				  List<JsonNode> findValues2 = jsonNode.findValues("data");
				  
				  if("[]".equals(findValues2.get(0).toString())){
					  return getAskForLeaveList(realTicket);
					  //return ipDataTableService.getPageEntity(pageRequest, getAskForLeaveList(realTicket));
				  }else{
					  List<TaskResponse> list = new ArrayList<TaskResponse>();
				        ArrayNode arrayNode = BaseUtils.getData(jsonNode);
				        for (int i = 0; arrayNode != null && i < arrayNode.size(); i++) {
				            TaskResponse resp =toObject(arrayNode.get(i).toString(), TaskResponse.class);
				            list.add(resp);
				        }
				        
				        for (TaskResponse arrayNode1 : list) {
				        	AskForLeave askForLeave= askForLeaveDao.findByTaskId(arrayNode1.getId());
							if(askForLeave!=null){
								realTicket.add(askForLeave);
							}
						}
				        return getAskForLeaveList(realTicket);
				        //return ipDataTableService.getPageEntity(pageRequest, getAskForLeaveList(realTicket));
				  }
		   }catch(Exception ex){
			   ex.printStackTrace();
		   }
		return null;
		   
			 
	}
	
	
	public Map<String,String> getHirerIdAndCoIdBySearchParams(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		SearchFilter searchFilterByHirerId =filters.get("EQ_hirerId");
		SearchFilter searchFilterByUserId =filters.get("EQ_userId");
		SearchFilter searchFilterByCoId =filters.get("EQ_coId");
		SearchFilter searchFilterByIsEnabled =filters.get("EQ_isEnabled");
		SearchFilter searchFilterByRoleName =filters.get("EQ_roleName");
		String hirerId = "";
		String userId="";
		String coId="";
		String isEnable="";
		String roleName="";
		
		Map<String,String> dataInfo=new HashMap<String, String>();
		if (searchFilterByHirerId!=null) {
			hirerId = (String) searchFilterByHirerId.value;
		}
		
		if (searchFilterByUserId!=null) {
			userId = (String) searchFilterByUserId.value;
		}
		
		if (searchFilterByCoId!=null) {
			coId = (String) searchFilterByCoId.value;
		}
		
		if (searchFilterByIsEnabled!=null) {
			isEnable = (String) searchFilterByIsEnabled.value;
		}
		
		if (searchFilterByRoleName!=null) {
			roleName = (String) searchFilterByRoleName.value;
			try {
				roleName = URLEncoder.encode(roleName,"utf-8");
				roleName=URLDecoder.decode(roleName,"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
		}
		
		dataInfo.put("hirerId", hirerId);
		dataInfo.put("userId", userId);
		dataInfo.put("coId", coId);
		dataInfo.put("isEnabled", isEnable);
		dataInfo.put("roleName", roleName);
		return dataInfo;
	}
	
	
	
	//解析参数信息
	public Map<String,String> getCheckedAskTicketBySearchParams(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		SearchFilter searchFilterByHirerId =filters.get("EQ_hirerId");
		SearchFilter searchFilterByCoId =filters.get("EQ_coId");
		SearchFilter searchFilterByIsEnabled =filters.get("EQ_isEnabled");
		SearchFilter searchFilterByRoleName =filters.get("EQ_roleName");
		SearchFilter searchFilterByUserId =filters.get("EQ_userId");
		String hirerId = "";
		String coId = "";
		String isEnabled="";
		String roleName="";
		String userId="";
		Map<String,String> dataInfo=new HashMap<String, String>();
		if (searchFilterByHirerId!=null) {
			hirerId = (String) searchFilterByHirerId.value;
		}
		if (searchFilterByCoId!=null) {
			coId = (String) searchFilterByCoId.value;
		}
		
		if (searchFilterByIsEnabled!=null) {
			isEnabled = (String) searchFilterByIsEnabled.value;
		}
		
		if (searchFilterByUserId!=null) {
			userId = (String) searchFilterByUserId.value;
		}
		
		
		if (searchFilterByRoleName!=null) {
			roleName = (String) searchFilterByRoleName.value;
			try {
				roleName = URLEncoder.encode(roleName,"utf-8");
				roleName=URLDecoder.decode(roleName,"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
		}
		dataInfo.put("hirerId", hirerId);
		dataInfo.put("coId", coId);
		dataInfo.put("isEnabled", isEnabled);
		dataInfo.put("roleName", roleName);
		dataInfo.put("userId", userId);
		return dataInfo;
	}
	
	
	 /**
		 * 通过jdbc查询的结果构建 list<实体>
		 * @param ipRoleMenuList
		 * @return
		 */
		public List<IpUser> buildAskTicketEntityList(List ipUserList) {
			List<IpUser> ipUsers = new ArrayList<IpUser>();
			IpUser ipUser;
			Iterator iterator = ipUserList.iterator();
			 while (iterator.hasNext()) {
				 Map map4ipMenu = (Map) iterator.next();
				 ipUser=new IpUser();
				 CopyPropertiesUtil.copyJdbcProperty(ipUser, map4ipMenu);
				 ipUsers.add(ipUser);
			}
			return ipUsers;
		}

		
		
    //获得		
	public List<AskForLeave> getAskForLeaveList(List<AskForLeave> list){
		
		for (AskForLeave askForLeave : list) {
			
			String uid = askForLeave.getUsid();
			IpUser findUserByUserId = userDao.findUserByUserId(uid);
			askForLeave.setProposer(findUserByUserId.getUserName());
		}
		
		return list;
	}	
		
		
		
	//获得当前人员的请假单	
	public Page<AskForLeave> getCurSubmitTicket(
			Map<String, Object> searchParams, int pageNumber, int pageSize) {
		   
		    PageRequest pageRequest = new PageRequest(pageNumber, pageSize, null);// 目前的pagesize是固定的，现在等待前台传值
		    Map<String,String> roleIdAndCoId=getCheckedAskTicketBySearchParams(searchParams);
			String userId = roleIdAndCoId.get("userId");
			//根据userId查询当前人员的请假单
			List<AskForLeave> findAskForLeaveByUserId = askForLeaveJDBCDao.findAskForLeaveByUserId(userId);
		    return ipDataTableService.getPageEntity(pageRequest, findAskForLeaveByUserId);
	
	}
	
	public static <T> T toObject(String json, Class<T> cls) {
        try {
        	 ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, cls);
        } catch (Exception e) {
            return null;
        }
    }
	
}
