package com.tcts.foresight.scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcts.foresight.scheduler.entity.SLAConfigurationActionsEntity;

public interface SLAConfigurationActionsRepo extends JpaRepository<SLAConfigurationActionsEntity, Long> {

}
