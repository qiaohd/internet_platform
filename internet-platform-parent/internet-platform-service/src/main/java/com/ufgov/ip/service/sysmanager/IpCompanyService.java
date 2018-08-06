package com.ufgov.ip.service.sysmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.utils.Clock;

import yonyou.bpm.rest.BpmRest;
import yonyou.bpm.rest.exception.RestException;
import yonyou.bpm.rest.request.identity.OrgQueryParam;
import yonyou.bpm.rest.request.identity.OrgResourceParam;
import yonyou.bpm.rest.request.identity.UserLinkQueryParam;
import yonyou.bpm.rest.request.identity.UserQueryParam;
import yonyou.bpm.rest.response.identity.OrgResponse;
import yonyou.bpm.rest.response.identity.UserLinkResponse;
import yonyou.bpm.rest.response.identity.UserResponse;
import yonyou.bpm.rest.utils.BaseUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ufgov.ip.api.sysmanager.IpCompanyServiceI;
import com.ufgov.ip.dao.sysmanager.ICompanyDao;
import com.ufgov.ip.dao.sysmanager.IpUserCompanyDao;
import com.ufgov.ip.dao.sysmanager.IpUserRoleDao;
import com.ufgov.ip.dao.sysmanagerimpl.ICompanyDaoImpl;
import com.ufgov.ip.dao.system.IpCompanySynMapper;
import com.ufgov.ip.dao.system.IpHirerMapper;
import com.ufgov.ip.dao.system.IuapUserDao;
import com.ufgov.ip.entity.sysmanager.IpCompany;
import com.ufgov.ip.entity.system.IpHirer;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.IpUserCompany;
import com.ufgov.ip.service.system.JsonResultService;
import com.ufgov.ip.service.system.ProcessService;
import com.ufgov.ip.utils.CopyPropertiesUtil;
import com.ufgov.ip.utils.PropertyUtilSys;
import com.ufgov.ip.utils.UUIDTools;
/**
 * 用户管理服务层
 * 书写readonly是说明没有特殊标识服务层只有只读事物
 * @author guangsa
 *
 */
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Service(version = "0.0.1")
public class IpCompanyService implements IpCompanyServiceI{

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;

	private static Logger logger = LoggerFactory.getLogger(IpCompanyService.class);
	@Autowired
	private ICompanyDao coDao;
	
	@Autowired
	private IpHirerMapper hirerDao;
	
	@Autowired
	private JsonResultService jsonResultService;
	
	@Autowired
	private IpUserCompanyDao ipUserCompanyDao;
	
	@Autowired
	private ICompanyDaoImpl iCompanyDaoImpl;
	
	@Autowired
	private IpUserRoleDao ipUserRoleDao;
	
	@Autowired
	private IuapUserDao userDao;
	
	@Autowired
	private IpCompanySynMapper ipCompanySynMapper;
	
	public IpUserCompanyDao getIpUserCompanyDao() {
		return ipUserCompanyDao;
	}

	public JsonResultService getJsonResultService() {
		return jsonResultService;
	}

	
	public IpHirerMapper getHirerDao() {
		return hirerDao;
	}

	@Autowired
	private ProcessService processService;

	public ProcessService getProcessService() {
		return processService;
	}
	

	public void setCoDao(ICompanyDao coDao) {
		this.coDao = coDao;
	}

	private Clock clock = Clock.DEFAULT;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean registercompany(IpCompany co) {
		try {
			
			coDao.saveCompany(co);
			
		} catch (Exception e) {
			logger.error("注册单位失败!");
			return false;
		}
		
		return true;
	}
	
	public IpCompany findIpCompanyByName(String companyName){
			
		return coDao.findByCoName(companyName);
	}
	
	
	public IpCompany findIpCompanyByNameAndparentCoId(String companyName,String pCoId){
		
		return coDao.findIpCompanyByCoNameAndParentCoId(companyName,pCoId);
	}
	
	public IpCompany findByCoCodeAndhirerId(String coCode, String hirerId) {
		// TODO 自动生成的方法存根
		return coDao.findIpCompanyByCoCodeAndHirerId(coCode, hirerId);
	}
	
