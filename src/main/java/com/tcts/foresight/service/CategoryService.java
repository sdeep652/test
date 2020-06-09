package com.tcts.foresight.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.tcts.foresight.entity.CategoryDetailsEntity;
import com.tcts.foresight.entity.SubCatDetailsEntity;
import com.tcts.foresight.repository.SubCatSummary;

public interface CategoryService {

	public List<CategoryDetailsEntity> getcategoryList(String module);

	public CategoryDetailsEntity addCategory(CategoryDetailsEntity user);

	public void deleteCategory(Long id);

	public @Valid CategoryDetailsEntity updateCategory(Long id, @Valid CategoryDetailsEntity categoryDetails);

	public Map<String, String> checkDuplicate(String categoryName, String module);

	public List<CategoryDetailsEntity> getSubcategoryList(String name);

	public SubCatDetailsEntity addSubCategory(@Valid SubCatDetailsEntity subCat);

	public List<SubCatDetailsEntity> checkAddSubCategory(@Valid SubCatDetailsEntity subCat);

	public List<SubCatDetailsEntity> fetchAllSubCategory(String module);

	public List<SubCatDetailsEntity> multipleCatSubcatfilterList(String module, Long id);

	public void deleteSubCategory(Long id);

	public @Valid SubCatDetailsEntity updateSubCategory(Long id, SubCatDetailsEntity subCategoryDetails);

	public List<SubCatDetailsEntity> getsubcategoryList(String module);

	public List<SubCatDetailsEntity> getAllsubCatList();

	public String checkDupSubCategory(String subCatName, List<Long> categoryIdList, String module);

	public SubCatDetailsEntity getByName(String subCatName, List<Long> categoryIdList);

	public List<SubCatSummary> getSubCatByCatId(Long categoryId, String module);

	// Testing Purpose
	public List<HashMap<String, String>> fetchCatId(String catName, String module);

	public HashMap<String, Object> checkAssociationsCategory(Long id);

}
