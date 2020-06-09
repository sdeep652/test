package com.tcts.foresight.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcts.foresight.entity.ConfigEntityCached;
import com.tcts.foresight.entity.ErrorDetails;
import com.tcts.foresight.entity.ImpactDetailsEntity;
import com.tcts.foresight.entity.IncidentAttachmentEntity;
import com.tcts.foresight.entity.MappedVariableInstanceLogVO;
import com.tcts.foresight.entity.ResolutionMethodEntity;
import com.tcts.foresight.entity.ResolutionTypeEntity;
import com.tcts.foresight.entity.SourceDetailsEntity;
import com.tcts.foresight.entity.StatusDetailsEntity;
import com.tcts.foresight.entity.StatusDetailsRemarkEntity;
import com.tcts.foresight.entity.UrgencyDetailsEntity;
import com.tcts.foresight.entity.problem.ProblemAttachmentEntity;
import com.tcts.foresight.entity.problem.ProblemDetailsEntity;
import com.tcts.foresight.repository.CategoryDetailsRepo;
import com.tcts.foresight.repository.GroupDetailsRepository;
import com.tcts.foresight.repository.GroupSummary;
import com.tcts.foresight.repository.ImpactDetailsRepo;
import com.tcts.foresight.repository.MappedVariableInstanceRepo;
import com.tcts.foresight.repository.PriorityDetailRepo;
import com.tcts.foresight.repository.ResolutionMethodRepo;
import com.tcts.foresight.repository.ResolutionTypeRepo;
import com.tcts.foresight.repository.SourceDetailsRepo;
import com.tcts.foresight.repository.StatusDetailsRemarkRepo;
import com.tcts.foresight.repository.StatusDetailsRepo;
import com.tcts.foresight.repository.SubCatDetailsRepo;
import com.tcts.foresight.repository.SubCatSummary;
import com.tcts.foresight.repository.UrgencyDetailsRepo;
import com.tcts.foresight.repository.UserDetailsSummary;
import com.tcts.foresight.repository.UserRepo;
import com.tcts.foresight.service.impl.IncidentCreationServiceImpl;

@Component
public class IncidentRequestValidation {

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
	private SourceDetailsRepo sourceDtlsRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private GroupDetailsRepository groupDtlsRepo;
	
	@Autowired
	MappedVariableInstanceRepo mappedVariableInstanceRepo;
	
	@Autowired
	StatusDetailsRemarkRepo statusDetailsRemarkRepo;
	
	@Autowired
	ResolutionTypeRepo resolutionTypeRepo;
	
	@Autowired
	StatusDetailsRepo statusDetailsRepo;
	
	@Autowired
	ResolutionMethodRepo resolutionMethodRepo;
	
	@Autowired
	IncidentCreationServiceImpl lIncidentCreationServiceImpl;
	
	@Autowired
	ConfigEntityCached lConfigEntityCached;

	public ErrorDetails createIncidentRequest(String authToken, MappedVariableInstanceLogVO jsonPayLoad) {

		ErrorDetails ed = null;

		ed = mandatoryAttributesPresentForCreateIncidentRequest(jsonPayLoad);
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

		ed = validateTitle(jsonPayLoad.getTitle());
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

		ed = validateDescription(jsonPayLoad.getDescriptions());
		if (ed != null) {
			return ed;
		}

		ed = validateSource(jsonPayLoad.getSource());
		if (ed != null) {
			return ed;
		}

		ed = validateSourceContact(jsonPayLoad.getSourceContact());
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

		ed = validateMajorIncident(jsonPayLoad.getMarkAsMajorIncident());
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

		ed = validateIncidentType(jsonPayLoad.getIncidentType());
		if (ed != null) {
			return ed;
		}

		ed = validateNotification(jsonPayLoad.getStatusChangeCheckbox(), jsonPayLoad.getFeedbackCheckbox(),
				jsonPayLoad.getEmail(), jsonPayLoad.getSms());
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

		ed = validateIncidentAttachments(jsonPayLoad);
		if (ed != null) {
			return ed;
		}

		ed = validateIncidentAttachmentSize(jsonPayLoad);
		if (ed != null) {
			return ed;
		}

		return null;
	}
	
