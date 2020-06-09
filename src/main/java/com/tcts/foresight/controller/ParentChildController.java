package com.tcts.foresight.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcts.foresight.entity.IncidentStatusEntity;
import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.pojo.ParentChildRelationDetails;
import com.tcts.foresight.service.impl.ParentChildServiceImpl;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/relations")
public class ParentChildController {

	@Autowired
	private ParentChildServiceImpl parentChildServiceimpl;

	@PostMapping("/parentChildMapping/{containerId}")
	public MappedVariableInstanceLogVO parentChildMapping(@RequestHeader("Authorization") String authToken,
			@PathVariable("containerId") String containerId, @RequestParam("client") String clientCode,
			@RequestBody MappedVariableInstanceLogVO parentchild) {

		MappedVariableInstanceLogVO parentChildEntity = parentChildServiceimpl.checkAndInsert(authToken, containerId,
				clientCode, parentchild);

		
		return parentchild;

	}

	// displays the incident relation on relation tab.
	@GetMapping("/fetchParentsChildRelationDetails/{incidentID}")
	public List<ParentChildRelationDetails> fetchParentsChildRelationDetails(@PathVariable String incidentID) {
		List<ParentChildRelationDetails> St = parentChildServiceimpl.fetchParentsChildRelationDetails(incidentID);
		return St;

	}

	//Untag
	@PutMapping("/untagRelationship/{containerId}")
	public void untagRelationship(@RequestHeader("Authorization") String authToken,
			@PathVariable("containerId") String containerId, @RequestParam("client") String clientCode,
			@RequestBody HashMap<String, String> body) {
		parentChildServiceimpl.untagRelation(body, authToken, containerId, clientCode);

	}

	//Fetch ticket with status not cancelled and closed and assignment Group

	@PostMapping("/getIncidentListForParentChild")
	public List<IncidentStatusEntity> getIncidentListForParentChild(@RequestBody String jsonPayLoadMap) {
		List<IncidentStatusEntity> incList = new ArrayList<IncidentStatusEntity>();
		incList = parentChildServiceimpl.getIncidentListForParentChild(jsonPayLoadMap);
		return incList;
	}

}
