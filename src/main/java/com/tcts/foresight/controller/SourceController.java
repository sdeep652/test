package com.tcts.foresight.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcts.foresight.entity.SourceDetailsEntity;
import com.tcts.foresight.service.SourceService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value ="/source")
public class SourceController {
	Logger logger = LoggerFactory.getLogger(SourceController.class);
	@Autowired
	private SourceService sourceService;
	
	@GetMapping("/getSource/{module}")
	public List<SourceDetailsEntity> getSource(@PathVariable String module) {
		//logger.info("inside create instance");
		return sourceService.getSource(module);
		
	}
	
	
	@GetMapping("/getSource")
	public List<SourceDetailsEntity> getAllSource(){
		return sourceService.getAllSource();
	}

}
