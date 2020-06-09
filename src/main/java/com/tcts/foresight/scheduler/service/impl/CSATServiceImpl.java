package com.tcts.foresight.scheduler.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.CSATEntity;
import com.tcts.foresight.repository.CSATRepo;

@Service
public class CSATServiceImpl {

	Logger logger = LoggerFactory.getLogger(CSATServiceImpl.class);

	@Autowired
	CSATRepo lCSATRepo;

	public HashMap<String, Object> getFeedbackData(Long rating) {

		HashMap<String, Object> feedBackList = new HashMap<String, Object>();
		try {
			List<CSATEntity> list = lCSATRepo.findByRating(rating);
			List<HashMap<String, String>> objList = new ArrayList<HashMap<String, String>>();
			for (CSATEntity lCSATEntity : list) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("reason", (lCSATEntity.getReason()));
				objList.add(map);
			}
			feedBackList.put("reasons", objList);
			feedBackList.put("Question", list.get(0).getQuestion());
		} catch (Exception e) {
			logger.error("Exception occured in getFeedbackData: " + e.getMessage(), e);
		}
		return feedBackList;
	}

//	public List<CSATEntity> checkFormSubmission1(Long ticketid) {
//
//		return null;
//	}

}
