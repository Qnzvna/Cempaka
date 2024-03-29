package org.cempaka.cyclone.storage.repositories;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toSet;

import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.cempaka.cyclone.beans.Parcel;
import org.cempaka.cyclone.storage.jdbi.ParcelDataAccess;

@Singleton
public class JdbiParcelRepository implements ParcelRepository
{
    private final ParcelDataAccess parcelDataAccess;

    @Inject
    public JdbiParcelRepository(final ParcelDataAccess parcelDataAccess)
    {
        this.parcelDataAccess = checkNotNull(parcelDataAccess);
    }

    @Nullable
    @Override
    public Parcel get(final UUID id)
    {
        return parcelDataAccess.get(id.toString());
    }

    @Override
    public boolean contains(final UUID id)
    {
        return parcelDataAccess.count(id.toString()) == 1;
    }

    @Override
    public void put(final Parcel parcel)
    {
        parcelDataAccess.upsert(parcel.getId().toString(), parcel.getData());
    }

    @Override
    public void delete(final UUID id)
    {
        parcelDataAccess.delete(id.toString());
    }

    @Override
    public Set<UUID> keys()
    {
        return parcelDataAccess.keys().stream().map(UUID::fromString).collect(toSet());
    }
}
