package com.tcts.foresight.service.impl;

import java.util.List;

import javax.sql.DataSource;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.tcts.foresight.dao.JBPMFilterStatusDAO;
import com.tcts.foresight.service.JBPMFilterStatusesService;


@Service
public class JBPMFilterStatusesServiceImpl implements JBPMFilterStatusesService {

	Logger logger = LoggerFactory.getLogger(JBPMFilterStatusesServiceImpl.class);

	
	@Autowired
	@Qualifier("auxDataSource")
	private DataSource dataSource;

	@Override
	public List<String> getJBPMFilterStatusList() {

		List<String> statusList = null;
		try {
			Jdbi jdbi = Jdbi.create(dataSource);
			jdbi.installPlugin(new SqlObjectPlugin());
			statusList = jdbi.onDemand(JBPMFilterStatusDAO.class).getJBPMFilterStatusList();
		} catch (Exception e) {
			logger.error("Exception occur in fbpmfilterstatus dao---" + e.getMessage(),e);
		}

		return statusList;
	}


}
