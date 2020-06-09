package com.tcts.foresight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tcts.foresight.entity.ConfigEntity;

@Repository
public interface ConfigRepo extends JpaRepository<ConfigEntity, Long> {

	@Query(nativeQuery = true, value = "SELECT config_value FROM config  where config_key = :configKey")
	String findByConfigKey(String configKey);

	@Query(nativeQuery = true, value = "SELECT * FROM config  where config_key = :configKey")
	ConfigEntity findByConfigKeyEmail(String configKey);

}
