package org.cempaka.cyclone.storage.repository;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.ParcelMetadata;
import org.cempaka.cyclone.storage.data.ParcelMetadataDataAccess;

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
    public Stream<String> list()
    {
        return parcelMetadataDataAccess.getKeys();
    }
}