	public ErrorDetails UpdateIncidentRequest(String authToken, String incidentID,
			MappedVariableInstanceLogVO jsonPayLoad) {

		ErrorDetails ed = null;

		ed = validateProcessInstanceAndIncidentID(incidentID, jsonPayLoad);
		if (ed != null) {
			return ed;
		}

		ed = mandatoryAttributesPresentForUpdateIncidentRequest(jsonPayLoad);
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

		ed = validateSource(jsonPayLoad.getSource());
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

		if (jsonPayLoad.getStatus().equals(Constant.INCIDENT_STATUS_ON_HOLD)
				|| jsonPayLoad.getStatus().equals(Constant.INCIDENT_STATUS_CANCELLED)) {
			ed = validateStatusRemark(jsonPayLoad.getStatus(), jsonPayLoad.getStatusRemark(), jsonPayLoad.getModule());
			if (ed != null) {
				return ed;
			}
		}

		if (jsonPayLoad.getStatus().equals(Constant.INCIDENT_STATUS_RESOLVED)) {
			ed = validateMandatoryResolvedAttributes(jsonPayLoad);
			if (ed != null) {
				return ed;
			}

			ed = validateResolutionType(jsonPayLoad.getResolutionType(), jsonPayLoad.getModule());
			if (ed != null) {
				return ed;
			}

			ed = validateResolutionMethod(jsonPayLoad.getResolutionMethod(), jsonPayLoad.getModule());
			if (ed != null) {
				return ed;
			}
			
			ed = validateResolutionRemarks(jsonPayLoad.getResolutionRemarks());
			if (ed != null) {
				return ed;
			}

			ed = validateResolvedBy(authToken, jsonPayLoad.getResolvedBy());
			if (ed != null) {
				return ed;
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

		ed = validateIncidentType(jsonPayLoad.getIncidentType());
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

		ed = validateWorkAround(jsonPayLoad.getIncidentID(), jsonPayLoad.getWorkAround());
		if (ed != null) {
			return ed;
		}
		ed = validateParentID(jsonPayLoad.getIncidentID(), jsonPayLoad.getParentTicketId());
		if (ed != null) {
			return ed;
		}

		ed = validateIncidentAttachments(jsonPayLoad);
		if (ed != null) {
			return ed;
		}

		ed = validateIncidentAttachmentSize(jsonPayLoad);
		if (ed != null) {
			return ed;
		}

		ed = validateNotification(jsonPayLoad.getStatusChangeCheckbox(), jsonPayLoad.getFeedbackCheckbox(),
				jsonPayLoad.getEmail(), jsonPayLoad.getSms());
		if (ed != null) {
			return ed;
		}

		return null;
	}

	

	private ErrorDetails mandatoryAttributesPresentForCreateIncidentRequest(MappedVariableInstanceLogVO jsonPayLoad) {
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
				return new ErrorDetails("200", "Created By Full Name is missing",
						"Created By Full Name is missing");
			}
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getIncidentType())) {

			} else {
				return new ErrorDetails("200", "Incident Type is missing",
						"Incident Type is missing");
			}

		}
		return null;
	}

	private ErrorDetails validateModule(String module) {
		if (StringUtil.isNotNullNotEmpty(module) && module.equals("IM")) {

		} else {
			return new ErrorDetails("200", "Module is Wrong", "Module is Wrong");
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
	
	private ErrorDetails validateResolutionRemarks(String resolutionRemarks) {
		if (StringUtil.isNotNullNotEmpty(resolutionRemarks)) {
			int length = Constant.LENGTH_1000;
			int actualLength = (int) resolutionRemarks.length();
			if (length >= actualLength) {

			} else {
				return new ErrorDetails("200", "Resolution Remarks Length is Greater than 1000",
						"Resolution Remarks Length is Greater than 1000");
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

	private ErrorDetails validateIncidentType(String incidenttype) {
		if (StringUtil.isNotNullNotEmpty(incidenttype)) {

			if (incidenttype.equals(Constant.INCIDENT_TYPE_INCIDENT)
					|| incidenttype.equals(Constant.INCIDENT_TYPE_SERVICE)) {

			} else {
				return new ErrorDetails("200", "Incident Type entered value is wrong",
						"Incident Type entered value is wrong");
			}

		}

		return null;
	}

	private ErrorDetails validateNotification(String statusChangeCheckbox, String feedbackCheckbox, String email,
			String sms) {

		if (statusChangeCheckbox.equalsIgnoreCase("TRUE") || feedbackCheckbox.equalsIgnoreCase("TRUE")) {
			if (StringUtil.isNotNullNotEmpty(email) || StringUtil.isNotNullNotEmpty(sms)) {
				if (StringUtil.isNotNullNotEmpty(email)) {
					List<String> emailList = Arrays.asList(email.split(";"));
					for (String emailList1 : emailList) {
						boolean check = isValidEmail(emailList1);
						if (check) {

						} else {
							return new ErrorDetails("200", "Email entered value is wrong",
									"Email entered value is wrong");
						}
					}
				}
				if (StringUtil.isNotNullNotEmpty(sms)) {

					List<String> smsList = Arrays.asList(sms.split(","));

					for (String smsList1 : smsList) {
						if (smsList1.matches("[0-9]+")) {

						} else {
							return new ErrorDetails("200", "SMS entered value is wrong", "SMS entered value is wrong");
						}

						if (smsList1.length() == 10) {

						} else {
							return new ErrorDetails("200", "Mobile no entered value is Wrong",
									"Mobile no entered value is Wrong");
						}
					}
				}
			} else {
				return new ErrorDetails("200", "Notifications is not properly filled",
						"Notifications is not properly filled");
			}
		}
		return null;

	}
	
	private boolean isValidEmail(String email) {
	      String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
	      return email.matches(regex);
	   }

	private ErrorDetails validateMajorIncident(String majorIncident) {
		if (StringUtil.isNotNullNotEmpty(majorIncident)) {

			if (majorIncident.equalsIgnoreCase("TRUE") || majorIncident.equalsIgnoreCase("FALSE")) {

			} else {
				return new ErrorDetails("200", "Mark as Major Incident entered value is wrong",
						"Mark as Major Incident entered value is wrong");
			}

		}

		return null;
	}

	private ErrorDetails validateIncidentAttachments(MappedVariableInstanceLogVO jsonPayLoad) {
		if (jsonPayLoad.getIncidentAttachList() != null && jsonPayLoad.getIncidentAttachList().size() > 0) {
			for (IncidentAttachmentEntity incidentAttachmentEntity : jsonPayLoad.getIncidentAttachList()) {
				if (StringUtil.isNotNullNotEmpty(incidentAttachmentEntity.getAttachment())) {
					// Do Nothing
				} else {
					return new ErrorDetails("200", "Attachment details is missing",
							"Attachment details is missing");
				}
				if (StringUtil.isNotNullNotEmpty(incidentAttachmentEntity.getAttachmentName())) {
					// Do Nothing
				} else {
					return new ErrorDetails("200", "Attachment Name is missing",
							"Attachment Name is missing");
				}
			}

		}
		return null;
	}

	private ErrorDetails validateMandatoryResolvedAttributes(MappedVariableInstanceLogVO jsonPayLoad) {
		if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getResolutionMethod())) {

		} else {
			return new ErrorDetails("200", "Resolution Method is missing", "Resolution Method is missing");
		}
		if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getResolutionType())) {

		} else {
			return new ErrorDetails("200", "Resolution Type is missing",
					"Resolution Type is missing");
		}
		if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getResolutionRemarks())) {

		} else {
			return new ErrorDetails("200", "Resolution Remarks is missing",
					"Resolution Remarks is missing");
		}
		if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getResolvedBy())) {

		} else {
			return new ErrorDetails("200", "Resolved By is missing", "Resolved By is missing");
		}

		return null;
	}
	
	private ErrorDetails validateProcessInstanceAndIncidentID(String incidentID, MappedVariableInstanceLogVO jsonPayLoad) {
		if (StringUtil.isNotNullNotEmpty(incidentID)
				&& StringUtil.isNotNullNotEmpty(jsonPayLoad.getProcessInstanceId())) {
			MappedVariableInstanceLogVO mappedVariableInstanceLogVO = mappedVariableInstanceRepo.findByIncidentID(incidentID);
			if (mappedVariableInstanceLogVO != null) {
				String processId = mappedVariableInstanceLogVO.getProcessInstanceId();
				if (StringUtil.isNotNullNotEmpty(processId)) {
					if (processId.equalsIgnoreCase(jsonPayLoad.getProcessInstanceId())) {
						String pId = mappedVariableInstanceLogVO.getIncidentID();
						if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getIncidentID())
								&& jsonPayLoad.getIncidentID().equals(pId)) {

						} else {
							return new ErrorDetails("200", "Invalid Incident ID",
									"Invalid Incident ID");
						}
					} else {
						return new ErrorDetails("200", "Process Instance Id is Wrong",
								"Process Instance Id is Wrong");
					}
				} else {
					return new ErrorDetails("200", "Incident ID is Invalid",
							"Incident ID is Invalid");
				}
			} else {
				return new ErrorDetails("200", "Incident ID is Invalid",
						"Incident ID is Invalid");
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
	
	private ErrorDetails mandatoryAttributesPresentForUpdateIncidentRequest(MappedVariableInstanceLogVO jsonPayLoad) {
		ErrorDetails ed = null;
		if (jsonPayLoad != null) {
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getIncidentID())) {

			} else {
				return new ErrorDetails("200", "Incident ID is missing", "Incident ID is missing");
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
			
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getSource())) {

			} else {
				return new ErrorDetails("200", "Source is missing", "Source is missing");
			}
			
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getComments())) {

			} else {
				return new ErrorDetails("200", "Comments is missing", "Comments is missing");
			}
			if (jsonPayLoad.getStatus().equals(Constant.INCIDENT_STATUS_CANCELLED) || jsonPayLoad.getStatus().equals(Constant.INCIDENT_STATUS_ON_HOLD)) {
				if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getStatusRemark())) {

				} else {
					return new ErrorDetails("200", "Status Remark  is missing",
							"Status Remark  is missing");
				}
			}

			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getLastUpdatedBy())) {

			} else {
				return new ErrorDetails("200", "Last Updated By  is missing",
						"Last Updated By  is missing");
			}
			
			if (StringUtil.isNotNullNotEmpty(jsonPayLoad.getIncidentType())) {

			} else {
				return new ErrorDetails("200", "Incident Type is missing",
						"Incident Type is missing");
			}
			
		}

		return ed;
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
	
	private ErrorDetails validateStatus(String status) {
		List<String> checkStatusList = statusDetailsRepo.findAll().stream()
				.filter(s -> !s.getStatus().equalsIgnoreCase("New") && !s.getStatus().equalsIgnoreCase("Closed"))
				.map(StatusDetailsEntity::getStatus).collect(Collectors.toList());
		if (checkStatusList.contains(status)) {

		} else {
			return new ErrorDetails("200", "Status is Wrong", "Status is Wrong");
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
	
	private ErrorDetails validateResolutionMethod(String resolutionMethod, String module) {
		if (StringUtil.isNotNullNotEmpty(resolutionMethod)) {
			List<String> resolutionTypeList = resolutionMethodRepo.findAll().stream()
					.filter(name -> name.getModule().equalsIgnoreCase(module))
					.map(ResolutionMethodEntity::getName).collect(Collectors.toList());
			if (resolutionTypeList.contains(resolutionMethod)) {
			} else {
				return new ErrorDetails("200", "Resolution Method is Wrong",
						"Resolution Method is Wrong");
			}
		}
		return null;
	}
	
	private ErrorDetails validateParentID(String incidentID, String parentID) {
		if(StringUtil.isNotNullNotEmpty(parentID)){
			String actualParentID = mappedVariableInstanceRepo.fetchParentsParent(incidentID);
			if(actualParentID.equals(parentID)) {
				
			}else {
				return new ErrorDetails("200", "Parent Ticket Id is Wrong",
						"Parent Ticket Id is Wrong");
			}
		}
		
		return null;		
	}
	
	private ErrorDetails validateWorkAround(String incidentid, String workAround) {
		if (workAround == null) {
			String workAroundCheck = mappedVariableInstanceRepo.findByIncidentID(incidentid).getWorkAround();
			if (workAroundCheck == null) {

			} else {
				return new ErrorDetails("200", "WorkAround Value cannot be Changed",
						"WorkAround Value cannot be Changed");
			}
		}

		if (StringUtil.isNotNullNotEmpty(workAround)) {
			String workAroundCheck = mappedVariableInstanceRepo.findByIncidentID(incidentid).getWorkAround();
			if (workAroundCheck != null) {
				if (workAround.equals(workAroundCheck)) {

				} else {
					return new ErrorDetails("200", "WorkAround Value cannot be Changed",
							"WorkAround Value cannot be Changed");
				}
			}
			if (workAroundCheck == null) {
				return new ErrorDetails("200", "WorkAround Value cannot be Changed",
						"WorkAround Value cannot be Changed");
			}
		}
		return null;
	}

	
	private ErrorDetails validateIncidentAttachmentSize(MappedVariableInstanceLogVO jsonPayLoad) {
		List<IncidentAttachmentEntity> attachmentList = new ArrayList<IncidentAttachmentEntity>();
		try {
			if (jsonPayLoad.getIncidentID() != null) {
				List<IncidentAttachmentEntity> attachmentListDB = lIncidentCreationServiceImpl
						.fetchIncidentAttchmentList(jsonPayLoad.getIncidentID());
				if (attachmentListDB != null && attachmentListDB.size() > 0) {
					attachmentList.addAll(attachmentListDB);
				}
			}

			if (jsonPayLoad.getIncidentAttachList() != null && jsonPayLoad.getIncidentAttachList().size() > 0) {
				attachmentList.addAll(jsonPayLoad.getIncidentAttachList());
			}

			if (attachmentList.size() > 0) {
				long totalByteArraySize = 0;
				long totalKBSize = 0;
				for (IncidentAttachmentEntity AttachmentObject : attachmentList) {
					String[] splitedString = AttachmentObject.getAttachment().split("base64,");
					String[] splitedAttachment = splitedString[1].split(",");
					for (int i = 0; i < splitedAttachment.length; i++) {
						String base64EncodedAttachment = splitedAttachment[i];
						byte[] attachment = Base64.getDecoder().decode(base64EncodedAttachment);
//						System.out.println("attachment name - "+AttachmentObject.getAttachmentName() +", size: "+attachment);
						totalByteArraySize = totalByteArraySize + attachment.length;
					}

				}
				totalKBSize = totalByteArraySize / 1024;
//				System.out.println("totalKBSize :"+totalKBSize);
				long defaultSize = Long.parseLong(lConfigEntityCached.getValue("total.attachment.size.kb"));

				if (totalKBSize > defaultSize) {
					return new ErrorDetails("200", "Attachment size is Greater than " + defaultSize / 1024 + "MB",
							"Attachment size is Greater than " + defaultSize / 1024 + "MB");

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}
}
