package com.tcts.foresight.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface JBPMFilterStatusDAO {
	@SqlQuery("SELECT status FROM status_details order by status")
	@RegisterBeanMapper(String.class)
	List<String> getJBPMFilterStatusList();

}
