package org.cempaka.cyclone.channel;

import java.nio.ByteBuffer;
import java.util.UUID;
import org.cempaka.cyclone.channel.payloads.Payload;

class MessageEncoder
{
    private final HeaderEncoder headerEncoder;
    private final TypePayloadEncoder typePayloadEncoder;

    MessageEncoder()
    {
        this.headerEncoder = new HeaderEncoder();
        this.typePayloadEncoder = new TypePayloadEncoder();
    }

    ByteBuffer encode(final Payload payload)
    {
        final ByteBuffer payloadData = typePayloadEncoder.encode(payload);
        final Header header = new Header(payloadData.remaining(), UUID.fromString(payload.getTestId()),
            payload.getType());
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
        return typePayloadEncoder.decode(header.getPayloadType(), testId, payloadData);
    }
}
