CREATE TABLE test_run_status
(
  test_run_id character varying NOT NULL,
  node_identifier text NOT NULL,
  state text,
  configuration jsonb
);

ALTER TABLE ONLY test_run_status ADD CONSTRAINT test_run_status_pkey PRIMARY KEY (test_run_id, node_identifier);