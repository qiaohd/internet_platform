package com.ufgov.ip.api.system;

import java.sql.Timestamp;
import java.util.Map;

import com.ufgov.ip.entity.system.AskForLeave;
import com.ufgov.ip.entity.system.IpUser;
import com.ufgov.ip.entity.system.TicketDetail;

public interface AskForLeaveServiceI {

	public AskForLeave saveEntity(AskForLeave askforleave);
	public AskForLeave findByBusinessId(String businessId);
	public void updateTicketById(String businessId,String taskId,int status, Timestamp time);
	public void saveTicketDetail(TicketDetail ticketDetail);
	public Map<String, String> submitTicketAndRun(final Map<String, String> reg,
			final AskForLeave askforleave, final IpUser user);
	public Map<String, Object> doApproveService(Map<String, Object> resultMap,
			String taskId, String userId, String check, String hirerId,
			String businessId, String suggestion, IpUser findUserByUserId);
	
	
}
