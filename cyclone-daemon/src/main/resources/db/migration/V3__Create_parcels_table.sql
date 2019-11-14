CREATE TABLE parcels
(
  id text NOT NULL,
  parcel bytea
);

ALTER TABLE ONLY parcels ADD CONSTRAINT parcels_pkey PRIMARY KEY (id);