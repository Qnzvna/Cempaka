package org.cempaka.cyclone.channel;

import java.nio.ByteBuffer;
import org.cempaka.cyclone.channel.payloads.StartedPayload;

public class StartedPayloadEncoder implements PayloadEncoder<StartedPayload>
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
