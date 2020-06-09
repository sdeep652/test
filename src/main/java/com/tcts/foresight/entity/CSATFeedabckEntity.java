package com.tcts.foresight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "c_sat_feedback")
public class CSATFeedabckEntity {

	@Id
	@Column(name = "ticket_id")
	private String ticketid;

	@Column(name = "form_sub_date")
	private String formSubDate;

	@Column(name = "rating")
	private Long rating;
	
	@Column(name = "ticket_created_date")
	private String ticketCreatedDate;

	@Column(name = "reason")
	private String reason;

	public String getTicketid() {
		return ticketid;
	}

	public void setTicketid(String ticketid) {
		this.ticketid = ticketid;
	}

	public String getFormSubDate() {
		return formSubDate;
	}

	public void setFormSubDate(String formSubDate) {
		this.formSubDate = formSubDate;
	}

	public Long getRating() {
		return rating;
	}

	public void setRating(Long rating) {
		this.rating = rating;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getTicketCreatedDate() {
		return ticketCreatedDate;
	}

	public void setTicketCreatedDate(String ticketCreatedDate) {
		this.ticketCreatedDate = ticketCreatedDate;
	}

	@Override
	public String toString() {
		return "CSATFeedabckEntity [ticketid=" + ticketid + ", formSubDate=" + formSubDate + ", rating=" + rating
				+ ", ticketCreatedDate=" + ticketCreatedDate + ", reason=" + reason + "]";
	}

	
	
	

}
