CREATE TABLE test_executions
(
  id character varying NOT NULL,
  node character varying NOT NULL,
  state text,
  properties jsonb,
  update_timestamp timestamp without time zone
);

ALTER TABLE ONLY test_executions ADD CONSTRAINT test_executions_pkey PRIMARY KEY (id, node);