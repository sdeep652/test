package com.tcts.foresight.scheduler.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcts.foresight.entity.ResolutionMethodEntity;
import com.tcts.foresight.scheduler.entity.SLAWorkFlowEntity;

@Repository
public interface SLAWorkFlowRepo extends JpaRepository<SLAWorkFlowEntity, Long> {

	public List<SLAWorkFlowEntity> findByModule(String module);

	@Query("SELECT slaWorkfolw.workFlowName FROM SLAWorkFlowEntity slaWorkfolw where (UPPER(slaWorkfolw.workFlowName) = UPPER(:workFlowName))")
	String findBySlaName(@Param("workFlowName") String workFlowName);

	@Query(value = "select * from resolution_method_details rmd where (UPPER(rmd.name) = UPPER(:name) ) and (rmd.module is not null or rmd.module =:module)", nativeQuery = true)
	ResolutionMethodEntity findNameByModule(@Param("name") String name, @Param("module") String module);

	public SLAWorkFlowEntity findByWorkflowId(Long workflowId);

	public SLAWorkFlowEntity findByWorkflowIdAndWorkflowTarget(Long workflowId, String slaTargetResolutionsla);

	@Modifying
	@org.springframework.transaction.annotation.Transactional
	@Query(value = "DELETE from sla_config_workflow where workflow_id IS NULL", nativeQuery = true)
	void deleteNullWorkflow();

}
