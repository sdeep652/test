package com.tcts.foresight.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "priority_details")
public class PriorityDetailsEntity extends AuthMessageEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "priority_seq_generator")
	@SequenceGenerator(name="priority_seq_generator", sequenceName = "priority_seq", allocationSize = 1)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "module")
	private String module;
	
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumn(name = "impact_id")
	private ImpactDetailsEntity impactId;
	
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "urgency_id")
	private UrgencyDetailsEntity urgencyId;

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ImpactDetailsEntity getImpactId() {
		return impactId;
	}

	public void setImpactId(ImpactDetailsEntity impactId) {
		this.impactId = impactId;
	}

	public UrgencyDetailsEntity getUrgencyId() {
		return urgencyId;
	}

	public void setUrgencyId(UrgencyDetailsEntity urgencyId) {
		this.urgencyId = urgencyId;
	}

	@Override
	public String toString() {
		return "PriorityDetailsEntity [id=" + id + ", name=" + name + "]";
	}	
}
