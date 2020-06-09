package com.tcts.foresight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcts.foresight.entity.ProblemNotificationEntity;
import com.tcts.foresight.entity.ProblemNotificationIncident;

@Repository
public interface ProblemNotificationRepo extends JpaRepository<ProblemNotificationEntity , Long>{

	//ProblemNotificationEntity findByIncidentID(String incidentID);
	
	/*
	 * @Query(value =
	 * "SELECT * FROM problem_notification_details where group_name IN (:groupName) and is_enable = (:enable)"
	 * , nativeQuery = true) List<ProblemNotificationEntity>
	 * findByenablegroupname(@Param("enable") String enable,
	 * 
	 * @Param("groupName") List<String> groupName);
	 */
	
// use joins instead of inner queries
	/*
	 * @Query(value =
	 * "select * FROM problem_notification_details where incident_id IN(SELECT ticket_id\r\n"
	 * +
	 * "			FROM mapped_variable_instance_log where mapped_variable_instance_log.ticket_id IN (SELECT incident_id\r\n"
	 * +
	 * "			FROM problem_notification_details where is_enable = (:enable)) and mapped_variable_instance_log.group_name IN (:groupName))"
	 * , nativeQuery = true) List<ProblemNotificationEntity>
	 * findByenableAndGroup(@Param("enable") String enable,
	 * 
	 * @Param("groupName") List<String> groupName);
	 */
	/*
	 * @Query(value =
	 * "select p.*,i.incident_id from problem_notification_details as p, problem_notification_details_incident as i where p.id IN \r\n"
	 * +
	 * "(select notification_id from problem_notification_details_incident where incident_id IN (SELECT ticket_id\r\n"
	 * +
	 * "	FROM public.mapped_variable_instance_log as m where m.ticket_id IN  (select i.incident_id from problem_notification_details_incident as i Inner join problem_notification_details as p  \r\n"
	 * +
	 * "ON i.notification_id = p.id where p.is_enable IN (:enable)) and  m.group_name IN (:groupName)))"
	 * , nativeQuery = true) List<ProblemNotificationEntity>
	 * findByenableAndGroup(@Param("enable") String enable,
	 * 
	 * @Param("groupName") List<String> groupName);
	 */
	
	
	
	@Query(value = "select pd.*,p.incident_id from problem_notification_details_incident p join mapped_variable_instance_log m \r\n" + 
			"ON p.incident_id = m.ticket_id join problem_notification_details pd on \r\n" + 
			"pd.id = p.notification_id where pd.is_enable = :enable and  m.group_name IN (:groupName)", nativeQuery = true)
	List<ProblemNotificationEntity> findByenableAndGroup(@Param("enable") String enable,
			@Param("groupName") List<String> groupName);
	
	
	
	
	List<ProblemNotificationEntity> findByIsEnable(String isEnable);



	@Query(value = "select * from problem_notification_details p join problem_notification_details_incident i On p.id = i.notification_id where i.incident_id IN"
			+ "(:listIncidents)", nativeQuery = true)
	List<ProblemNotificationEntity> getexistedIncidentIdRecordsDhiraj(@Param("listIncidents") List<String> listIncidents);
	
	
	
	@Query(value = "select * from problem_notification_details p join problem_notification_details_incident i On p.id = i.notification_id where i.incident_id IN"
			+ "(:listIncidents) and i.notification_id = (select max(notification_id) from problem_notification_details_incident)", nativeQuery = true)
	ProblemNotificationEntity getexistedIncidentIdRecords(@Param("listIncidents") List<String> listIncidents);
	
	
	

	/*
	 * @Query(value =
	 * "select * from problem_notification_details where incident_id @> :incident_id  \\:\\:varchar[]"
	 * , nativeQuery = true) public List<ProblemNotificationEntity>
	 * findByIncidentList(@Param("incident_id") String incident_id);
	 */

}