	@Transactional
	public Map<String, String> saveIpCompanyEntity(IpCompany ipCompanyEntity, Map<String, String> resultMap) {
		// TODO 自动生成的方法存根
		Boolean checkBoolean=checkIpCompany(ipCompanyEntity,resultMap);
		if(checkBoolean){
			if (ipCompanyEntity.getCoId()==null || ipCompanyEntity.getCoId()=="") {
				ipCompanyEntity.setCoId(UUIDTools.uuidRandom());
				//begin_工作流同步使用_20161215
				  ipCompanyEntity.setCoCodeTmp(ipCompanyEntity.getCoCode());
				//end_工作流同步使用_20161215
			}
			ipCompanyEntity.setCoFullname(ipCompanyEntity.getCoName());
			ipCompanyEntity.setIsEnabled("1");
			ipCompanyEntity.setLinkman("");
			ipCompanyEntity.setDeptDetail("");
			setIpCompanyLevelNum(ipCompanyEntity);
			// coDao.save(ipCompanyEntity);
			//添加dept_detail
			setDeptDetailForIpCompany(ipCompanyEntity);
			/**
			 * 
			 * OrgResourceParam
			 *  code	String	是	部门编码
				name	String	是	部门名称
				parent	String	否	父部门id(id和编码，提供一个即可)
				parentCode	String	否	父部门编码
				enable	Boolean	否	是否启用，默认为true
				source	String	否	来源
				revision	Integer	否	保存时revision=0，更新时revision>0，更新时id不能为空。保存提供批量方法，更新不提供

			 */
			//begin_通过rest服务同步部门信息
			String getbpmEnabledByKey = PropertyUtilSys.getbpmEnabledByKey("bpm.enabled");
		      if("true".equals(getbpmEnabledByKey)){//开启工作流
			    String hirerId = ipCompanyEntity.getHirerId();
			    IpHirer ipHirer=new IpHirer();
			    ipHirer.setHirerId(hirerId);
			    //IpHirer findHirerByHirerId = hirerDao.findAll(hirerId).get(0);
			    IpHirer findHirerByHirerId = hirerDao.findHirerByCondition(ipHirer);
			    BpmRest bpmRestServices=processService.getBpmRestServices(hirerId,hirerId);
			    String tenantLimitId = bpmRestServices.getIdentityService().getBaseParam().getTenantLimitId();
			    OrgResourceParam orgResourceParam = new OrgResourceParam();
				//orgResourceParam.setId(ipCompanyEntity.getCoId());
				//orgResourceParam.setTenantId(tenantLimitId);
				    //begin_此处需要查询父级部门或所属租户,然后同步到bpm中
				       String parentCoId = ipCompanyEntity.getParentCoId();
				       IpCompany findByCoId = coDao.findByCoId(parentCoId);
				       String coCode = findByCoId.getCoCode();
				       //根据code查出bpm的部门信息，获得部门id，作为当前部门的父级id
				          OrgQueryParam orgQueryParam = new OrgQueryParam();
						  orgQueryParam.setTenantId(hirerId);
						  orgQueryParam.setCode(coCode);
						  JsonNode org_jsonNode=null;
						try {
							org_jsonNode = (JsonNode)bpmRestServices.getIdentityService().queryOrgs(orgQueryParam);
							List<JsonNode> findValues = org_jsonNode.findValues("id");
							for (JsonNode jsonNode : findValues) {
								String co_id = jsonNode.toString();
								co_id = co_id.substring(1, co_id.length()-1);
								orgResourceParam.setParent(co_id);
								orgResourceParam.setParentCode(coCode);
							}
						} catch (RestException e1) {
							// TODO 自动生成的 catch 块
							e1.printStackTrace();
						}
				    //end_此处需要查询父级部门或所属租户,然后同步到bpm中
				orgResourceParam.setEnable(true);
				orgResourceParam.setCode(ipCompanyEntity.getCoCode());
				orgResourceParam.setName(ipCompanyEntity.getCoName());
				orgResourceParam.setCreator(ipCompanyEntity.getHirerId());
				JsonNode jsonNode=null;
				try {
					 jsonNode = (JsonNode)bpmRestServices.getIdentityService().saveOrg(orgResourceParam);
					
					System.out.println(jsonNode.toString());
					System.out.println("co_id:"+jsonNode.findValues("id").get(0).toString()+"***");
				} catch (RestException e) {
					e.printStackTrace();
				}
		      }
			//end_通过rest服务同步部门信息
			coDao.save(ipCompanyEntity);
			
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * 设置部门详细字段 
	 * 在人员导入时，根据父子拼接的部门名称，找到唯一的人员所属部门.
	 * 租户注册时，往ip_company添加了一条记录. 
	 * @param ipCompanyEntity
	 */
	public void setDeptDetailForIpCompany(IpCompany ipCompanyEntity) {
		IpCompany ipCompany_d = ipCompanyEntity;
		List<String> companyNameList=new ArrayList<String>();
		if(ipCompany_d.getLevelNum()!=1){
			for(int i=Integer.valueOf(ipCompany_d.getLevelNum());i>0;i--){
				if(i==1)
				  break;
				if(companyNameList.size()==0){
				  companyNameList.add(ipCompany_d.getCoName());
				}
				ipCompany_d = coDao.findByCoId(ipCompany_d.getParentCoId());
				//begin_修改增加一级部门时不拼接公司名
				if(!"1".equals(String.valueOf(ipCompany_d.getLevelNum()))){
					companyNameList.add(ipCompany_d.getCoName());
				}
				//end_修改增加一级部门时不拼接公司名
			}
		}
		Collections.reverse(companyNameList);
		StringBuffer str=new StringBuffer();
		for (String string : companyNameList) {
			str.append(string);
		}
		ipCompanyEntity.setDeptDetail(str.toString());
	}
	
	/**
	 * 更新部门
	 * @param ipCompanyEntity
	 * @param resultMap
	 */
	@Transactional
	public Map<String, String> updateIpCompanyEntity(IpCompany ipCompanyEntity, Map<String, String> resultMap) {
		
		Boolean checkBoolean = checkIpCompany(ipCompanyEntity, resultMap);
		if(checkBoolean){
			IpCompany findByCoId = coDao.findByCoId(ipCompanyEntity.getCoId());
			// 取原值 
			ipCompanyEntity.setDispOrder(findByCoId.getDispOrder());
			CopyPropertiesUtil.copyProperty(findByCoId, ipCompanyEntity);
			findByCoId.setCoFullname(findByCoId.getCoName());
			setIpCompanyLevelNum(findByCoId);
			setDeptDetailForIpCompany(findByCoId);
			
			String getbpmEnabledByKey = PropertyUtilSys.getbpmEnabledByKey("bpm.enabled");//获得工作流开/关状态值
			if("true".equals(getbpmEnabledByKey)){
				//同步更新bpm部门
				 //1.先根据code查询出bpm的部门信息
				String coCode = findByCoId.getCoCode();
				OrgQueryParam orgQueryParam = new OrgQueryParam();
				orgQueryParam.setCode(ipCompanyEntity.getOldCoCode());
				BpmRest bpmRestServices=processService.getBpmRestServices(findByCoId.getHirerId(),findByCoId.getHirerId());
			    List<OrgResponse> list = new ArrayList<OrgResponse>();
			    try {
					JsonNode jsonNode = (JsonNode)bpmRestServices.getIdentityService().queryOrgs(orgQueryParam);
					 ArrayNode arrayNode = BaseUtils.getData(jsonNode);
					 for (int i = 0; arrayNode != null && i < arrayNode.size(); i++) {
						 OrgResponse resp =jsonResultService.toObject(arrayNode.get(i).toString(), OrgResponse.class);
				            list.add(resp);
				        }
					 
				       IpCompany p_co = coDao.findByCoId(findByCoId.getParentCoId());
				       String p_coCode = p_co.getCoCode();
				       //根据code查出bpm的部门信息，获得部门id，作为当前部门的父级id
				          OrgQueryParam p_orgQueryParam = new OrgQueryParam();
				          p_orgQueryParam.setTenantId(findByCoId.getHirerId());
				          p_orgQueryParam.setCode(p_coCode);
						  JsonNode org_jsonNode=null;
							try {
								org_jsonNode = (JsonNode)bpmRestServices.getIdentityService().queryOrgs(p_orgQueryParam);
								List<JsonNode> findValues = org_jsonNode.findValues("id");
								OrgResourceParam orgResourceParam = new OrgResourceParam();
								//orgResourceParam.setId(ipCompanyEntity.getCoId());
								//orgResourceParam.setTenantId(tenantLimitId);
							   orgResourceParam.setId(list.get(0).getId());
							   orgResourceParam.setRevision(list.get(0).getRevision());
							    orgResourceParam.setEnable(true);
								orgResourceParam.setCode(findByCoId.getCoCode());
								orgResourceParam.setName(findByCoId.getCoName());
								orgResourceParam.setCreator(list.get(0).getTenantId());
								orgResourceParam.setParent(findValues.get(0).toString().substring(1, findValues.get(0).toString().length()-1));
								orgResourceParam.setParentCode(p_coCode);
								bpmRestServices.getIdentityService().saveOrg(orgResourceParam);
								
								/*for (JsonNode jsonNode : findValues) {
									String co_id = jsonNode.toString();
									co_id = co_id.substring(1, co_id.length()-1);
									orgResourceParam.setParent(co_id);
									orgResourceParam.setParentCode(coCode);
								}*/
							} catch (RestException e1) {
								// TODO 自动生成的 catch 块
								e1.printStackTrace();
							}
					 
					
					/* for (OrgResponse orgResponse : list) {
						
						  OrgResourceParam orgResourceParam = new OrgResourceParam();
							//orgResourceParam.setId(ipCompanyEntity.getCoId());
							//orgResourceParam.setTenantId(tenantLimitId);
						   orgResourceParam.setId(orgResponse.getId());
						   orgResourceParam.setRevision(orgResponse.getRevision());
						    orgResourceParam.setEnable(true);
							orgResourceParam.setCode(findByCoId.getCoCode());
							orgResourceParam.setName(findByCoId.getCoName());
							orgResourceParam.setCreator(orgResponse.getTenantId());					 
							bpmRestServices.getIdentityService().saveOrg(orgResourceParam);
					 }*/
					
					 System.out.println(jsonNode);
				} catch (RestException e) {
					e.printStackTrace();
				}
			}
			coDao.save(findByCoId);
			
			return resultMap;
		}
		return resultMap;
	}
	
	
	/**
	 * 设置部门级别
	 * @param ipCompanyEntity
	 */
	public void setIpCompanyLevelNum(IpCompany ipCompanyEntity) {
		if(ipCompanyEntity.getParentCoId()!=null){
			String parentCoId=ipCompanyEntity.getParentCoId();
			IpCompany ipCompanyByParent= findIpCompanyByCoId(parentCoId);
			if(ipCompanyByParent!=null){
//				ipCompanyEntity.setLevelNum(String.valueOf((Integer.valueOf(ipCompanyByParent.getLevelNum())+1)));
				ipCompanyEntity.setLevelNum(ipCompanyByParent.getLevelNum()+1);
			}
		}
	}
	
	/**
	 * 验证
	 * @param entity
	 * @param resultMap
	 * @return
	 */
	public Boolean checkIpCompany(IpCompany ipCompanyEntity, Map<String, String> resultMap) {
		IpCompany ipCompany= findIpCompanyByNameAndparentCoId(ipCompanyEntity.getCoName(),ipCompanyEntity.getParentCoId());
		if(ipCompany!=null){
			// 更新 
			if(null!=ipCompanyEntity.getCoId()&&!("").equals(ipCompanyEntity.getCoId())){
				if(!ipCompanyEntity.getCoId().equals(ipCompany.getCoId())){
					 getErrorMsg(resultMap,"该部门名称已存在！");
					 return false;
				}
			}else{// 新增直接返回错误信息 
			  getErrorMsg(resultMap,"该部门名称已存在！");
			  return false;
			}
		}
		// 判断部门编码 
		IpCompany ipCompanyByCoCode =findByCoCodeAndhirerId(ipCompanyEntity.getCoCode(),ipCompanyEntity.getHirerId());
		if(ipCompanyByCoCode!=null){
			// 更新 
			if(null!=ipCompanyEntity.getCoId()&&!("").equals(ipCompanyEntity.getCoId())){
				if(!ipCompanyEntity.getCoId().equals(ipCompanyByCoCode.getCoId())){
					 getErrorMsg(resultMap,"该部门编码已存在！");
					 return false;
				}
			}else{// 新增直接返回错误信息 
			  getErrorMsg(resultMap,"该部门编码已存在！");
			  return false;
			}
		}
		if(null!=ipCompanyEntity.getParentCoId()&&!("").equals(ipCompanyEntity.getParentCoId())){
			IpCompany parentMenu =findIpCompanyByCoId(ipCompanyEntity.getParentCoId());
			if(parentMenu==null){
				getErrorMsg(resultMap,"父菜单已不存在，请确认！");
				return false;
			}
		}
		return true;
		
	}
	
	

	public Boolean checkIpCompanyForUpdate(IpCompany ipCompanyEntity, Map<String, String> resultMap) {
		
		if(null!=ipCompanyEntity.getParentCoId()&&!("").equals(ipCompanyEntity.getParentCoId())){
			IpCompany parentMenu =findIpCompanyByCoId(ipCompanyEntity.getParentCoId());
			if(parentMenu==null){
				getErrorMsg(resultMap,"父菜单已不存在，请确认！");
				return false;
			}
		}
		return true;
		
	}
	
	
	/**
	 * 通过部门ID查询部门信息 
	 * @param coId 部门ID
	 * @return
	 */
	public IpCompany findIpCompanyByCoId(String coId) {
		return coDao.findByCoId(coId);
	}
	
	/**
	 * 获得当前部门的所有子部门
	 * @param parentCoId
	 * @return
	 */
	public List<IpCompany> findCompanyByParentCoId(String parentCoId){
		
		List<IpCompany> ipCompanies= coDao.findCompanyByParentCoId(parentCoId);
		List<IpCompany> ipCompanyList =new ArrayList<IpCompany>();
		int order = 1;
		// 部门顺序显示  从1开始1、2、3、4、....
//		for (IpCompany ipCompany : ipCompanies) {
//			ipCompany.setDispOrder(order+"");
//			ipCompanyList.add(ipCompany);
//			order++;
//		}
		return ipCompanies;
	}
	

	/**
	 * 错误信息 
	 * @param resultMap
	 * @param reason
	 */
	public void getErrorMsg(Map<String, String> resultMap,String reason) {
		resultMap.put("result", "fail");
		resultMap.put("reason", reason);
	}

	public List<IpCompany> getCompanyPage(Map<String, Object> searchParams) {
		// TODO 自动生成的方法存根
		Sort sort = new Sort(Direction.ASC, "levelNum","dispOrder","coCode");
		 Specification<IpCompany> spec = buildSpecification(searchParams);
		 return coDao.findAll(spec, sort);
	}  
	/**
     * 创建动态查询条件组合.
     */
    public Specification<IpCompany> buildSpecification(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<IpCompany> spec = DynamicSpecifications.bySearchFilter(filters.values(), IpCompany.class);
        return spec;
    }

	public IpCompany findByCoId(String coId) {
		// TODO 自动生成的方法存根
		return coDao.findByCoId(coId);
	}

	@Transactional
	public void updateCompanyBycoId(String coId, String parentCoId) {
		// TODO 自动生成的方法存根
			coDao.updateCompanyBycoId(coId,parentCoId);
	}
    
	@Transactional
	public String delete(IpCompany findByCoId) {

		//begin_restful_同步删除bpm的部门信息
		// 获得所有的子部门_含有子部门不能删除
				List<IpCompany> findCompanyByParentCoId = coDao
						.findCompanyByParentCoId(findByCoId.getCoId());
				if (findCompanyByParentCoId.size() != 0) {
						return "has_son";
				} else {
					List<IpUserCompany> findIpUserCompanyByCoId = ipUserCompanyDao
							.findIpUserCompanyByCoId(findByCoId.getCoId());
					if (findIpUserCompanyByCoId.size() != 0) {
							return "has_employee";
					} else {
							String getbpmEnabledByKey = PropertyUtilSys.getbpmEnabledByKey("bpm.enabled");//获得工作流开/关状态值
							if("true".equals(getbpmEnabledByKey)){
								//同步删除bpm的对应的部门
								 //1.按照code查询bpm的部门,获得id
								  OrgQueryParam orgQueryParam = new OrgQueryParam();
								  orgQueryParam.setTenantId(findByCoId.getHirerId());
								  orgQueryParam.setCode(findByCoId.getCoCode());
								  BpmRest bpmRestServices=processService.getBpmRestServices(findByCoId.getHirerId(),findByCoId.getHirerId());
								  JsonNode jsonNode=null;
								  try {
									 jsonNode = (JsonNode)bpmRestServices.getIdentityService().queryOrgs(orgQueryParam);
									 String bpm_coId=jsonNode.findValues("id").get(0).toString();
									 bpm_coId = bpm_coId.substring(1, bpm_coId.length()-1);
									//2.根据id删除bpm对应的用户组
									  bpmRestServices.getIdentityService().deleteOrg(bpm_coId);
								} catch (RestException e) {
									e.printStackTrace();
								}
							}
							// 删除当前的部门
							coDao.delete(findByCoId);
							return "true";
					}
				}
	}

	public List<IpCompany> findCompanyByDeptDetailLike(String deptDetail,String hirerId) {
		return coDao.findCompanyByDeptDetailLike(deptDetail,hirerId);
	}
	
	public List<IpCompany> findAll() {
		return (List<IpCompany>) coDao.findAll();
	}

	public List<IpCompany> findCompanyByHirerId(String hirerId) {
		// TODO 自动生成的方法存根
		return coDao.findCompanyByHirerId(hirerId);
	}
	
	public void doSearchCompany(IpCompany findByCoId,List <IpCompany> totalChildLists){
		String curCoId=findByCoId.getCoId();
		//找到当前父部们下的所有子部门
				List curChildLists=new ArrayList();
					curChildLists=iCompanyDaoImpl.findChildCompanyByCoId(curCoId);
					List<IpCompany> ipCompanys= buildIpMenuEntityList(curChildLists);
					for(IpCompany ipCompany:ipCompanys){				
						totalChildLists.add(ipCompany);
						doSearchCompany(ipCompany,totalChildLists);
					}
	}
	public List<IpCompany> findAllChild(IpCompany findByCoId){
		List <IpCompany> totalChildLists=new ArrayList<IpCompany>();
		doSearchCompany(findByCoId,totalChildLists);
		return totalChildLists;
	}
	
	@Transactional
	public void deletePart(IpCompany findByCoId) {
		List<IpCompany> childList=findAllChild(findByCoId);
		//再加上自己本身
		childList.add(findByCoId);
		List<String> userCodeList=new ArrayList<String>();
		List<String> bpmUserIdList=new ArrayList<String>();
		for(IpCompany ipCompany:childList){
			//从ipCompany找到userId，删除ipuser表中的用户信息
			List<IpUserCompany> ipUserCompanys=ipUserCompanyDao.findIpUserCompanyByCoId(ipCompany.getCoId());
			for(IpUserCompany ipUserCompany :ipUserCompanys){
				String userId=ipUserCompany.getUserId();
				IpUser findByUserIdAndisEnabled = userDao.findByUserIdAndisEnabled(userId, "1");
				userCodeList.add(findByUserIdAndisEnabled.getLoginName());
				userDao.deleteByUserId(userId);
				
			}
			// 删除当前的部门
			coDao.deleteByCoId(ipCompany.getCoId());
			//删除ipusercompany中的所有员工和ipuserrole表中的人员
			ipUserCompanyDao.deleteByCoId(ipCompany.getCoId());
			ipUserRoleDao.deleteByCoId(ipCompany.getCoId());
			
			 //begin_记录删除的部门_20161219
				   IpCompany ipCompany2 = new IpCompany();
				   ipCompany2.setHirerId(ipCompany.getHirerId());
				   ipCompany2.setCoCodeTmp(ipCompany.getCoCodeTmp());
				   ipCompany2.setCoId(ipCompany.getCoId());
				   ipCompanySynMapper.insert(ipCompany);
			 //end_记录删除的部门_20161219
			
			
			
			
			//同步删除bpm的数据
			String getbpmEnabledByKey = PropertyUtilSys.getbpmEnabledByKey("bpm.enabled");//获得工作流开/关状态值
			if("true".equals(getbpmEnabledByKey)){
					try {
						BpmRest bpmRestServices=processService.getBpmRestServices(ipCompany.getHirerId(),ipCompany.getHirerId());
						for (String bpmUserCode : userCodeList) {
							//查询bpm的user
							UserQueryParam userQueryParam = new UserQueryParam();
							userQueryParam.setCode(bpmUserCode);
							JsonNode jsonNode = (JsonNode)bpmRestServices.getIdentityService().queryUsers(userQueryParam);
							 ArrayNode arrayNode = BaseUtils.getData(jsonNode);
							 for (int i = 0; arrayNode != null && i < arrayNode.size(); i++) {
								 UserResponse resp =jsonResultService.toObject(arrayNode.get(i).toString(), UserResponse.class);
								 bpmUserIdList.add(resp.getId());
								//1.删除指定的人员
								 bpmRestServices.getIdentityService().deleteUser(resp.getId());
						        }
						 }
						 
						//2.删除部门
						  OrgQueryParam orgQueryParam = new OrgQueryParam();
						  orgQueryParam.setCode(ipCompany.getCoCode());
						  orgQueryParam.setName(ipCompany.getCoName());
						  JsonNode jsonNode = (JsonNode)bpmRestServices.getIdentityService().queryOrgs(orgQueryParam);
						  ArrayNode arrayNode = BaseUtils.getData(jsonNode);
							 for (int i = 0; arrayNode != null && i < arrayNode.size(); i++) {
								 OrgResponse resp =jsonResultService.toObject(arrayNode.get(i).toString(), OrgResponse.class);
								 bpmRestServices.getIdentityService().deleteOrg(resp.getId());
						        }
						//3.删除用户管理关系
							 UserLinkQueryParam userLinkQueryParam = new UserLinkQueryParam();
							 for (String bpmUserId : bpmUserIdList) {
								 userLinkQueryParam.setTenantId(ipCompany.getHirerId());
								 userLinkQueryParam.setUserId(bpmUserId);
								 JsonNode jsonNodeLink = (JsonNode)bpmRestServices.getIdentityService().queryUserLinks(userLinkQueryParam);
								 ArrayNode arrayNodeLink = BaseUtils.getData(jsonNodeLink);
								 for (int i = 0; arrayNodeLink != null && i < arrayNodeLink.size(); i++) {
									 UserLinkResponse resp =jsonResultService.toObject(arrayNode.get(i).toString(), UserLinkResponse.class);
									 bpmRestServices.getIdentityService().deleteUserLink(resp.getId());
							        }
							 }
				} catch (RestException e) {
					e.printStackTrace();
				}
				
			}
			
		}
    }
	public List<IpCompany> buildIpMenuEntityList(List coIdMapLists) {
		// TODO 自动生成的方法存根
		List<IpCompany> coIds = new ArrayList<IpCompany>();
		IpCompany ipCompany;
		Iterator iterator = coIdMapLists.iterator();
		 while (iterator.hasNext()) {
			 Map map4ipMenu = (Map) iterator.next();
			 ipCompany = new IpCompany();
			 ipCompany.setCoId((String)map4ipMenu.get("coId"));	
			 ipCompany.setHirerId((String)map4ipMenu.get("hirerId"));
			 ipCompany.setCoName((String)map4ipMenu.get("coName"));
			 coIds.add(ipCompany);
		}
		return coIds;
	}

	@Override
	public List<IpCompany> findAllCurDeptAndAllChild(IpCompany ipComapny) {
		List<IpCompany> childList=findAllChild(ipComapny);
		//再加上自己本身
		childList.add(ipComapny);
		return childList;
	}
}
