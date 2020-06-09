package com.tcts.foresight.scheduler.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "workflow")
public class SLAWorkFlowEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "workflow_id")
	private Long workflowId;

	@Column(name = "workflow_name")
	private String workFlowName;

	@Column(name = "module")
	private String module;

	@Column(name = "workflow_target")
	private String workflowTarget;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "workflow_id", referencedColumnName = "workflow_id")
	private List<SLAConfigWorkFlow> sLAConfigWorkFlow;

	public Long getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Long workflowId) {
		this.workflowId = workflowId;
	}

	public String getWorkFlowName() {
		return workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getWorkflowTarget() {
		return workflowTarget;
	}

	public void setWorkflowTarget(String workflowTarget) {
		this.workflowTarget = workflowTarget;
	}

	public List<SLAConfigWorkFlow> getsLAConfigWorkFlow() {
		return sLAConfigWorkFlow;
	}

	public void setsLAConfigWorkFlow(List<SLAConfigWorkFlow> sLAConfigWorkFlow) {
		this.sLAConfigWorkFlow = sLAConfigWorkFlow;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

}
