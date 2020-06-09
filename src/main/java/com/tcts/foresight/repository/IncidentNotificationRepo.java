package com.tcts.foresight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tcts.foresight.entity.IncidentNotificationDetailsEntity;

public interface IncidentNotificationRepo extends JpaRepository<IncidentNotificationDetailsEntity, Long> {

	List<IncidentNotificationDetailsEntity> findByTicketid(String ticket_id);
	@Query(nativeQuery = true, value = "select * from incident_notifications_detail where ticket_id = :ticket_id order by notification_sent_till desc")
	List<IncidentNotificationDetailsEntity> findByTicketidDescNotificationSent(@Param(value = "ticket_id") String ticket_id);

}
