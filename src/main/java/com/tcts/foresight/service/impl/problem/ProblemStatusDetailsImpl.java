package com.tcts.foresight.service.impl.problem;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.problem.ProblemStatusDetailsEntity;
import com.tcts.foresight.repository.problem.ProblemStatusRepo;

@Service
public class ProblemStatusDetailsImpl {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ProblemStatusRepo problemStatusRepo;

	public List<ProblemStatusDetailsEntity> getAllStatus() {
		return problemStatusRepo.findAll();
	}

	public List<ProblemStatusDetailsEntity> getFilterStatus() {
		return problemStatusRepo.findAll().stream().filter(status -> (!status.getStatus().equalsIgnoreCase("Closed"))
				&& (!status.getStatus().equalsIgnoreCase("Cancelled"))).collect(Collectors.toList());
	}
	
	public List<ProblemStatusDetailsEntity> getUpdateProbleStatus() {
		return problemStatusRepo.findAll().stream().filter(status -> (!status.getStatus().equalsIgnoreCase("Closed"))
				&& (!status.getStatus().equalsIgnoreCase("New"))).collect(Collectors.toList());
	}
}
