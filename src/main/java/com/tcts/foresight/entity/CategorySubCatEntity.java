package com.tcts.foresight.entity;

import javax.persistence.Column;
import javax.persistence.Id;

public class CategorySubCatEntity {
	
	@Id
	@Column(name = "subcategory_id")
	private Long subcatid;
	
	@Column(name = "category_id")
	private Long catId;

	public Long getSubcatid() {
		return subcatid;
	}

	public void setSubcatid(Long subcatid) {
		this.subcatid = subcatid;
	}

	public Long getCatId() {
		return catId;
	}

	public void setCatId(Long catId) {
		this.catId = catId;
	}

	@Override
	public String toString() {
		return "CategorySubCatEntity [subcatid=" + subcatid + ", catId=" + catId + "]";
	}
	

}
