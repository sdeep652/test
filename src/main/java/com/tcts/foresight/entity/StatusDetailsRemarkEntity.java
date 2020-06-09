package com.tcts.foresight.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@Entity
@Table(name = "status_remark")
public class StatusDetailsRemarkEntity extends AuthMessageEntity{
	
	

        @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_remark_seq_generator")
	    @SequenceGenerator(name="status_remark_seq_generator", sequenceName = "status_remark_seq", allocationSize = 1)
		@Column(name = "id")
		private Long id;
		
		@Column(name = "status")
		private String status;
		
		@Column(name = "remark")
		private String remark;
		
		@Column(name = "module")
		private String module;

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

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getModule() {
			return module;
		}

		public void setModule(String module) {
			this.module = module;
		}

		@Override
		public String toString() {
			return "StausDetailsEntity [id=" + id + ", status=" + status + ", remark=" + remark + ", module=" + module
					+ "]";
		}
		
	
	
	
	
	
}
