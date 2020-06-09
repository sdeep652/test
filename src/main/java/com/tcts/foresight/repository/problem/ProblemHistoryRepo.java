package com.tcts.foresight.repository.problem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcts.foresight.entity.IncidentHistoryEntity;
import com.tcts.foresight.entity.problem.ProblemDetailsEntity;
import com.tcts.foresight.entity.problem.ProblemHistoryEntity;

@Repository
public interface ProblemHistoryRepo extends JpaRepository<ProblemHistoryEntity, Long> {

	@Query(nativeQuery = true, value = "SELECT * FROM problem_history where ticket_id =:ticket_id order by updated_date DESC")
	List<ProblemHistoryEntity> findByticketID(@Param("ticket_id") String problemID);

	@Query(value="select * from problem_history where ticket_id=:problemID and field_name='status'",nativeQuery = true )
	List<ProblemHistoryEntity> findAllStatusById(@Param("problemID")String problemID);


}
