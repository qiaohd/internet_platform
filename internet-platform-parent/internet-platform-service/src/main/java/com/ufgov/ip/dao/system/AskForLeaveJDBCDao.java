package com.ufgov.ip.dao.system;

import java.util.List;

import com.ufgov.ip.entity.system.AskForLeave;


public interface AskForLeaveJDBCDao {


	public List<AskForLeave> findAskForLeaveByUserId(String userId);
	
	public List<AskForLeave> findAskForLeaveByBpmUserId(String bpm_userId);
	

}
