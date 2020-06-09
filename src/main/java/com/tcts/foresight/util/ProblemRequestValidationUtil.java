package com.tcts.foresight.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcts.foresight.entity.ConfigEntityCached;
import com.tcts.foresight.entity.ErrorDetails;
import com.tcts.foresight.entity.ImpactDetailsEntity;
import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.entity.ResolutionTypeEntity;
import com.tcts.foresight.entity.SourceDetailsEntity;
import com.tcts.foresight.entity.StatusDetailsRemarkEntity;
import com.tcts.foresight.entity.UrgencyDetailsEntity;
import com.tcts.foresight.entity.problem.ProblemAttachmentEntity;
import com.tcts.foresight.entity.problem.ProblemDetailsEntity;
import com.tcts.foresight.entity.problem.ProblemIncidentEntity;
import com.tcts.foresight.entity.problem.ProblemRcaDetailsEntity;
import com.tcts.foresight.entity.problem.ProblemStatusDetailsEntity;
import com.tcts.foresight.repository.CategoryDetailsRepo;
import com.tcts.foresight.repository.GroupDetailsRepository;
import com.tcts.foresight.repository.GroupSummary;
import com.tcts.foresight.repository.ImpactDetailsRepo;
import com.tcts.foresight.repository.PriorityDetailRepo;
import com.tcts.foresight.repository.ResolutionTypeRepo;
import com.tcts.foresight.repository.SourceDetailsRepo;
import com.tcts.foresight.repository.StatusDetailsRemarkRepo;
import com.tcts.foresight.repository.SubCatDetailsRepo;
import com.tcts.foresight.repository.SubCatSummary;
import com.tcts.foresight.repository.UrgencyDetailsRepo;
import com.tcts.foresight.repository.UserDetailsSummary;
import com.tcts.foresight.repository.UserRepo;
import com.tcts.foresight.repository.problem.ProblemDetailsRepo;
import com.tcts.foresight.repository.problem.ProblemIncidentRepo;
import com.tcts.foresight.repository.problem.ProblemRcaRepo;
import com.tcts.foresight.repository.problem.ProblemStatusRepo;
import com.tcts.foresight.service.impl.IncidentCreationServiceImpl;
import com.tcts.foresight.service.impl.problem.ProblemManagementImpl;

@Component
public class ProblemRequestValidationUtil {

	@Autowired
	CategoryDetailsRepo categoryDetailsRepo;

	@Autowired
	SubCatDetailsRepo subCatDetailsRepo;

	@Autowired
	ImpactDetailsRepo impactDetailsRepo;

	@Autowired
	private UrgencyDetailsRepo urgencyDetailsRepo;

	@Autowired
	private PriorityDetailRepo priorityDetailRepo;

	@Autowired
	private GroupDetailsRepository groupDtlsRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private SourceDetailsRepo sourceDtlsRepo;

	@Autowired
	ProblemStatusRepo problemStatusRepo;

	@Autowired
	StatusDetailsRemarkRepo statusDetailsRemarkRepo;

	@Autowired
	ResolutionTypeRepo resolutionTypeRepo;

	@Autowired
	ProblemRcaRepo problemRcaRepo;

	@Autowired
	ProblemDetailsRepo problemDetailsRepo;

	@Autowired
	IncidentCreationServiceImpl incidentCreationServiceImpl;

	@Autowired
	ProblemIncidentRepo problemIncidentRepo;
	
	@Autowired
	private ProblemManagementImpl lProblemManagementImpl;

	@Autowired
	ConfigEntityCached lConfigEntityCached;
	/********************************************
	 * Create Problem Validation
	 ******************************************/

	public ErrorDetails createProblemRequest(String authToken, ProblemDetailsEntity jsonPayLoad) {

		ErrorDetails ed = null;

		ed = mandatoryAttributesPresentForCreateProblemRequest(jsonPayLoad);
		if (ed != null) {
			return ed;
		}

		ed = validateModule(jsonPayLoad.getModule());
		if (ed != null) {
			return ed;
		}

		ed = validateAuthToken(authToken, jsonPayLoad.getModule());
		if (ed != null) {
			return ed;
		}

		ed = validateCategory(jsonPayLoad.getCategory(), jsonPayLoad.getModule());
		if (ed != null) {
			return ed;
		}

		ed = validateSubCategory(jsonPayLoad.getCategory(), jsonPayLoad.getSubCategory(), jsonPayLoad.getModule());
		if (ed != null) {
			return ed;
		}

		ed = validateImpact(jsonPayLoad.getImpact());
		if (ed != null) {
			return ed;
		}

		ed = validateUrgency(jsonPayLoad.getUrgency());
		if (ed != null) {
			return ed;
		}

		ed = validatePriority(jsonPayLoad.getPriority(), jsonPayLoad.getImpact(), jsonPayLoad.getUrgency());
		if (ed != null) {
			return ed;
		}

		ed = validateAssignmentGroup(jsonPayLoad.getAssignmentGroup(), jsonPayLoad.getModule());
		if (ed != null) {
			return ed;
		}

		ed = validateAssignTo(jsonPayLoad.getAssignTo(), jsonPayLoad.getAssignmentGroup());
		if (ed != null) {
			return ed;
		}

		ed = validateSource(jsonPayLoad.getSource());
		if (ed != null) {
			return ed;
		}

		ed = validateCreatedBy(authToken, jsonPayLoad.getCreatedBy());
		if (ed != null) {
			return ed;
		}

		ed = validateCreatedByFullName(authToken, jsonPayLoad.getCreatedByFullName());
		if (ed != null) {
			return ed;
		}

		ed = validateTitle(jsonPayLoad.getTitle());
		if (ed != null) {
			return ed;
		}

		ed = validateDescription(jsonPayLoad.getDescriptions());
		if (ed != null) {
			return ed;
		}

		ed = validateSourceContact(jsonPayLoad.getSourceContact());
		if (ed != null) {
			return ed;
		}

		ed = validateConfigurationItem(jsonPayLoad.getConfigurationItem());
		if (ed != null) {
			return ed;
		}

		ed = validateComments(jsonPayLoad.getComments());
		if (ed != null) {
			return ed;
		}

		ed = validateMajorProblem(jsonPayLoad.getMarkAsMajorProblem());
		if (ed != null) {
			return ed;
		}

		ed = validateProblemAttachments(jsonPayLoad);
		if (ed != null) {
			return ed;
		}
		
		ed = validateProblemAttachmentSize(jsonPayLoad);
		if (ed != null) {
			return ed;
		}
		
		ed = validateAssociatedIncidents(authToken, jsonPayLoad);
		if (ed != null) {
			return ed;
		}
		return ed;
	}

