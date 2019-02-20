package org.cempaka.cyclone.protocol;

import java.nio.ByteBuffer;

class HeaderEncoder
{
    static int SIZE = 8;

    ByteBuffer encode(final Header header)
    {
        return ByteBuffer.allocate(SIZE)
            .putInt(header.getSize())
            .putInt(header.getPayloadType().getCode());
    }

    Header decode(final byte[] data)
    {
        final ByteBuffer buffer = ByteBuffer.allocate(SIZE).put(data);
        buffer.flip();
        final int size = buffer.getInt();
        final PayloadType payloadType = PayloadType.fromCode(buffer.getInt());
        return new Header(size, payloadType);
    }
}
