package com.tcts.foresight.scheduler.service.impl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.repository.MappedVariableInstanceRepo;
import com.tcts.foresight.scheduler.entity.IncidentSLAHistoryEntity;
import com.tcts.foresight.scheduler.entity.SLAConfigurationActionsEntity;
import com.tcts.foresight.scheduler.entity.SLAConfigurationEntity;
import com.tcts.foresight.scheduler.repository.IncidentSLAHistoryRepo;
import com.tcts.foresight.scheduler.repository.SLAConfigurationRepo;
import com.tcts.foresight.scheduler.service.SLAHistoryService;
import com.tcts.foresight.service.impl.IncidentCreationServiceImpl;
import com.tcts.foresight.util.Constant;
import com.tcts.foresight.util.DateUtil;
import com.tcts.foresight.util.StringUtil;

@Service
public class SLAHistoryServiceImpl implements SLAHistoryService {

	private static final boolean INCIDENT_STATUS_RESOLVED = false;

	Logger logger = LoggerFactory.getLogger(SLAHistoryServiceImpl.class);

	@Autowired
	SLAConfigurationRepo slaConfigRepo;
	
	@Autowired
	IncidentCreationServiceImpl lIncidentCreationServiceImpl;
	
	@Autowired
	IncidentSLAHistoryRepo incidentSLAHRepo;

	@Autowired
	MappedVariableInstanceRepo mappedVarInsRepo;
	
	@Autowired
	SLAConfigurationRepo slaConfigRepo1;
	
	

	//dummy method for unit testing.
	@Override
	public IncidentSLAHistoryEntity createIncidentSLA(IncidentSLAHistoryEntity incSLAHEntity) {
		IncidentSLAHistoryEntity returnIncSLAHisE = null;
		MappedVariableInstanceLogVO mappedVarInstLogVo = null;
		try {
			mappedVarInstLogVo = mappedVarInsRepo.findByIncidentID(incSLAHEntity.getIncidentID());
			if (mappedVarInstLogVo.getStatus().equalsIgnoreCase(Constant.INCIDENT_STATUS_IN_PROGRESS)) {
				incSLAHEntity.setSlaStatus(Constant.SLA_STATUS_COMPLETED);
			}
			returnIncSLAHisE = incidentSLAHRepo.save(incSLAHEntity);
		} catch (Exception e) {
		logger.error("Exception occur while in createIncidentSLA" + e.getMessage(),e);
		}
		return returnIncSLAHisE;
	}

	@Override
	public List<IncidentSLAHistoryEntity> fetchIncidentSLADetails(String incidentID) {
		return incidentSLAHRepo.findByIncidentID(incidentID);
	}


	
	

	@Override
	public IncidentSLAHistoryEntity saveIncidentSLA(@Valid IncidentSLAHistoryEntity incSlaHisEntity) {
		// TODO Auto-generated method stub
		return incidentSLAHRepo.save(incSlaHisEntity);
	}
	
