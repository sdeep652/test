package com.tcts.foresight.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcts.foresight.entity.IncidentStatusEntity;
import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.entity.problem.ProblemStatusEntity;
import com.tcts.foresight.pojo.MegaMenuDTO;
import com.tcts.foresight.pojo.Pair;
import com.tcts.foresight.pojo.ParentChildRelationDetails;
import com.tcts.foresight.repository.IncidentHistoryRepo;
import com.tcts.foresight.repository.MappedVariableInstanceRepo;
import com.tcts.foresight.service.ParentChildService;
import com.tcts.foresight.util.StringUtil;

@Service
public class ParentChildServiceImpl implements ParentChildService {

	Logger logger = LoggerFactory.getLogger(ParentChildServiceImpl.class);

	private static String PARENT_RELATION="Parent";
	private static String CHILD_RELATION="Child";
	String json;



	@Autowired
	private JbpmProcessServiceImpl jbpmProcessServiceImpl;

	@Autowired
	private IncidentCreationServiceImpl lIncidentCreationServiceImpl;

	@Autowired
	private MappedVariableInstanceRepo mappedVariableInstanceRepo;

	@Autowired
	private IncidentCreationServiceImpl incidentCreationServiceImpl;

	@Autowired
	IncidentHistoryRepo lIncidentHistoryRepo;


	public MappedVariableInstanceLogVO checkAndInsert(String authToken, String  containerId, String clientCode, MappedVariableInstanceLogVO lMappedVariableInstanceLogVO) {


		if (StringUtil.isNotNullNotEmpty(lMappedVariableInstanceLogVO.getParentTicketId())) {
			addParent(authToken, containerId, clientCode, lMappedVariableInstanceLogVO);
		}

		if (StringUtil.isNotNullNotEmpty(lMappedVariableInstanceLogVO.getChildTicketId())) {
			addChild(authToken, containerId, clientCode, lMappedVariableInstanceLogVO);
		}

		return lMappedVariableInstanceLogVO;

	}

	@Override
	public void addChild(String authToken, String  containerId, String clientCode, MappedVariableInstanceLogVO lParentMappedVariableInstanceLogVO1) {

		try {

			String childTicketId = lParentMappedVariableInstanceLogVO1.getChildTicketId();
			MappedVariableInstanceLogVO lParentMappedVariableInstanceLogVO = (MappedVariableInstanceLogVO)mappedVariableInstanceRepo.findByIncidentID(lParentMappedVariableInstanceLogVO1.getIncidentID());
			List<MappedVariableInstanceLogVO> updateList = new ArrayList<MappedVariableInstanceLogVO>();
			String child[]=childTicketId.split(",");
			for(int i=0;i<child.length;i++)
			{
				MappedVariableInstanceLogVO mapChild=(MappedVariableInstanceLogVO)mappedVariableInstanceRepo.findByIncidentID(child[i]).clone();
				mapChild.setLastUpdatedBy(lParentMappedVariableInstanceLogVO1.getLastUpdatedBy());
				mapChild.setParentTicketId(lParentMappedVariableInstanceLogVO.getIncidentID());
				updateList.add(mapChild);
			}

			for(MappedVariableInstanceLogVO lMappedVariableInstanceLogVO :updateList)
			{
				updateIncident(authToken, containerId, "", clientCode, lMappedVariableInstanceLogVO.getProcessInstanceId(), lMappedVariableInstanceLogVO, lParentMappedVariableInstanceLogVO);
			}

		}catch (Exception e) {
			logger.error("Exception occured in addChild: " + e.getMessage(), e);  
			e.printStackTrace();
		}
	}
	@Override
	public void addParent(String authToken, String  containerId, String clientCode, MappedVariableInstanceLogVO lMappedVariableInstanceLogVO) {
		String parent[]=lMappedVariableInstanceLogVO.getParentTicketId().split(",");
		for(int i=0;i<parent.length;i++)
		{
			try {
				MappedVariableInstanceLogVO mapParent=mappedVariableInstanceRepo.findByIncidentID(parent[i]);
				MappedVariableInstanceLogVO mapChild=(MappedVariableInstanceLogVO)mappedVariableInstanceRepo.findByIncidentID(lMappedVariableInstanceLogVO.getIncidentID()).clone();
				mapChild.setLastUpdatedBy(lMappedVariableInstanceLogVO.getLastUpdatedBy());
				mapChild.setParentTicketId(mapParent.getIncidentID());

				updateIncident(authToken, containerId, "", clientCode, mapChild.getProcessInstanceId(), mapChild, mapParent);
				break;
			}catch (Exception e) {
				logger.error("Exception occured in addParent" + e.getMessage(), e);  
				e.printStackTrace();
			}
		}	
	}


