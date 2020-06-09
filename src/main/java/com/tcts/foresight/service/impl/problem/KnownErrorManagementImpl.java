package com.tcts.foresight.service.impl.problem;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.entity.problem.KnownErrorEntity;
import com.tcts.foresight.entity.problem.ProblemDetailsEntity;
import com.tcts.foresight.entity.problem.ProblemIncidentEntity;
import com.tcts.foresight.repository.MappedVariableInstanceRepo;
import com.tcts.foresight.repository.problem.KnownErrorRepo;
import com.tcts.foresight.repository.problem.ProblemDetailsRepo;
import com.tcts.foresight.repository.problem.ProblemIncidentRepo;
import com.tcts.foresight.util.StringUtil;

@Service
public class KnownErrorManagementImpl {

	@Autowired
	KnownErrorRepo lKnownErrorRepo;

	@Autowired
	MappedVariableInstanceRepo mappedVariableInstanceRepo;

	@Autowired
	private ProblemIncidentRepo problemIncidentRepo;

	@Autowired
	private ProblemDetailsRepo problemDetailsRepo;

	public void checkAndInsertInKnownError(ProblemDetailsEntity jsonPayload) {
		// save
		String saveToKEDB = jsonPayload.getAddKedb();
		String saveToBroadcast = jsonPayload.getBroadcastWorkAround();
		if ("true".equalsIgnoreCase(saveToKEDB)) {
			System.out.println("========================="+jsonPayload.getProblemID());
			deleteFromKEDB(jsonPayload.getProblemID());
			saveToKEDB(jsonPayload);
			saveWordAround(jsonPayload);
		}
		
		
		

		if ("false".equalsIgnoreCase(saveToKEDB)) {
			lKnownErrorRepo.deleteByProblemID(jsonPayload.getProblemID());
			saveWordAround(jsonPayload);
		}

		
	}

	
	  private void deleteFromKEDB(String problemID) {
	  
		  try {
			  System.out.println("deleteng record");
			lKnownErrorRepo.deleteByProblemID(problemID);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	  }
	 

	private void saveToKEDB(ProblemDetailsEntity jsonPayload) {
		KnownErrorEntity lKnownErrorEntity = new KnownErrorEntity();
		lKnownErrorEntity.setDescription(jsonPayload.getKnownErrorDescription());
		lKnownErrorEntity.setTitle(jsonPayload.getKnownErrorTitle());
		lKnownErrorEntity.setWorkaround(jsonPayload.getWorkAround());
		lKnownErrorEntity.setProblemID(jsonPayload.getProblemID());
		lKnownErrorRepo.save(lKnownErrorEntity);
	}


	private void saveWordAround(ProblemDetailsEntity jsonPayload) {
		List<String> listOfProblemID = problemDetailsRepo.findAll().stream().map(ProblemDetailsEntity::getProblemID)
				.collect(Collectors.toList());
		if (listOfProblemID.contains(jsonPayload.getProblemID())) {

			if (StringUtil.isNotNullNotEmpty(jsonPayload.getWorkAround())) {
				List<ProblemIncidentEntity> listofincident = problemIncidentRepo
						.findByProblemID(jsonPayload.getProblemID());
				if (!(listofincident.isEmpty())) {
					if("true".equalsIgnoreCase(jsonPayload.getBroadcastWorkAround())) {
					workAroundBroadCast(listofincident, jsonPayload);
					}else {
						deleteWorkAroundBroadCast(listofincident, jsonPayload);
					}
				}
			}
		}
	}

	private void workAroundBroadCast(List<ProblemIncidentEntity> problemIncidentEntity,
			ProblemDetailsEntity problemDetailsEntity) {
		for (ProblemIncidentEntity list : problemIncidentEntity) {
			MappedVariableInstanceLogVO incidentID = mappedVariableInstanceRepo.findByIncidentID(list.getIncidentID());
			boolean check = true;
			if("Closed".equals(incidentID.getStatus()) || "Cancelled".equals(incidentID.getStatus())) {
				check = false;
			}
			if(check) {
				incidentID.setWorkAround(problemDetailsEntity.getWorkAround());
				mappedVariableInstanceRepo.save(incidentID);
			}
		}
	}
	
	
	private void deleteWorkAroundBroadCast(List<ProblemIncidentEntity> problemIncidentEntity,
			ProblemDetailsEntity problemDetailsEntity) {
		for (ProblemIncidentEntity list : problemIncidentEntity) {
			MappedVariableInstanceLogVO incidentID = mappedVariableInstanceRepo.findByIncidentID(list.getIncidentID());
			if (!(incidentID.getStatus().equals("Closed")) || !(incidentID.getStatus().equals("Cancelled"))) {
				incidentID.setWorkAround("");
				mappedVariableInstanceRepo.save(incidentID);
			}
		}
	}
}