	/********************************************
	 * Update Problem Validations
	 *******************************************/

	public ErrorDetails UpdateProblemRequest(String authToken, String problemID, ProblemDetailsEntity jsonPayLoad) {

		ErrorDetails ed = null;

		ed = validateProcessInstanceAndProblemID(problemID, jsonPayLoad);
		if (ed != null) {
			return ed;
		}

		ed = mandatoryAttributesPresentForUpdateProblemRequest(jsonPayLoad);
		if (ed != null) {
			return ed;
		}

		ed = validateModule(jsonPayLoad.getModule());
		if (ed != null) {
			return ed;
		}

		ed = validateAuthToken(authToken, jsonPayLoad.getModule());
		if (ed != null) {
			return ed;
		}

		ed = validateStatus(jsonPayLoad.getStatus());
		if (ed != null) {
			return ed;
		}

		ed = validateCategory(jsonPayLoad.getCategory(), jsonPayLoad.getModule());
		if (ed != null) {
			return ed;
		}

		ed = validateSubCategory(jsonPayLoad.getCategory(), jsonPayLoad.getSubCategory(), jsonPayLoad.getModule());
		if (ed != null) {
			return ed;
		}

		ed = validateImpact(jsonPayLoad.getImpact());
		if (ed != null) {
			return ed;
		}

		ed = validateUrgency(jsonPayLoad.getUrgency());
		if (ed != null) {
			return ed;
		}

		ed = validatePriority(jsonPayLoad.getPriority(), jsonPayLoad.getImpact(), jsonPayLoad.getUrgency());
		if (ed != null) {
			return ed;
		}

		ed = validateAssignmentGroup(jsonPayLoad.getAssignmentGroup(), jsonPayLoad.getModule());
		if (ed != null) {
			return ed;
		}

		ed = validateAssignTo(jsonPayLoad.getAssignTo(), jsonPayLoad.getAssignmentGroup());
		if (ed != null) {
			return ed;
		}

		ed = validateLastUpdatedBy(authToken, jsonPayLoad.getLastUpdatedBy());
		if (ed != null) {
			return ed;
		}

		if (jsonPayLoad.getStatus().equals(Constant.PROBLEM_STATUS_PENDING)
				|| jsonPayLoad.getStatus().equals(Constant.PROBLEM_STATUS_CANCELLED)) {
			ed = validateStatusRemark(jsonPayLoad.getStatus(), jsonPayLoad.getStatusRemark(), jsonPayLoad.getModule());
			if (ed != null) {
				return ed;
			}
		}

		if (jsonPayLoad.getStatus().equals(Constant.PROBLEM_STATUS_RESOLVED)) {
			ed = validateMandatoryResolvedAttributes(jsonPayLoad);
			if (ed != null) {
				return ed;
			}

			ed = validateResolutionType(jsonPayLoad.getResolutionType(), jsonPayLoad.getModule());
			if (ed != null) {
				return ed;
			}

			ed = validateRCA(jsonPayLoad.getRca());
			if (ed != null) {
				return ed;
			}

			ed = validateResolvedBy(authToken, jsonPayLoad.getResolvedBy());
			if (ed != null) {
				return ed;
			}

			if (jsonPayLoad.getMarkAsMajorProblem().equalsIgnoreCase("TRUE")) {
				ProblemDetailsEntity problemVariableInstanceLogVOOLD = null;

				problemVariableInstanceLogVOOLD = problemDetailsRepo.findByProblemID(jsonPayLoad.getProblemID());
				if (problemVariableInstanceLogVOOLD.getStatus().equals(jsonPayLoad.getStatus())
						&& problemVariableInstanceLogVOOLD.getMarkAsMajorProblem()
								.equalsIgnoreCase(jsonPayLoad.getMarkAsMajorProblem())) {
					ed = validateMandatoryProblemReview(jsonPayLoad);
					if (ed != null) {
						return ed;
					}

					ed = validateDoWell(jsonPayLoad.getMajorDoWell());
					if (ed != null) {
						return ed;
					}

					ed = validateBeenBetter(jsonPayLoad.getMajorBeenBetter());
					if (ed != null) {
						return ed;
					}

					ed = validatePreventReoccurrence(jsonPayLoad.getMajorPreventReoccurrence());
					if (ed != null) {
						return ed;
					}

					ed = validateDependencies(jsonPayLoad.getMajorDependencies());
					if (ed != null) {
						return ed;
					}
				}

			}

		}

		ed = validateTitle(jsonPayLoad.getTitle());
		if (ed != null) {
			return ed;
		}

		ed = validateDescription(jsonPayLoad.getDescriptions());
		if (ed != null) {
			return ed;
		}

		ed = validateSourceContact(jsonPayLoad.getSourceContact());
		if (ed != null) {
			return ed;
		}

		ed = validateComments(jsonPayLoad.getComments());
		if (ed != null) {
			return ed;
		}

		ed = validateConfigurationItem(jsonPayLoad.getConfigurationItem());
		if (ed != null) {
			return ed;
		}

		ed = validateAddtoKB(jsonPayLoad.getAddToKb());
		if (ed != null) {
			return ed;
		}
		
		ed = validateWorkAround(jsonPayLoad.getWorkAround());
		if (ed != null) {
			return ed;
		}
		
		ed = validateSaveToKEDB(jsonPayLoad.getAddKedb());
		if (ed != null) {
			return ed;
		}
		
		ed = validateBoardCastWorkAround(jsonPayLoad.getBroadcastWorkAround());
		if (ed != null) {
			return ed;
		}
		
		if("true".equalsIgnoreCase(jsonPayLoad.getAddKedb())) {
			System.out.println("validateMandantorySaveToKEDB");
			ed = validateMandantorySaveToKEDB(jsonPayLoad.getKnownErrorTitle(),jsonPayLoad.getKnownErrorDescription(),jsonPayLoad.getDescriptions());
			if (ed != null) {
				return ed;
			}
		}
		
		if("true".equalsIgnoreCase(jsonPayLoad.getBroadcastWorkAround())) {
			ed = validateMandatoryWorkAround(jsonPayLoad.getWorkAround());
			if (ed != null) {
				return ed;
			}
		}

		ed = validateProblemAttachments(jsonPayLoad);
		if (ed != null) {
			return ed;
		}
		
		ed = validateProblemAttachmentSize(jsonPayLoad);
		if (ed != null) {
			return ed;
		}
		
		ed = validateAssociatedIncidents(authToken, jsonPayLoad);
		if (ed != null) {
			return ed;
		}

		return null;
	}