	private SLAConfigurationEntity matchSLAActions(SLAConfigurationEntity sLAConfigurationEntitylist, MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW)
	{

		//logger.info("matchSLAActions - 1");
		
		
		SLAConfigurationEntity matchedActions = null;
		if (sLAConfigurationEntitylist.getSlaConfigActionsEList() != null && sLAConfigurationEntitylist.getSlaConfigActionsEList().size() > 0) {
			String query = "select * from mapped_variable_instance_log where ticket_id='"+ lMappedVariableInstanceLogVONEW.getIncidentID() + "' AND ";
			if(sLAConfigurationEntitylist.getSlaConfigActionsEList()!=null && sLAConfigurationEntitylist.getSlaConfigActionsEList().size()>0)
			{
				//logger.info("matchSLAActions - 2");
				Collections.sort(sLAConfigurationEntitylist.getSlaConfigActionsEList());
			}
			//logger.info("matchSLAActions - 3");
			for (SLAConfigurationActionsEntity lSLAConfigurationActionsEntity : sLAConfigurationEntitylist.getSlaConfigActionsEList()) {
				List<String> result=null;
				String columnValue=null;
				String columnName = lSLAConfigurationActionsEntity.getConditionOn();
				
				//Hardcoded
				if(columnName.equalsIgnoreCase("subcategory")) {
					columnName="sub_category";
				}
				if(lSLAConfigurationActionsEntity.getValue().contains(",")) {
					
					result = Arrays.asList(lSLAConfigurationActionsEntity.getValue().split("\\s*,\\s*"));
					//logger.info("matchSLAActions - 4");
			       
					
					String inClause = "";
					for (String str : result) {
						
						inClause = inClause +"'" +str+"',";
						
						
					}
					
					if(inClause.endsWith(","))
					{
						inClause = inClause.substring(0, inClause.length()-1);
					}
					query = query + columnName + " IN ("+inClause+")";
					
					
				}else {
				 columnValue = lSLAConfigurationActionsEntity.getValue();
				 query = query + columnName + "='" + columnValue + "'";
				}
				String operator = lSLAConfigurationActionsEntity.getOperator();
				//logger.info("operator "+operator);
				
				
				
				
				if (StringUtil.isNotNullNotEmpty(operator)) {
					query = query + " " + operator + " ";
				}
				
				//logger.info("columnName =------------" +columnName);
				//logger.info("query: "+query);
				

			}
			//logger.info("matchSLAActions - 5");
			query = query.trim();
			if (query.endsWith("AND")) {
				
				query = query.substring(0, query.length() - 3);
			}
			
			//logger.info("query " + query);
			List<MappedVariableInstanceLogVO> listofIncidents = lIncidentCreationServiceImpl.findByQuery(query);

			if (listofIncidents != null && listofIncidents.size() > 0) {
//				isSLAConditionMatched = true;
				matchedActions = sLAConfigurationEntitylist;
			}

		}
	
		
		return matchedActions;
	}

