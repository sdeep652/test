package com.tcts.foresight.scheduler.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.CSATFeedabckEntity;
import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.repository.CSATFeedbackRepo;
import com.tcts.foresight.repository.MappedVariableInstanceRepo;
import com.tcts.foresight.util.Constant;

@Service
public class CSATFeedbackServiceImpl {

	Logger logger = LoggerFactory.getLogger(CSATFeedbackServiceImpl.class);

	@Autowired
	CSATFeedbackRepo lCSATRepo;

	@Autowired
	MappedVariableInstanceRepo mappedVariableInstanceRepo;

	public CSATFeedabckEntity saveFeedback(CSATFeedabckEntity cSATFeedabckEntity) {
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);


		try {
			cSATFeedabckEntity.setFormSubDate(sdf.format(new Date()));
			MappedVariableInstanceLogVO oMappedVariableInstanceLogVOObject = mappedVariableInstanceRepo.findByIncidentID(cSATFeedabckEntity.getTicketid());
			cSATFeedabckEntity.setTicketCreatedDate(oMappedVariableInstanceLogVOObject.getIncidentCreationDate());
		} catch (Exception e) {
			logger.error("Exception Occurred in saveFeedback: " + e.getMessage(), e);
		}
		return lCSATRepo.save(cSATFeedabckEntity);
	}

	public Map<String, Object> getScore() {

		LinkedHashMap<String, Object> allScoreMap = new LinkedHashMap<String, Object>();

		try {
			List<CSATFeedabckEntity> listOfAllRAting = lCSATRepo.findAll();
			Long ratingOne = 0L;
			Long ratingtwo = 0L;
			Long ratingThree = 0L;
			Long ratingFour = 0L;
			Long ratingFive = 0L;

			for (CSATFeedabckEntity list : listOfAllRAting) {

				if (list.getRating() != null && list.getRating() == 1L) {
					ratingOne++;
				}
				if (list.getRating() != null && list.getRating() == 2L) {
					ratingtwo++;
				}
				if (list.getRating() != null && list.getRating() == 3L) {
					ratingThree++;
				}
				if (list.getRating() != null && list.getRating() == 4L) {
					ratingFour++;
				}
				if (list.getRating() != null && list.getRating() == 5L) {
					ratingFive++;
				}

			}
			int sumOfAll = (int) (ratingOne + ratingtwo + ratingThree + ratingFour + ratingFive);
			float overAllRating = (float) (ratingOne * 1 + ratingtwo * 2 + ratingThree * 3 + ratingFour * 4 + ratingFive * 5) / sumOfAll;
			
			
			allScoreMap.put("Five", ratingFive);
			allScoreMap.put("Four", ratingFour);
			allScoreMap.put("Three", ratingThree);
			allScoreMap.put("Two", ratingtwo);
			allScoreMap.put("One", ratingOne);
			allScoreMap.put("Over_All_Rating", overAllRating);

			// Feedback Form Link

		} catch (Exception e) {
			logger.error("Exception Occurred in getScore: " + e.getMessage(), e);
		}
		return allScoreMap;

	}

	public Map<String, String> checkFormSubmission(String ticketid) {

		Map<String, String> map = new HashMap<String, String>();
		try {

			logger.info("Ticket ID", ticketid);
			MappedVariableInstanceLogVO oMappedVariableInstanceLogVOObject = mappedVariableInstanceRepo
					.findByIncidentID(ticketid);
			if (oMappedVariableInstanceLogVOObject != null) {
				map.put("Current_status", oMappedVariableInstanceLogVOObject.getStatus());
			}

			CSATFeedabckEntity lCSATFeedabckEntityObject = lCSATRepo.findByTicketid(ticketid);
			if (lCSATFeedabckEntityObject != null) {
				map.put("form_submission_status", "true");
				return map;
			}

		} catch (Exception e) {
			logger.error("Exception Occurred in getScore: " + e.getMessage(), e);
		}
		map.put("form_submission_status", "false");
		return map;

	}

}
