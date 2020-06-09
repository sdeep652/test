package com.tcts.foresight.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcts.foresight.entity.ProgressBasedNotificationEntity;
import com.tcts.foresight.exception.ResourceNotFoundException;
import com.tcts.foresight.service.impl.IncidentProgressNotificationServiceImpl;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/progressNotification")
public class IncidentProgressNotificationController {

	@Autowired
	private IncidentProgressNotificationServiceImpl incidentProgressNotificationServiceImpl;

	@GetMapping("/getAllNotification/{module}")
	public List<ProgressBasedNotificationEntity> getAllNotificationDetails(@PathVariable String module) {
		List<ProgressBasedNotificationEntity> list = new ArrayList<ProgressBasedNotificationEntity>();
		try {
			list = incidentProgressNotificationServiceImpl.getAllNotificationDetails(module);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	@PostMapping("/addNotification")
	public ProgressBasedNotificationEntity insertNotificationDetails(
			@RequestBody ProgressBasedNotificationEntity progressBasedNotificationEntity) {
		ProgressBasedNotificationEntity lprogressBasedNotificationEntity = new ProgressBasedNotificationEntity();
		try {
			lprogressBasedNotificationEntity = incidentProgressNotificationServiceImpl
					.insertNotificationDetails(progressBasedNotificationEntity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lprogressBasedNotificationEntity;
	}

	@PutMapping("/editNotification/{id}")
	public ProgressBasedNotificationEntity editNotificationDetails(@PathVariable Long id,
			@RequestBody ProgressBasedNotificationEntity progressBasedNotificationEntity)
			throws ResourceNotFoundException {

		ProgressBasedNotificationEntity lprogressBasedNotificationEntity = null;
		try {
			lprogressBasedNotificationEntity = incidentProgressNotificationServiceImpl.editNotificationDetails(id,
					progressBasedNotificationEntity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lprogressBasedNotificationEntity;

	}

	@DeleteMapping("/deleteNotification/{id}")
	public void deleteNotificationDetails(@PathVariable Long id) {

		try {
			incidentProgressNotificationServiceImpl.deleteNotificationDetails(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
