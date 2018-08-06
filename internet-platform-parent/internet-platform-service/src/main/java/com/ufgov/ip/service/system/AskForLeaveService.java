package com.ufgov.ip.service.system;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import yonyou.bpm.rest.BpmRest;
import yonyou.bpm.rest.exception.RestException;
import yonyou.bpm.rest.request.RestVariable;
import yonyou.bpm.rest.request.identity.OrgQueryParam;
import yonyou.bpm.rest.request.identity.UserQueryParam;
import yonyou.bpm.rest.request.task.TaskQueryParam;
import yonyou.bpm.rest.response.historic.HistoricProcessInstanceResponse;
import yonyou.bpm.rest.response.identity.OrgResponse;
import yonyou.bpm.rest.utils.BaseUtils;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ufgov.ip.api.system.AskForLeaveServiceI;
import com.ufgov.ip.api.system.JsonResultServiceI;
import com.ufgov.ip.api.system.ProcessServiceI;
import com.ufgov.ip.dao.sysmanager.ICompanyDao;
import com.ufgov.ip.dao.sysmanager.IpUserCompanyDao;
import com.ufgov.ip.dao.system.AskForLeaveDao;
import com.ufgov.ip.dao.system.TicketDetailDao;
import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.system.ApproveStatus;
import com.ufgov.ip.entity.system.AskForLeave;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.IpUserCompany;
import com.ufgov.ip.entity.system.TicketDetail;
import com.ufgov.ip.utils.UUIDTools;
@Service(version = "0.0.1")
public class AskForLeaveService implements AskForLeaveServiceI{

	@Autowired
	private AskForLeaveDao AskForLeaveDao;
	
	@Autowired
	private TicketDetailDao ticketDetailDao;
	
	//@Reference(version = "0.0.1")
	@Autowired
	private ProcessServiceI bpmServioce;
	
	/*@Autowired
	private PlatformTransactionManager transactionManager;*/
	
	@Autowired
	private IpUserCompanyDao ipUserCompanyDao;
	
	@Autowired
	private ICompanyDao coDao;
	
	@Autowired
	private JsonResultServiceI jsonResultService;

	public JsonResultServiceI getJsonResultService() {
		return jsonResultService;
	}
	
	
	public TicketDetailDao getTicketDetailDao() {
		return ticketDetailDao;
	}


	@Transactional
	public AskForLeave saveEntity(AskForLeave askforleave){
		AskForLeave save = AskForLeaveDao.save(askforleave);
		return save;
	}


	public AskForLeave findByBusinessId(String businessId) {
		// TODO 自动生成的方法存根
		return AskForLeaveDao.findById(businessId);
		
	}

	@Transactional
	public void updateTicketById(String businessId,String taskId,int status, Timestamp time) {
		// TODO 自动生成的方法存根
		AskForLeaveDao.updateTicketById(businessId,taskId,status,time);
		
	}

	
	//保存请假详情
	@Transactional
	public void saveTicketDetail(TicketDetail ticketDetail) {
		ticketDetailDao.save(ticketDetail);
	}


	//@Override
	@Transactional
	public Map<String, String> submitTicketAndRun(final Map<String, String> reg,
			final AskForLeave askforleave, final IpUser user) {

					//定义流程变量
			    	List<RestVariable> variables=new ArrayList<RestVariable> ();
			    	RestVariable restVariable=new RestVariable();
			    	restVariable.setName("流程名称");
			    	restVariable.setValue("平台请假流程");
			    	variables.add(restVariable);
			    	
			    	RestVariable entityVariables=new RestVariable();
			    	entityVariables.setName("业务数据");
			    	entityVariables.setValue(askforleave);
			    	variables.add(entityVariables);
			    	
			    	try {
			    		
			    		
			    		//启动流程
						HistoricProcessInstanceResponse startProcessByKey = bpmServioce.startProcessByKey(askforleave.getUsid(), "process_999", askforleave.getId(),variables);
						String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date());  
						Timestamp time = Timestamp.valueOf(current);
						
						//保存请假单
						askforleave.setCurProcessinstanceId(startProcessByKey.getId());
						
						//根据流程实例Id查询当前任务Id
						TaskQueryParam taskQueryParam = new TaskQueryParam();
					    taskQueryParam.setProcessInstanceId(startProcessByKey.getId());
					    BpmRest bpmRestServices = bpmServioce.getBpmRestServices(askforleave.getUsid(), user.getHirerId());
					    JsonNode jsonNode = (JsonNode)bpmRestServices.getTaskService().queryTasks(taskQueryParam);
						String jsonInfo_id=jsonNode.findValues("id").get(0).toString();
						String taskId = jsonInfo_id.substring(1, jsonInfo_id.length()-1);//得到任务id
						
						askforleave.setTaskId(taskId);
						askforleave.setCreateTime(time);
						AskForLeave saveEntity = saveEntity(askforleave);
						String businessId = saveEntity.getId();//获得businessId
						//新增请假详情(为了查询当前用户的请假单)
						   TicketDetail ticketDetail=new TicketDetail();
						   ticketDetail.setId(UUIDTools.uuidRandom());
						   ticketDetail.setBusinessId(businessId);
						   ticketDetail.setStatus(ApproveStatus.SUBMIT);
						   ticketDetail.setUsid(askforleave.getUsid());
						   ticketDetail.setSuggestion("已提交");
						   ticketDetail.setBpmUid("");
						   ticketDetail.setCreateTime(time);
						   saveTicketDetail(ticketDetail);
						   reg.put("uid", askforleave.getUsid());
					} catch (RestException e) {
						reg.put("result","发起流程时报错了");
						throw new RuntimeException("发起流程时报错了",e);
	                 }
					return reg;
				}


	//@Override
	@Transactional
	public Map<String, Object> doApproveService(Map<String, Object> resultMap,
			String taskId, String userId, String check, String hirerId,
			String businessId, String suggestion, IpUser findUserByUserId) {
		BpmRest bpmRestServices = bpmServioce.getBpmRestServices(userId, hirerId);
		UserQueryParam userQueryParam = new UserQueryParam();
		userQueryParam.setMail(findUserByUserId.getUserEmail());
		userQueryParam.setName(findUserByUserId.getUserName());
		
		AskForLeave findByBusinessId2 = findByBusinessId(businessId);
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
					   		 IpUserCompany findIpUserCompanyByUserId = ipUserCompanyDao.findIpUserCompanyByUserId(userId);
					   		 String coId = findIpUserCompanyByUserId.getCoId();
					   		 IpCompany findByCoId = coDao.findByCoId(coId);
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
					        			 updateTicketById(businessId,bpm_task_Id,status,time); 
					        		   }else{//同意
					        			   status=ApproveStatus.APPROVE;
					        			   updateTicketById(businessId,bpm_task_Id,status,time);
					        			   
					        		   }
					        	    
									 //新增请假详情
									   TicketDetail ticketDetail=new TicketDetail();
									   ticketDetail.setId(UUIDTools.uuidRandom());
									   ticketDetail.setBusinessId(businessId);
									   ticketDetail.setStatus(status);
									   ticketDetail.setUsid(findByBusinessId2.getUsid());
									   ticketDetail.setSuggestion(suggestion);
									   ticketDetail.setBpmUid(bpm_userId);
									   ticketDetail.setCreateTime(time);
									   saveTicketDetail(ticketDetail);
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
}
	
	
}