	private MappedVariableInstanceLogVO updateChildFromParentDetails(MappedVariableInstanceLogVO parentMappedVariableInstanceLogVO, MappedVariableInstanceLogVO childMappedVariableInstanceLogVO)
	{
		childMappedVariableInstanceLogVO.setStatus(parentMappedVariableInstanceLogVO.getStatus());
		childMappedVariableInstanceLogVO.setStatusRemark(parentMappedVariableInstanceLogVO.getStatusRemark());
		childMappedVariableInstanceLogVO.setResolutionMethod(parentMappedVariableInstanceLogVO.getResolutionMethod());
		childMappedVariableInstanceLogVO.setResolutionRemarks(parentMappedVariableInstanceLogVO.getResolutionRemarks());
		childMappedVariableInstanceLogVO.setResolutionType(parentMappedVariableInstanceLogVO.getResolutionType());
		childMappedVariableInstanceLogVO.setComments(parentMappedVariableInstanceLogVO.getComments());
		childMappedVariableInstanceLogVO.setAssignTo(parentMappedVariableInstanceLogVO.getAssignTo());
		childMappedVariableInstanceLogVO.setAssignmentGroup(parentMappedVariableInstanceLogVO.getAssignmentGroup());
		childMappedVariableInstanceLogVO.setResolvedBy(parentMappedVariableInstanceLogVO.getResolvedBy());
		return childMappedVariableInstanceLogVO;

	}





	@Override
	public List<ParentChildRelationDetails> fetchParentsChildRelationDetails(String incidentID) {
		List<ParentChildRelationDetails> list = fetchRelationDetails(incidentID);
		return list;
	}




	@Override
	public List<ParentChildRelationDetails> fetchRelationDetails(String incidentId) {

		List<ParentChildRelationDetails> childDetails = new ArrayList<ParentChildRelationDetails>();

		List<MappedVariableInstanceLogVO> childList = mappedVariableInstanceRepo.fetchChilds(incidentId);
		if(childList!=null && childList.size()>0)
		{
			for(MappedVariableInstanceLogVO childIncident: childList)
			{
				ParentChildRelationDetails lParentChildRelationDetails = new ParentChildRelationDetails();
				lParentChildRelationDetails.setIncidentId(childIncident.getIncidentID());
				lParentChildRelationDetails.setRelationType(CHILD_RELATION);
				lParentChildRelationDetails.setIncidentTitle(childIncident.getTitle());
				lParentChildRelationDetails.setIncidentCategory(childIncident.getCategory());
				lParentChildRelationDetails.setIncidentPriority(childIncident.getPriority());
				childDetails.add(lParentChildRelationDetails);
			}
		}


		MappedVariableInstanceLogVO currentIncidentRecord = mappedVariableInstanceRepo.findByIncidentID(incidentId);
		if(currentIncidentRecord!=null && StringUtil.isNotNullNotEmpty(currentIncidentRecord.getParentTicketId()))
		{

			MappedVariableInstanceLogVO parentIncident = mappedVariableInstanceRepo.findByticketID(currentIncidentRecord.getParentTicketId());

			if(parentIncident!=null)
			{
				ParentChildRelationDetails lParentChildRelationDetails = new ParentChildRelationDetails();
				lParentChildRelationDetails.setIncidentId(parentIncident.getIncidentID());
				lParentChildRelationDetails.setRelationType(PARENT_RELATION);
				MappedVariableInstanceLogVO parentVO = mappedVariableInstanceRepo.findByticketID(parentIncident.getIncidentID());
				lParentChildRelationDetails.setIncidentTitle(parentVO.getTitle());
				lParentChildRelationDetails.setIncidentCategory(parentVO.getCategory());
				lParentChildRelationDetails.setIncidentPriority(parentVO.getPriority());

				childDetails.add(lParentChildRelationDetails);
			}

		}


		return childDetails;
	}



