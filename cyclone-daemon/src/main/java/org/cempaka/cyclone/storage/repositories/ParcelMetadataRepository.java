package org.cempaka.cyclone.storage.repositories;

import org.cempaka.cyclone.beans.ParcelMetadata;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.stream.Stream;

public interface ParcelMetadataRepository
{
    @Nullable
    ParcelMetadata get(String name);

    void put(ParcelMetadata parcelMetadata);

    void delete(String id);

    Set<ParcelMetadata> getAll();

    Stream<String> list();
}
