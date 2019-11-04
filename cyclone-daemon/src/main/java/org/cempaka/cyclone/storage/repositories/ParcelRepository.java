package org.cempaka.cyclone.storage.repositories;

import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import org.cempaka.cyclone.beans.Parcel;

public interface ParcelRepository
{
    @Nullable
    Parcel get(UUID id);

    void put(Parcel parcel);

    void delete(UUID id);

    Set<UUID> keys();
}
