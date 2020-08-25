package org.cempaka.cyclone.storage.repositories;

import java.util.Optional;
import org.cempaka.cyclone.jooq.tables.records.MetadataRecord;

public interface MetadataRepository
{
    void put(MetadataRecord metadataRecord);

    Optional<MetadataRecord> get(String metadataId);

    void delete(String metadataId);
}
