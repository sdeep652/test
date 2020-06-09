package com.tcts.foresight.service;

import java.util.HashMap;
import java.util.List;

import com.tcts.foresight.entity.IncidentAttachmentEntity;
import com.tcts.foresight.entity.IncidentHistoryEntity;
import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.exception.CustomValidationException;

public interface IncidentCreationService {
	
	

	MappedVariableInstanceLogVO completeTask(String authToken, String containerId, String taskInstanceId, String clientCode,String processInstanceId, MappedVariableInstanceLogVO jsonPayload) throws CustomValidationException;

	public 	List<MappedVariableInstanceLogVO> filteredJobList(String jsonPayLoadMap);
	
	//public 	List<MappedVariableInstanceLogVO> filteredjoblistWithGroupVisited(String jsonPayLoadMap);

	List<MappedVariableInstanceLogVO> filteredJobList1(String userName);

	HashMap<String,String> assignToMe(String authToken, String containerId, List<MappedVariableInstanceLogVO> jsonPayload,String clientCode);

	List<MappedVariableInstanceLogVO> filteredJobListByPagination(Integer pageNo, Integer pageSize, String sortBy,
			String jsonPayLoadMap);

	List<MappedVariableInstanceLogVO> fetchAllIncidentList(String incidentID);

	// Bulk Incident TT's Resolve
	HashMap<String,String> resolveIncidentTT(String authToken, String containerId, 
			String clientCode, List<MappedVariableInstanceLogVO> jsonPayload);

	List<IncidentHistoryEntity> fetchAllIncidentHistory(String incidentID);
	
	List<IncidentAttachmentEntity> addAttachment(List<IncidentAttachmentEntity> incidentAttachmentList);
	// Delete Incident Attachment
	public void deleteAttachment(Long attachmentId, String authToken);
	//Fetching All Incident Attachments
	List<IncidentAttachmentEntity> fetchIncidentAttchmentList(String ticketId);

	 List<String> getIncidentId(List<String> status);

	 HashMap<String, List<String>>  fetchStatusVisited(String ticketId);


	 List<String> fechAllRecords(String module, String statusClosed, String statusCancelled);

	public MappedVariableInstanceLogVO incidentClone(String authToken, String containerId, String processId,
			String clientCode, String requestPayload, String incidentId);
	
	public void sendLink(MappedVariableInstanceLogVO lMappedVariableInstanceLogVO);
	 
		

}
