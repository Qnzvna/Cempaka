package org.cempaka.cyclone.storage.repositories;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
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
    public Stream<UUID> list()
    {
        return parcelDataAccess.listKeys().stream().map(UUID::fromString);
    }
}
