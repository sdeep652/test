package com.tcts.foresight.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tcts.foresight.entity.IncidentAttachmentEntity;
import com.tcts.foresight.entity.IncidentHistoryEntity;
import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.entity.problem.ProblemDetailsEntity;
import com.tcts.foresight.entity.problem.ProblemHistoryEntity;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import de.danielbechler.diff.node.Visit;

@Component
public class ObjectDiffUtil {
	
	Logger logger = LoggerFactory.getLogger(ObjectDiffUtil.class);
	
	private static ArrayList<String> ignoreList = new ArrayList<String>();
	static {
		ignoreList.add("atm");
		ignoreList.add("addToKb");
		ignoreList.add("isProblemReviewSubmitted");
		
	
	}

	private static ArrayList<String> ignoreCompositeList = new ArrayList<String>();
	static {
		ignoreCompositeList.add("incidentAttachList");
		ignoreCompositeList.add("problemAttachmentList");
		ignoreCompositeList.add("associatedIncidentList");
		
	}
		
	public List<IncidentHistoryEntity> checkDifference(MappedVariableInstanceLogVO lMappedVariableInstanceLogVOOLD, MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW)
	{
		
		final List<IncidentHistoryEntity> lIncidentHistoryEntityList = new ArrayList<IncidentHistoryEntity>();
//		  MappedVariableInstanceLogVO lMappedVariableInstanceLogVOOLDClass = (MappedVariableInstanceLogVO)lMappedVariableInstanceLogVOOLD;
//		  MappedVariableInstanceLogVO lMappedVariableInstanceLogVONewClass = (MappedVariableInstanceLogVO)lMappedVariableInstanceLogVONEW;
		 incidentAttachmentDifference(lIncidentHistoryEntityList, lMappedVariableInstanceLogVOOLD, lMappedVariableInstanceLogVONEW);
		DiffNode diff = ObjectDifferBuilder.buildDefault().compare(lMappedVariableInstanceLogVOOLD, lMappedVariableInstanceLogVONEW);
		  
		  if (diff.hasChanges()) {
			  
		      diff.visit(new DiffNode.Visitor() {
		          public void node(DiffNode node, Visit visit)
		          {
		        	  boolean proceed = true;
		        	  
		        	  if(node.getPath()!=null)
		        	  {
		        		  String path = node.getPath().toString();
		        		  
		        		  for(String ignoreItems: ignoreCompositeList)
		        		  {
		        			  if(path.contains(ignoreItems))
		        			  {
		        				
		        				  proceed =false;
		        			  }
		        		  }
		        	  }
		        	
		              if (!node.hasChildren() && proceed) { // Only print if the property has no child

		                  final String fieldName = (String)node.getPropertyName();
		                  
		                  //logger.info("checkDifference() Field Name {} "+fieldName);
		                  
		                  if(!ignoreList.contains(fieldName))
		                  {
		                	  final String oldValue = (String)node.canonicalGet(lMappedVariableInstanceLogVOOLD);
			                  final String newValue = (String)node.canonicalGet(lMappedVariableInstanceLogVONEW);
		                	  boolean doConsider = true;
		                	  
		                	  if(oldValue==null && (newValue!=null && newValue.trim().equals("") ))
		                	  {
		                		  doConsider = false;
		                	  }
		                	  if(oldValue==null && newValue==null)
		                	  {
		                		  doConsider = false;
		                	  }

		                	  if(doConsider)
		                	  {
		                		  final String message = node.getPropertyName() + " changed from " +
				                          oldValue + " to " + newValue;
				                
				                  IncidentHistoryEntity lIncidentHistoryEntity = new IncidentHistoryEntity();
				                  lIncidentHistoryEntity.setFieldName((String)fieldName);
				                  lIncidentHistoryEntity.setOldValue((String)oldValue);
				                  lIncidentHistoryEntity.setNewValue((String)newValue);
				                  lIncidentHistoryEntityList.add(lIncidentHistoryEntity);
		                	  }
		                	  
		                  
		                  }
		                  
		                  
		              }
		          }
		      });
		  } else {
		     
		  }
		return lIncidentHistoryEntityList;
	}
	
