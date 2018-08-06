package com.ufgov.ip.service.system;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.WebApplicationContextUtils;

import yonyou.bpm.rest.BpmRest;
import yonyou.bpm.rest.BpmRests;
import yonyou.bpm.rest.RuntimeService;
import yonyou.bpm.rest.exception.RestException;
import yonyou.bpm.rest.param.BaseParam;
import yonyou.bpm.rest.request.RestVariable;
import yonyou.bpm.rest.request.identity.OrgQueryParam;
import yonyou.bpm.rest.request.identity.UserQueryParam;
import yonyou.bpm.rest.request.runtime.ProcessInstanceStartParam;
import yonyou.bpm.rest.response.historic.HistoricProcessInstanceResponse;
import yonyou.bpm.rest.response.identity.OrgResponse;
import yonyou.bpm.rest.response.runtime.task.TaskResponse;
import yonyou.bpm.rest.utils.BaseUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ufgov.ip.api.system.ProcessServiceI;
import com.ufgov.ip.dao.sysmanager.ICompanyDao;
import com.ufgov.ip.dao.sysmanager.IpUserCompanyDao;
import com.ufgov.ip.dao.system.IpHirerMapper;
import com.ufgov.ip.dao.system.IuapUserDao;
import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.IpUserCompany;
/*import com.ufgov.ip.dao.IuapHirerDao;
import com.ufgov.ip.dao.IuapUserDao;
import com.ufgov.ip.entity.IpHirer;
import com.ufgov.ip.entity.IpUser;
import com.ufgov.ip.entity.IpUserCompany;
import com.ufgov.ip.sysmanager.dao.ICompanyDao;
import com.ufgov.ip.sysmanager.dao.IpUserCompanyDao;
import com.ufgov.ip.sysmanager.entity.IpCompany;*/


/**
 * bpm流程操作基本服务
 * @author zhangbch
 */
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Service(version = "0.0.1")
public class ProcessService implements ProcessServiceI {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private JsonResultService jsonResultService;
    @Value("${bpm.rest.server}")
    private String serverUrl;
    @Value("${bpm.rest.tenant}")
    private String tenant;
    @Value("${bpm.rest.token}")
    private String token;
    
    //设置redis缓存
//    @Autowired
//    private SessionCacheManager sessionCacheManager;
//    
//    public SessionCacheManager getSessionCacheManager() {
//		return sessionCacheManager;
//	}


	@Autowired
    private IpUserCompanyDao ipUserCompanyDao;
    
    @Autowired
    private ICompanyDao iCompanyDao;
    
    public ICompanyDao getiCompanyDao() {
		return iCompanyDao;
	}

	public IpUserCompanyDao getIpUserCompanyDao() {
		return ipUserCompanyDao;
	}

	@Autowired
	private IuapUserDao userDao;

    public IuapUserDao getUserDao() {
		return userDao;
	}
    
    @Autowired
	private IpHirerMapper hirerDao;
	
	public IpHirerMapper getHirerDao() {
		return hirerDao;
	}

