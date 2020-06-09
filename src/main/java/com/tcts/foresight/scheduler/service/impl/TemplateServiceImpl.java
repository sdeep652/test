package com.tcts.foresight.scheduler.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.TemplateEntity;
import com.tcts.foresight.repository.CategoryDetailsRepo;
import com.tcts.foresight.repository.GroupDetailsRepository;
import com.tcts.foresight.repository.PriorityDetailRepo;
import com.tcts.foresight.repository.ResolutionMethodRepo;
import com.tcts.foresight.repository.ResolutionTypeRepo;
import com.tcts.foresight.repository.SourceDetailsRepo;
import com.tcts.foresight.repository.SubCatDetailsRepo;
import com.tcts.foresight.repository.TemplateRepo;
import com.tcts.foresight.repository.UserRepo;
import com.tcts.foresight.scheduler.entity.SLAConfigurationActionsEntity;
import com.tcts.foresight.scheduler.entity.SLAConfigurationEntity;
import com.tcts.foresight.scheduler.repository.SLAConfigurationRepo;
import com.tcts.foresight.util.JSONUtil;
import com.tcts.foresight.util.StringUtil;

@Service
public class TemplateServiceImpl {

	Logger logger = LoggerFactory.getLogger(TemplateServiceImpl.class);

	@Autowired
	private CategoryDetailsRepo categoryDetailsRepo;

	@Autowired
	private PriorityDetailRepo priorityDetailRepo;

	@Autowired
	private SubCatDetailsRepo subCatDetailsRepo;

	@Autowired
	private SourceDetailsRepo sourceDetailsRepo;

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private GroupDetailsRepository groupDetailsRepository;

	@Autowired
	private ResolutionMethodRepo resolutionMethodRepo;

//	@Autowired
//	private ResolutionTypeRepo resolutionTypeRepo;

	@Autowired
	private TemplateRepo lTemplateRepo;

	@Autowired
	private SLAConfigurationRepo lSLAConfigurationRepo;

	public TemplateEntity saveTemplate(TemplateEntity lTemplateEntity) {
		TemplateEntity template = new TemplateEntity();
		try {
				template = lTemplateRepo.save(lTemplateEntity);
		} catch (Exception e) {
			logger.error("Exception Occurred in saveTamplate: " + e.getMessage(), e);
		}
		return template;
	}

