CREATE TABLE test_executions
(
  id character varying NOT NULL,
  node text NOT NULL,
  state text,
  properties jsonb
);

ALTER TABLE ONLY test_executions ADD CONSTRAINT test_executions_pkey PRIMARY KEY (id, node);