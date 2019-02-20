package org.cempaka.cyclone.protocol;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

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

    List<ByteBuffer> encode(final Payload payload)
    {
        final ByteBuffer payloadData = payloadEncoder.encode(payload);
        final Header header = new Header(payloadData.remaining(), payload.getType());
        final byte[] headerData = headerEncoder.encode(header).array();

        final ArrayList<ByteBuffer> buffers = new ArrayList<>();
        while (payloadData.hasRemaining()) {
            final int maxSize = SIZE - HeaderEncoder.SIZE;
            final int remaining = payloadData.remaining();
            final int size = remaining < maxSize ? remaining : maxSize;
            final byte[] data = new byte[size];
            payloadData.get(data);
            buffers.add(ByteBuffer.allocate(size + HeaderEncoder.SIZE)
                .put(headerData)
                .put(data));
        }
        return buffers;
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
