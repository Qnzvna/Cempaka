package org.cempaka.cyclone.protocol;

import java.util.Objects;

class Header
{
    private final int size;
    private final PayloadType payloadType;

    Header(final int size, final PayloadType payloadType)
    {
        this.size = size;
        this.payloadType = payloadType;
    }

    public int getSize()
    {
        return size;
    }

    PayloadType getPayloadType()
    {
        return payloadType;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final Header header = (Header) o;
        return size == header.size &&
            payloadType == header.payloadType;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(size, payloadType);
    }
}