	/*public BpmRest bpmRestServices(String operatorID, IpHirer hirer,boolean isExistTenant) {
    	if(operatorID==null){
			throw new IllegalArgumentException("获取BpmRest时传入的operatorID["+operatorID+"]是空");
		}
		BaseParam baseParam=new BaseParam();
		baseParam.setOperatorID(operatorID);
		baseParam.setServer(serverUrl);
		//2.==========rest安全调用=========begin
		//租户code
		//管理端租户管理节点生成的token
		String limitTenantId=null;
		if(isExistTenant==false){//当前租户没注册，用默认的租户信息,否则连不上rest服务
			baseParam.setTenant(tenant);
			baseParam.setClientToken(token);
			//limitTenantId=TenantLimit.getTenantLimit();
		}else{//用当前已有的租户信息进行rest操作
			baseParam.setTenant(hirer.getHirerNo());
			baseParam.setClientToken(token);
			//limitTenantId=TenantLimit.getTenantLimit();
		}
		//==========rest安全调用=========end
		if(limitTenantId!=null&&!"".equals(limitTenantId.trim())){
			baseParam.setTenantLimitId(limitTenantId);
		}
		return BpmRests.getBpmRest(baseParam);
    }*/

	
	//设置一个默认的主租户，子租户互相隔离
	public BpmRest getBpmRestServices(String operatorID,String hirerId) {
    	if(operatorID==null){
			throw new IllegalArgumentException("获取BpmRest时传入的userId["+operatorID+"]是空");
		}
		BaseParam baseParam=new BaseParam();
		baseParam.setOperatorID(operatorID);
		//1.U审rest服务地址：http://ys.yyuap.com/ubpm-web-rest
		baseParam.setServer(serverUrl);
		
		//2.==========rest安全调用=========begin
		//租户code
		//管理端租户管理节点生成的token
		baseParam.setTenant(tenant);
		baseParam.setClientToken(token);
		/**
		    *TenantLimit底层用了ThreadLocal，将hirerId设置到当前线程里面
            *同时又达到了租户之间的隔离，也就是数据隔离，因为线程之间的变量是独有的
            *互不干扰的，当然那个hirerId贯穿了整个线程，baseParam.setTenantLimitId(limitTenantId);
            *的作用就是在同步租户信息的时候，为我们自动的将hirerId设置到BPM租户信息表的tenant_id字段
            *(租户id)里面，同步角色表时，为我们自动的将hirerId设置到BPM部门信息表的tenant_id字段上
            *总之，就是将hirerId同步到BPM对应表的Tenant_id字段上
		 */

		TenantLimit.set(hirerId);
		String limitTenantId=TenantLimit.getTenantLimit();
		//==========rest安全调用=========end
		if(limitTenantId!=null&&!"".equals(limitTenantId.trim())){
			baseParam.setTenantLimitId(limitTenantId);
		}
		return BpmRests.getBpmRest(baseParam);

    }
	
	//设置一个默认的主租户，子租户互相隔离
		public BpmRest getBpmRestServices_start(String operatorID,String hirerId,String org_id) {
	    	if(operatorID==null){
				throw new IllegalArgumentException("获取BpmRest时传入的userId["+operatorID+"]是空");
			}
			BaseParam baseParam=new BaseParam();
			baseParam.setOperatorID(operatorID);
			baseParam.setOrg(org_id);
			//1.U审rest服务地址：http://ys.yyuap.com/ubpm-web-rest
			baseParam.setServer(serverUrl);
			
			//2.==========rest安全调用=========begin
			//租户code
			//管理端租户管理节点生成的token
			baseParam.setTenant(tenant);
			baseParam.setClientToken(token);
			/**
			    *TenantLimit底层用了ThreadLocal，将hirerId设置到当前线程里面
	            *同时又达到了租户之间的隔离，也就是数据隔离，因为线程之间的变量是独有的
	            *互不干扰的，当然那个hirerId贯穿了整个线程，baseParam.setTenantLimitId(limitTenantId);
	            *的作用就是在同步租户信息的时候，为我们自动的将hirerId设置到BPM租户信息表的tenant_id字段
	            *(租户id)里面，同步角色表时，为我们自动的将hirerId设置到BPM部门信息表的tenant_id字段上
	            *总之，就是将hirerId同步到BPM对应表的Tenant_id字段上
			 */

			TenantLimit.set(hirerId);
			String limitTenantId=TenantLimit.getTenantLimit();
			//==========rest安全调用=========end
			if(limitTenantId!=null&&!"".equals(limitTenantId.trim())){
				baseParam.setTenantLimitId(limitTenantId);
			}
			return BpmRests.getBpmRest(baseParam);

	    }
	
	
	
