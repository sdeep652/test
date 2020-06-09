package com.tcts.foresight.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.AutoClosureDetailsEntity;
import com.tcts.foresight.entity.CategoryDetailsEntity;
import com.tcts.foresight.entity.PriorityDetailsEntity;
import com.tcts.foresight.entity.SourceDetailsEntity;
import com.tcts.foresight.entity.SubCatDetailsEntity;
import com.tcts.foresight.repository.AutoClosureRepo;
import com.tcts.foresight.repository.CategoryDetailsRepo;
import com.tcts.foresight.repository.PriorityDetailRepo;
import com.tcts.foresight.repository.SourceDetailsRepo;
import com.tcts.foresight.repository.SubCatDetailsRepo;
import com.tcts.foresight.repository.SubCatSummary;
import com.tcts.foresight.scheduler.entity.SLAConfigurationEntity;
import com.tcts.foresight.scheduler.repository.SLAConfigurationRepo;
import com.tcts.foresight.service.CategoryService;
import com.tcts.foresight.service.ImpactService;

@Service
@Transactional
public class CategorySeviceImpl implements CategoryService {
	Logger logger = LoggerFactory.getLogger(CategorySeviceImpl.class);

	@Autowired
	private CategoryDetailsRepo categoryDetailsRepo;

	@Autowired
	private SubCatDetailsRepo subCatDetailsRepo;

	@Autowired
	PriorityDetailRepo priorityDetailRepo;
	@Autowired
	SourceDetailsRepo sourceDetailsRepo;

	@Autowired
	ImpactService impactService;
	
	@Autowired
	SLAConfigurationRepo sLAConfigurationRepo;
	
	@Autowired
	AutoClosureRepo autoClosureRepo;

	@Override
	public List<CategoryDetailsEntity> getcategoryList(String module) {
		return categoryDetailsRepo.findAll().stream().filter(name -> name.getModule().equalsIgnoreCase(module))
				.collect(Collectors.toList());
	}

	@Override
	public CategoryDetailsEntity addCategory(CategoryDetailsEntity categoryDetails) {
		return categoryDetailsRepo.save(categoryDetails);
	}

	@Override
	public CategoryDetailsEntity updateCategory(Long id, CategoryDetailsEntity categoryDetails) {
		CategoryDetailsEntity categoryDt = categoryDetailsRepo.findById(id).get();
		//logger.info("my data in categoryDD=" + categoryDt);
		if (categoryDt != null) {
			categoryDetails.setId(id);
			categoryDetailsRepo.save(categoryDetails);

			return categoryDetails;
		}
		return categoryDetails;
	}

	@Override
	public void deleteCategory(Long id) {
		// Delete relation from Auto closure
		autoClosureRepo.deletebyCatId(id);	
		categoryDetailsRepo.deleteById(id);
	}
	
	@Override
	public HashMap<String, Object> checkAssociationsCategory(Long id) {
		// Checking relation from SLA, Autoclosure, Subcategory
		HashMap<String, String> responseMap = new HashMap<String, String>();
		HashMap<String, Object> associationMap = new HashMap<String, Object>();
		ArrayList<HashMap<String, String>> rmap = new ArrayList<HashMap<String, String>>();

		try {
			List<AutoClosureDetailsEntity> autoClosureDetailsEntity = autoClosureRepo.findbyCatId(id);
			List<SubCatSummary> subCategory = getSubCatByCatId(id, "IM");
			List<String> slaName = sLAConfigurationRepo.findBySLAContionOn("category", categoryDetailsRepo.findByCatId(id))
					.stream().map(SLAConfigurationEntity::getSlaName).collect(Collectors.toList());
			
			if (subCategory.size() > 0) {

				for (SubCatSummary subCat : subCategory) {

					if (responseMap.containsKey("subCategory")) {
						String currentValue = responseMap.get("subCategory");
						currentValue = currentValue + ", " + subCat.getName();
						responseMap.put("subCategory", currentValue);
					} else {
						responseMap.put("subCategory", subCat.getName());
					}
				}
			} else {
				responseMap.put("subCategory", "");
			}

			if (autoClosureDetailsEntity.size() > 0) {
				for (AutoClosureDetailsEntity auto : autoClosureDetailsEntity) {
					if (responseMap.containsKey("autoClosureDetails")) {
						String currentValue = responseMap.get("AutoClosureDetails");
						currentValue = currentValue + ", " + auto.getId().toString();
						responseMap.put("AutoClosureDetails", currentValue);
					} else {
						responseMap.put("AutoClosureDetails", auto.getId().toString());
					}
				}
			} else {
				responseMap.put("AutoClosureDetails", "");
			}

			if (slaName.size() > 0) {
				for (String sla : slaName) {
					if (responseMap.containsKey("slaNameDetails")) {
						String currentValue = responseMap.get("slaNameDetails");
						currentValue = currentValue + ", " + sla;
						responseMap.put("slaNameDetails", currentValue);
					} else {
						responseMap.put("slaNameDetails", sla);
					}
				}
			} else {
				responseMap.put("slaNameDetails", "");
			}
			rmap.add(responseMap);
			associationMap.put("Association", rmap);
		} catch (Exception e) {
			
			logger.error("Exception occured in checkAssociationsCategory: " + e.getMessage(), e);  
		}
		return associationMap;

	}

