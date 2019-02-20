package org.cempaka.cyclone.beans;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import java.util.UUID;

public class Parcel
{
    private final UUID id;
    private final byte[] data;

    private Parcel(final UUID id, final byte[] data)
    {
        this.id = checkNotNull(id);
        this.data = checkNotNull(data);
    }

    public static Parcel of(final UUID id, final byte[] data)
    {
        return new Parcel(id, data);
    }

    public UUID getId()
    {
        return id;
    }

    public byte[] getData()
    {
        return data;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final Parcel aParcel = (Parcel) o;
        return Objects.equal(id, aParcel.id) &&
            Objects.equal(data, aParcel.data);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(id, data);
    }
}