	public List<ProblemHistoryEntity> checkProblemDifference(ProblemDetailsEntity problemVariableInstanceLogVOOLD,
			ProblemDetailsEntity problemVariableInstanceLogVONEW) {

		final List<ProblemHistoryEntity> problemHistoryEntityList = new ArrayList<ProblemHistoryEntity>();
		System.out.println("Before Compare");
		//System.out.println("Attachment list of problemVariableInstanceLogVONEW "+problemVariableInstanceLogVONEW.getProblemAttachmentList());
		//System.out.println("Attachment list of problemVariableInstanceLogVOOLD "+problemVariableInstanceLogVOOLD.getProblemAttachmentList());
		
		problemVariableInstanceLogVOOLD.setProblemAttachmentList(null);
		ProblemDetailsEntity problemDetailsEntityClone = null;
		try {
			problemDetailsEntityClone = (ProblemDetailsEntity) problemVariableInstanceLogVONEW.clone();
			problemDetailsEntityClone.setProblemAttachmentList(null);

		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DiffNode diff = ObjectDifferBuilder.buildDefault().compare(problemVariableInstanceLogVOOLD,problemDetailsEntityClone);
		System.out.println("After Compare");
		if (diff.hasChanges()) {

			diff.visit(new DiffNode.Visitor() {
				public void node(DiffNode node, Visit visit) {
					boolean proceed = true;

					if (node.getPath() != null) {
						String path = node.getPath().toString();

						for (String ignoreItems : ignoreCompositeList) {
							System.out.println("path: "+path);
							System.out.println("ignoreItems"+ ignoreItems);
							if (path.contains(ignoreItems)) {
								proceed = false;
							}
							System.out.println("proceed: "+proceed);
							
						}
					}

					if (!node.hasChildren() && proceed) { // Only print if the property has no child

						final String fieldName = (String) node.getPropertyName();

						// logger.info("checkDifference() Field Name {} "+fieldName);

						if (!ignoreList.contains(fieldName)) {
							final String oldValue = (String) node.canonicalGet(problemVariableInstanceLogVOOLD);
							final String newValue = (String) node.canonicalGet(problemVariableInstanceLogVONEW);
							boolean doConsider = true;

							if (oldValue == null && (newValue != null && newValue.trim().equals(""))) {
								doConsider = false;
							}
							if (oldValue == null && newValue == null) {
								doConsider = false;
							}

							if (doConsider) {
								final String message = node.getPropertyName() + " changed from " + oldValue + " to "
										+ newValue;

								ProblemHistoryEntity problemHistoryEntity = new ProblemHistoryEntity();
								problemHistoryEntity.setFieldName((String) fieldName);
								problemHistoryEntity.setOldValue((String) oldValue);
								problemHistoryEntity.setNewValue((String) newValue);
								problemHistoryEntityList.add(problemHistoryEntity);
							}

						}

					}
				}
			});
		} else {

		}
		return problemHistoryEntityList;
	}
	
	private void incidentAttachmentDifference(List<IncidentHistoryEntity> lIncidentHistoryEntityList, MappedVariableInstanceLogVO lMappedVariableInstanceLogVOOLD, MappedVariableInstanceLogVO lMappedVariableInstanceLogVONEW)
	{
	
		HashMap<String, String> map = new HashMap<String, String>(); 
		if(lMappedVariableInstanceLogVOOLD.getIncidentAttachList()!=null && lMappedVariableInstanceLogVOOLD.getIncidentAttachList().size()>0)
		{
			for(IncidentAttachmentEntity lIncidentAttachmentEntity: lMappedVariableInstanceLogVOOLD.getIncidentAttachList())
			{
				map.put(lIncidentAttachmentEntity.getAttachmentName(), "Removed");
			}
		}
		
		if(lMappedVariableInstanceLogVONEW.getIncidentAttachList()!=null && lMappedVariableInstanceLogVONEW.getIncidentAttachList().size()>0)
		{
			for(IncidentAttachmentEntity lIncidentAttachmentEntity: lMappedVariableInstanceLogVONEW.getIncidentAttachList())
			{
				if(map.containsKey(lIncidentAttachmentEntity.getAttachmentName()))
				{
					map.put(lIncidentAttachmentEntity.getAttachmentName(), "Not Changed");
				}else{
					map.put(lIncidentAttachmentEntity.getAttachmentName(), "Newly Added");
					
				}
			}
		}
		
		for(String key: map.keySet())
		{
			String value = map.get(key);
			System.out.println("Attachement "+key +" is "+map.get(key));
			if(value.equals("Removed"))
			{
				IncidentHistoryEntity lIncidentHistoryEntity = new IncidentHistoryEntity();
	            lIncidentHistoryEntity.setFieldName("Attachment");
	            lIncidentHistoryEntity.setOldValue(key+" was present in DB");
	            lIncidentHistoryEntity.setNewValue(key+" is removed from DB");
	            lIncidentHistoryEntityList.add(lIncidentHistoryEntity);
			}else if(value.equals("Newly Added"))
			{
				IncidentHistoryEntity lIncidentHistoryEntity = new IncidentHistoryEntity();
	            lIncidentHistoryEntity.setFieldName("Attachment");
	            lIncidentHistoryEntity.setOldValue("");
	            lIncidentHistoryEntity.setNewValue(key+" is newly added");
	            lIncidentHistoryEntityList.add(lIncidentHistoryEntity);
			}
			
		}
		
		
	}

}
