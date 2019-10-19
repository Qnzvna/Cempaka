package org.cempaka.cyclone.protocol;

import java.util.UUID;
import org.cempaka.cyclone.protocol.payloads.Payload;

import java.nio.ByteBuffer;

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
        final Header header = new Header(payloadData.remaining(), UUID.fromString(payload.getTestId()), payload.getType());
        final byte[] headerData = headerEncoder.encode(header).array();

        return ByteBuffer.allocate(HeaderEncoder.SIZE + payloadData.remaining())
            .put(headerData)
            .put(payloadData);
    }

    Payload decode(final byte[] data)
    {
        final ByteBuffer buffer = ByteBuffer.allocate(data.length).put(data);
        buffer.flip();
        final byte[] headerData = new byte[HeaderEncoder.SIZE];
        buffer.get(headerData);
        final Header header = headerEncoder.decode(headerData);
        final String testId = header.getTestId().toString();
        final byte[] payloadData = new byte[header.getSize()];
        buffer.get(payloadData);
        return payloadEncoder.decode(header.getPayloadType(), testId, payloadData);
    }
}