    /**
     * 根据流程定义、租户ID和业务key启动流程实例
     *
     * @param userId
     * @param tenantId
     * @param processKey
     * @param businessKey
     * @return
     * @throws RestException
     */
    public HistoricProcessInstanceResponse startProcessByKey(String userId, String processKey, String procInstName, List<RestVariable> variables) throws RestException {
    	if (log.isDebugEnabled()) log.debug("启动流程。流程变量数据=" + jsonResultService.toJson(variables));
    	IpUser findUserByUserId = userDao.findUserByUserId(userId);
    	String hirerId = findUserByUserId.getHirerId();
    	//IpHirer findHirerByHirerId = hirerDao.findAll(hirerId).get(0);
    	IpHirer ipHirer=new IpHirer();
	    ipHirer.setHirerId(hirerId);
	    IpHirer findHirerByHirerId = hirerDao.findHirerByCondition(ipHirer);
	    
    	BpmRest bpmRestServices = getBpmRestServices(userId, findUserByUserId.getHirerId());
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
    	
		
		
		//设置当前用户所属的组织
		 IpUserCompany findIpUserCompanyByUserId = ipUserCompanyDao.findIpUserCompanyByUserId(userId);
		 String coId = findIpUserCompanyByUserId.getCoId();
		 IpCompany findByCoId = iCompanyDao.findByCoId(coId);
		   //按照部门Code查询bpm的部门记录
		    OrgQueryParam orgQueryParam = new OrgQueryParam();
			orgQueryParam.setCode(findByCoId.getCoCode());
			BpmRest bpmRestServices2 = getBpmRestServices(findByCoId.getHirerId(),findByCoId.getHirerId());
			List<OrgResponse> list = new ArrayList<OrgResponse>();
			JsonNode jsonNode1 = (JsonNode)bpmRestServices.getIdentityService().queryOrgs(orgQueryParam);
			 ArrayNode arrayNode = BaseUtils.getData(jsonNode1);
			 for (int i = 0; arrayNode != null && i < arrayNode.size(); i++) {
				 OrgResponse resp =jsonResultService.toObject(arrayNode.get(i).toString(), OrgResponse.class);
		            list.add(resp);
		        }
	     String org_id = list.get(0).getId();
		 
    	RuntimeService rt = getBpmRestServices_start(bpm_userId,findHirerByHirerId.getHirerId(),org_id).getRuntimeService();
        ProcessInstanceStartParam parm = new ProcessInstanceStartParam();
        parm.setProcessDefinitionKey(processKey);
        parm.setVariables(variables);
       // parm.setProcessInstanceName(procInstName);
        parm.setBusinessKey(procInstName);
        ObjectNode node = (ObjectNode) rt.startProcess(parm);

        HistoricProcessInstanceResponse resp = jsonResultService.toObject(node.toString(), HistoricProcessInstanceResponse.class);
        List<TaskResponse> tasks = resp.getTasks();
        String superProcessInstanceId = resp.getSuperProcessInstanceId();
        System.out.println("当前用户开启的流程实例id"+"******"+superProcessInstanceId);
        
        
       /* TaskQueryParam taskQueryParam = new TaskQueryParam();
	    taskQueryParam.setProcessInstanceId(resp.getId());
	    
	    JsonNode task_jsonNode = (JsonNode)bpmRestServices.getTaskService().queryTasks(taskQueryParam);
		String bpm_task_id=task_jsonNode.findValues("id").get(0).toString();
		String real_bpm_task_id = bpm_task_id.substring(1, bpm_task_id.length()-1);*/
        
        
        //将当前用户发起的流程id放进缓存中
        //sessionCacheManager.cacheUser(resp.getStartUserId(), resp.getId());
        return resp;
    }

