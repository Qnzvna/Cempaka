package org.cempaka.cyclone.core.channel;

import static org.cempaka.cyclone.core.utils.Preconditions.checkNotNull;

import java.util.Objects;
import java.util.UUID;

class Header
{
    private final int size;
    private final UUID testId;
    private final PayloadType payloadType;

    Header(final int size, final UUID testId, final PayloadType payloadType)
    {
        this.size = size;
        this.testId = checkNotNull(testId);
        this.payloadType = payloadType;
    }

    public int getSize()
    {
        return size;
    }

    public UUID getTestId()
    {
        return testId;
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
            Objects.equals(testId, header.testId) &&
            payloadType == header.payloadType;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(size, testId, payloadType);
    }
}
