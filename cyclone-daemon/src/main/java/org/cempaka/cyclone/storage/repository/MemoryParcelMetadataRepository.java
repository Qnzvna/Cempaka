package org.cempaka.cyclone.storage.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.ParcelMetadata;

@Singleton
public class MemoryParcelMetadataRepository implements ParcelMetadataRepository
{
    private final Map<String, ParcelMetadata> storage;

    @Inject
    public MemoryParcelMetadataRepository()
    {
        this.storage = new ConcurrentHashMap<>();
    }

    @Nullable
    @Override
    public ParcelMetadata get(final String name)
    {
        return storage.get(name);
    }

    @Override
    public void put(final ParcelMetadata parcelMetadata)
    {
        storage.put(parcelMetadata.getId().toString(), parcelMetadata);
    }

    @Override
    public void delete(final String name)
    {
        storage.remove(name);
    }

    @Override
    public Stream<String> list()
    {
        return storage.keySet().stream();
    }
}
