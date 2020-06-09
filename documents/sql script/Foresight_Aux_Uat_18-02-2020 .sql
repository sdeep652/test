
/*mappedvariable added 4 fields*/


ALTER TABLE mapped_variable_instance_log
  ADD COLUMN email VARCHAR(50),
   ADD COLUMN sms VARCHAR(50),
    ADD COLUMN status_change_checkbox VARCHAR(50),
	 ADD COLUMN feedback_checkbox VARCHAR(50); 



/*added new table progresbased_notification*/



CREATE TABLE public.progresbased_notification
(
    id integer NOT NULL DEFAULT nextval('progresbased_notification_id_seq'::regclass),
    email_list character varying(500) COLLATE pg_catalog."default",
    phone_number_list character varying(500) COLLATE pg_catalog."default",
    module character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT progresbased_notification_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.progresbased_notification
    OWNER to postgres;
	
	
	
//Page_details added  pagenames_details 33,34,35

INSERT INTO public.pagenames_details(
	id, pagename, module)
	VALUES (33, 'IM_Page_StatusChangeNotification', 'IM');
	
INSERT INTO public.pagenames_details(
	id, pagename, module)
	VALUES (34, 'IM_Action_Mygroupaction', 'IM');
	
INSERT INTO public.pagenames_details(
	id, pagename, module)
	VALUES (35, 'IM_Adminconfig_incidentModel', 'IM');

	
	
	
	

/*Added incident_template table*/


CREATE TABLE public.incident_template
(
    category character varying(500) COLLATE pg_catalog."default",
    sub_category character varying(500) COLLATE pg_catalog."default",
    title character varying(250) COLLATE pg_catalog."default",
    impact character varying(250) COLLATE pg_catalog."default",
    urgency character varying(250) COLLATE pg_catalog."default",
    priority character varying(250) COLLATE pg_catalog."default",
    source character varying(250) COLLATE pg_catalog."default",
    source_contact character varying(10000) COLLATE pg_catalog."default",
    group_name character varying(250) COLLATE pg_catalog."default",
    assign_to character varying(250) COLLATE pg_catalog."default",
    comments text COLLATE pg_catalog."default",
    module character varying(50) COLLATE pg_catalog."default",
    resolution_type character varying(255) COLLATE pg_catalog."default",
    resolution_remarks character varying(5000) COLLATE pg_catalog."default",
    resolution_method character varying(1000) COLLATE pg_catalog."default",
    descriptions text COLLATE pg_catalog."default",
    template_name character varying(500) COLLATE pg_catalog."default",
    active character varying(50) COLLATE pg_catalog."default",
    publish_for_groups character varying(1000) COLLATE pg_catalog."default",
    template_id bigint NOT NULL
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.incident_template
    OWNER to postgres;


 
