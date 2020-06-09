package com.tcts.foresight.controller;

import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcts.foresight.entity.ConfigEntityCached;
import com.tcts.foresight.entity.ErrorDetails;
import com.tcts.foresight.util.StringUtil;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/config")
public class ConfigController {

	@Autowired
	ConfigEntityCached configEntityCached;
	
		
	Logger logger = LoggerFactory.getLogger(ConfigController.class);
	
//	@GetMapping("/")
//	public ResponseEntity<Object> findAllKeys() {
//System.out.println("i m here ");
//		try {
//			HashMap<String, String> map =new HashMap<String, String>();
//				map= configEntityCached.getAllValue();
//			
//			if(map!=null && map.size()>=0) {
//				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(map);	
//			}
//			else {
//				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ErrorDetails.NOCONTENT());
//			}
//			}
//		catch (Exception e) {
//			logger.error("Exception Occurred in findAllKeys the Constant from Config");
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
//		}
//	}

	
	@GetMapping(value = {"/","/{key}"})
	public ResponseEntity<Object> findByKey(@PathVariable(required = false) String key) {

		try {
			
			if(StringUtil.isNullOrEmpty(key))
			{
				TreeMap<String, String> map =new TreeMap<String, String>();
				map= configEntityCached.getAllValue();
				if(map!=null && map.size()>=0) {
					return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(map);	
				}
				else {
					return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ErrorDetails.NOCONTENT());
				}
			}
			
			
			
			String value = configEntityCached.getValue(key);
			if(value != null) {
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(value);
			}else {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ErrorDetails.NOCONTENT());
			}
			}
		catch (Exception e) {
			logger.error("Exception Occurred in findByKey the Constant from Config",e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
	}
	
	
	@GetMapping(value = {"/reset"})
	public ResponseEntity<Object> resetValues() {

		try {
				configEntityCached.cleanAllValue();
				return ResponseEntity.status(HttpStatus.OK).body(configEntityCached.getAllValue());
			}
		catch (Exception e) {
			logger.error("Exception Occurred in findByKey the Constant from Config",e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDetails.getInternalServerError(e));
		}
	}
	
	
	
}
