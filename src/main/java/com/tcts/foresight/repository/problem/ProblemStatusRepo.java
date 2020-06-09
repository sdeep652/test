package com.tcts.foresight.repository.problem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.tcts.foresight.entity.problem.ProblemStatusDetailsEntity;

public interface ProblemStatusRepo
		extends JpaRepository<ProblemStatusDetailsEntity, Long>, CrudRepository<ProblemStatusDetailsEntity, Long> {

}
