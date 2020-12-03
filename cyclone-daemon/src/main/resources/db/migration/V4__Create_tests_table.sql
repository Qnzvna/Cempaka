CREATE TABLE tests
(
  id    character varying NOT NULL,
  name  character varying NOT NULL,
  "value" jsonb
);

ALTER TABLE ONLY tests ADD CONSTRAINT tests_pkey PRIMARY KEY (id, name);