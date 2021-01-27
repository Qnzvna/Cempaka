package org.cempaka.cyclone.core.channel;

import java.nio.ByteBuffer;

class StartedPayloadEncoder implements PayloadEncoder<StartedPayload>
{
    @Override
    public ByteBuffer encode(final StartedPayload payload)
    {
        return ByteBuffer.wrap(new byte[]{});
    }

    @Override
    public StartedPayload decode(final String testId, final byte[] data)
    {
        return new StartedPayload(testId);
    }
}
