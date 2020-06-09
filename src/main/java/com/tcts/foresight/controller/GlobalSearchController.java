package com.tcts.foresight.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.pojo.ParentChildRelationDetails;
import com.tcts.foresight.repository.MappedVariableInstanceRepo;
import com.tcts.foresight.service.impl.GlobalSearchServiceImpl;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/GlobalSearch")
public class GlobalSearchController {

	@Autowired
	private  GlobalSearchServiceImpl lGlobalSearchServiceImpl;
	
	@PostMapping("/search")
	public List<MappedVariableInstanceLogVO> fetchGlobalSearch(@RequestBody String payload) {
		
		return lGlobalSearchServiceImpl.fetchGlobalSearch(payload);

	}
}
