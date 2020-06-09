package com.tcts.foresight.util;

import java.text.SimpleDateFormat;

public class Constant {

	public static String ROLE_DESCRIPTION = "type : %type%, module : %mdule%";

	public static final String CONSTANT1 = "CONSTANT";
	// Task class, String literal
	public static final String POT_OWNERS_PATH = "queries/tasks/instances/pot-owners";
	public static final String VM_55_AllData_QueryId = "7d90f063-bb37-4588-8459-9572bdb4ff2c";
	public static final String VM__55_Distinct_Status_TaskOwner_QueryId = "0a05702c-f2dc-4a24-8c70-deb4bddd993d";
	public static final String FOLDER_NAME = "/upload/";
	// DBConnectionUtil class, String literals

	public static final String WFM_DB_URL = "jdbc:postgresql://10.147.3.55:5433/TTAUXDB";
	public static final String WFM_DB_USER = "postgres";
	public static final String WFM_DB_PSWD = "jbpm";

	public static final String fileName = "mainConfig.properties";
	// TaskUtil class, String literals
	public static final String SKIP_TASK_USER_NAME = "wfmadmin";
	public static final String SKIP_TASK_PASWD = "wfmadmin";

	public static final String SLA_DELETED = "SLA Deleted";
	public static final String SLA_UPDATED = "SLA Updated";
	public static final String NOTIFICATION_UPDATED = "Notification Updated";
	public static final String NOTIFICATION_DELETED = "Notification Deleted";

	public static final String EMAIL_NOTIFICATION_UPDATED = "Email Notification Updated";
	public static final String EMAIL_NOTIFICATION_UPDATION_FAIL = "Email Notification not Updated";
	/* constants for config file */
	public static final String BPM_SERVER_HOST = "BPM_SERVER_HOST";
	public static final String BPM_SERVER_PORT = "BPM_SERVER_PORT";
	public static final String FILTER_DATA = "/filtered-data";
	public static final String QURES_DEFINITIONS = "queries/definitions/";
	public static final String MAPPER = "mapper";
	public static final String PAGE_SIZE = "pageSize";
	public static final String RHPAM_DB_URL = "RHPAM_DB_URL";
	public static final String RHPAM_DB_USER = "RHPAM_DB_USER";
	public static final String RHPAM_DB_PASWD = "RHPAM_DB_PASSWORD";
	public static final String CONTAINERS = "containers/";
	public static final String PROCESS_INSTANCE_ID = "process-instance-id";
	public static final String AUTHORIZATION = "Authorization";
	public static final String BASIC = "Basic ";
	public static final String STATUS = "status";
	
	//Incident Status
	public static final String INCIDENT_STATUS_NEW = "New";
	public static final String INCIDENT_STATUS_RESOLVED = "Resolved";
	public static final String INCIDENT_STATUS_ON_HOLD = "On Hold";
	public static final String INCIDENT_STATUS_IN_PROGRESS = "In Progress";
	public static final String INCIDENT_STATUS_CANCELLED = "Cancelled";
	public static final String INCIDENT_STATUS_RUNNING = "Running";
	public static final String INCIDENT_STATUS_CLOSED = "Closed";


	
	
	
	//SLA Status
	public static final String SLA_STATUS_COMPLETED = "Completed";
	public static final String SLA_STATUS_BREACHED = "Breached";
	public static final String SLA_STATUS_RUNNING = "Running";
	public static final String SLA_STATUS_CANCELLED = "Cancelled";
	public static final String SLA_STATUS_PAUSE = "Pause";
    //public static final String SLA_STATUS_RESOLVED= "Resolved";
    //public static final String SLA_STATUS_ON_HOLD= "ON HOld";

	
	
	
	//SLA Type
	public static final String SLA_TARGET_RESPONSESLA = "Response";
	public static final String SLA_TARGET_RESOLUTIONSLA = "Resolution";
	


	public static final String CONTAINERS_PATH = "containers/";

	// Keycloak constants
	public static final String USER_CREATED = "User created successfully";
	public static final String USER_NOT_CREATED = "User creation failed";
	public static final String USER_DELETED = "User deleted successfully";
	public static final String USER_NOT_DELETED = "User deletion failed";
	public static final String USER_UPDATED = "User updated successfully";
	public static final String USER_NOT_UPDATED = "User updation failed";
	public static final String USER_PRESENT = "User already exists";
	public static final String ADMIN = "Administrator";
	public static final String DISPATCHER = "dispatcher";
	public static final String FE = "fieldengineer";
	
	public static final String User_Does_Not_Have_Authorization = "User Does Not Have Authorization";
	
	//PageNames User Management 
	public static final String UM_Adminconfig_user = "UM_Adminconfig_user";
	public static final String UM_Adminconfig_group = "UM_Adminconfig_group";
	public static final String UM_Adminconfig_role = "UM_Adminconfig_role";
	
