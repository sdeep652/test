package com.tcts.foresight.scheduler.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.exception.ResourceNotFoundException;
import com.tcts.foresight.scheduler.entity.SLAConfigWorkFlow;
import com.tcts.foresight.scheduler.entity.SLAWorkFlowEntity;
import com.tcts.foresight.scheduler.repository.SLAWorkFlowRepo;
import com.tcts.foresight.scheduler.service.SLAWorkFlowService;
import com.tcts.foresight.util.StringUtil;

@Service
public class SLAWorkFlowServiceImpl implements SLAWorkFlowService {

	@Autowired
	SLAWorkFlowRepo slaWrkFlwRepo;

	@Override
	public SLAWorkFlowEntity createSLAWorkFlow(SLAWorkFlowEntity slaWorkFlowEntity) {
		List<SLAConfigWorkFlow> slaConfigWorkFlowNew = new ArrayList<SLAConfigWorkFlow>();
		if (slaWorkFlowEntity.getsLAConfigWorkFlow() != null && slaWorkFlowEntity.getsLAConfigWorkFlow().size() > 0) {

			List<SLAConfigWorkFlow> slaConfigWorkFlow = slaWorkFlowEntity.getsLAConfigWorkFlow();
			for (SLAConfigWorkFlow l : slaConfigWorkFlow) {
				if (l != null && StringUtil.isNotNullNotEmpty(l.getSlaConfigId())) {
					if (l.getSlaConfigId().contains(",")) {

						String[] slaConfigIdArray = (String[]) l.getSlaConfigId().split(",");
						for (int i = 0; i < slaConfigIdArray.length; i++) {
							SLAConfigWorkFlow lSLAConfigWorkFlowDhiraj = new SLAConfigWorkFlow();
							lSLAConfigWorkFlowDhiraj.setEmailList(l.getEmailList());
							lSLAConfigWorkFlowDhiraj.setPhoneNumberList(l.getPhoneNumberList());
							lSLAConfigWorkFlowDhiraj.setSlaElapsed(l.getSlaElapsed());
							lSLAConfigWorkFlowDhiraj.setSlaConfigId(slaConfigIdArray[i]);
							slaConfigWorkFlowNew.add(lSLAConfigWorkFlowDhiraj);

						}

					} else {
						slaConfigWorkFlowNew.add(l);
					}
				}
			}
		}
		slaWorkFlowEntity.setsLAConfigWorkFlow(slaConfigWorkFlowNew);
		SLAWorkFlowEntity sLAWorkFlowEntityobject = slaWrkFlwRepo.save(slaWorkFlowEntity);

		// ================================================================
		try {

			HashMap<Long, SLAConfigWorkFlow> map = new HashMap<Long, SLAConfigWorkFlow>();

			for (SLAConfigWorkFlow slaConfig : sLAWorkFlowEntityobject.getsLAConfigWorkFlow()) {

				if (map.containsKey(slaConfig.getSlaElapsed())) {
					SLAConfigWorkFlow lSLAConfigWorkFlowMap = map.get(slaConfig.getSlaElapsed());
					String lSlaConfigId = lSLAConfigWorkFlowMap.getSlaConfigId();
					lSLAConfigWorkFlowMap.setSlaConfigId(lSlaConfigId + "," + slaConfig.getSlaConfigId());
					map.put(slaConfig.getSlaElapsed(), lSLAConfigWorkFlowMap);

				} else {

					map.put(slaConfig.getSlaElapsed(), slaConfig);
				}
			}

			List<SLAConfigWorkFlow> temObjListVaibhav = new ArrayList<SLAConfigWorkFlow>();
			for (Long key : map.keySet()) {
				temObjListVaibhav.add(map.get(key));
			}

			sLAWorkFlowEntityobject.setsLAConfigWorkFlow(temObjListVaibhav);
			return sLAWorkFlowEntityobject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		// =========================================================================
	}

	@Override
	public SLAWorkFlowEntity updateworkFlow(SLAWorkFlowEntity slaWrkFlwEntity) throws ResourceNotFoundException {
		SLAWorkFlowEntity returnSlaWorkFlowObj = null;

		List<SLAConfigWorkFlow> slaConfigWorkFlowNew = new ArrayList<SLAConfigWorkFlow>();
		if (slaWrkFlwEntity.getsLAConfigWorkFlow() != null && slaWrkFlwEntity.getsLAConfigWorkFlow().size() > 0) {
			List<SLAConfigWorkFlow> slaConfigWorkFlow = slaWrkFlwEntity.getsLAConfigWorkFlow();
			for (SLAConfigWorkFlow l : slaConfigWorkFlow) {
				if (l != null && StringUtil.isNotNullNotEmpty(l.getSlaConfigId())) {
					if (l.getSlaConfigId().contains(",")) {
						String[] slaConfigIdArray = (String[]) l.getSlaConfigId().split(",");
						for (int i = 0; i < slaConfigIdArray.length; i++) {
							SLAConfigWorkFlow lSLAConfigWorkFlowDhiraj = new SLAConfigWorkFlow();
							lSLAConfigWorkFlowDhiraj.setEmailList(l.getEmailList());
							lSLAConfigWorkFlowDhiraj.setPhoneNumberList(l.getPhoneNumberList());
							lSLAConfigWorkFlowDhiraj.setSlaElapsed(l.getSlaElapsed());
							lSLAConfigWorkFlowDhiraj.setSlaConfigId(slaConfigIdArray[i]);

							slaConfigWorkFlowNew.add(lSLAConfigWorkFlowDhiraj);
						}

					} else {
						slaConfigWorkFlowNew.add(l);
					}
				}
			}
		}

		slaWrkFlwEntity.setsLAConfigWorkFlow(slaConfigWorkFlowNew);

		SLAWorkFlowEntity sLAWorkFlowEntityobject = slaWrkFlwRepo.save(slaWrkFlwEntity);
		slaWrkFlwRepo.deleteNullWorkflow();

		// ================================================================
		try {

			HashMap<Long, SLAConfigWorkFlow> map = new HashMap<Long, SLAConfigWorkFlow>();

			for (SLAConfigWorkFlow slaConfig : sLAWorkFlowEntityobject.getsLAConfigWorkFlow()) {

				if (map.containsKey(slaConfig.getSlaElapsed())) {
					SLAConfigWorkFlow lSLAConfigWorkFlowMap = map.get(slaConfig.getSlaElapsed());
					String lSlaConfigId = lSLAConfigWorkFlowMap.getSlaConfigId();
					lSLAConfigWorkFlowMap.setSlaConfigId(lSlaConfigId + "," + slaConfig.getSlaConfigId());
					map.put(slaConfig.getSlaElapsed(), lSLAConfigWorkFlowMap);

				} else {

					map.put(slaConfig.getSlaElapsed(), slaConfig);
				}
			}

			List<SLAConfigWorkFlow> temObjListVaibhav = new ArrayList<SLAConfigWorkFlow>();
			for (Long key : map.keySet()) {
				temObjListVaibhav.add(map.get(key));
			}

			sLAWorkFlowEntityobject.setsLAConfigWorkFlow(temObjListVaibhav);
			return sLAWorkFlowEntityobject;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		// =========================================================================

	}

	@Override
	public void deleteWorkflowById(Long workflowId) {
		slaWrkFlwRepo.deleteById(workflowId);
	}

	@Override
	public List<SLAWorkFlowEntity> fetchAllSlaWorkFlow() {
		return slaWrkFlwRepo.findAll();
	}

	@Override
	public List<SLAWorkFlowEntity> fetchSlaWorkFlowByModule(String module) {
		List<SLAWorkFlowEntity> slaWorkFLowEntity = new ArrayList<SLAWorkFlowEntity>();
		try {
			slaWorkFLowEntity = slaWrkFlwRepo.findByModule(module);
			SLAWorkFlowEntity slaNewWorkFlow = new SLAWorkFlowEntity();
			for (SLAWorkFlowEntity slaWorkFlow : slaWorkFLowEntity) {
				slaNewWorkFlow = fetchSingleSlaWorkFlow(slaWorkFlow.getWorkflowId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return slaWorkFLowEntity;
	}
	
	@Override
	public SLAWorkFlowEntity fetchSingleSlaWorkFlow(Long workflowid) {
		SLAWorkFlowEntity sLAWorkFlowEntity = new SLAWorkFlowEntity();
		try {
			sLAWorkFlowEntity = slaWrkFlwRepo.findByWorkflowId(workflowid);
			HashMap<Long, SLAConfigWorkFlow> map = new HashMap<Long, SLAConfigWorkFlow>();

			for (SLAConfigWorkFlow slaConfig : sLAWorkFlowEntity.getsLAConfigWorkFlow()) {

				if (map.containsKey(slaConfig.getSlaElapsed())) {
					SLAConfigWorkFlow lSLAConfigWorkFlowMap = map.get(slaConfig.getSlaElapsed());
					String lSlaConfigId = lSLAConfigWorkFlowMap.getSlaConfigId();
					lSLAConfigWorkFlowMap.setSlaConfigId(lSlaConfigId + "," + slaConfig.getSlaConfigId());
					map.put(slaConfig.getSlaElapsed(), lSLAConfigWorkFlowMap);

				} else {

					map.put(slaConfig.getSlaElapsed(), slaConfig);
				}
			}

			List<SLAConfigWorkFlow> temObjListVaibhav = new ArrayList<SLAConfigWorkFlow>();
			for (Long key : map.keySet()) {
				temObjListVaibhav.add(map.get(key));
			}

			sLAWorkFlowEntity.setsLAConfigWorkFlow(temObjListVaibhav);
			return sLAWorkFlowEntity;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String fetchSlaName(String slaName) {
		String name= slaWrkFlwRepo.findBySlaName(slaName);
		String stringToReturn=null;
		if(StringUtil.isNotNullNotEmpty(name) && name.equalsIgnoreCase(slaName)) {
			stringToReturn=name;
		}
		return stringToReturn;
	}
	
	public String removeDuplicatePhoneNo(String stringToSortDuplicates) {
		
		   String[] splitedString = stringToSortDuplicates.split(",");
	        LinkedHashSet<String> sortedStringlList= new LinkedHashSet<String>( Arrays.asList(splitedString));
	        StringBuilder sbTemp = new StringBuilder();
	        int index = 0;
	        
	        for(String str : sortedStringlList){
	            
	            if(index > 0)
	                sbTemp.append(",");
	            	sbTemp.append(str);
	            	index++;
	        }
	        String sortedString = sbTemp.toString();
		return sortedString;
		
	}
	
	public String removeDuplicateEmail(String stringToSortDuplicates) {
		
		   String[] splitedString = stringToSortDuplicates.split(";");
	        LinkedHashSet<String> sortedStringlList= new LinkedHashSet<String>( Arrays.asList(splitedString));
	        StringBuilder sbTemp = new StringBuilder();
	        int index = 0;
	        
	        for(String str : sortedStringlList){
	            
	            if(index > 0)
	                sbTemp.append(",");
	            	sbTemp.append(str);
	            	index++;
	        }
	        String sortedString = sbTemp.toString();
		return sortedString;
		
	}
	


}
