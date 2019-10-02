CREATE TABLE parcels_metadata
(
  id    text NOT NULL,
  value jsonb
);

ALTER TABLE ONLY parcels_metadata ADD CONSTRAINT parcels_metadata_pkey PRIMARY KEY (id);