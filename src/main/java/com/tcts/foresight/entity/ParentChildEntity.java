//package com.tcts.foresight.entity;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.SequenceGenerator;
//import javax.persistence.Table;
//
//@Entity
//@Table(name = "parentchild_mapping")
//public class ParentChildEntity {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parentchild_seq_generator")
//	@SequenceGenerator(name="parentchild_seq_generator", sequenceName = "parentchild_seq", allocationSize = 1)
//	@Column(name="id")
//	int id;
//	@Column(name="selected_ticketid")
//	String selectedTicketId;
//	@Column(name="parent_ticketid")
//	String parentTicketId;
//	@Column(name="child_ticketid")
//	String childTicketId;
//	
//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}
//	
//	public String getSelectedTicketId() {
//		return selectedTicketId;
//	}
//	public void setSelectedTicketId(String selectedTicketId) {
//		this.selectedTicketId = selectedTicketId;
//	}
//	public String getChildTicketId() {
//		return childTicketId;
//	}
//	public void setChildTicketId(String childTicketId) {
//		this.childTicketId = childTicketId;
//	}
//	public String getParentTicketId() {
//		return parentTicketId;
//	}
//	public void setParentTicketId(String parentTicketId) {
//		this.parentTicketId = parentTicketId;
//	}
//	@Override
//	public String toString() {
//		return "ParentChildEntity [id=" + id + ", selectedTicketId=" + selectedTicketId + ", parentTicketId="
//				+ parentTicketId + ", childTicketId=" + childTicketId + "]";
//	}
//
//
//	
//	
//}
