package com.ufgov.ip.api.system;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ufgov.ip.entity.system.AskForLeave;
import com.ufgov.ip.entity.system.IpUser;

public interface AskTicketListServiceI {

	
	//获得当前部门已审批的请假单
	public  Page<AskForLeave> getCheckedTicketlistAll(Map<String, Object> searchParams,
			PageRequest pageRequest);

	public  Page<AskForLeave> getTicketlistAll(Map<String, Object> searchParams,
			PageRequest pageRequest);
	
	
	public Map<String,String> getHirerIdAndCoIdBySearchParams(Map<String, Object> searchParams);
	
	
	
	//解析参数信息
	public Map<String,String> getCheckedAskTicketBySearchParams(Map<String, Object> searchParams);
	
	
	 /**
		 * 通过jdbc查询的结果构建 list<实体>
		 * @param ipRoleMenuList
		 * @return
		 */
    public List<IpUser> buildAskTicketEntityList(List ipUserList);

		
		
    //获得		
	public List<AskForLeave> getAskForLeaveList(List<AskForLeave> list);	
		
		
		
	//获得当前人员的请假单	
	/*public Page<AskForLeave> getCurSubmitTicket(
			Map<String, Object> searchParams, PageRequest pageRequest);*/

	public Page<AskForLeave> getCurSubmitTicket(
			Map<String, Object> searchParams, int pageNumber, int pageSize);
	
	public  List<AskForLeave> getCurTaskList(String hirerId,String userId);
	

}
