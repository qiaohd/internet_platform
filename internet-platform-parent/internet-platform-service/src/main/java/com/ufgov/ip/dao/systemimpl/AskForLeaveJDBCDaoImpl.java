package com.ufgov.ip.dao.systemimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ufgov.ip.dao.system.AskForLeaveJDBCDao;
import com.ufgov.ip.entity.system.AskForLeave;
@Repository
public class AskForLeaveJDBCDaoImpl implements AskForLeaveJDBCDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	@Override
	public List<AskForLeave> findAskForLeaveByUserId(String userId) {

		
		String sql="select ask.id as id, ask.start_date as startDate, ask.end_date as endDate, ask.type as type, ask.reason as reason, ticket.suggestion as result,ask.create_time as createTime"
				  +" from ask_for_leave ask, ticket_detail ticket"
				  +" where ask.id = ticket.business_id"
				  +" and ask.usid = ticket.usid and ask.status=ticket.status" 
				  +" and ask.create_time=ticket.create_time" 
				  +" and ask.usid = ?";
		
		
		
		/*String sql="select ask.id as id,"
                   +"ask.start_date as startDate,"
			       +"ask.end_date as endDate,"
			       +"ask.reason as reason,"
			       +"ask.type as type,"
			       +"(select ticket.suggestion"
			       +" from ticket_detail ticket"
			       +" where ask.uid = ticket.uid"
			       +" and ask.status = ticket.status) as result"
			       +" from ask_for_leave ask where ask.uid=?";*/
		List<AskForLeave> askForLeave =jdbcTemplate.query(sql,new Object[] {userId}, BeanPropertyRowMapper.newInstance(AskForLeave.class));
		return askForLeave;
	}

	
	@Override
	public List<AskForLeave> findAskForLeaveByBpmUserId(String bpm_userId) {

		String sql="select ask.id as id, ask.start_date as startDate, ask.end_date as endDate, ask.type as type, ask.reason as reason,ask.usid as usid,ticket.suggestion as result,ask.create_time as createTime"
				  +" from ask_for_leave ask, ticket_detail ticket"
				  +" where ask.id = ticket.business_id"
				  +" and ticket.bpm_uid = ?";
		
		
		
		List<AskForLeave> askForLeave =jdbcTemplate.query(sql,new Object[] {bpm_userId}, BeanPropertyRowMapper.newInstance(AskForLeave.class));
		return askForLeave;
	}

}
