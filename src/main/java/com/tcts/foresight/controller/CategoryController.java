package com.tcts.foresight.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcts.foresight.entity.CategoryDetailsEntity;
import com.tcts.foresight.entity.SubCatDetailsEntity;
import com.tcts.foresight.repository.CategoryDetailsRepo;
import com.tcts.foresight.repository.SubCatSummary;
import com.tcts.foresight.service.CategoryService;
import com.tcts.foresight.util.AuthUtil;
import com.tcts.foresight.util.Constant;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/category")
public class CategoryController {
	private static final Logger logger = LogManager.getLogger(CategoryController.class);

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ObjectMapper objMapper;

	@Autowired
	private AuthUtil authUtil;

	@Autowired
	CategoryDetailsRepo categoryDetailsRepo;

	@GetMapping("/AllCategoryList/{module}")
	public List<CategoryDetailsEntity> getcategoryList(@PathVariable String module) {
		return categoryService.getcategoryList(module);
	}

	@PostMapping("/addCategory")
	public @Valid CategoryDetailsEntity addCategory(@RequestHeader("Authorization") String authToken,
			@Valid @RequestBody CategoryDetailsEntity user) {
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Adminconfig_category) ) {
				logger.info("Create User Wrapper Object:- " + objMapper.writeValueAsString(user));
				categoryService.addCategory(user);
			} else {
				CategoryDetailsEntity cd = new CategoryDetailsEntity();
				cd.setMessage(Constant.User_Does_Not_Have_Authorization);
				return cd;
			}
		} catch (JsonProcessingException e) {
			logger.error("Exception occur while in add category" + e.getMessage(),e);
		}
		return user;
	}

	@PutMapping(value = "/update/{id}")
	public CategoryDetailsEntity updateCategory(@RequestHeader("Authorization") String authToken, @PathVariable Long id,
			@Valid @RequestBody CategoryDetailsEntity categoryDetails) {
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Adminconfig_category)) {
				categoryDetails = categoryService.updateCategory(id, categoryDetails);
			} else {
				CategoryDetailsEntity cd = new CategoryDetailsEntity();
				cd.setMessage(Constant.User_Does_Not_Have_Authorization);
				return cd;
			}

		} catch (Exception e) {
			logger.error("Exception occur while updating in Category details " + e.getMessage(),e);
		}
		return categoryDetails;
	}

	@CrossOrigin
	@DeleteMapping(value = "/Delete/{id}")
	public String deleteCategory(@RequestHeader("Authorization") String authToken, @PathVariable Long id) {
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Adminconfig_category)) {
				categoryService.deleteCategory(id);
			} else {
				return Constant.User_Does_Not_Have_Authorization;
			}
		} catch (Exception e) {
			logger.error("Exception occur while in JsonProcessingException " + e.getMessage(),e);
		}
		return "Category Deleted Successfully";

	}
	
	@CrossOrigin
	@GetMapping(value = "/checkCategoryAssociations/{id}")
	public HashMap<String,Object > checkAssociationsCategory(@RequestHeader("Authorization") String authToken, @PathVariable Long id) {
		HashMap<String,Object> rmap = new HashMap<String, Object>();
		
		try {
				return categoryService.checkAssociationsCategory(id);
			
		} catch (Exception e) {
			logger.error("Exception occur while in JsonProcessingException " + e.getMessage());
		}
		return rmap;
	}
		
	@GetMapping("/duplicate/{categoryName}/{module}")
	public @Valid Map<String, String> checkDuplicate(@PathVariable String categoryName, @PathVariable String module) {
		return categoryService.checkDuplicate(categoryName, module);
	}

	@GetMapping("/fetchSubCatList/{module}")
	public List<SubCatDetailsEntity> getsubcategoryList(@PathVariable("module") String module) {
		return categoryService.getsubcategoryList(module);
	}

	@GetMapping("/fetchAllSubCatList")
	public List<SubCatDetailsEntity> getAllsubCatList() {
		return categoryService.getAllsubCatList();
	}

	@PostMapping("/checkAddSubCategory")
	public @Valid List<SubCatDetailsEntity> checkAddSubCategory(@Valid @RequestBody SubCatDetailsEntity subCat) {

		List<SubCatDetailsEntity> lCatSubcatMapping = null;

		if (subCat != null && subCat.getCategoryList() != null && subCat.getCategoryList().get(0) != null
				&& subCat.getCategoryList().get(0).getModule() != null
				&& subCat.getCategoryList().get(0).getName() != null) {
			try {
				logger.info("Create User Wrapper Object:- " + objMapper.writeValueAsString(subCat));
				lCatSubcatMapping = categoryService.checkAddSubCategory(subCat);
			} catch (JsonProcessingException e) {
				logger.error("Exception occur while check add Subcategory" + e.getMessage(),e);
			}
		}
		return lCatSubcatMapping;
	}

	@GetMapping("/checkDupSubCategory/{subCatName}/{categoryIdList}/{module}")
	public String checkDupSubCategory(@PathVariable String subCatName, @PathVariable List<Long> categoryIdList,
			@PathVariable String module) {
		return categoryService.checkDupSubCategory(subCatName, categoryIdList, module);
	}

	@GetMapping("/getByName/{subCatName}/{categoryIdList}")
	public SubCatDetailsEntity getByName(@PathVariable String subCatName, @PathVariable List<Long> categoryIdList) {
		return categoryService.getByName(subCatName, categoryIdList);
	}

	@GetMapping("/getSubCatByCatId/{categoryId}/{module}")
	public List<SubCatSummary> getSubCatByCatId(@PathVariable Long categoryId, @PathVariable String module) {
		return categoryService.getSubCatByCatId(categoryId, module);
	}

	@GetMapping("/fetchAllSubCategory/{module}")
	public @Valid List<SubCatDetailsEntity> fetchAllSubCategory(@PathVariable String module) {
		List<SubCatDetailsEntity> lCatSubcatMapping = null;

		if (module != null) {
			try {
				lCatSubcatMapping = categoryService.fetchAllSubCategory(module);
			} catch (Exception e) {
				logger.error("Exception occur fech All Subcategory" + e.getMessage(),e);
			}
		}
		return lCatSubcatMapping;
	}

	@PostMapping("/multipleCatSubcatfilterList/{module}")
	public @Valid List<SubCatDetailsEntity> multipleCatSubcatfilterList(@PathVariable String module,
			@RequestBody List<SubCatDetailsEntity> subCat) {
		List<SubCatDetailsEntity> lCatSubcatMapping = new ArrayList<>();
		List<SubCatDetailsEntity> categoryFinalList = new ArrayList<SubCatDetailsEntity>();
		HashMap<String, SubCatDetailsEntity> uniqueCategoryNameMap = new HashMap<String, SubCatDetailsEntity>();
		List<SubCatDetailsEntity> lCatSubcatMappingNew = new ArrayList<SubCatDetailsEntity>();
		if (module != null) {
			try {
				for (SubCatDetailsEntity se : subCat) {
					lCatSubcatMapping = categoryService.multipleCatSubcatfilterList(module, se.getId());
					lCatSubcatMappingNew.addAll(lCatSubcatMapping);
				}

				for (SubCatDetailsEntity lSubCatDetailsEntity : lCatSubcatMappingNew) {
					if (uniqueCategoryNameMap.containsKey(lSubCatDetailsEntity.getName())) {

					} else {
						//logger.info("Adding subcategory :" + lSubCatDetailsEntity.getName());
						uniqueCategoryNameMap.put(lSubCatDetailsEntity.getName(), lSubCatDetailsEntity);
					}
				}

				for (String key : uniqueCategoryNameMap.keySet()) {
					categoryFinalList.add((SubCatDetailsEntity) uniqueCategoryNameMap.get(key));
				}

			} catch (Exception e) {
				logger.error("Exception occur while in multipleCatSubcatfilterList" + e.getMessage(),e);

				// .printStackTrace();
			}
		}
		return categoryFinalList;
	}

	@PostMapping("/addSubCategory")
	public @Valid SubCatDetailsEntity addSubCategory(@RequestHeader("Authorization") String authToken,
			@Valid @RequestBody SubCatDetailsEntity subCat) {
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Adminconfig_subcategory)) {
				logger.info("Create User Wrapper Object:- " + objMapper.writeValueAsString(subCat));
				categoryService.addSubCategory(subCat);
			} else {
				SubCatDetailsEntity scd = new SubCatDetailsEntity();
				scd.setMessage(Constant.User_Does_Not_Have_Authorization);
				return scd;
			}
		} catch (JsonProcessingException e) {
			logger.error("Exception occur while in addSubCategory" + e.getMessage(),e);
		}
		return subCat;
	}

	@CrossOrigin
	@DeleteMapping(value = "/deleteSubCategory/{id}")
	public String deleteSubCategory(@RequestHeader("Authorization") String authToken, @PathVariable Long id) {
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Adminconfig_subcategory)) {
				categoryService.deleteSubCategory(id);
			} else {
				return Constant.User_Does_Not_Have_Authorization;
			}
		} catch (Exception e) {
			logger.error("Exception occur while in JsonProcessingException " + e.getMessage(),e);
		}
		return "SubCategory Deleted Successfully";
	}

	@PutMapping(value = "/updateSubCat/{id}")
	public SubCatDetailsEntity updateSubCategory(@RequestHeader("Authorization") String authToken,
			@PathVariable Long id, @Valid @RequestBody SubCatDetailsEntity subcategoryDetails) {
		try {
			if (authUtil.authenticateAndAuthorizeCheck(authToken, Constant.IM_Adminconfig_subcategory)) {
				subcategoryDetails = categoryService.updateSubCategory(id, subcategoryDetails);
			} else {
				SubCatDetailsEntity cd = new SubCatDetailsEntity();
				cd.setMessage(Constant.User_Does_Not_Have_Authorization);
				return cd;
			}

		} catch (Exception e) {
			logger.error("Exception occur while updating in Category details " + e.getMessage(),e);
		}
		return subcategoryDetails;
	}

	
	@GetMapping("/fetchSlaByCondition/{catName}/{module}")
	public List<HashMap<String, String>> fetchSlaByCondition(@PathVariable String catName,@PathVariable String module) {
		return categoryService.fetchCatId(catName,module);
	}
	

}