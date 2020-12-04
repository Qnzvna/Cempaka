package org.cempaka.cyclone.channel;

import java.nio.ByteBuffer;
import org.cempaka.cyclone.channel.payloads.Payload;

public interface PayloadEncoder<V extends Payload>
{
    ByteBuffer encode(V payload);

    V decode(String testId, byte[] data);
}
