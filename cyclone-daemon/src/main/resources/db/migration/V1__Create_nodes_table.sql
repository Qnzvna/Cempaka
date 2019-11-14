CREATE TABLE nodes
(
  identifier  character varying NOT NULL,
  status      text,
  "timestamp" timestamp without time zone
);

ALTER TABLE ONLY nodes ADD CONSTRAINT nodes_pkey PRIMARY KEY (identifier);