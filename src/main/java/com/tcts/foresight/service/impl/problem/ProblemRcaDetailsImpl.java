package com.tcts.foresight.service.impl.problem;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.problem.ProblemRcaDetailsEntity;
import com.tcts.foresight.repository.problem.ProblemRcaRepo;

import ch.qos.logback.classic.Logger;

@Service
public class ProblemRcaDetailsImpl {

	Logger logger = (Logger) LoggerFactory.getLogger(ProblemRcaDetailsImpl.class);

	@Autowired
	ProblemRcaRepo problemRcaRepo;

	public ProblemRcaDetailsEntity addProblemRca(ProblemRcaDetailsEntity problemRcaDetailsEntity) {
		return problemRcaRepo.save(problemRcaDetailsEntity);
	}

	public ProblemRcaDetailsEntity updateProblemRca(ProblemRcaDetailsEntity problemRcaDetailsEntity) {
		return problemRcaRepo.save(problemRcaDetailsEntity);
	}

	public List<ProblemRcaDetailsEntity> fetchAllProblemRca() {
		return problemRcaRepo.findAll();
	}

	public void DeleteProblemRca(Long id) {
		problemRcaRepo.deleteById(id);
	}

	public ProblemRcaDetailsEntity DuplicateCheck() {
		ProblemRcaDetailsEntity problemRcaDetailsEntity = null;
		return problemRcaDetailsEntity;
	}
}
