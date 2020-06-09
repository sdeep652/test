package com.tcts.foresight.repository.problem;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tcts.foresight.entity.problem.ProblemIncidentEntity;

public interface ProblemIncidentRepo extends JpaRepository<ProblemIncidentEntity, Long> {

	List<ProblemIncidentEntity> findByProblemID(String ticketId);
	
	List<ProblemIncidentEntity> findByProblemIDAndRelationType(String ticketId,String relationType );
	
	ProblemIncidentEntity findByIncidentID(String incidentId);

	ProblemIncidentEntity findByIncidentIDAndProblemID(String incidentID, String problemID);
}
