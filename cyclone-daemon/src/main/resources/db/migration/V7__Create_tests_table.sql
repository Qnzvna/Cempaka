CREATE TABLE tests
(
  id    text NOT NULL,
  name  text NOT NULL,
  value jsonb
);

ALTER TABLE ONLY tests ADD CONSTRAINT tests_pkey PRIMARY KEY (id, name);