	// PageNames Incident Management
	public static final String IM_Adminconfig_category = "IM_Adminconfig_category";
	public static final String IM_Adminconfig_subcategory ="IM_Adminconfig_subcategory";
	public static final String IM_Adminconfig_priority = "IM_Adminconfig_priority";	
	public static final String IM_Adminconfig_statusremark = "IM_Adminconfig_statusremark";
	public static final String IM_Adminconfig_resolutionmethod ="IM_Adminconfig_resolutionmethod";
	public static final String IM_Adminconfig_autoclose = "IM_Adminconfig_autoclose";
	public static final String IM_Page_incidentcreation = "IM_Page_incidentcreation";
	
	public static final String IM_Action_resolveincident = "IM_Action_resolveincident";
	public static final String IM_Page_assignedtome = "IM_Page_assignedtome";		
	public static final String IM_Page_mygroup = "IM_Page_mygroup";	
	public static final String IM_Page_incidentupdate = "IM_Page_incidentupdate";
	
	//Page Names Problem Management
	public static final String PM_Page_problemcreation = "PM_Page_problemcreation";
	public static final String PM_Page_problemupdate = "PM_Page_problemupdate";
	
	
	// Default Password 
	public static final String Default_Password = "Foresight@123";
																	 
	//public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final int RESPONSE_SLA = 15;
	
	public static final String INCIDENT_ANALYSIS_DETAILS = "incidentAnalysisDetails";
	
	//Mapped Varible instance Entity Constants
//	public static final String  INCIDENT_ID = "incidentID";
//	public static final String  INCIDENT_CREATIONDATE= "incidentCreationDate"; 
//	public static final String  CATEGORY= "category"; 
//	public static final String  SUBCATEGORY= "subCategory"; 
//	public static final String  PRIORITY= "priority"; 
//	public static final String  SOURCE= "source"; 
//	public static final String  INCIDENT_AGEING= "incidentAgeing"; 
//	public static final String  RESOLVED_TIMER= "resolvedTimer"; 
//	public static final String  RESPONSE_SLABREACH= "responseSlaBreach"; 
//	public static final String  RESOLUTION_SLABREACH= "resolutionSlaBreach"; 
//	public static final String  PARENT_TICKETID= "parentTicketId"; 
//	public static final String  CONFIGURATION_ITEM= "configurationItem"; 
//	public static final String  RESOLUTION_METHOD= "resolutionMethod"; 
//	public static final String  RESOLUTION_TYPE = "resolutionType";
//	public static final String  STATUS_REMARK = "statusRemark";
//	public static final String  RESOLUTION_REMARKS = "resolutionRemarks";
//	public static final String  RESOLVED_DATE = "resolvedDate";
//	public static final String  RESOLVED_BY = "resolvedBy";
//	public static final String  TITLE = "title";
//	public static final String  CREATED_BY = "createdBy";
//	public static final String  RESOLUTION_SLA_BRACKET = "resolutionSlaBracket";
//	public static final String  INCIDENT_CLOSED_DATE = "incidentClosedDate";
//	public static final String  ASSIGNMENT_GROUP = "assignmentGroup";
//	public static final String  CREATEDBY_FULLNAME = "createdByFullName";
//	public static final String  LAST_UPDATED_DATE = "lastUpdatedDate";
//	public static final String  LAST_UPDATEDBY = "lastUpdatedBy";
	
	public static final String READ_ONLY_GROUP = "ReadOnlyGroup";
	public static final String READ_ONLY_ROLE = "ReadOnlyRole";
	
	public static final String  CONTAINER_ID = "Foresight_1_1.0.0";
	public static final String  PROCESS_ID = "src.main.resources.foresight";
	public static final String  CLIENT="login";
	
	public static final String  INCIDENT_MGMT_MODULE="IM";
	public static final String  PROBLEM_MGMT_MODULE="PM";
	
	public static final String PROBLEM_MGMT_RESOLVED_TIMER = "120";
	
	//Problem Status
	public static final String PROBLEM_STATUS_NEW = "New";
	public static final String PROBLEM_STATUS_RESOLVED = "Resolved";
	public static final String PROBLEM_STATUS_PENDING = "Pending";
	public static final String PROBLEM_STATUS_IN_PROGRESS = "In Progress";
	public static final String PROBLEM_STATUS_CANCELLED = "Cancelled";
	public static final String PROBLEM_STATUS_CLOSED = "Closed";
	
	public static final int LENGTH_250  = 250;	
	public static final int LENGTH_1000 = 1000;	
	public static final int LENGTH_5000 = 5000;
	
	public static final String INCIDENT_TYPE_INCIDENT = "Incident";
	public static final String INCIDENT_TYPE_SERVICE = "Service Request";
	
}
