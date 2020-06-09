package com.tcts.foresight.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.CSATFeedabckEntity;
import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.entity.PriorityDetailsEntity;
import com.tcts.foresight.repository.CSATFeedbackRepo;
import com.tcts.foresight.repository.IncidentNotificationRepo;
import com.tcts.foresight.scheduler.entity.IncidentSLAHistoryEntity;
import com.tcts.foresight.scheduler.repository.IncidentSLAHistoryRepo;
import com.tcts.foresight.scheduler.repository.SLAWorkFlowRepo;
import com.tcts.foresight.service.IncidentCreationService;
import com.tcts.foresight.util.Constant;
import com.tcts.foresight.util.DateUtil;
import com.tcts.foresight.util.StringUtil;

@Service
public class IncidentStaticDashboardServiceImpl {

	Logger logger = LoggerFactory.getLogger(IncidentStaticDashboardServiceImpl.class);
	@Autowired
	ImpactServiceImpl impactServiceImpl;

	@Autowired
	IncidentNotificationRepo lIncidentNotificationRepo;

	@Autowired
	IncidentCreationServiceImpl incidentCreationServiceImpl;

	@Autowired
	private IncidentCreationService incidentCreationService;

	@Autowired
	IncidentSLAHistoryRepo lIncidentSLAHistoryRepo;

	@Autowired
	SLAWorkFlowRepo lSLAWorkflowRepo;

	@Autowired
	CSATFeedbackRepo lCSATRepo;

