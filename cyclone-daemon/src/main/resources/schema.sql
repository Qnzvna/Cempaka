CREATE TABLE parcels_metadata
(
  id    text NOT NULL,
  value jsonb
);

ALTER TABLE ONLY parcels_metadata
  ADD CONSTRAINT parcels_metadata_pkey PRIMARY KEY (id);

CREATE TABLE test_run_events
(
  test_run_id character varying NOT NULL,
  "timestamp" timestamp without time zone NOT NULL,
  event_type  character varying NOT NULL
);

CREATE TABLE test_run_metrics
(
  test_run_id character varying NOT NULL,
  "timestamp" timestamp without time zone,
  type        text,
  name        text,
  value       double precision
);

CREATE TABLE test_run_stack_traces
(
  test_run_id character varying NOT NULL,
  stack_trace text
);

ALTER TABLE ONLY test_run_stack_traces
  ADD CONSTRAINT test_run_stack_traces_pkey PRIMARY KEY (test_run_id);

CREATE TABLE nodes
(
  identifier  character varying NOT NULL,
  status      text,
  "timestamp" timestamp without time zone
);

ALTER TABLE ONLY nodes
  ADD CONSTRAINT nodes_pkey PRIMARY KEY (identifier);

CREATE TABLE test_run_status
(
  test_run_id character varying NOT NULL,
  node_identifier text NOT NULL,
  state text
);

ALTER TABLE ONLY test_run_status
  ADD CONSTRAINT test_run_status_pkey PRIMARY KEY (test_run_id, node_identifier);

CREATE TABLE test_run_configuration
(
  test_run_id character varying NOT NULL,
  value jsonb
);

ALTER TABLE ONLY test_run_configuration
  ADD CONSTRAINT test_run_configuration_pkey PRIMARY KEY (test_run_id);