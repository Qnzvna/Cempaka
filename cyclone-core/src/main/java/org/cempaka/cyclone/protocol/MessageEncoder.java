package org.cempaka.cyclone.protocol;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.cempaka.cyclone.protocol.payloads.Payload;

class MessageEncoder
{
    private final HeaderEncoder headerEncoder;
    private final PayloadEncoder payloadEncoder;

    static int SIZE = 4096;

    MessageEncoder()
    {
        this.headerEncoder = new HeaderEncoder();
        this.payloadEncoder = new PayloadEncoder();
    }

    ByteBuffer encode(final Payload payload)
    {
        final ByteBuffer payloadData = payloadEncoder.encode(payload);
        final Header header = new Header(payloadData.remaining(), payload.getType());
        final byte[] headerData = headerEncoder.encode(header).array();

        return ByteBuffer.allocate(HeaderEncoder.SIZE + payloadData.remaining())
            .put(headerData)
            .put(payloadData);
    }

    Payload decode(final byte[] data)
    {
        final ByteBuffer buffer = ByteBuffer.allocate(SIZE).put(data);
        buffer.flip();
        final byte[] headerData = new byte[HeaderEncoder.SIZE];
        buffer.get(headerData);
        final Header header = headerEncoder.decode(headerData);

        final byte[] payloadData = new byte[header.getSize()];
        buffer.get(payloadData);
        return payloadEncoder.decode(header.getPayloadType(), payloadData);
    }
}
