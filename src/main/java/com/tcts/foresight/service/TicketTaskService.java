package com.tcts.foresight.service;

import java.util.List;

import com.tcts.foresight.jbpm.db.pojo.ActualVariableInstanceLogVO;




public interface TicketTaskService {

	public List<ActualVariableInstanceLogVO> getProcessInstancesList();
	
}