	public void untagRelation(HashMap<String, String> body, String authToken, String containerId, String clientCode) {
		
		

		String type = body.get("type");
		String incidentId = body.get("incidentId"); // untag incident id
		String lastUpdatedBy = body.get("lastUpdatedBy");
		String currentIncidentId = body.get("currentIncidentId"); // current ticket id (incident which we are viewing on popup)

		if(type!=null && incidentId!=null && currentIncidentId!=null)
		{
			
			try {

				if (PARENT_RELATION.equals(type)) {
					MappedVariableInstanceLogVO mapChild = (MappedVariableInstanceLogVO) mappedVariableInstanceRepo.findByIncidentID(currentIncidentId).clone();
					mapChild.setParentTicketId(null);
					mapChild.setLastUpdatedBy(lastUpdatedBy);
					updateIncident(authToken, containerId, "", clientCode, mapChild.getProcessInstanceId(), mapChild, null);
//					mappedVariableInstanceRepo.save(mapChild);
				} else if (CHILD_RELATION.equals(type)) {

					List<MappedVariableInstanceLogVO> childList = mappedVariableInstanceRepo
							.fetchChilds(currentIncidentId);

					if (childList != null && childList.size() > 0)
						for (MappedVariableInstanceLogVO lMappedVariableInstanceLogVO : childList) {
							if (lMappedVariableInstanceLogVO != null
									&& lMappedVariableInstanceLogVO.getIncidentID() != null
									&& incidentId.equals(lMappedVariableInstanceLogVO.getIncidentID())) {
								
								MappedVariableInstanceLogVO lMappedVariableInstanceLogVOClone = (MappedVariableInstanceLogVO)lMappedVariableInstanceLogVO.clone();
								lMappedVariableInstanceLogVOClone.setParentTicketId(null);
								lMappedVariableInstanceLogVOClone.setLastUpdatedBy(lastUpdatedBy);
								updateIncident(authToken, containerId, "", clientCode, lMappedVariableInstanceLogVOClone.getProcessInstanceId(), lMappedVariableInstanceLogVOClone, null);
								
//								mappedVariableInstanceRepo.save(lMappedVariableInstanceLogVO);

							}
						}
				}

			}catch (Exception e) {
				// TODO: handle exception
			}
		}


	}


	private void updateIncident(String authToken, String containerId, String taskInstanceId,
			String clientCode, String processInstanceId, MappedVariableInstanceLogVO childMappedVariableInstaceLogVO, MappedVariableInstanceLogVO parentMappedVariableInstaceLogVO)
	{
		try {

			if(parentMappedVariableInstaceLogVO!=null)
			{
				childMappedVariableInstaceLogVO = updateChildFromParentDetails(parentMappedVariableInstaceLogVO,childMappedVariableInstaceLogVO);
			}
			
			String taskId = jbpmProcessServiceImpl.getTaskId(authToken,childMappedVariableInstaceLogVO.getProcessInstanceId());
			if(StringUtil.isNotNullNotEmpty(taskId))
			{
				JSONObject jobj = new JSONObject(taskId);
				JSONArray jArray = jobj.getJSONArray("task-summary");
				String taskInstanceIdNew = jArray.getJSONObject(0).optString("task-id");
				lIncidentCreationServiceImpl.completeTask(authToken, containerId, taskInstanceIdNew, clientCode,
						childMappedVariableInstaceLogVO.getProcessInstanceId(),
						childMappedVariableInstaceLogVO);
			}


		}catch (Exception e) {
			logger.error("Exception occured in updateIncident: " + e.getMessage(), e);  
			e.printStackTrace();
		}

	}

	// BELOW METHOD WILL BE CALLED BY INCIDENTCREATIONSERVICEIMPL.COMPLETETASK METHOD
	public void fetchAndUpdateAllChilds(String authToken, String containerId, String taskInstanceId,
			String clientCode, String processInstanceId, MappedVariableInstanceLogVO mappedVariableInstaceLogVO)
	{

		if(mappedVariableInstaceLogVO!=null && StringUtil.isNotNullNotEmpty(mappedVariableInstaceLogVO.getIncidentID()))
		{
			List<ParentChildRelationDetails> relationList = fetchRelationDetails(mappedVariableInstaceLogVO.getIncidentID());
			for(ParentChildRelationDetails relation: relationList)
			{
				try {
					if (relation != null && CHILD_RELATION.equals(relation.getRelationType())) {
						MappedVariableInstanceLogVO childMappedVariableInstanceLogVO = (MappedVariableInstanceLogVO)mappedVariableInstanceRepo.findByIncidentID(relation.getIncidentId()).clone();
						childMappedVariableInstanceLogVO.setLastUpdatedBy(mappedVariableInstaceLogVO.getLastUpdatedBy());
						updateIncident(authToken, containerId, taskInstanceId, clientCode, processInstanceId, childMappedVariableInstanceLogVO, mappedVariableInstaceLogVO);

					}
				} catch (Exception e) {
					logger.error("ERROR in fetchAndUpdateAllChilds:", e.getMessage(),e);
				}
			}

		}

	}
	private List<String> fetchAllChilds(String incidentID) {


		List<String> megaMenuList = new ArrayList<String>();
		ArrayList<Pair> pairs = new ArrayList<Pair>();
		List<String> childList= fetch(incidentID,pairs,megaMenuList);
		return childList;
	}

