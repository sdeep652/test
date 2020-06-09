package com.tcts.foresight.repository.problem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcts.foresight.entity.problem.ProblemRcaDetailsEntity;

@Repository
public interface ProblemRcaRepo extends JpaRepository<ProblemRcaDetailsEntity, Long> {

}
