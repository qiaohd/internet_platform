package com.ufgov.ip.dao.system;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ufgov.ip.entity.system.AskForLeave;

public interface AskForLeaveDao extends PagingAndSortingRepository<AskForLeave, String>,JpaSpecificationExecutor<AskForLeave>{

	//List<AskForLeave> findByStatusAndDis(int status,int dis);

	AskForLeave findById(String businessId);

	
	
	
	@Modifying(clearAutomatically = true) @Query("update AskForLeave askForLeave set askForLeave.taskId=:taskId,askForLeave.status=:status,askForLeave.createTime=:createTime where askForLeave.id=:id")
	void updateTicketById(@Param("id")String id, @Param("taskId")String taskId,@Param("status")int status, @Param("createTime")Timestamp createTime);

	AskForLeave findByTaskId(String bpm_task_id);

	
	
	
	
}
