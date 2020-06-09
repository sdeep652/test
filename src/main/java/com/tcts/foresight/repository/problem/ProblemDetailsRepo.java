package com.tcts.foresight.repository.problem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcts.foresight.entity.problem.ProblemDetailsEntity;

public interface ProblemDetailsRepo extends JpaRepository<ProblemDetailsEntity, String>{

	ProblemDetailsEntity findByProblemID(String problemID);
	
	List<ProblemDetailsEntity> findByStatusNotIn(List<String> status);
}
