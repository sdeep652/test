package com.tcts.foresight.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcts.foresight.entity.IncidentAttachmentEntity;

@Repository
public interface IncidentAttachmentRepo extends JpaRepository<IncidentAttachmentEntity, Long>{

	List<IncidentAttachmentEntity> findByIncidentID(String ticketId);
	
	@Query(value="select attachment_name from incident_attachments where id = :idParam",nativeQuery = true )
	String getAttachmentName(@Param("idParam") long id);
	
	@Query(value="select ticket_id from incident_attachments where id = :idParam",nativeQuery = true )
	String getIncidentIdAttachment(@Param("idParam") long id);
	


}