    /**
     * 提交表单
     *
     * @param userId
     * @param processId
     * @param variables
     * @return
     * @throws Exception
     */
    /*public HistoricProcessInstanceResponse startProcessById(String userId, String processId, String procInstName, List<RestVariable> variables)
            throws RestException {
        if (log.isDebugEnabled()) log.debug("启动流程。流程变量数据=" + jsonResultService.toJson(variables));
        RuntimeService rt = bpmRestServices(userId).getRuntimeService();
        ProcessInstanceStartParam parm = new ProcessInstanceStartParam();
        parm.setProcessDefinitionId(processId);
        parm.setVariables(variables);
        parm.setProcessInstanceName(procInstName);

        ObjectNode node = (ObjectNode) rt.startProcess(parm);
        HistoricProcessInstanceResponse resp = jsonResultService.toObject(node.toString(), HistoricProcessInstanceResponse.class);
        return resp;
    }
    
    public List<ProcessDefinitionResponse> queryProcessDefinitionList(String userId,String key) throws RestException{
    	ProcessDefinitionQueryParam processDefinitionQueryParam=new ProcessDefinitionQueryParam();
    	List<ProcessDefinitionResponse> list = new ArrayList<ProcessDefinitionResponse>();
    	RepositoryService repositoryService = bpmRestServices(userId).getRepositoryService();
    	JsonNode jsonNode=(JsonNode)repositoryService.getProcessDefinitions(processDefinitionQueryParam);
    	 ArrayNode arrayNode = BaseUtils.getData(jsonNode);
         for (int i = 0; arrayNode != null && i < arrayNode.size(); i++) {
        	 ProcessDefinitionResponse resp = jsonResultService.toObject(arrayNode.get(i).toString(), ProcessDefinitionResponse.class);
             list.add(resp);
         }
         return list;
    	
    }
    

    *//**
     * 根据用户id查询该用户的待办列表
     *
     * @param bpmUserId
     * @return 待办任务id和对应流程实例id
     * @throws RestException
     *//*
    public ViewListData<TaskResponse> queryTodoList(String userId,
                                                    String keyword, String taskDue, String taskToday, Integer priority,
                                                    int size, int start) throws RestException {
        TaskService ts = bpmRestServices(userId).getTaskService();
        TaskQueryParam tqp = new TaskQueryParam();
        tqp.setOrder("desc");
        tqp.setSort("createTime");
        tqp.setSize(size);
        tqp.setStart(start);

        if (taskDue != null && taskDue.length() > 0) {
            tqp.setDueBefore(new Date());
        }
        if (taskToday != null && taskToday.length() > 0) {
            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String todayStr = sdf.format(today).substring(0, 10);
            try {
                Date today0 = sdf.parse(todayStr + " 00:00:00");
                Date today1 = sdf.parse(todayStr + " 23:59:59");
                tqp.setCreatedAfter(today0);
                tqp.setCreatedBefore(today1);
            } catch (ParseException e) {
                log.error("时间解析错误", e);
            }
        }
        if (priority != null) {
            tqp.setPriority(priority);
        }

        if (keyword != null && keyword.trim().length() > 0) {
            tqp.setNameLike("%" + keyword + "%");
        }

        JsonNode jsonNode = (JsonNode) ts.queryTasksToDo(userId, tqp);

        List<TaskResponse> list = new ArrayList<TaskResponse>();
        ArrayNode arrayNode = BaseUtils.getData(jsonNode);
        for (int i = 0; arrayNode != null && i < arrayNode.size(); i++) {
            TaskResponse resp = jsonResultService.toObject(arrayNode.get(i).toString(), TaskResponse.class);
            list.add(resp);
        }

        return createViewListData(jsonNode, list);
    }

    *//**
     * 根据流程实例ID获取审批评论信息
     *
     * @param processInstanceId
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @throws RestException
     *//*
    public List<CommentResponse> queryCommentList(String userId, String processInstanceId)
            throws RestException {
        HistoryService hs = bpmRestServices(userId).getHistoryService();
        JsonNode jsonNode = (JsonNode) hs.getHistoricProcessInstancesComments(processInstanceId);

        ArrayNode arrayNode = BaseUtils.getData(jsonNode);

        int size = arrayNode == null ? 0 : arrayNode.size();
        List<CommentResponse> retList = new ArrayList<CommentResponse>(size);
        for (int i = 0; i < size; i++) {
            JsonNode jn = arrayNode.get(i);
            CommentResponse commentResp = jsonResultService.toObject(jn.toString(), CommentResponse.class);
            //可能还有别的类型的数据
            if (commentResp.getMessage() == null) {
                continue;
            }
            retList.add(commentResp);
        }
        return retList;
    }


    *//**
     * 根据用户id查询该用户的已办列表
     *
     * @param userId
     * @return 待办任务id和对应流程实例id
     * @throws RestException
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     *//*
    public ViewListData<HistoricTaskInstanceResponse> queryDoneList(String userId,
                                                                    String keyword, String taskDue, String taskToday, Integer priority,
                                                                    int size, int start) throws RestException {
        return queryTaskList(userId, keyword, taskDue, taskToday, priority, size, start, true, false);
    }

    *//**
     * 根据用户id查询该用户的任务列表
     *
     * @param userId
     * @return 待办任务id和对应流程实例id
     * @throws RestException
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     *//*
    public ViewListData<HistoricTaskInstanceResponse> queryTaskList(String userId,
                                                                    String keyword, String taskDue, String taskToday, Integer priority,
                                                                    int size, int start, Boolean isDone, Boolean returnHisProcessIns) throws RestException {
        HistoricTaskQueryParam htqp = new HistoricTaskQueryParam();
        htqp.setFinished(isDone);
        htqp.setTaskAssignee(userId);
        htqp.setIncludeProcessVariables(true);
        htqp.setOrder("desc");
        htqp.setSort("startTime");
        htqp.setReturnHistoricProcessInstance(returnHisProcessIns);
        htqp.setSize(size);
        htqp.setStart(start);

        if (taskDue != null && taskDue.length() > 0) {
            htqp.setDueDateBefore(new Date());
        }
        if (taskToday != null && taskToday.length() > 0) {
            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String todayStr = sdf.format(today).substring(0, 10);
            try {
                Date today0 = sdf.parse(todayStr + " 00:00:00");
                Date today1 = sdf.parse(todayStr + " 23:59:59");
                htqp.setTaskCreatedAfter(today0);
                htqp.setTaskCreatedBefore(today1);
            } catch (ParseException e) {
                log.error("时间解析错误", e);
            }
        }
        if (priority != null) {
            htqp.setTaskPriority(priority.toString());
        }
        if (keyword != null && keyword.trim().length() > 0) {
            htqp.setTaskNameLike("%" + keyword + "%");
        }

        HistoryService ht = bpmRestServices(userId).getHistoryService();
        JsonNode jsonNode = (JsonNode) ht.getHistoricTaskInstances(htqp);

        ArrayNode arrayNode = BaseUtils.getData(jsonNode);
        int dataSize = arrayNode == null ? 0 : arrayNode.size();
        List<HistoricTaskInstanceResponse> list = new ArrayList<HistoricTaskInstanceResponse>(dataSize);
        for (int i = 0; i < dataSize; i++) {
            JsonNode jn = arrayNode.get(i);// {"data":[],"total":0,"start":0,"sort":"taskInstanceId","order":"asc","size":0}这样的数据size居然不是0
            HistoricTaskInstanceResponse taskResp = jsonResultService.toObject(jn.toString(), HistoricTaskInstanceResponse.class);
            list.add(taskResp);
        }
        return createViewListData(jsonNode, list);
    }

    *//**
     * 查询某人发起的流程实例
     *
     * @param userId
     * @param bpmUserId
     *//*
    public ViewListData<HistoricProcessInstanceResponse> queryStartList(String userId, String keyword, int size, int start)
            throws RestException {
        HistoricProcessInstancesQueryParam hpiqp = new HistoricProcessInstancesQueryParam();
        hpiqp.setStartedBy(userId);
        hpiqp.setOrder("desc");
        hpiqp.setSort("startTime");
        hpiqp.setIncludeProcessVariables(true);
        hpiqp.setStart(start);
        hpiqp.setSize(size);

        if (keyword != null && keyword.trim().length() > 0) {
            hpiqp.setProcessInstanceNameLike("%" + keyword + "%");
        }

        HistoryService hs = bpmRestServices(userId).getHistoryService();
        JsonNode jsonNode = (JsonNode) hs.getHistoricProcessInstances(hpiqp);
        ArrayNode node = BaseUtils.getData(jsonNode);
        List<HistoricProcessInstanceResponse> list = new ArrayList<HistoricProcessInstanceResponse>();

        for (int i = 0; node != null && i < node.size(); i++) {
            JsonNode jn = node.get(i);
            HistoricProcessInstanceResponse resp = jsonResultService.toObject(jn.toString(), HistoricProcessInstanceResponse.class);
            list.add(resp);
        }
        return createViewListData(jsonNode, list);
    }


    *//**
     * 获得流程后续task
     *
     * @param instanceId
     * @return
     * @throws RestException
     *//*
    public List<HistoricTaskInstanceResponse> queryInstanceNotFinishTaskAssigneeList(String userId, String instanceId)
            throws RestException {
        List<HistoricTaskInstanceResponse> taskAssigneeList = new ArrayList<HistoricTaskInstanceResponse>();

        HistoryService ht = bpmRestServices(userId).getHistoryService();// 历史服务
        JsonNode obj = (JsonNode) ht.getHistoricProcessInstance(instanceId);
        String endTime = obj.get("endTime").textValue();
        if (endTime != null) {
            // 说明该流程实例已结束
            return taskAssigneeList;
        }

        HistoricTaskQueryParam htp = new HistoricTaskQueryParam();
        htp.setProcessInstanceId(instanceId);
        htp.setFinished(false);// 只查询下一个未完成的task
        JsonNode jsonNode = (JsonNode) ht.getHistoricTaskInstances(htp);
        ArrayNode arrNode = BaseUtils.getData(jsonNode);

        if (arrNode == null) return taskAssigneeList;

        int size = arrNode.size();
        for (int i = 0; i < size; i++) {
            JsonNode jn = arrNode.get(i);
            HistoricTaskInstanceResponse resp = jsonResultService.toObject(jn.toString(), HistoricTaskInstanceResponse.class);
            taskAssigneeList.add(resp);
        }
        return taskAssigneeList;
    }


    *//**
     * 查询流程所有task列表
     *
     * @param userId
     * @param instanceId
     * @return
     * @throws Exception
     *//*
    public List<HistoricTaskInstanceResponse> queryInstanceAllHistoryTaskList(String userId, String instanceId)
            throws RestException {
        HistoryService ht = bpmRestServices(userId).getHistoryService();// 历史服务
        HistoricTaskQueryParam htp = new HistoricTaskQueryParam();
        htp.setProcessInstanceId(instanceId);
        htp.setIncludeProcessVariables(true);//包含变量
        JsonNode jsonNode = (JsonNode) ht.getHistoricTaskInstances(htp);
        if (log.isDebugEnabled()) log.debug("queryInstanceAllHistoryTaskList==>" + jsonNode);
        if (jsonNode == null) return null;

        ArrayNode arrayNode = BaseUtils.getData(jsonNode);
        int size = arrayNode == null ? 0 : arrayNode.size();
        List<HistoricTaskInstanceResponse> retList = new ArrayList<HistoricTaskInstanceResponse>(size);
        for (int i = 0; i < size; i++) {
            JsonNode jn = arrayNode.get(i);
            HistoricTaskInstanceResponse taskInstResp = jsonResultService.toObject(jn.toString(), HistoricTaskInstanceResponse.class);
          
            retList.add(taskInstResp);
        }
        return retList;
    }

    *//**
     * 根据租户查询表单列表
     *
     * @param tenantId
     * @return
     * @throws Exception
     *//*
    public ViewListData<String[]> queryFormList(String tenantId, int size, int start)
            throws RestException {
        FormService fs = bpmRestServices(null).getFormService();
        FormQueryParam formQueryParam = new FormQueryParam();
        formQueryParam.setOrganizationKey(tenantId);
        formQueryParam.setActivityId("null");
        formQueryParam.setSize(size);
        formQueryParam.setStart(start);

        JsonNode obj = (JsonNode) fs.queryForms(formQueryParam);

        ArrayNode arr = BaseUtils.getData(obj);
        int dataSize = (arr == null ? 0 : arr.size());
        List<String[]> list = new ArrayList<String[]>(dataSize);
        for (int i = 0; i < dataSize; i++) {
            JsonNode node = arr.get(i);
            String formId = node.get("formId").textValue();
            String processId = node.get("processId").textValue();
            String description = node.get("description").textValue();
            String[] tmp = new String[]{formId, processId, description};
            list.add(tmp);
        }

        return createViewListData(obj, list);
    }

    *//**
     * 提交某个任务的审批
     *
     * @param taskId
     * @param agreed       是否同意申请
     * @param auditComment 填写的审批意见
     * @return 是否提交成功
     * @throws RestException
     *//*
    public boolean completeTask(String userId, String taskId, boolean agreed, String auditComment)
            throws RestException {
        List<RestVariable> taskVariables = new ArrayList<RestVariable>();

        RestVariable agreeVariable = new RestVariable();
        agreeVariable.setName("agree");
        agreeVariable.setValue(agreed ? "Y" : "N");
        agreeVariable.setVariableScope(RestVariableScope.LOCAL);
        taskVariables.add(agreeVariable);

        TaskService ts = bpmRestServices(userId).getTaskService();

        Object node = ts.completeWithComment(taskId, taskVariables, null, null,
                auditComment);
        if (node != null) {
            return true;
        } else {
            return false;
        }
    }
    *//**
     * 部署流程定义
     * @param userId
     * @param inputStream
     * @param name 文件名称
     * @throws RestException
     *//*
    public void deployment(String userId,InputStream inputStream,String name) throws RestException{
    	bpmRestServices(userId).getRepositoryService().postNewDeploymentBPMNFile(inputStream, name);
    }

    *//**
     * 取消抢占
     *
     * @param taskId
     * @return
     *//*
    public boolean withdrawTask(String userId, String taskId) throws RestException {
        return bpmRestServices(userId).getTaskService().withdrawTask(taskId);
    }

    *//**
     * 对一个任务进行评论
     *
     * @param userId
     * @param taskId
     * @param comment
     * @param saveInstanceId
     * @return
     * @throws Exception
     *//*
    public List<CommentResponse> commentTask(String userId, String taskId, String comment, boolean saveInstanceId)
            throws RestException {
        TaskService ts = bpmRestServices(userId).getTaskService();
        JsonNode obj = (JsonNode) ts.addComment(taskId, comment, saveInstanceId);
        if (log.isDebugEnabled()) log.debug("commentTask 返回:" + obj);
        ArrayNode arr = BaseUtils.getData(obj);
        List<CommentResponse> list = new ArrayList<CommentResponse>(arr.size());
        for (int i = 0; i < arr.size(); i++) {
            JsonNode node = arr.get(i);
            CommentResponse resp = jsonResultService.toObject(node.toString(), CommentResponse.class);
            list.add(resp);
        }
        return list;
    }
    *//**
     * 添加附件
     * @param userId
     * @param taskId
     * @param name
     * @param desc
     * @param is
     * @return
     * @throws RestException
     *//*
    public AttachmentResponse createAttachment(String userId, String taskId, String name, String desc, InputStream is)
            throws RestException {
        TaskService ts = bpmRestServices(userId).getTaskService();
        TaskAttachmentResourceParam parm = new TaskAttachmentResourceParam();
        parm.setName(name);
        parm.setDescription(desc);
        parm.setValue(is);
        JsonNode obj = (JsonNode) ts.createAttachmentWithContent(taskId, parm);
        //AttachmentResponse ??
        if (log.isDebugEnabled()) {
            log.debug("上传附件返回:" + obj);
        }

        return jsonResultService.toObject(obj.toString(), AttachmentResponse.class);
    }
    *//**
     * 查询附件
     * @param userId
     * @param taskId
     * @return
     * @throws RestException
     *//*
    public List<AttachmentResponse> queryAttachmentList(String userId, String taskId)
            throws RestException {
        TaskService ts = bpmRestServices(userId).getTaskService();
        JsonNode node = (JsonNode) ts.getAttachments(taskId);

        if (log.isDebugEnabled()) {
            log.debug("获取附件列表返回:" + node);
        }

        ArrayNode arr = BaseUtils.getData(node);
        int size = arr == null ? 0 : arr.size();
        List<AttachmentResponse> list = new ArrayList<AttachmentResponse>(size);
        for (int i = 0; i < size; i++) {
            AttachmentResponse resp = jsonResultService.toObject(arr.get(i).toString(), AttachmentResponse.class);
            list.add(resp);
        }
        return list;
    }
    *//**
     * 获取附件内容
     * @param userId
     * @param taskId
     * @param attachmentId
     * @return
     * @throws RestException
     *//*
    public InputStream getAttachment(String userId, String taskId, String attachmentId)
            throws RestException {
        TaskService ts = bpmRestServices(userId).getTaskService();
        byte[] bytes = (byte[]) ts.getAttachmentContent(taskId, attachmentId);
        return new ByteArrayInputStream(bytes);
    }

    *//**
     * 对实例添加comment
     *
     * @param userId
     * @param instanceId
     * @param comment
     * @throws Exception
     *//*
    public CommentResponse commentInstance(String userId, String instanceId, String comment)
            throws RestException {
        HistoryService hs = bpmRestServices(userId).getHistoryService();
        JsonNode obj = (JsonNode) hs.addComment(instanceId, comment);
        log.debug("HistoryService.addComment=" + obj);
        if (obj != null)
            return jsonResultService.toObject(obj.toString(), CommentResponse.class);
        return null;
    }
    *//**
     * 查询流程实例全部信息
     *
     * @param userId
     * @param instId
     * @throws RestException
     *//*
    public JsonNode getProcessInstanceAllInfo(String userId, String instId, BpmHistoricProcessInstanceParam parm) throws RestException {
        HistoryService ht = bpmRestServices(userId).getHistoryService();// 历史服务
        JsonNode node = (JsonNode) ht.getHistoricProcessInstance(instId, parm);
        System.out.println("getProcessInstanceAllInfo=\r\n" + node);
        return node;
    }

    *//**
     * 获得流程实例信息
     *
     * @param userId
     * @param instId
     * @return
     * @throws Exception
     *//*
    public HistoricProcessInstanceResponse getProcessInstance(String userId, String instId, boolean includeProcessVariable)
            throws RestException {
        if (log.isDebugEnabled()) log.debug("根据实例id查询实例信息:" + instId);
        HistoryService ht = bpmRestServices(userId).getHistoryService();// 历史服务

        HistoricProcessInstancesQueryParam param = new HistoricProcessInstancesQueryParam();
        param.setProcessInstanceId(instId);
        if (includeProcessVariable)
            param.setIncludeProcessVariables(true);
        else
            param.setIncludeProcessVariables(false);

        JsonNode node = (JsonNode) ht.getHistoricProcessInstances(param);
        if (log.isDebugEnabled()) log.debug("getHistoricProcessInstance=" + node);
        ArrayNode arrNode = BaseUtils.getData(node);
        if (arrNode != null && arrNode.size() > 0) {
            HistoricProcessInstanceResponse resp = jsonResultService.toObject(arrNode.get(0).toString(), HistoricProcessInstanceResponse.class);
            return resp;
        }
        return null;
    }

    public HistoricTaskInstanceResponse getInstanceNotFinishFirstTask(String userId, String instanceId)
            throws RestException {
        HistoryService ht = bpmRestServices(userId).getHistoryService();// 历史服务
        HistoricTaskQueryParam htp = new HistoricTaskQueryParam();
        htp.setProcessInstanceId(instanceId);
        htp.setFinished(false);// 只查询下一个未完成的task
        htp.setSize(1);
        JsonNode jsonNode = (JsonNode) ht.getHistoricTaskInstances(htp);
        ArrayNode obj2 = BaseUtils.getData(jsonNode);
        int size = obj2.size();
        if (size > 0) {
            HistoricTaskInstanceResponse resp = jsonResultService.toObject(obj2.get(0).toString(), HistoricTaskInstanceResponse.class);
            return resp;
        }
        return null;
    }*/

    private <T> ViewListData<T> createViewListData(JsonNode node, List<T> list) {
        ViewListData<T> vd = new ViewListData<T>();
        vd.setTotal(node.get("total").intValue());
        vd.setStart(node.get("start").intValue());
        vd.setSize(node.get("size").intValue());
        vd.setSort(node.get("sort").textValue());
        vd.setOrder(node.get("order").textValue());
        vd.setList(list);
        return vd;
    }
}