	private ErrorDetails mandatoryAttributesPresentForUpdateProblemRequest(ProblemDetailsEntity jsonPayLoad) {
		ErrorDetails ed = null;
		if (jsonPayLoad != null) {
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getProblemID())) {

			} else {
				return new ErrorDetails("200", "Problem ID is missing", "Problem ID is missing");
			}
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getStatus())) {

			} else {
				return new ErrorDetails("200", "Status is missing", "Status is missing");
			}
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getCategory())) {

			} else {
				return new ErrorDetails("200", "Category is missing", "Category is missing");
			}
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getSubCategory())) {

			} else {
				return new ErrorDetails("200", "Sub-Category is missing", "Sub-Category is missing");
			}
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getTitle())) {

			} else {
				return new ErrorDetails("200", "Title is missing", "Title is missing");
			}
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getUrgency())) {

			} else {
				return new ErrorDetails("200", "Urgency is missing", "Urgency is missing");
			}
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getImpact())) {

			} else {
				return new ErrorDetails("200", "Impact is missing", "Impact is missing");
			}
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getPriority())) {

			} else {
				return new ErrorDetails("200", "Priority is missing", "Priority is missing");
			}
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getComments())) {

			} else {
				return new ErrorDetails("200", "Comments is missing", "Comments is missing");
			}
			if (jsonPayLoad.getStatus().equals("Cancelled") || jsonPayLoad.getStatus().equals("Pending")) {
				if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getStatusRemark())) {

				} else {
					return new ErrorDetails("200", "StatusRemark  is missing",
							"StatusRemark  is missing");
				}
			}

			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getLastUpdatedBy())) {

			} else {
				return new ErrorDetails("200", "Last Updated By  is missing",
						"Last Updated By  is missing");
			}
		}

		return ed;
	}

	private ErrorDetails validateMandatoryProblemReview(ProblemDetailsEntity jsonPayLoad) {

		if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getMajorDoWell())) {

		} else {
			return new ErrorDetails("200", "What did we do well  is missing",
					"What did we do well  is missing");
		}
		if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getMajorBeenBetter())) {

		} else {
			return new ErrorDetails("200", "What could have been better is missing",
					"What could have been better is missing");
		}
		if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getMajorPreventReoccurrence())) {

		} else {
			return new ErrorDetails("200", "How to pevent re-occurrence is missing",
					"How to pevent re-occurrence is missing");
		}
		if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getMajorDependencies())) {

		} else {
			return new ErrorDetails("200", "3rd party dependencies is missing",
					"3rd party dependencies is missing");
		}

		return null;

	}

	private ErrorDetails validateMandatoryResolvedAttributes(ProblemDetailsEntity jsonPayLoad) {
		if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getRca())) {

		} else {
			return new ErrorDetails("200", "RCA is missing", "RCA is missing");
		}
		if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getResolutionRemarks())) {

		} else {
			return new ErrorDetails("200", "Resolution Remarks is missing",
					"Resolution Remarks is missing");
		}
		if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getResolutionType())) {

		} else {
			return new ErrorDetails("200", "Resolution Type is missing",
					"Resolution Type is missing");
		}
		if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getResolvedBy())) {

		} else {
			return new ErrorDetails("200", "Resolved By is missing", "Resolved By is missing");
		}

		return null;
	}

	private ErrorDetails mandatoryAttributesPresentForCreateProblemRequest(ProblemDetailsEntity jsonPayLoad) {
		if (jsonPayLoad != null) {
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getCategory())) {

			} else {
				return new ErrorDetails("200", "Category is missing", "Category is missing");
			}
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getSubCategory())) {

			} else {
				return new ErrorDetails("200", "Sub-Category is missing", "Sub-Category is missing");
			}
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getTitle())) {

			} else {
				return new ErrorDetails("200", "Title is missing", "Title is missing");
			}
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getUrgency())) {

			} else {
				return new ErrorDetails("200", "Urgency is missing", "Urgency is missing");
			}
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getImpact())) {

			} else {
				return new ErrorDetails("200", "Impact is missing", "Impact is missing");
			}
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getPriority())) {

			} else {
				return new ErrorDetails("200", "Priority is missing", "Priority is missing");
			}
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getSource())) {

			} else {
				return new ErrorDetails("200", "Source is missing", "Source is missing");
			}
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getComments())) {

			} else {
				return new ErrorDetails("200", "Comments is missing", "Comments is missing");
			}
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getModule())) {

			} else {
				return new ErrorDetails("200", "Module is missing", "Module is missing");
			}
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getCreatedBy())) {

			} else {
				return new ErrorDetails("200", "CreatedBy is missing", "CreatedBy is missing");
			}
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getCreatedByFullName())) {

			} else {
				return new ErrorDetails("200", "CreatedByFullName is missing", "CreatedByFullName is missing");
			}

		}
		return null;
	}

	private ErrorDetails validateProcessInstanceAndProblemID(String problemID, ProblemDetailsEntity jsonPayLoad) {
		if (StringUtil.isNotNullNotEmpty(problemID)
				&& StringUtil.isNotNullNotEmpty(jsonPayLoad.getProcessInstanceId())) {
			ProblemDetailsEntity problemDetailsEntity = problemDetailsRepo.findByProblemID(problemID);
			if (problemDetailsEntity != null) {
				String processId = problemDetailsEntity.getProcessInstanceId();
				if (StringUtil.isNotNullNotEmpty(processId)) {
					if (processId.equalsIgnoreCase(jsonPayLoad.getProcessInstanceId())) {
						String pId = problemDetailsEntity.getProblemID();
						if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getProblemID())
								&& jsonPayLoad.getProblemID().equals(pId)) {

						} else {
							return new ErrorDetails("200", "Invalid Problem ID",
									"Invalid Problem ID");
						}
					} else {
						return new ErrorDetails("200", "processinstanceId is Wrong",
								"processinstanceId is Wrong");
					}
				} else {
					return new ErrorDetails("200", "Problem ID is Invalid",
							"Problem ID is Invalid");
				}
			} else {
				return new ErrorDetails("200", "Problem ID is Invalid",
						"Problem ID is Invalid");
			}

		} else {
			return new ErrorDetails("200", "processinstanceId is Missing",
					"processinstanceId is Missing");
		}
		return null;
	}

	private ErrorDetails validateAuthToken(String authToken, String module) {
		List<String> moduleList = userRepo.findModulebyUsername(userRepo.findByAuthToken(authToken).getEmail());
		if (moduleList.contains(module)) {

		} else {
			return new ErrorDetails("200", "Unauthorized User for this module",
					"Unauthorized User for this module");
		}
		return null;
	}

	private ErrorDetails validateAssociatedIncidents(String authToken, ProblemDetailsEntity jsonPayLoad)
	{
		if (jsonPayLoad.getAssociatedIncidentList() != null && jsonPayLoad.getAssociatedIncidentList().size() > 0)
		{
			for (ProblemIncidentEntity problemIncidentEntity : jsonPayLoad.getAssociatedIncidentList())
			{
				if (StringUtil.isNotNullNotEmpty(problemIncidentEntity.getIncidentID()))
				{
					// Do Nothing
				}
				else 
				{
					return new ErrorDetails("200", "Incident Id is missing", "Incident Id is missing");
				}
				if (StringUtil.isNotNullNotEmpty(problemIncidentEntity.getRelationType()) && problemIncidentEntity.getRelationType().equals("Associated Incident"))
				{
					// Do Nothing
				} 
				else 
				{
					return new ErrorDetails("200", " Relation Type is wrong or missing", "Relation Type is wrong or missing");
				}
			}
			
			HashMap<String, ErrorDetails> errorDetailsMap = new HashMap<String, ErrorDetails>();

			String groups = groupDtlsRepo.findGroupDetailByAuthToken(authToken).stream().distinct()
					.map(GroupSummary::getGroupName).collect(Collectors.joining(","));
			
			if (StringUtil.isNotNullNotEmpty(groups))
			{
				String payLoad = "{\"assignmentGroup\":" + "\"" + groups + "\"}";
				List<String> incidentList = new ArrayList<String>();
				
				incidentList = incidentCreationServiceImpl.filteredJobList(payLoad).stream()
						.map(MappedVariableInstanceLogVO::getIncidentID).collect(Collectors.toList());
				
				if (incidentList.size() > 0)
				{
					
					List<String> statusList = new ArrayList<String>(Arrays.asList(Constant.PROBLEM_STATUS_CLOSED, Constant.PROBLEM_STATUS_CANCELLED));
					
					List<String> activeProblemList = problemDetailsRepo.findByStatusNotIn(statusList).stream()
							.map(ProblemDetailsEntity::getProblemID).collect(Collectors.toList());
					
					for (ProblemIncidentEntity associatedIncList : jsonPayLoad.getAssociatedIncidentList())
					{
						if (incidentList.contains(associatedIncList.getIncidentID()))
						{
							
							if (!activeProblemList.isEmpty())
							{
								ProblemIncidentEntity problemIncidentEntity = problemIncidentRepo.findByIncidentID(associatedIncList.getIncidentID());
								
								if (problemIncidentEntity != null)
								{
									String problemId = problemIncidentEntity.getProblemID();
									
									if (StringUtil.isNullOrEmpty(jsonPayLoad.getProblemID()))
									{
										if (StringUtil.isNullOrEmpty(problemId)) 
										{
											// Do Nothing
										} 
										else 
										{
											if (errorDetailsMap.containsKey("errorMessage"))
											{
												ErrorDetails errorDetails = null;
												errorDetails = (ErrorDetails) errorDetailsMap.get("errorMessage");
												String errorMsg = errorDetails.getErrorReason();
												String finalErrorMsg = errorMsg + " , " + "Incident ID: "
														+ associatedIncList.getIncidentID()
														+ " is associated with Problem ID: " + problemId;
												errorDetailsMap.put("errorMessage", new ErrorDetails("200",
														finalErrorMsg,
														"Please disassociate incident Ticket(s) from problem Ticket(s) to continue"));
											} 
											else 
											{
												String errorMsg = "Incident ID: " + associatedIncList.getIncidentID()
														+ " is associated with Problem ID: " + problemId;
												errorDetailsMap.put("errorMessage", new ErrorDetails("200", errorMsg,
														"Please disassociate incident Ticket(s) from problem Ticket(s) to continue"));
											}
										}
									} 
									else if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getProblemID()))
									{
										if (StringUtil.isNullOrEmpty(problemId) || jsonPayLoad.getProblemID().equals(problemId))
										{
											// Do Nothing
										} 
										else
										{
											if (errorDetailsMap.containsKey("errorMessage"))
											{
												ErrorDetails errorDetails = null;
												errorDetails = (ErrorDetails) errorDetailsMap.get("errorMessage");
												String errorMsg = errorDetails.getErrorReason();
												String finalErrorMsg = errorMsg + " , " + "Incident ID: "
														+ associatedIncList.getIncidentID()
														+ " is associated with Problem ID: " + problemId;
												errorDetailsMap.put("errorMessage", new ErrorDetails("200",
														finalErrorMsg,
														"Please disassociate incident Ticket(s) from problem Ticket(s) to continue"));
											} 
											else
											{
												String errorMsg = "Incident ID: " + associatedIncList.getIncidentID()
														+ " is associated with Problem ID: " + problemId;
												errorDetailsMap.put("errorMessage", new ErrorDetails("200", errorMsg,
														"Please disassociate incident Ticket(s) from problem Ticket(s) to continue"));
											}
										}
									}
								}

							}

						} 
						else 
						{
							return new ErrorDetails("200","Incident ID: " + associatedIncList.getIncidentID() + " is Wrong","Please Check Value of incidentID JSON Key");
						}
					}
				}
				else
				{
					return new ErrorDetails("200", "No incidents found", "User's group does not have any incident");
				}
			} 
			else
			{
				return new ErrorDetails("200", "Invalid authToken", "No groups associated to this user");
			}
			if (errorDetailsMap.containsKey("errorMessage")) {
				return errorDetailsMap.get("errorMessage");
			}
		}
		return null;
	}

	private ErrorDetails validateModule(String module) {
		if (StringUtil.isNotNullNotEmpty(module) && module.equals("PM")) {

		} else {
			return new ErrorDetails("200", "Module is Wrong", "Module is Wrong");
		}
		return null;
	}

	private ErrorDetails validateStatus(String status) {
		List<String> checkStatusList = problemStatusRepo.findAll().stream()
				.filter(s -> !s.getStatus().equalsIgnoreCase("New") && !s.getStatus().equalsIgnoreCase("Closed"))
				.map(ProblemStatusDetailsEntity::getStatus).collect(Collectors.toList());
		if (checkStatusList.contains(status)) {

		} else {
			return new ErrorDetails("200", "Status is Wrong", "Status is Wrong");
		}
		return null;
	}

	private ErrorDetails validateCategory(String Category, String Module) {
		String cat = categoryDetailsRepo.findNameByModule(Category, Module);
		if (StringUtil.isNotNullNotEmpty(cat) && cat.equals(Category)) {

		} else {
			return new ErrorDetails("200", "Category is Wrong", "Category is Wrong");
		}
		return null;
	}

	private ErrorDetails validateSubCategory(String category, String subCategory, String module) {
		List<String> subCatList = subCatDetailsRepo
				.getSubCatByCatId(categoryDetailsRepo.findIdByName(category, module), module).stream()
				.map(SubCatSummary::getName).collect(Collectors.toList());
		if (subCatList.contains(subCategory)) {
		} else {
			return new ErrorDetails("200", "SubCategory is Wrong", "SubCategory is Wrong");
		}
		return null;
	}

	private ErrorDetails validateImpact(String impact) {
		List<String> impactList = impactDetailsRepo.findAll().stream().map(ImpactDetailsEntity::getName)
				.collect(Collectors.toList());
		if (StringUtil.isNotNullNotEmpty(impact) && impactList.contains(impact)) {

		} else {
			return new ErrorDetails("200", "Impact is Wrong", "Impact is Wrong");
		}
		return null;
	}

	private ErrorDetails validateUrgency(String urgency) {

		List<String> urgencyList = urgencyDetailsRepo.findAll().stream().map(UrgencyDetailsEntity::getName)
				.collect(Collectors.toList());
		if (StringUtil.isNotNullNotEmpty(urgency) && urgencyList.contains(urgency)) {

		} else {
			return new ErrorDetails("200", "Urgency is Wrong", "Urgency is Wrong");
		}
		return null;
	}

	private ErrorDetails validatePriority(String priority, String impact, String urgency) {
		String priorityCheck = priorityDetailRepo.findNameByImpactId_nameAndUrgencyId_name(impact, urgency).getName();
		if (StringUtil.isNotNullNotEmpty(priorityCheck) && priorityCheck.equals(priority)) {

		} else {
			return new ErrorDetails("200", "Priority is Wrong", "Priority is Wrong");
		}
		return null;
	}

	private ErrorDetails validateAssignmentGroup(String gName, String module) {
		if (StringUtil.isNotNullNotEmpty(gName)) {
			List<String> gList = groupDtlsRepo.findOnlyGroup(module).stream().map(GroupSummary::getGroupName)
					.collect(Collectors.toList());
			if (gList.contains(gName)) {

			} else {
				return new ErrorDetails("200", "Group name is Wrong", "Group name is Wrong");
			}
		}
		return null;
	}

	private ErrorDetails validateAssignTo(String assignTo, String gName) {
		String gId = null;
		if (StringUtil.isNotNullNotEmpty(gName) || StringUtil.isNullOrEmpty(assignTo)) {
			gId = groupDtlsRepo.findIDByGroupName(gName);
			List<String> aasignList = userRepo.findUserByGroupList_IdAndGroupList_GroupName(gId, gName).stream()
					.map(UserDetailsSummary::getEmail).collect(Collectors.toList());
			if (StringUtil.isNotNullNotEmpty(assignTo)) {
				if (aasignList.contains(assignTo)) {

				} else {
					return new ErrorDetails("200", "Assign To User is Wrong",
							"Assign To User is Wrong");
				}
			}
		} else {
			return new ErrorDetails("200", "Cannot Assign User Without Group",
					"Cannot Assign User Without Group");
		}
		return null;
	}

	private ErrorDetails validateSource(String source) {
		if (StringUtil.isNotNullNotEmpty(source)) {
			List<String> sList = sourceDtlsRepo.findAll().stream().map(SourceDetailsEntity::getName)
					.collect(Collectors.toList());
			if (sList.contains(source)) {

			} else {
				return new ErrorDetails("200", "Source is Wrong", "Source is Wrong");
			}
		}
		return null;
	}

	private ErrorDetails validateCreatedByFullName(String authToken, String createdByFullName) {
		if (StringUtil.isNotNullNotEmpty(createdByFullName)) {
			String fullName = userRepo.findByAuthToken(authToken).getFullName();
			if (createdByFullName.equals(fullName)) {

			} else {
				return new ErrorDetails("200", "Created By Full Name is Wrong",
						"Created By Full Name is Wrong");
			}
		}
		return null;
	}

	private ErrorDetails validateResolvedBy(String authToken, String resolvedBy) {
		if (StringUtil.isNotNullNotEmpty(resolvedBy)) {
			String fullName = userRepo.findByAuthToken(authToken).getFullName();
			if (resolvedBy.equals(fullName)) {

			} else {
				return new ErrorDetails("200", "Resolved By Full Name is Wrong",
						"Resolved By Full Name is Wrong");
			}
		}
		return null;
	}

	private ErrorDetails validateLastUpdatedBy(String authToken, String lastUpdatedBy) {
		if (StringUtil.isNotNullNotEmpty(lastUpdatedBy)) {
			String fullName = userRepo.findByAuthToken(authToken).getFullName();
			if (lastUpdatedBy.equals(fullName)) {

			} else {
				return new ErrorDetails("200", "Last Updated By Full Name is Wrong",
						"Last Updated By Full Name is Wrong");
			}
		}
		return null;
	}

	private ErrorDetails validateCreatedBy(String authtoken, String createdBy) {
		if (StringUtil.isNotNullNotEmpty(createdBy)) {
			String userName = userRepo.findByAuthToken(authtoken).getEmail();
			if (createdBy.equals(userName)) {

			} else {
				return new ErrorDetails("200", "Created By is Wrong", "Created By is Wrong");
			}
		}
		return null;
	}

	private ErrorDetails validateStatusRemark(String status, String statusRemark, String module) {
		if (StringUtil.isNotNullNotEmpty(statusRemark)) {
			StatusDetailsRemarkEntity statusDetailsRemarkEntity = null;
			statusDetailsRemarkEntity = statusDetailsRemarkRepo.findStatusRemarkByModule(status, statusRemark, module);
			if (statusDetailsRemarkEntity != null) {
			} else {
				return new ErrorDetails("200", "Status Remark is Wrong", "Status Remark is Wrong");
			}
		}
		return null;
	}

	private ErrorDetails validateResolutionType(String resolutionType, String module) {
		if (StringUtil.isNotNullNotEmpty(resolutionType)) {
			List<String> resolutionTypeList = resolutionTypeRepo.findAll().stream()
					.filter(name -> name.getModule().equalsIgnoreCase(module))
					.map(ResolutionTypeEntity::getResolutiontype).collect(Collectors.toList());
			if (resolutionTypeList.contains(resolutionType)) {
			} else {
				return new ErrorDetails("200", "Resolution Type is Wrong",
						"Resolution Type is Wrong");
			}
		}
		return null;
	}

	private ErrorDetails validateRCA(String rca) {
		if (StringUtil.isNotNullNotEmpty(rca)) {
			List<String> rcaList = problemRcaRepo.findAll().stream().map(ProblemRcaDetailsEntity::getRca)
					.collect(Collectors.toList());
			if (rcaList.contains(rca)) {
			} else {
				return new ErrorDetails("200", "RCA is Wrong", "RCA is Wrong");
			}
		}
		return null;
	}

	private ErrorDetails validateMajorProblem(String majorProblem) {
		if (StringUtil.isNotNullNotEmpty(majorProblem)) {

			if (majorProblem.equalsIgnoreCase("TRUE") || majorProblem.equalsIgnoreCase("FALSE")) {

			} else {
				return new ErrorDetails("200", "Mark as Major Problem entered value is wrong",
						"Mark as Major Problem entered value is wrong");
			}

		}

		return null;
	}

	private ErrorDetails validateAddtoKB(String addtokb) {
		if (StringUtil.isNotNullNotEmpty(addtokb)) {

			if (addtokb.equalsIgnoreCase("TRUE") || addtokb.equalsIgnoreCase("FALSE")) {

			} else {
				return new ErrorDetails("200", "Add to kb entered value is wrong",
						"Add to kb entered value is wrong");
			}

		}

		return null;
	}
	
	private ErrorDetails validateProblemAttachments(ProblemDetailsEntity jsonPayLoad)
	{
		if (jsonPayLoad.getProblemAttachmentList() != null && jsonPayLoad.getProblemAttachmentList().size() > 0)
		{
			for (ProblemAttachmentEntity problemAttachmentEntity : jsonPayLoad.getProblemAttachmentList())
			{
				if (StringUtil.isNotNullNotEmpty(problemAttachmentEntity.getAttachment()))
				{
					// Do Nothing
				}
				else 
				{
					return new ErrorDetails("200", "Attachment details is missing", "Attachment details is missing");
				}
				if (StringUtil.isNotNullNotEmpty(problemAttachmentEntity.getAttachmentName()))
				{
					// Do Nothing
				} 
				else 
				{
					return new ErrorDetails("200", " Attachment Name is missing", "Attachment Name is missing");
				}
			}
			
		}
		return null;
	}

	private ErrorDetails validateTitle(String title) {
		if (StringUtil.isNotNullNotEmpty(title)) {
			int length = Constant.LENGTH_250;
			int actualLength = (int) title.length();
			if (length >= actualLength) {

			} else {
				return new ErrorDetails("200", "Title Length is Greater than 250",
						"Title Length is Greater than 250");
			}
		}
		return null;

	}

	private ErrorDetails validateDescription(String descriptions) {
		if (StringUtil.isNotNullNotEmpty(descriptions)) {
			int length = Constant.LENGTH_5000;
			int actualLength = (int) descriptions.length();
			if (length >= actualLength) {

			} else {
				return new ErrorDetails("200", "Descriptions Length is Greater than 5000",
						"Descriptions Length is Greater than 5000");
			}
		}
		return null;

	}

	private ErrorDetails validateSourceContact(String sourceContact) {
		if (StringUtil.isNotNullNotEmpty(sourceContact)) {
			int length = Constant.LENGTH_250;
			int actualLength = (int) sourceContact.length();
			if (length >= actualLength) {

			} else {
				return new ErrorDetails("200", "Source Contact Length is Greater than 5000",
						"Source Contact Length is Greater than 5000");
			}
		}
		return null;

	}

	private ErrorDetails validateComments(String comments) {
		if (StringUtil.isNotNullNotEmpty(comments)) {
			int length = Constant.LENGTH_5000;
			int actualLength = (int) comments.length();
			if (length >= actualLength) {

			} else {
				return new ErrorDetails("200", "Comments Length is Greater than 5000",
						"Comments Length is Greater than 5000");
			}
		}
		return null;

	}

	private ErrorDetails validateConfigurationItem(String configurationItem) {
		if (StringUtil.isNotNullNotEmpty(configurationItem)) {
			int length = Constant.LENGTH_250;
			int actualLength = (int) configurationItem.length();
			if (length >= actualLength) {

			} else {
				return new ErrorDetails("200", "Configuration Item Length is Greater than 5000",
						"Configuration Item Length is Greater than 5000");
			}
		}
		return null;

	}

	private ErrorDetails validateDoWell(String doWell) {
		if (StringUtil.isNotNullNotEmpty(doWell)) {
			int length = Constant.LENGTH_1000;
			int actualLength = (int) doWell.length();
			if (length >= actualLength) {

			} else {
				return new ErrorDetails("200", "What did we do well Length is Greater than 1000",
						"What did we do well Length is Greater than 1000");
			}
		}
		return null;

	}

	private ErrorDetails validateBeenBetter(String beenBetter) {
		if (StringUtil.isNotNullNotEmpty(beenBetter)) {
			int length = Constant.LENGTH_1000;
			int actualLength = (int) beenBetter.length();
			if (length >= actualLength) {

			} else {
				return new ErrorDetails("200", "What could have been better Length is Greater than 1000",
						"What could have been better Length is Greater than 1000");
			}
		}
		return null;

	}

	private ErrorDetails validatePreventReoccurrence(String preventReoccurrence) {
		if (StringUtil.isNotNullNotEmpty(preventReoccurrence)) {
			int length = Constant.LENGTH_1000;
			int actualLength = (int) preventReoccurrence.length();
			if (length >= actualLength) {

			} else {
				return new ErrorDetails("200", "How to pevent re-occurrence Length is Greater than 1000",
						"How to pevent re-occurrence Length is Greater than 1000");
			}
		}
		return null;

	}

	private ErrorDetails validateDependencies(String dependencies) {
		if (StringUtil.isNotNullNotEmpty(dependencies)) {
			int length = Constant.LENGTH_1000;
			int actualLength = (int) dependencies.length();
			if (length >= actualLength) {

			} else {
				return new ErrorDetails("200", "3rd party dependencies Length is Greater than 1000",
						"3rd party dependencies Length is Greater than 1000");
			}
		}
		return null;

	}
	

	public ErrorDetails validateWorkAround(String workaround) {
		if (StringUtil.isNotNullNotEmpty(workaround)) {
			int length = Constant.LENGTH_5000;
			int actualLength = (int) workaround.length();
			if (length >= actualLength) {

			} else {
				return new ErrorDetails("200", "Work Around Length is Greater than 5000",
						"Work Around Length is Greater than 5000");
			}
		}
		return null;

	}
	
	private ErrorDetails validateProblemAttachmentSize(ProblemDetailsEntity jsonPayLoad) {

		List<ProblemAttachmentEntity> attachmentList = new ArrayList<ProblemAttachmentEntity>();
		try {
			if (jsonPayLoad.getProblemID() != null) {

				attachmentList = lProblemManagementImpl.fetchProblemAttchmentList(jsonPayLoad.getProblemID());

				if (attachmentList != null) {
					if (jsonPayLoad.getProblemAttachmentList() != null) {
						attachmentList.addAll(jsonPayLoad.getProblemAttachmentList());

					} else {

					}

					/*
					 * for (ProblemAttachmentEntity problemAttachmentEntity :
					 * jsonPayLoad.getProblemAttachmentList()) { String attachment = new
					 * String(problemAttachmentEntity.getAttachment());
					 * problemAttachmentEntity.setAttachment(attachment);
					 * attachmentList.add(problemAttachmentEntity); }
					 */

				} else {
					if (jsonPayLoad.getProblemAttachmentList() != null) {
						attachmentList.addAll(jsonPayLoad.getProblemAttachmentList());

					}
				}
			} else {
				if (jsonPayLoad.getProblemAttachmentList() != null) {
					attachmentList.addAll(jsonPayLoad.getProblemAttachmentList());

				}
			}

			long sizeInKb = 0L;
			for (ProblemAttachmentEntity AttachmentObject : attachmentList) {

				String[] splitedString = AttachmentObject.getAttachment().split(",");
				String base64String = AttachmentObject.getAttachment();
				int splitedStringLength = splitedString[0].length() + 1;
				long stringLength = base64String.length() - splitedStringLength;
				long sizeInBytes = (long) (4 * Math.ceil((stringLength / 3)) * 0.5624896334383812);
				sizeInKb = sizeInKb + sizeInBytes / 1000;

			}
			long defaultSize = Long.parseLong(lConfigEntityCached.getValue("total.attachment.size.kb"));
			if (sizeInKb > defaultSize) {
				return new ErrorDetails("200", "Attachment size is Greater than " + defaultSize / 1000 + "MB",
						"Attachment size is Greater than " + defaultSize / 1000 + "MB");

			}
		} catch (Exception e) {

		}

		return null;

	}
	
	private ErrorDetails validateBoardCastWorkAround(String BoardCastWorkAround) {
		if (StringUtil.isNotNullNotEmpty(BoardCastWorkAround)) {

			if (BoardCastWorkAround.equalsIgnoreCase("TRUE") || BoardCastWorkAround.equalsIgnoreCase("FALSE")) {

			} else {
				return new ErrorDetails("200", "BoardCastWorkAround entered value is wrong",
						"BoardCastWorkAround entered value is wrong");
			}

		}

		return null;
	}
	
	private ErrorDetails validateSaveToKEDB(String saveToKEDB) {
		if (StringUtil.isNotNullNotEmpty(saveToKEDB)) {

			if (saveToKEDB.equalsIgnoreCase("TRUE") || saveToKEDB.equalsIgnoreCase("FALSE")) {

			} else {
				return new ErrorDetails("200", "Save To KEDB entered value is wrong",
						"Save To KEDB entered value is wrong");
			}

		}

		return null;
	}
	
	private ErrorDetails validateMandantorySaveToKEDB(String title, String description, String workAround) {

		if (StringUtil.isNotNullNotEmpty(title)) {

		} else {
			return new ErrorDetails("200", "Known Error Title is missing", "Known Error Title is missing");
		}
		if (StringUtil.isNotNullNotEmpty(description)) {

		} else {
			return new ErrorDetails("200", "Known Error Description is missing", "Known Error Description is missing");
		}
		ErrorDetails workAroundCheck = validateMandatoryWorkAround(workAround);
		if(workAroundCheck != null) {
			return workAroundCheck;
		}

		return null;

	}

	private ErrorDetails validateMandatoryWorkAround(String workAround) {
		if (StringUtil.isNotNullNotEmpty(workAround)) {

		} else {
			return new ErrorDetails("200", "Work Around is missing", "Work Around is missing");
		}
		return null;
	}

}
