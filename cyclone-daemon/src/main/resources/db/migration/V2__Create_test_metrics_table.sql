CREATE TABLE test_metrics
(
  test_run_id character varying NOT NULL,
  "timestamp" timestamp without time zone,
  name        text,
  "value"     double precision
);