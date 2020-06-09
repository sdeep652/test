package com.tcts.foresight.repository;

import org.springframework.data.repository.CrudRepository;

import com.tcts.foresight.entity.MappedVariableInstanceLogVO;

public interface TicketTaskRepository extends CrudRepository<MappedVariableInstanceLogVO, String> {
	MappedVariableInstanceLogVO findByProcessInstanceId(String processInstanceId);
}