	@Override
	public Map<String, String> checkDuplicate(String categoryName, String module) {
		Map<String, String> categoryMap = new HashMap<>();
		String catName = categoryDetailsRepo.findNameByModule(categoryName, module);
		categoryMap.put("name", catName);
		return categoryMap;
	}

	@Override
	public List<SubCatDetailsEntity> checkAddSubCategory(@Valid SubCatDetailsEntity subCat) {
		CategoryDetailsEntity lCategoryDetailsEntity = subCat.getCategoryList().get(0);

		if (lCategoryDetailsEntity != null && lCategoryDetailsEntity.getModule() != null
				&& lCategoryDetailsEntity.getName() != null) {
			return subCatDetailsRepo.queryBy(subCat.getName(), subCat.getCategoryList().get(0).getName(),
					subCat.getCategoryList().get(0).getModule());
		} else {
			return null;
		}

	}

	@Override
	public List<SubCatDetailsEntity> fetchAllSubCategory(String module) {

		List<SubCatDetailsEntity> lList = subCatDetailsRepo.fetchAllSubCategories(module);
		List<SubCatDetailsEntity> lList2 = new ArrayList<SubCatDetailsEntity>();
		HashMap<Long, String> map = new HashMap<>();
		for (SubCatDetailsEntity lSubCatDetailsEntity : lList) {
			if (map.containsKey(lSubCatDetailsEntity.getId())) {

			} else {
				lList2.add(lSubCatDetailsEntity);
				map.put(lSubCatDetailsEntity.getId(), "ID");
			}
		}
		return lList2;
	}

	@Override
	public List<SubCatDetailsEntity> multipleCatSubcatfilterList(String module, Long id) {

		List<SubCatDetailsEntity> lList = subCatDetailsRepo.fetchAllMulSubCategories(module, id);

		List<SubCatDetailsEntity> lList2 = new ArrayList<SubCatDetailsEntity>();

		HashMap<Long, String> map = new HashMap<>();
		for (SubCatDetailsEntity lSubCatDetailsEntity : lList) {
			if (map.containsKey(lSubCatDetailsEntity.getId())) {

			} else {
				lList2.add(lSubCatDetailsEntity);
				map.put(lSubCatDetailsEntity.getId(), "ID");
			}

		}
		return lList2;
	}

	@Override
	public SubCatDetailsEntity addSubCategory(@Valid SubCatDetailsEntity subCat) {
		return subCatDetailsRepo.save(subCat);

	}

	@Override
	public List<SubCatDetailsEntity> getsubcategoryList(String module) {
		List<SubCatDetailsEntity> subCatDetailsEnt = subCatDetailsRepo.findDistinctBycategoryList_Module(module);
		return subCatDetailsEnt;

	}

	@Override
	public void deleteSubCategory(Long id) {
		subCatDetailsRepo.deleteById(id);

	}

