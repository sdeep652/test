/* Alter Table for MappedVariableInstancelog Table*/

ALTER TABLE mapped_variable_instance_log ALTER COLUMN resolution_sla_bracket TYPE VARCHAR(50);


/*Creat table c_sat for CSAT in  MappedVariableInstancelog Table*/

CREATE TABLE public.c_sat
(
    id bigint NOT NULL,
    rating bigint NOT NULL,
    reason character varying(500) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT c_sat_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.c_sat
    OWNER to postgres; 
 



/*Creat table  c_sat_feedback for CSAT in  MappedVariableInstancelog Table*/
CREATE TABLE public.c_sat_feedback
(
    ticket_id character varying(250) COLLATE pg_catalog."default" NOT NULL,
    form_sub_date character varying(250) COLLATE pg_catalog."default" NOT NULL,
    rating bigint,
    reason character varying(500) COLLATE pg_catalog."default"
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.c_sat_feedback
    OWNER to postgres; 
 
