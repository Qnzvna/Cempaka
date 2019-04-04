package org.cempaka.cyclone.storage.repository;

import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.cempaka.cyclone.beans.Parcel;

public interface ParcelRepository
{
    @Nullable
    Parcel get(UUID id);

    void put(Parcel parcel);

    void delete(UUID id);

    Stream<UUID> list();
}