	private List<String> fetch(String incidentID, ArrayList<Pair> pairs, List<String> megaMenuList)
	{
		List<MappedVariableInstanceLogVO> pc= mappedVariableInstanceRepo.fetchChilds(incidentID);	

		for(MappedVariableInstanceLogVO p:pc) {

			pairs.add(new Pair( p.getIncidentID(),p.getParentTicketId()));

		}
		if(pc.size()!=0) {
			for(MappedVariableInstanceLogVO p:pc) 
			{
				fetch(p.getIncidentID(), pairs, megaMenuList);
			}
		}



		return pairsMapping(pairs, incidentID,megaMenuList);



	}





	public List<String> pairsMapping(List<Pair> pairs, String parentIncidentId ,List<String> megaMenuList){
		HashMap<String,MegaMenuDTO> hm = new HashMap<String,MegaMenuDTO>();


		// you are using MegaMenuDTO as Linked list with next and before link 

		// populate a Map
		for(Pair p:pairs){

			//  ----- Child -----
			MegaMenuDTO mmdChild ;
			if(hm.containsKey(p.getChildId())){
				mmdChild = hm.get(p.getChildId());
			}
			else{
				mmdChild = new MegaMenuDTO();
				hm.put(p.getChildId(),mmdChild);
			}           
			mmdChild.setId(p.getChildId());
			mmdChild.setParentId(p.getParentId());
			// no need to set ChildrenItems list because the constructor created a new empty list



			// ------ Parent ----
			MegaMenuDTO mmdParent ;
			if(hm.containsKey(p.getParentId())){
				mmdParent = hm.get(p.getParentId());
			}
			else{
				mmdParent = new MegaMenuDTO();
				hm.put(p.getParentId(),mmdParent);
			}
			mmdParent.setId(p.getParentId());
			mmdParent.setParentId("null");                              
			mmdParent.addChildrenItem(mmdChild);


		}

		// Get the root
		//		List<MegaMenuDTO> DX = new ArrayList<MegaMenuDTO>(); 
		//		for(MegaMenuDTO mmd : hm.values()){
		//			if(mmd.getParentId().equals("null"))
		//				DX.add(mmd);
		//		}


		//vaibhav
		List<MegaMenuDTO> DX = new ArrayList<MegaMenuDTO>(); 
		for(MegaMenuDTO mmd : hm.values()){

			if(mmd.getId().equals(parentIncidentId))
			{
				DX.add(mmd);
				break;
			}

		}

		return 	code(DX,megaMenuList);


	}

	private List<String> code(List<MegaMenuDTO> megaMenuList,List<String> childrenList) {
		for(MegaMenuDTO mDto:megaMenuList)
		{
			childrenList.add(mDto.getId());
		}

		return childrenList;
	}




	@Override
	public List<IncidentStatusEntity> getIncidentListForParentChild(String jsonPayLoadMap) {
		List<IncidentStatusEntity> incList = new ArrayList<IncidentStatusEntity>();
		List<MappedVariableInstanceLogVO> mappedList = new ArrayList<MappedVariableInstanceLogVO>();
		try {		
			mappedList = incidentCreationServiceImpl.filteredJobList(jsonPayLoadMap);
			for (MappedVariableInstanceLogVO m : mappedList) {
				IncidentStatusEntity incStatus = new IncidentStatusEntity();
				incStatus.setProcessInstanceId(m.getProcessInstanceId());
				incStatus.setIncidentId(m.getIncidentID());
				incList.add(incStatus);
			}

		} catch (Exception e) {
			logger.error("Exception occured in getIncidentListForParentChild " + e.getMessage(), e);  
			e.printStackTrace();
		}
		return incList;
	}

	
	public List<ProblemStatusEntity> getIncidentListForProblem(String jsonPayLoadMap) {
		List<ProblemStatusEntity> incList = new ArrayList<ProblemStatusEntity>();
		List<MappedVariableInstanceLogVO> mappedList = new ArrayList<MappedVariableInstanceLogVO>();
		try {		
			mappedList = incidentCreationServiceImpl.filteredJobList(jsonPayLoadMap);
			for (MappedVariableInstanceLogVO m : mappedList) {
				ProblemStatusEntity incStatus = new ProblemStatusEntity();
				incStatus.setProcessInstanceId(m.getProcessInstanceId());
				incStatus.setIncidentId(m.getIncidentID());
				incStatus.setPriority(m.getPriority());
				incStatus.setCategory(m.getCategory());
				
				incList.add(incStatus);
			}

		} catch (Exception e) {
			logger.error("Exception occured in getIncidentListForProblem " + e.getMessage(), e);  
			e.printStackTrace();
		}
		return incList;
	}





}

