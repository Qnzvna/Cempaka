CREATE TABLE parcels
(
  id character varying NOT NULL,
  parcel bytea
);

ALTER TABLE ONLY parcels ADD CONSTRAINT parcels_pkey PRIMARY KEY (id);