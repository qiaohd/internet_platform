package com.ufgov.ip.api.system;

import java.util.List;

import yonyou.bpm.rest.BpmRest;
import yonyou.bpm.rest.exception.RestException;
import yonyou.bpm.rest.request.RestVariable;
import yonyou.bpm.rest.response.historic.HistoricProcessInstanceResponse;

/**
 * bpm流程操作基本服务
 * @author zhangbch
 *
 */
public interface ProcessServiceI {

	public BpmRest getBpmRestServices(String operatorID,String hirerId);
	public BpmRest getBpmRestServices_start(String operatorID,String hirerId,String org_id);
	public HistoricProcessInstanceResponse startProcessByKey(String userId, String processKey, String procInstName, List<RestVariable> variables) throws RestException;
	
	
}
