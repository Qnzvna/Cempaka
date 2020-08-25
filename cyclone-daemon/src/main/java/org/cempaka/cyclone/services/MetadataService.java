package org.cempaka.cyclone.services;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.ImmutableMetadata;
import org.cempaka.cyclone.beans.Metadata;
import org.cempaka.cyclone.jooq.tables.records.MetadataRecord;
import org.cempaka.cyclone.storage.repositories.MetadataRepository;

@Singleton
public class MetadataService
{
    private final MetadataRepository metadataRepository;

    @Inject
    public MetadataService(final MetadataRepository metadataRepository)
    {
        this.metadataRepository = checkNotNull(metadataRepository);
    }

    public void put(final Metadata metadata)
    {
        checkNotNull(metadata);
        metadataRepository.put(new MetadataRecord(metadata.getId(), metadata.getValue()));
    }

    public Optional<Metadata> get(final String metadataId)
    {
        checkNotNull(metadataId);
        return metadataRepository.get(metadataId)
            .map(metadataRecord -> ImmutableMetadata.builder()
                .id(metadataRecord.getMetadataId())
                .value(metadataRecord.getValue())
                .build());
    }

    public void delete(final String metadataId)
    {
        checkNotNull(metadataId);
        metadataRepository.delete(metadataId);
    }
}