	public HashMap<String, Object> getMTTRDashboards(String jsonPayLoadMap) {

		String MTTRKEY = "MTTR";
		String incidentDetailsKey = "incidentDetails";

		HashMap<String, Object> map = new HashMap<String, Object>();
		List<MappedVariableInstanceLogVO> mappedList = new ArrayList<MappedVariableInstanceLogVO>();
		List<String> priorityList = new ArrayList<String>();

		try {
			if (StringUtil.isNotNullNotEmpty(jsonPayLoadMap)) {

				priorityList = impactServiceImpl.getAllDistinctName().stream().map(PriorityDetailsEntity::getName).collect(Collectors.toList());
				mappedList = incidentCreationServiceImpl.filteredJobList(jsonPayLoadMap);

				for (MappedVariableInstanceLogVO incident : mappedList) {
					
					if(priorityList.contains(incident.getPriority()) && StringUtil.isNotNullNotEmpty(incident.getPriority())) {
						
						if (map.containsKey(incident.getPriority())) {
							List<Object> incList = new ArrayList<Object>();
							long diff = 0L;
							HashMap<String, Object> incMap = (HashMap<String, Object>) map.get(incident.getPriority());
							long oldMTTR = (long) incMap.get(MTTRKEY);
							diff = DateUtil.dateDifference(incident.getIncidentCreationDate(), incident.getResolvedDate());
							long convertedLong = oldMTTR + diff;
							incMap.put(MTTRKEY, convertedLong);
							incList = (List<Object>) incMap.get(incidentDetailsKey);
							incList.add(incident);
							incMap.put(incidentDetailsKey, incList);
							map.put(incident.getPriority(), incMap);
							
						} else {
							HashMap<String, Object> incMap = new HashMap<String, Object>();
							List<Object> incList = new ArrayList<Object>();

							long diff = 0L;
							diff = DateUtil.dateDifference(incident.getIncidentCreationDate(), incident.getResolvedDate());
							incMap.put(MTTRKEY, diff);
							incList.add(incident);
							incMap.put(incidentDetailsKey, incList);
							map.put(incident.getPriority(), incMap);
						}
					}
				} // end for

				for (String pList : priorityList) {

					if (!map.containsKey(pList)) {
						
						HashMap<String, Object> incMap = new HashMap<String, Object>();
						List<Object> incList = new ArrayList<Object>();
						long incMTTR = 0;
						incMap.put(incidentDetailsKey, incList);
						incMap.put(MTTRKEY, incMTTR);
						map.put(pList, incMap);
						
					}

				}

				for (String key : map.keySet()) {
					if (map.containsKey(key)) {

						float finalMTTR = 0.0f;
						HashMap<String, Object> incMap = new HashMap<String, Object>();
						incMap = (HashMap<String, Object>) map.get(key);
						long incMTTR = (long) incMap.get(MTTRKEY);
						List<Object> incList = (List<Object>) incMap.get(incidentDetailsKey);
						if (incList.size() > 0) {
							finalMTTR = (float) incMTTR / incList.size();
						}
						float finalValue = (float) finalMTTR / (1000 * 60 * 60);
						incMap.put(MTTRKEY, finalValue);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception occured in getMTTRDashboards: " + e.getMessage(), e);
		}
		return map;
	}

	public Map<String, HashMap<String, Object>> getslaBasedDashboards(String jsonPayLoadMap) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);

 

        Map<String, HashMap<String, Object>> slaBasedMap = new HashMap<String, HashMap<String, Object>>();
        HashMap<String, Object> internalMap = new HashMap<String, Object>();
        ArrayList<MappedVariableInstanceLogVO> greenList = new ArrayList<MappedVariableInstanceLogVO>();
        ArrayList<MappedVariableInstanceLogVO> yellowList = new ArrayList<MappedVariableInstanceLogVO>();
        ArrayList<MappedVariableInstanceLogVO> redList = new ArrayList<MappedVariableInstanceLogVO>();

 

        // SLA Based Dashbords
        try {
            List<MappedVariableInstanceLogVO> mappedVariableInstanceLogVOList = incidentCreationService
                    .filteredJobList(jsonPayLoadMap);

 

            if (mappedVariableInstanceLogVOList != null && mappedVariableInstanceLogVOList.size() > 0) {

 
            	List<String> incidentList = mappedVariableInstanceLogVOList.stream().map(x->x.getIncidentID()).collect(Collectors.toList());
            	List<IncidentSLAHistoryEntity> lIncidentSLAHistoryEntity1 = lIncidentSLAHistoryRepo.findByIncidentIDAndSlaTypeNative(incidentList,Constant.SLA_TARGET_RESOLUTIONSLA);
            	  
                for (MappedVariableInstanceLogVO lMappedVariableInstanceLogVOobject : mappedVariableInstanceLogVOList) 
                {

 

//                    List<IncidentSLAHistoryEntity> lIncidentSLAHistoryEntity1 = lIncidentSLAHistoryRepo
//                            .findByIncidentIDAndSlaType(lMappedVariableInstanceLogVOobject.getIncidentID(),
//                                    Constant.SLA_TARGET_RESOLUTIONSLA);
                    for (IncidentSLAHistoryEntity lIncidentSLAHistoryEntity : lIncidentSLAHistoryEntity1) {
                        if (lIncidentSLAHistoryEntity != null && lIncidentSLAHistoryEntity.getIncidentID().equals(lMappedVariableInstanceLogVOobject.getIncidentID())) {
                                try {
                                    Calendar startSLADate = DateUtil.stringToCal(lIncidentSLAHistoryEntity.getCreatedDate(),
                                            sdf);
                                    Calendar completeSLADate = DateUtil
                                            .stringToCal(lIncidentSLAHistoryEntity.getSlaCompletionDate(), sdf);
                                    if (completeSLADate.getTimeInMillis() > startSLADate.getTimeInMillis()) {
                                        Long slaPercenatgePassed = 100 - (((Long.parseLong(lIncidentSLAHistoryEntity.getSlaTimeRemaining()))* 100 )/ (completeSLADate.getTimeInMillis() - startSLADate.getTimeInMillis()));
                                        if (slaPercenatgePassed.longValue() >= 100) {
                                            redList.add(lMappedVariableInstanceLogVOobject);
                                            break;
                                        } else if (slaPercenatgePassed.longValue() < 100
                                                && slaPercenatgePassed.longValue() >= 90) {
                                            yellowList.add(lMappedVariableInstanceLogVOobject);
                                            break;
                                        } else {
                                            greenList.add(lMappedVariableInstanceLogVOobject);
                                            break;
                                        }
                                    }

 

                                } catch (ArithmeticException e) {
                                    logger.error("Exception occured in getslaBasedDashboards in inner catch: "
                                            + e.getMessage(), e);
                                }
                            } else
                            {
                            	
//                            	greenList.add(lMappedVariableInstanceLogVOobject);
                            }
                                
                        }

 

                    Set<MappedVariableInstanceLogVO> setRedList = new HashSet<>(redList);
                    Set<MappedVariableInstanceLogVO> setgreenList = new HashSet<>(greenList);
                    Set<MappedVariableInstanceLogVO> setYellowList = new HashSet<>(yellowList);
                    redList.clear();
                    greenList.clear();
                    yellowList.clear();
                    redList.addAll(setRedList);
                    greenList.addAll(setgreenList);
                    yellowList.addAll(setYellowList);
                }

 

            }

 

            internalMap = new HashMap<String, Object>();
            internalMap.put("count", redList.size());
            internalMap.put("data", redList);
            slaBasedMap.put("red", internalMap);

 

            internalMap = new HashMap<String, Object>();
            internalMap.put("count", yellowList.size());
            internalMap.put("data", yellowList);
            slaBasedMap.put("yellow", internalMap);

 

            internalMap = new HashMap<String, Object>();
            internalMap.put("count", greenList.size());
            internalMap.put("data", greenList);
            slaBasedMap.put("green", internalMap);
        } catch (Exception e) {
            logger.error("Exception occured in getslaBasedDashboards: " + e.getMessage(), e);
        }
        return slaBasedMap;
    }

	public Map<String, Object> customerSatisfactionChart(String jsonPayLoadMap) {

		List<MappedVariableInstanceLogVO> mappedVariableInstanceLogVOList = incidentCreationService
				.filteredJobList(jsonPayLoadMap);

		List<CSATFeedabckEntity> listOfAllRAting = lCSATRepo.findAll();
		Map<String, Object> allScoreMap = new HashMap<String, Object>();
		Long ratingOne = 0L;
		Long ratingtwo = 0L;
		Long ratingThree = 0L;
		Long ratingFour = 0L;
		Long ratingFive = 0L;

		if (mappedVariableInstanceLogVOList != null && listOfAllRAting != null) {

			for (MappedVariableInstanceLogVO lMappedVariableInstanceLogVO : mappedVariableInstanceLogVOList) {

				for (CSATFeedabckEntity lCSATFeedabckEntitylist : listOfAllRAting) {

					if (lMappedVariableInstanceLogVO.getIncidentID()
							.equalsIgnoreCase(lCSATFeedabckEntitylist.getTicketid())) {

						if (lCSATFeedabckEntitylist.getRating() != null && lCSATFeedabckEntitylist.getRating() == 1L) {
							ratingOne++;
						}
						if (lCSATFeedabckEntitylist.getRating() != null && lCSATFeedabckEntitylist.getRating() == 2L) {
							ratingtwo++;
						}
						if (lCSATFeedabckEntitylist.getRating() != null && lCSATFeedabckEntitylist.getRating() == 3L) {
							ratingThree++;
						}
						if (lCSATFeedabckEntitylist.getRating() != null && lCSATFeedabckEntitylist.getRating() == 4L) {
							ratingFour++;
						}
						if (lCSATFeedabckEntitylist.getRating() != null && lCSATFeedabckEntitylist.getRating() == 5L) {
							ratingFive++;
						}
					}

				}
			}

			int sumOfAll = (int) (ratingOne + ratingtwo + ratingThree + ratingFour + ratingFive);
			float overAllRating = (float) (ratingOne * 1 + ratingtwo * 2 + ratingThree * 3 + ratingFour * 4
					+ ratingFive * 5) / sumOfAll;
			allScoreMap.put("One", ratingOne);
			allScoreMap.put("Two", ratingtwo);
			allScoreMap.put("Three", ratingThree);
			allScoreMap.put("Four", ratingFour);
			allScoreMap.put("Five", ratingFive);
			if (ratingOne == 0 && ratingtwo == 0 && ratingThree == 0 && ratingFour == 0 && ratingFive == 0) {

				allScoreMap.put("Over_All_Rating", null);

			} else {
				allScoreMap.put("Over_All_Rating", overAllRating);
			}

		}

		return allScoreMap;
	}

}