	public List<TemplateEntity> findByModule(String module) {
		List<TemplateEntity> lTemplateEntity1 = new ArrayList<TemplateEntity>();
		List<TemplateEntity> finalTemplateEntity = new ArrayList<TemplateEntity>();
		try {
			lTemplateEntity1 = lTemplateRepo.findByModule(module);
			for (TemplateEntity template : lTemplateEntity1) {
				if (StringUtil.isNotNullNotEmpty(template.getPriorityId())) {
					template.setCategory(categoryDetailsRepo.findByCatId(Long.parseLong(template.getCategoryId())));
				}
				if (StringUtil.isNotNullNotEmpty(template.getSubCategoryId())) {
					template.setSubCategory(subCatDetailsRepo.findSubCatById(Long.parseLong(template.getSubCategoryId())));
				}
				if (StringUtil.isNotNullNotEmpty(template.getPriorityId())) {
					template.setPriority(priorityDetailRepo.findNameById(Long.parseLong(template.getPriorityId())));
				}
				if (StringUtil.isNotNullNotEmpty(template.getAssignToId())) {
					template.setAssignTo(userRepo.findUserEmailByID(Long.parseLong(template.getAssignToId())));
				}
				if (StringUtil.isNotNullNotEmpty(template.getAssignToId())) {
					template.setFullName(userRepo.findUserNameByID(Long.parseLong(template.getAssignToId())));
					}
				if (StringUtil.isNotNullNotEmpty(template.getResolutionMethodId())) {
					template.setResolutionMethod(resolutionMethodRepo.findNameById(Long.parseLong(template.getResolutionMethodId())));
				}
				finalTemplateEntity.add(template);
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in find all by module method: " + e.getMessage(), e);
		}
		return lTemplateEntity1;
	}
	public TemplateEntity updateTemplate(TemplateEntity lTemplateEntity) {
		TemplateEntity template = new TemplateEntity();
		try {
			if (StringUtil.isNotNullNotEmpty(template.getCategoryId())) {
				template.setCategory(categoryDetailsRepo.findByCatId(Long.parseLong(template.getCategoryId())));
			}
			if (StringUtil.isNotNullNotEmpty(template.getSubCategoryId())) {
				template.setSubCategory(subCatDetailsRepo.findSubCatById(Long.parseLong(template.getSubCategoryId())));
			}
			if (StringUtil.isNotNullNotEmpty(template.getPriorityId())) {
				template.setPriority(priorityDetailRepo.findNameById(Long.parseLong(template.getPriorityId())));
			}
			if (StringUtil.isNotNullNotEmpty(template.getAssignToId())) {
				template.setFullName(userRepo.findUserNameByID(Long.parseLong(template.getAssignToId())));
			}
			if (StringUtil.isNotNullNotEmpty(template.getAssignToId())) {
				template.setAssignTo(userRepo.findUserEmailByID(Long.parseLong(template.getAssignToId())));
			}
			if (StringUtil.isNotNullNotEmpty(template.getResolutionMethodId())) {
				template.setResolutionMethod(resolutionMethodRepo.findNameById(Long.parseLong(template.getResolutionMethodId())));
			}
			template = lTemplateRepo.save(lTemplateEntity);

		} catch (Exception e) {
			logger.error("Exception Occurred in updateTemplate: " + e.getMessage(), e);
		}
		return template;
	}
	public void deleteById(Long templateId) {
		lTemplateRepo.deleteById(templateId);
	}
	public List<TemplateEntity> findTemplateByGroup(String jsonPayLoadMap) {

		HashMap<String, String> filter = new HashMap<String, String>();
		List<TemplateEntity> list = new ArrayList<TemplateEntity>();
		List<TemplateEntity> finalList = new ArrayList<TemplateEntity>();
		List<String> groupList = new ArrayList<String>();
		try {
			if (jsonPayLoadMap != null && StringUtil.isNotNullNotEmpty(jsonPayLoadMap.trim())) {
				filter = JSONUtil.jsonpayloadMapToHashMap(jsonPayLoadMap);
				if (filter != null && filter.size() > 0) {
					groupList = Stream.of(filter.get("assignmentGroup").split(",", -1)).collect(Collectors.toList());
					list = lTemplateRepo.findAll();
					for (TemplateEntity template : list) {
						if (template.getActive().equalsIgnoreCase("true")) {
							
							if (StringUtil.isNotNullNotEmpty(template.getCategoryId())) {
								template.setCategory(categoryDetailsRepo.findByCatId(Long.parseLong(template.getCategoryId())));
							}
							if (StringUtil.isNotNullNotEmpty(template.getSubCategoryId())) {
								template.setSubCategory(subCatDetailsRepo.findSubCatById(Long.parseLong(template.getSubCategoryId())));
							}

							if (StringUtil.isNotNullNotEmpty(template.getPriorityId())) {
								template.setPriority(priorityDetailRepo.findNameById(Long.parseLong(template.getPriorityId())));
							}
							if (StringUtil.isNotNullNotEmpty(template.getAssignToId())) {
								template.setAssignTo(userRepo.findUserEmailByID(Long.parseLong(template.getAssignToId())));
							}
							
							if (StringUtil.isNotNullNotEmpty(template.getAssignToId())) {
								template.setFullName(userRepo.findUserNameByID(Long.parseLong(template.getAssignToId())));
							}
	
							if (StringUtil.isNotNullNotEmpty(template.getResolutionMethodId())) {
								template.setResolutionMethod(resolutionMethodRepo.findNameById(Long.parseLong(template.getResolutionMethodId())));
							}

							List<String> temGroupList = Stream.of(template.getPublishForGroups().split(",", -1)).collect(Collectors.toList());
							
							for (String s : temGroupList) {
								if (groupList.contains(s)) {
									finalList.add(template);
								}
							}
						}
					}

				}
			}
		} catch (Exception e) {
			logger.error("Exception Occurred in findTemplateByGroup: " + e.getMessage(), e);
		}
		return finalList.stream().distinct().collect(Collectors.toList());
	}

	public List<SLAConfigurationEntity> getSlaTemplate(String jsonpayloadMapToHashMap) {

		// TODO Auto-generated method stub
		int flag = 0;
		List<Long> finalIdList = new ArrayList<Long>();
		String slaCondition[] = { "category", "subCategory", "priority", "source" };
		HashMap<String, String> filter = new HashMap<String, String>();
		filter = JSONUtil.jsonpayloadMapToHashMap(jsonpayloadMapToHashMap);

		List<SLAConfigurationEntity> result = new ArrayList<SLAConfigurationEntity>();
		try {
			List<SLAConfigurationEntity> lSLAConfigurationEntity = lSLAConfigurationRepo.findByIsActive("true");
			// iterate SlaCondition Value

			for (String lslaCondition : slaCondition) {
				// iterate SLAConfigurationEntity
				for (SLAConfigurationEntity pSLAConfigurationEntity : lSLAConfigurationEntity) {
					// iterate SLAConfigurationActionsEntity
					for (SLAConfigurationActionsEntity lSLAConfigurationActionsEntity : pSLAConfigurationEntity
							.getSlaConfigActionsEList()) {

						// For AND CHECK
						if (flag == 1) {
							for (String lslaConditions : slaCondition) {
								if (lSLAConfigurationActionsEntity.getConditionOn().equalsIgnoreCase(lslaConditions)) {

									List<String> fixedLenghtList1 = Stream
											.of(lSLAConfigurationActionsEntity.getValue().split(",", -1))
											.collect(Collectors.toList());

									List<String> fixedLenghtList2 = Stream.of(filter.get(lslaConditions).split(",", -1))
											.collect(Collectors.toList());
									if (fixedLenghtList1.containsAll(fixedLenghtList2)) {

									} else {
										finalIdList.remove(pSLAConfigurationEntity.getId());
									}
								}
							}
						}

						if (lSLAConfigurationActionsEntity.getConditionOn().equalsIgnoreCase(lslaCondition)) {

							List<String> fixedLenghtList1 = Stream
									.of(lSLAConfigurationActionsEntity.getValue().split(",", -1))
									.collect(Collectors.toList());

							List<String> fixedLenghtList2 = Stream.of(filter.get(lslaCondition).split(",", -1))
									.collect(Collectors.toList());

							if (fixedLenghtList1.containsAll(fixedLenghtList2)) {
								finalIdList.add(pSLAConfigurationEntity.getId());
							}
						} else {
							break;
						}

						flag = 1;

					}
					flag = 0;
				}
			}

			result = lSLAConfigurationRepo.fetchAttachedSla(finalIdList);
		} catch (Exception e) {
			logger.error("Exception Occurred in getSlaTemplate: " + e.getMessage(), e);
		}
		return result;
	}

}
