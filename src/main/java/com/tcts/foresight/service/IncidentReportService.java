package com.tcts.foresight.service;

import java.util.HashMap;
import java.util.List;

import com.tcts.foresight.entity.MappedVariableInstanceLogVO;

public interface IncidentReportService {

	List<HashMap<String, Object>> getAllIncidentAnalysisReport(String jsonPayLoadMap);

	Long getIncidentAgeing(String incidentID);

	List<MappedVariableInstanceLogVO> fetchAgeingReport(String jsonPayLoadMap);

    HashMap<String, List<String>> fetchAllIncidentCreatedResolvedUser();

	HashMap<String, String> fetchAvailableColumnDisplay();
	
}
