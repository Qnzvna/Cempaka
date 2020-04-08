package org.cempaka.cyclone.channel;

import java.nio.ByteBuffer;
import java.util.UUID;
import org.cempaka.cyclone.channel.payloads.PayloadType;

class HeaderEncoder
{
    static int SIZE = 24;

    ByteBuffer encode(final Header header)
    {
        final UUID testId = header.getTestId();
        return ByteBuffer.allocate(SIZE)
            .putInt(header.getSize())
            .putLong(testId.getLeastSignificantBits())
            .putLong(testId.getMostSignificantBits())
            .putInt(header.getPayloadType().getCode());
    }

    Header decode(final byte[] data)
    {
        final ByteBuffer buffer = ByteBuffer.allocate(SIZE).put(data);
        buffer.flip();
        final int size = buffer.getInt();
        final long leastSignificant = buffer.getLong();
        final long mostSignificant = buffer.getLong();
        final UUID testId = new UUID(mostSignificant, leastSignificant);
        final PayloadType payloadType = PayloadType.fromCode(buffer.getInt());
        return new Header(size, testId, payloadType);
    }
}
