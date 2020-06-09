package com.tcts.foresight.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class MasterDataSourceConfig {

	Logger logger = LoggerFactory.getLogger(MasterDataSourceConfig.class);

	@Value("${spring.datasource.url}")
	private String foresightAuxURL;

	@Value("${spring.datasource.username}")
	private String foresightAuxUser;

	@Value("${spring.datasource.password}")
	private String foresightAuxPassword;
	
	
	@Value("${RHPAM_DB_URL}")
	private String jbpmDBURL;

	@Value("${RHPAM_DB_USER}")
	private String jbpmDBUser;

	@Value("${RHPAM_DB_PASSWORD}")
	private String jbpmDBPassword;
	
	

	@Bean(name = "auxDataSource")
	@Primary
	public DataSource createAuxDBDataSource() {
		HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl(foresightAuxURL);
		ds.setUsername(foresightAuxUser);
		ds.setPassword(foresightAuxPassword);
		return ds;
	}
	
	
	@Bean(name = "jbpmDataSource")
	public DataSource createSecondaryDataSource() {
		HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl(jbpmDBURL);
		ds.setUsername(jbpmDBUser);
		ds.setPassword(jbpmDBPassword);
		return ds;
	}

}