	@Override
	public SubCatDetailsEntity updateSubCategory(Long id, SubCatDetailsEntity subCategoryDetails) {
		SubCatDetailsEntity categoryDt = subCatDetailsRepo.findById(id).get();
		//logger.info("my data in SubCategoryDD=" + categoryDt);
		if (categoryDt != null) {
			subCategoryDetails.setId(id);
			categoryDetailsRepo.save(subCategoryDetails);
			return subCategoryDetails;
		}
		return subCategoryDetails;
	}

	@Override
	public List<CategoryDetailsEntity> getSubcategoryList(String name) {
		return null;
	}

	@Override
	public List<SubCatDetailsEntity> getAllsubCatList() {
		List<SubCatDetailsEntity> subCatDetailsEnt = subCatDetailsRepo.findAll();
		return subCatDetailsEnt;
	}

	@Override
	public String checkDupSubCategory(String subCatName, List<Long> categoryIdList, String module) {
		String dupCatName = null;
		List<SubCatDetailsEntity> subCatDtls = null;
		try {
//			subCatDtls = subCatDetailsRepo.findByName(subCatName);
//			if (subCatDtls != null) {
//				for (CategoryDetailsEntity catDetailsEntity : subCatDtls.getCategoryList()) {
//					if (categoryIdList.contains(catDetailsEntity.getId())
//							&& catDetailsEntity.getModule().equalsIgnoreCase(module)) {
//						return subCatName;
//					} else if (subCatDtls.getName().equalsIgnoreCase(subCatName)
//							&& catDetailsEntity.getModule().equals(module)) {
//						return subCatName;
//					}
//				}
//			}
			
			subCatDtls = subCatDetailsRepo.findByNameAndCategoryList_Module(subCatName,module);
			if(subCatDtls.size()>0) {
				return subCatName;
			}

		} catch (Exception e) {
			logger.error("Exception occur while in checkDupSubCategory " + e.getMessage(),e);
		}
		return dupCatName;
	}

	@Override
	public SubCatDetailsEntity getByName(String subCatName, List<Long> categoryIdList) {
		return subCatDetailsRepo.getByName(subCatName, categoryIdList);
	}

	@Override
	public List<SubCatSummary> getSubCatByCatId(Long categoryId, String module) {
		return subCatDetailsRepo.getSubCatByCatId(categoryId, module);
	}

	@Override
	public List<HashMap<String, String>> fetchCatId(String catName, String module) {
		String name = catName;
		List<HashMap<String, String>> returnList1 = new ArrayList<HashMap<String, String>>();
		switch (name) {
		case "category":
			List<CategoryDetailsEntity> catlist1 = getcategoryList(module);
			for (CategoryDetailsEntity l : catlist1) {
				if (l != null && l.getModule().equalsIgnoreCase(module)) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("id", String.valueOf(l.getId()));
					map.put("name", l.getName());
					returnList1.add(map);
				}
			}
			break;

		case "subcategory":
			List<SubCatDetailsEntity> subCatDetailsEntityList = new ArrayList<>();
			subCatDetailsEntityList = subCatDetailsRepo.findDistinctBycategoryList_Module(module);
			for (SubCatDetailsEntity l : subCatDetailsEntityList) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("id", String.valueOf(l.getId()));
				map.put("name", l.getName());
				returnList1.add(map);
			}
			break;

		case "priority":
			List<PriorityDetailsEntity> priorityDetailsEntityList = new ArrayList<>();
			priorityDetailsEntityList = impactService.getAllDistinctName();
			for (PriorityDetailsEntity l : priorityDetailsEntityList) {
				if (l != null && l.getModule().equalsIgnoreCase("IM")) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("id", String.valueOf(l.getId()));
					map.put("name", l.getName());
					returnList1.add(map);
				}
			}
			break;

		case "source":
			List<SourceDetailsEntity> sourceDetailsEntityList = new ArrayList<>();
			sourceDetailsEntityList = sourceDetailsRepo.findAll();
			for (SourceDetailsEntity l : sourceDetailsEntityList) {
				if (l != null && l.getModule().equalsIgnoreCase(module)) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("id", String.valueOf(l.getId()));
					map.put("name", l.getName());
					returnList1.add(map);
				}
			}
			break;

		default:
			break;
		}
		return returnList1;
	}
}
