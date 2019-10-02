-- Deprecated tables
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

ALTER TABLE ONLY test_run_stack_traces ADD CONSTRAINT test_run_stack_traces_pkey PRIMARY KEY (test_run_id);