	@Override
	public void createResponceSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW) {
                                                                                                                                                                                                                                                                                                               		// TODO Auto-generated method stub
//logger.info("createSLAHistory 1");
		try {

			if (lMappedVariableInstanceLogVONEW != null) {

				if (Constant.INCIDENT_STATUS_NEW.equals(lMappedVariableInstanceLogVONEW.getStatus())) {
					//logger.info("createSLAHistory 2");
					SLAConfigurationEntity matchedActions = null;
					List<SLAConfigurationEntity> list = slaConfigRepo.findAll();
					for (SLAConfigurationEntity sLAConfigurationEntitylist : list) {
						//logger.info("createSLAHistory 3");
						if (sLAConfigurationEntitylist != null
								&& sLAConfigurationEntitylist.getSlaTarget().equalsIgnoreCase("Response") && "true".equals(sLAConfigurationEntitylist.getIsActive())) {
							//logger.info("createSLAHistory 4");
							
							try {
								matchedActions = matchSLAActions(sLAConfigurationEntitylist, lMappedVariableInstanceLogVONEW);
								if(matchedActions!=null)
								{
									createSLARecord(lMappedVariableInstanceLogVONEW, matchedActions);
								}
								
							}catch (Exception e) {
								// TODO: handle exception
								logger.error("i m in exception for matchedActions: "+e.getMessage(),e);
							}
							
							
						}
					}


				} 
			}

		} catch (Exception e) {
			logger.error("Exception occured in createResponceSLAHistory" + e.getMessage(),e);
		}

	
		
	}
	
	private void createSLARecord(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW,  SLAConfigurationEntity matchedActions)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);


		try {
			//logger.info("createSLARecord 6");
			
			List<IncidentSLAHistoryEntity> lIncidentSLAHistoryEntityList = incidentSLAHRepo.findByIncidentIDAndSlaTypeDescOrder(lMappedVariableInstanceLogVONEW.getIncidentID(), matchedActions.getSlaTarget());
			IncidentSLAHistoryEntity lIncidentSLAHistoryEntityPreviousLatest = null;
			if(lIncidentSLAHistoryEntityList!=null && lIncidentSLAHistoryEntityList.size()>0)
			{
				lIncidentSLAHistoryEntityPreviousLatest = lIncidentSLAHistoryEntityList.get(0);
			}
			// latest record
			//
			
			for(IncidentSLAHistoryEntity lIncidentSLAHistoryEntity: lIncidentSLAHistoryEntityList)
			{
				//logger.info("lIncidentSLAHistoryEntity.toString(): "+lIncidentSLAHistoryEntity.toString());
				//logger.info("matchedActions.getId(): "+matchedActions.getId());
				if(lIncidentSLAHistoryEntity!=null)
				{
					if(((Constant.SLA_STATUS_RUNNING.equals(lIncidentSLAHistoryEntity.getSlaStatus()) || Constant.SLA_STATUS_PAUSE.equals(lIncidentSLAHistoryEntity.getSlaStatus()))
							|| Constant.SLA_STATUS_BREACHED.equals(lIncidentSLAHistoryEntity.getSlaStatus())) && lIncidentSLAHistoryEntity.getSlaConfigId().longValue()==matchedActions.getId().longValue())
					{
						return;
					}
				}
			}
			
			//logger.info("createSLARecord 7");
			int days = 0;
			int hours = 0;
			int minutes = 0;
			int seconds = 0;
			String slaTargetTime = "";
			if (StringUtil.isNotNullNotEmpty(matchedActions.getTimeInDays()) ) {
				days = Integer.parseInt(matchedActions.getTimeInDays());
				slaTargetTime = matchedActions.getTimeInDays()+" Days";
			}

			if (StringUtil.isNotNullNotEmpty(matchedActions.getTimeInHours())) {
				hours = Integer.parseInt(matchedActions.getTimeInHours());
				if(StringUtil.isNotNullNotEmpty(slaTargetTime))
				{
					slaTargetTime = slaTargetTime + ", "+matchedActions.getTimeInHours()+" Hours";
				}else {
					slaTargetTime = matchedActions.getTimeInDays()+" Days";
				}
				
			}

			if (StringUtil.isNotNullNotEmpty(matchedActions.getTimeInMins())) {
				minutes = Integer.parseInt(matchedActions.getTimeInMins());
				if(StringUtil.isNotNullNotEmpty(slaTargetTime))
				{
					slaTargetTime = slaTargetTime + ", "+matchedActions.getTimeInMins()+" Minutes";
				}else {
					slaTargetTime = matchedActions.getTimeInMins()+" Minutes";
				}
			}

			if (StringUtil.isNotNullNotEmpty(matchedActions.getTimeInSecs())) {
				seconds = Integer.parseInt(matchedActions.getTimeInSecs());
				
				if(StringUtil.isNotNullNotEmpty(slaTargetTime))
				{
					slaTargetTime = slaTargetTime + ", "+matchedActions.getTimeInSecs()+" Seconds";
				}else {
					slaTargetTime = matchedActions.getTimeInSecs()+" Seconds";
				}
			}
			//logger.info("matchedActions.getSlaName(): "+matchedActions.getSlaName());
			//logger.info("days " + days);
			//logger.info("hours " + hours);
			//logger.info("minutes " + minutes);
			//logger.info("seconds " + seconds);
			int daysInSeconds = days * 24 * 60 * 60;
			int hoursInSeconds = hours * 60 * 60;
			int minutesInSeconds = minutes * 60;

			int totalSeconds = daysInSeconds + hoursInSeconds + minutesInSeconds + seconds;
			//logger.info("data in total====" + totalSeconds);

			IncidentSLAHistoryEntity lIncidentSLAHistoryEntity = new IncidentSLAHistoryEntity();
			lIncidentSLAHistoryEntity.setCreatedDate(lMappedVariableInstanceLogVONEW.getIncidentCreationDate());
			lIncidentSLAHistoryEntity.setIncidentID(lMappedVariableInstanceLogVONEW.getIncidentID());
			lIncidentSLAHistoryEntity.setSlaStatus(Constant.SLA_STATUS_RUNNING);
			
			lIncidentSLAHistoryEntity.setSlaType(matchedActions.getSlaTarget());
			lIncidentSLAHistoryEntity.setSlaTargetTime(slaTargetTime);
			lIncidentSLAHistoryEntity.setSlaConfigId(matchedActions.getId());
			lIncidentSLAHistoryEntity.setSlaName(matchedActions.getSlaName());
			String lastUpdatedDate =sdf.format(new Date());
			lIncidentSLAHistoryEntity.setLastUpdatedDate(lastUpdatedDate);
			Date incidentCreationDate =sdf.parse(lMappedVariableInstanceLogVONEW.getIncidentCreationDate());
			
			Calendar completionDateCal = Calendar.getInstance();
			if(lIncidentSLAHistoryEntityPreviousLatest!=null)
			{
				//  
				//
				// newSLACompletionTime = NewSLAConfigTime-( CurrentTime-IncidentCreationTime )
				
				
				Calendar incCreateCal = DateUtil.stringToCal(lMappedVariableInstanceLogVONEW.getIncidentCreationDate(),sdf);
				Calendar now = Calendar.getInstance();
				
				long diffInMilis = now.getTimeInMillis() - incCreateCal.getTimeInMillis();
				int diffInSecs = (int)diffInMilis/1000;
				int reducedTotalTime = totalSeconds - diffInSecs;
				completionDateCal.setTime(incidentCreationDate);
				completionDateCal.add(Calendar.SECOND, reducedTotalTime);
				String slaCompletionDateStr = sdf.format(completionDateCal.getTime());
				lIncidentSLAHistoryEntity.setSlaCompletionDate(slaCompletionDateStr);
				
			}else {
				completionDateCal.setTime(incidentCreationDate);
				completionDateCal.add(Calendar.SECOND, totalSeconds);
				String slaCompletionDateStr =sdf.format(completionDateCal.getTime());
				lIncidentSLAHistoryEntity.setSlaCompletionDate(slaCompletionDateStr);	
			}
			
			
			lIncidentSLAHistoryEntity.setSlaTimeRemaining(calculateSlaTimeRemaining(lIncidentSLAHistoryEntity.getSlaCompletionDate()));
			
			//logger.info("createSLAHistory 8");
			saveIncidentSLA(lIncidentSLAHistoryEntity);
			
		}catch (Exception e) {
			logger.error("Exception occured in createSLARecord: " + e.getMessage(), e);  

		}
	
	}
	
	private String calculateSlaTimeRemaining(String completionDateStr)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);

		String slaTimeRemainingDateStr = "0";
		try {
			
			Calendar completionDateCal = DateUtil.stringToCal(completionDateStr,sdf);
			
			Calendar now = Calendar.getInstance();
			now.setTime(new Date());
			long diff=completionDateCal.getTimeInMillis() - now.getTimeInMillis() ;
			if(diff>0)
			{
				slaTimeRemainingDateStr = String.valueOf(diff);
			}

			
			
		}catch (Exception e) {
			logger.error("Exception occured in calculateSlaTimeRemaining: " + e.getMessage(), e);  

		}
		
		return slaTimeRemainingDateStr;
	}
	
	
	

	@Override
	public void completeResponceSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW) {
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);

		
		try {

			if (!Constant.INCIDENT_STATUS_NEW.equals(lMappedVariableInstanceLogVONEW.getStatus())) {

					
					List<IncidentSLAHistoryEntity> lIncidentSLAHistoryEntityList = incidentSLAHRepo.findByIncidentIDAndSlaType(lMappedVariableInstanceLogVONEW.getIncidentID(),
							Constant.SLA_TARGET_RESPONSESLA);
					
					for(IncidentSLAHistoryEntity lIncidentSLAHistoryEntity: lIncidentSLAHistoryEntityList)
					{
						if (lIncidentSLAHistoryEntity != null && !lIncidentSLAHistoryEntity.getSlaStatus().equals(Constant.SLA_STATUS_COMPLETED)) {

							if(lIncidentSLAHistoryEntity.getSlaStatus().equals(Constant.SLA_STATUS_BREACHED)) {
								lIncidentSLAHistoryEntity.setSlaStatus(Constant.SLA_STATUS_BREACHED);
							}
							else{
								lIncidentSLAHistoryEntity.setSlaStatus(Constant.SLA_STATUS_COMPLETED);
							}
														
							String lastUpdatedDate = sdf.format(new Date());
							lIncidentSLAHistoryEntity.setLastUpdatedDate(lastUpdatedDate);
							
							lIncidentSLAHistoryEntity.setSlaTimeRemaining(calculateSlaTimeRemaining(lIncidentSLAHistoryEntity.getSlaCompletionDate()));
							
							saveIncidentSLA(lIncidentSLAHistoryEntity);
							//logger.info("Status is changed as complete");
						} else {
							//logger.info("incident not found with responseSLA type");
						}
					}
					

				}

		} catch (Exception e) {
			logger.error("Exception occured in completeResponceSLAHistory" + e.getMessage(),e);
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelResponceSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW) {
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);

		
		try {

			if (Constant.INCIDENT_STATUS_CANCELLED.equals(lMappedVariableInstanceLogVONEW.getStatus())) {

				List<IncidentSLAHistoryEntity> lIncidentSLAHistoryEntityList = incidentSLAHRepo.findByIncidentIDAndSlaType(lMappedVariableInstanceLogVONEW.getIncidentID(),Constant.SLA_TARGET_RESPONSESLA);
				if (lIncidentSLAHistoryEntityList != null) {
					for (IncidentSLAHistoryEntity lIncidentSLAHistoryEntity : lIncidentSLAHistoryEntityList) {
						if (lIncidentSLAHistoryEntity != null
								&& !lIncidentSLAHistoryEntity.getSlaStatus().equals(Constant.SLA_STATUS_CANCELLED)) {

							lIncidentSLAHistoryEntity.setSlaStatus(Constant.SLA_STATUS_CANCELLED);

							String lastUpdatedDate = sdf.format(new Date());
							lIncidentSLAHistoryEntity.setLastUpdatedDate(lastUpdatedDate);
							lIncidentSLAHistoryEntity.setSlaTimeRemaining(
									calculateSlaTimeRemaining(lIncidentSLAHistoryEntity.getSlaCompletionDate()));
							saveIncidentSLA(lIncidentSLAHistoryEntity);
							//logger.info("Status is changed as Cancel");
						} else {
							//logger.info("incident not found with responseSLA type");
						}
					}
				}

			}else
			{
				List<IncidentSLAHistoryEntity> lIncidentSLAHistoryEntityList = incidentSLAHRepo
						.findByIncidentIDAndSlaType(lMappedVariableInstanceLogVONEW.getIncidentID(),
								Constant.SLA_TARGET_RESPONSESLA);
				boolean slaReCreationRequired = true;
				for (IncidentSLAHistoryEntity lIncidentSLAHistoryEntity : lIncidentSLAHistoryEntityList) {
					if (lIncidentSLAHistoryEntity != null
							&& !lIncidentSLAHistoryEntity.getSlaStatus().equals(Constant.SLA_STATUS_CANCELLED)) {
						Long originalSLAConfigId = lIncidentSLAHistoryEntity.getSlaConfigId();
						SLAConfigurationEntity originalSLAConfig = slaConfigRepo.findSlaById(originalSLAConfigId);
						SLAConfigurationEntity matchedSLAConfig = null;
						if (originalSLAConfig != null) {
							matchedSLAConfig = matchSLAActions(originalSLAConfig, lMappedVariableInstanceLogVONEW);
							if (matchedSLAConfig != null) {
								// matched
							} else {
								slaReCreationRequired = false;
								// not matched
								// cancel the SLA
								lIncidentSLAHistoryEntity.setSlaStatus(Constant.SLA_STATUS_CANCELLED);
								String lastUpdatedDate = sdf.format(new Date());
								lIncidentSLAHistoryEntity.setLastUpdatedDate(lastUpdatedDate);
								lIncidentSLAHistoryEntity.setSlaTimeRemaining(
										calculateSlaTimeRemaining(lIncidentSLAHistoryEntity.getSlaCompletionDate()));
								saveIncidentSLA(lIncidentSLAHistoryEntity);
								//logger.info("Status is changed as Cancel");
								createResponceSLAHistory(lMappedVariableInstanceLogVONEW);
							}
						}
					}
				}

				

			}

		} catch (Exception e) {
			logger.error("Exception occured in cancelResponceSLAHistory: " + e.getMessage(),e);
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW) {
		try {

			if (lMappedVariableInstanceLogVONEW != null) {
                  //logger.info("checkSLAHistory i m here ");
					
                  	createResponceSLAHistory(lMappedVariableInstanceLogVONEW);
					
					completeResponceSLAHistory(lMappedVariableInstanceLogVONEW);
					
					cancelResponceSLAHistory(lMappedVariableInstanceLogVONEW);
                      
					createResolutionSLAHistory(lMappedVariableInstanceLogVONEW);// working
					
					pauseResolutionSLAHistory(lMappedVariableInstanceLogVONEW); //working (need to be tested from ashok)
                    
					completeResolutionSLAHistory(lMappedVariableInstanceLogVONEW); //working (need to be tested from ashok)
					
					cancelResolutionSLAHistory(lMappedVariableInstanceLogVONEW);
                       
					

			}

		} catch (Exception e) {
			logger.error("Exception occured in checkSLAHistory: " + e.getMessage(),e);
		}

	}

	

	@Override
	public void checkStartConditionSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW) {
		// TODO Auto-generated method stub
		
	}

	public void completeSLAStatus(String incidentID, String slaTypeResponsesla) {
		// TODO Auto-generated method stub
		
	}

	
	
	public static void main(String[] args) {
		
		
		long long1 = 46;
		long long2 = 46;
		
		if(long1==long2)
		{
			
		}else {
			
		}

		
		
		
	}

	@Override
	public void createResolutionSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW) {

		// TODO Auto-generated method stub
		//logger.info("createResolutionSLAHistory 1");
		try {

			if (lMappedVariableInstanceLogVONEW != null) {

				if (Constant.INCIDENT_STATUS_IN_PROGRESS.equals(lMappedVariableInstanceLogVONEW.getStatus())
						|| Constant.INCIDENT_STATUS_NEW.equals(lMappedVariableInstanceLogVONEW.getStatus())) {
					//logger.info("createResolutionSLAHistory 2");
					SLAConfigurationEntity matchedActions = null;
					List<SLAConfigurationEntity> list = slaConfigRepo.findAll();
					for (SLAConfigurationEntity sLAConfigurationEntitylist : list) {
						//logger.info("createResolutionSLAHistory 3");
						if (sLAConfigurationEntitylist != null
								&& sLAConfigurationEntitylist.getSlaTarget().equalsIgnoreCase("Resolution")
								&& "true".equals(sLAConfigurationEntitylist.getIsActive())) {
							//logger.info("createResolutionSLAHistory 4");

							try {
								matchedActions = matchSLAActions(sLAConfigurationEntitylist,lMappedVariableInstanceLogVONEW);
								if(matchedActions!=null)
								{
									createSLARecord(lMappedVariableInstanceLogVONEW, matchedActions);
								}
								
							} catch (Exception e) {
								// TODO: handle exception
								logger.error("i m in exception createResolutionSLAHistory: " + e.getMessage(),e);
								}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception occured in createResolutionSLAHistory: " + e.getMessage(),e);
		}

	}
	


	@Override
	public void completeResolutionSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW) {
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);

		
			try {

			if (Constant.INCIDENT_STATUS_CLOSED.equals(lMappedVariableInstanceLogVONEW.getStatus()))  {

				List<IncidentSLAHistoryEntity> lIncidentSLAHistoryEntityList = incidentSLAHRepo.findByIncidentIDAndSlaType(lMappedVariableInstanceLogVONEW.getIncidentID(),
							Constant.SLA_TARGET_RESOLUTIONSLA);
					
					for(IncidentSLAHistoryEntity lIncidentSLAHistoryEntity: lIncidentSLAHistoryEntityList)
					{
						if (lIncidentSLAHistoryEntity != null) {
							
							lIncidentSLAHistoryEntity.setSlaStatus(Constant.SLA_STATUS_COMPLETED);
							
							String lastUpdatedDate = sdf.format(new Date());
							lIncidentSLAHistoryEntity.setLastUpdatedDate(lastUpdatedDate);
							
							lIncidentSLAHistoryEntity.setSlaTimeRemaining(calculateSlaTimeRemaining(lIncidentSLAHistoryEntity.getSlaCompletionDate()));
							saveIncidentSLA(lIncidentSLAHistoryEntity);
							//logger.info("Status is changed as complete");
						} else {
							//logger.info("incident not found with resolution SLA type");
						}
					}
					

				}

		} catch (Exception e) {
			logger.error("Exception occured in completeResolutionSLAHistory: " + e.getMessage(),e);
		}
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelResolutionSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW) {
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);

				try {

			if (Constant.INCIDENT_STATUS_CANCELLED.equals(lMappedVariableInstanceLogVONEW.getStatus())) {

				List<IncidentSLAHistoryEntity> lIncidentSLAHistoryEntityList = incidentSLAHRepo.findByIncidentIDAndSlaType(lMappedVariableInstanceLogVONEW.getIncidentID(),Constant.SLA_TARGET_RESOLUTIONSLA);
				if (lIncidentSLAHistoryEntityList != null) {
					for (IncidentSLAHistoryEntity lIncidentSLAHistoryEntity : lIncidentSLAHistoryEntityList) {
						if (lIncidentSLAHistoryEntity != null
								&& !lIncidentSLAHistoryEntity.getSlaStatus().equals(Constant.SLA_STATUS_CANCELLED)) {

							lIncidentSLAHistoryEntity.setSlaStatus(Constant.SLA_STATUS_CANCELLED);

							String lastUpdatedDate = sdf.format(new Date());
							lIncidentSLAHistoryEntity.setLastUpdatedDate(lastUpdatedDate);
							lIncidentSLAHistoryEntity.setSlaTimeRemaining(
									calculateSlaTimeRemaining(lIncidentSLAHistoryEntity.getSlaCompletionDate()));
							saveIncidentSLA(lIncidentSLAHistoryEntity);
							//logger.info("Status is changed as Cancel");
						} else {
							//logger.info("incident not found with resolutionSLA type");
						}
					}
				}

			}else
			{
				/*
				 * List<IncidentSLAHistoryEntity> lIncidentSLAHistoryEntityList =
				 * incidentSLAHRepo
				 * .findByIncidentIDAndSlaType(lMappedVariableInstanceLogVONEW.getIncidentID(),
				 * Constant.SLA_TYPE_RESOLUTIONSLA); boolean slaReCreationRequired = true; for
				 * (IncidentSLAHistoryEntity lIncidentSLAHistoryEntity :
				 * lIncidentSLAHistoryEntityList) { if (lIncidentSLAHistoryEntity != null &&
				 * !lIncidentSLAHistoryEntity.getSlaStatus().equals(Constant.
				 * SLA_STATUS_CANCELLED)) { Long originalSLAConfigId =
				 * lIncidentSLAHistoryEntity.getSlaConfigId(); SLAConfigurationEntity
				 * originalSLAConfig = slaConfigRepo.findSlaById(originalSLAConfigId);
				 * SLAConfigurationEntity matchedSLAConfig = null; if (originalSLAConfig !=
				 * null) { matchedSLAConfig = matchSLAActions(originalSLAConfig,
				 * lMappedVariableInstanceLogVONEW); if (matchedSLAConfig != null) { // matched
				 * } else { slaReCreationRequired = false; // not matched // cancel the SLA
				 * lIncidentSLAHistoryEntity.setSlaStatus(Constant.SLA_STATUS_CANCELLED); String
				 * lastUpdatedDate = sdf.format(new Date());
				 * lIncidentSLAHistoryEntity.setLastUpdatedDate(lastUpdatedDate);
				 * lIncidentSLAHistoryEntity.setSlaTimeRemaining(calculateSlaTimeRemaining(
				 * lIncidentSLAHistoryEntity.getSlaCompletionDate()));
				 * saveIncidentSLA(lIncidentSLAHistoryEntity);
				 * //logger.info("Status is changed as Cancel");
				 * createResponceSLAHistory(lMappedVariableInstanceLogVONEW); } } } }
				 * 
				 */}

		} catch (Exception e) {
			logger.error("Exception occured in cancelResolutionSLAHistory " + e.getMessage(),e);
		}
		
	

		
	}

@Override
	public void pauseResolutionSLAHistory(MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW) {
	
	SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);


		try {
			List<IncidentSLAHistoryEntity> lIncidentSLAHistoryEntityList = incidentSLAHRepo.findByIncidentIDAndSlaType(lMappedVariableInstanceLogVONEW.getIncidentID(),Constant.SLA_TARGET_RESOLUTIONSLA);
			//logger.info("pauseResolutionSLAHistory i m here 1");
			if (Constant.INCIDENT_STATUS_ON_HOLD.equals(lMappedVariableInstanceLogVONEW.getStatus())
					|| Constant.INCIDENT_STATUS_RESOLVED.equals(lMappedVariableInstanceLogVONEW.getStatus())) {
				//logger.info("pauseResolutionSLAHistory i m here 2 ");
				if (lIncidentSLAHistoryEntityList != null) {
					
		
					for (IncidentSLAHistoryEntity lIncidentSLAHistoryEntity : lIncidentSLAHistoryEntityList) {
						
						//logger.info("lIncidentSLAHistoryEntity.getSlaStatus(): "+lIncidentSLAHistoryEntity.getSlaStatus());
						
						if (lIncidentSLAHistoryEntity != null
								&& !lIncidentSLAHistoryEntity.getSlaStatus().equals(Constant.SLA_STATUS_PAUSE)) {
							
							//logger.info("pauseResolutionSLAHistory i m here 2a");
							
							if(lIncidentSLAHistoryEntity.getSlaStatus().equals(Constant.SLA_STATUS_BREACHED)) {
								lIncidentSLAHistoryEntity.setSlaStatus(Constant.SLA_STATUS_BREACHED);
							}
							else{
								lIncidentSLAHistoryEntity.setSlaStatus(Constant.SLA_STATUS_PAUSE);
							}
							String lastUpdatedDate = sdf.format(new Date());
							lIncidentSLAHistoryEntity.setLastUpdatedDate(lastUpdatedDate);
							lIncidentSLAHistoryEntity.setSlaTimeRemaining(
									calculateSlaTimeRemaining(lIncidentSLAHistoryEntity.getSlaCompletionDate()));
							saveIncidentSLA(lIncidentSLAHistoryEntity);
							//logger.info("Status is changed as pause");
						}
					}
				}

			} else {
				// incident is not onhold / no resolved and not closed
				
				if(!Constant.INCIDENT_STATUS_CLOSED.equals(lMappedVariableInstanceLogVONEW.getStatus()))
				{

					if (lIncidentSLAHistoryEntityList != null) {
						for (IncidentSLAHistoryEntity lIncidentSLAHistoryEntity : lIncidentSLAHistoryEntityList) {
							//logger.info("lIncidentSLAHistoryEntity.getSlaStatus(): "+ lIncidentSLAHistoryEntity.getSlaStatus());
							if (lIncidentSLAHistoryEntity != null
									&& lIncidentSLAHistoryEntity.getSlaStatus().equals(Constant.SLA_STATUS_PAUSE)) {
								//logger.info("pauseResolutionSLAHistory i m here 2a");
								lIncidentSLAHistoryEntity.setSlaStatus(Constant.SLA_STATUS_RUNNING);
								String lastUpdatedDate = sdf.format(new Date());
								lIncidentSLAHistoryEntity.setLastUpdatedDate(lastUpdatedDate);
								lIncidentSLAHistoryEntity.setSlaTimeRemaining(
										calculateSlaTimeRemaining(lIncidentSLAHistoryEntity.getSlaCompletionDate()));
								saveIncidentSLA(lIncidentSLAHistoryEntity);
								//logger.info("pauseResolutionSLAHistory i m here 3");
								//logger.info("Status is changed as running");
							}
						}
					}

				}
			}

		} catch (Exception e) {
			logger.error("Exception occured in pauseResolutionSLAHistory: " + e.getMessage(),e);
		}
// TODO Auto-generated method stub

	}
}
