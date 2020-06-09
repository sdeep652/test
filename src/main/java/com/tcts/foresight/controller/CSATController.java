package com.tcts.foresight.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcts.foresight.entity.CSATFeedabckEntity;
import com.tcts.foresight.scheduler.service.impl.CSATFeedbackServiceImpl;
import com.tcts.foresight.scheduler.service.impl.CSATServiceImpl;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/csat")
public class CSATController {

	@Autowired
	CSATFeedbackServiceImpl lCSATFeedbackServiceImpl;

	@Autowired
	CSATServiceImpl lCSATServiceImpl;

	@PostMapping("/savefeedback")
	public CSATFeedabckEntity saveFeedback(@RequestBody CSATFeedabckEntity cSATFeedabckEntity) {
		System.out.println("in a controller");
		System.out.println(cSATFeedabckEntity.toString());
		return lCSATFeedbackServiceImpl.saveFeedback(cSATFeedabckEntity);
	}

	@GetMapping("/getFeedbackData/{rating}")
	public HashMap<String, Object> getFeedbackData(@PathVariable Long rating) {
		return lCSATServiceImpl.getFeedbackData(rating);
	}

	@GetMapping("/getscore")
	public Map<String, Object> getScore() {
		return lCSATFeedbackServiceImpl.getScore();
	}

	@GetMapping("/checsubmission/{ticketid}")
	public Map<String, String> checkFormSubmission(@PathVariable String ticketid) {
		return lCSATFeedbackServiceImpl.checkFormSubmission(ticketid);
	}
}
