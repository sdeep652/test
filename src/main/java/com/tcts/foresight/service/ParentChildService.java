package com.tcts.foresight.service;

import java.util.List;

import com.tcts.foresight.entity.IncidentStatusEntity;
import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.pojo.ParentChildRelationDetails;

public interface ParentChildService {

//	ParentChildEntity addParentChild(ParentChildEntity parentchild);

	
	
//	void addParent(String selectedIncidentId, String parentIncidentsId);
	
//	List<String> fetchParentsParent(String incidentID);
//	List<String> fetchParentschild(String incidentID);

	List<ParentChildRelationDetails> fetchParentsChildRelationDetails(String incidentID);

	List<ParentChildRelationDetails> fetchRelationDetails(String incidentId);

	void addChild(String authToken, String containerId, String clientCode,
			MappedVariableInstanceLogVO lMappedVariableInstanceLogVO);

	void addParent(String authToken, String containerId, String clientCode,
			MappedVariableInstanceLogVO lMappedVariableInstanceLogVO);

	public List<IncidentStatusEntity> getIncidentListForParentChild(String jsonPayLoadMap);
	
//	void addChild(MappedVariableInstanceLogVO lMappedVariableInstanceLogVO);
	
	

	
}
