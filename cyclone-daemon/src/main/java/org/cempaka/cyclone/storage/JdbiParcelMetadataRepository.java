package org.cempaka.cyclone.storage;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.ParcelMetadata;

@Singleton
public class JdbiParcelMetadataRepository implements ParcelMetadataRepository
{
    private final ParcelMetadataDataAcess parcelMetadataDataAcess;

    @Inject
    public JdbiParcelMetadataRepository(final ParcelMetadataDataAcess parcelMetadataDataAcess)
    {
        this.parcelMetadataDataAcess = checkNotNull(parcelMetadataDataAcess);
    }

    @Nullable
    @Override
    public ParcelMetadata get(final String id)
    {
        return parcelMetadataDataAcess.findById(id);
    }

    @Override
    public void put(final ParcelMetadata parcelMetadata)
    {
        parcelMetadataDataAcess.upsert(parcelMetadata.getId().toString(), parcelMetadata);
    }

    @Override
    public void delete(final String id)
    {
        parcelMetadataDataAcess.delete(id);
    }

    @Override
    public Stream<String> list()
    {
        return parcelMetadataDataAcess.getKeys();
    }
}
