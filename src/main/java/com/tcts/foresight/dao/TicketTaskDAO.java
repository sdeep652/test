//package com.tcts.foresight.dao;
//
//import java.util.List;
//
//import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
//import org.jdbi.v3.sqlobject.statement.SqlQuery;
//
//import com.tcts.foresight.jbpm.db.pojo.ActualVariableInstanceLogVO;
//
//
//
//public interface TicketTaskDAO {
//
//	@SqlQuery("SELECT * FROM variableinstancelog order by log_date desc")
//	@RegisterBeanMapper(ActualVariableInstanceLogVO.class)
//	List<ActualVariableInstanceLogVO> getProcessInstancesList();
//}
