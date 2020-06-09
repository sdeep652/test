package com.tcts.foresight.jbpm.db;

import java.util.ArrayList;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import com.tcts.foresight.jbpm.db.pojo.ActualVariableInstanceLogVO;



public interface VariableInstanceLogInterface {
	
	
	 @SqlQuery("SELECT * FROM variableinstancelog order by log_date desc")
	 @RegisterBeanMapper(ActualVariableInstanceLogVO.class)
	 ArrayList<ActualVariableInstanceLogVO> getProcessInstancesList();
}
