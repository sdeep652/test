ALTER TABLE mapped_variable_instance_log
  ADD COLUMN response_sla_breach VARCHAR(50),
   ADD COLUMN resolution_sla_breach VARCHAR(50),
    ADD COLUMN resolution_sla_bracket BIGINT;
