package com.tcts.foresight.repository.problem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcts.foresight.entity.problem.ProblemAttachmentEntity;


@Repository
public interface ProblemAttachmentRepo extends JpaRepository<ProblemAttachmentEntity, Long>{

	List<ProblemAttachmentEntity> findByProblemID(String ticketId);
	
}


	

