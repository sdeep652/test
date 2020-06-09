package com.tcts.foresight.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcts.foresight.entity.UserDetailsEntity;
import com.tcts.foresight.service.UserManagementService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/bulkuser")
public class BulkUserController {
	Logger logger = LoggerFactory.getLogger(BulkUserController.class); 


	@Autowired
	private UserManagementService userMngService;
	
	@PostMapping("/create")
	public List<UserDetailsEntity> bulkUserCreation(@RequestBody List<UserDetailsEntity> bulkUserList){
		
		bulkUserList.stream().forEach(list -> logger.info("Password --------------> "+list.getPassword()));
		return userMngService.bulkUserCreation(bulkUserList);
	}
}
