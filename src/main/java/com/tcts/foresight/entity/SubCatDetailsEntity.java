package com.tcts.foresight.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "subcategory_details")
public class SubCatDetailsEntity extends AuthMessageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subcategory_seq_generator")
	@SequenceGenerator(name="subcategory_seq_generator", sequenceName = "subcategory_seq", allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@OneToMany
	@JoinTable(name = "category_subcategory",
	joinColumns = {@JoinColumn(name = "subcategory_id" , unique = false)},
	inverseJoinColumns = {@JoinColumn(name = "category_id" , unique = false) })
	public List<CategoryDetailsEntity> categoryList;

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

	public List<CategoryDetailsEntity> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<CategoryDetailsEntity> categoryList) {
		this.categoryList = categoryList;
	}

	@Override
	public String toString() {
		return "SubCatDetailsEntity [id=" + id + ", name=" + name + ", categoryList=" + categoryList + "]";
	}
}
