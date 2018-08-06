package com.ufgov.ip.dao.system;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ufgov.ip.entity.system.TicketDetail;

public interface TicketDetailDao extends PagingAndSortingRepository<TicketDetail, String>,JpaSpecificationExecutor<TicketDetail>{

	List<TicketDetail> findByStatus(int intValue);

}
