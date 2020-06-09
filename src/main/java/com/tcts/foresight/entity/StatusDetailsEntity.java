package com.tcts.foresight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "status_details")
public class StatusDetailsEntity {

		

            @Id
	        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_seq_generator")
	        @SequenceGenerator(name="status_seq_generator", sequenceName = "status_seq", allocationSize = 1)
	 		@Column(name = "id")
			private Long id;
			
			@Column(name = "status")
			private String status;

			public Long getId() {
				return id;
			}

			public void setId(Long id) {
				this.id = id;
			}

			public String getStatus() {
				return status;
			}

			public void setStatus(String status) {
				this.status = status;
			}

			@Override
			public String toString() {
				return "StatusDetailsEntity [id=" + id + ", status=" + status + "]";
			}
		
	
	
	
}
