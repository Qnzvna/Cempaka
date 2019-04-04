package org.cempaka.cyclone.storage.repository;

import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.cempaka.cyclone.beans.ParcelMetadata;

public interface ParcelMetadataRepository
{
    @Nullable
    ParcelMetadata get(String name);

    void put(ParcelMetadata parcelMetadata);

    void delete(String name);

    Stream<String> list();
}
