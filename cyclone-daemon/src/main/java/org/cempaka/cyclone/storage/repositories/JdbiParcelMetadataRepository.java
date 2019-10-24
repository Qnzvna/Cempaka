package org.cempaka.cyclone.storage.repositories;

import org.cempaka.cyclone.beans.ParcelMetadata;
import org.cempaka.cyclone.storage.jdbi.ParcelMetadataDataAccess;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
public class JdbiParcelMetadataRepository implements ParcelMetadataRepository
{
    private final ParcelMetadataDataAccess parcelMetadataDataAccess;

    @Inject
    public JdbiParcelMetadataRepository(final ParcelMetadataDataAccess parcelMetadataDataAccess)
    {
        this.parcelMetadataDataAccess = checkNotNull(parcelMetadataDataAccess);
    }

    @Nullable
    @Override
    public ParcelMetadata get(final String id)
    {
        return parcelMetadataDataAccess.findById(id);
    }

    @Override
    public void put(final ParcelMetadata parcelMetadata)
    {
        parcelMetadataDataAccess.upsert(parcelMetadata.getId().toString(), parcelMetadata);
    }

    @Override
    public void delete(final String id)
    {
        parcelMetadataDataAccess.delete(id);
    }

    @Override
    public Set<ParcelMetadata> getAll()
    {
        return parcelMetadataDataAccess.getAll();
    }

    @Override
    public Stream<String> list()
    {
        return parcelMetadataDataAccess.getKeys();
    }
}
