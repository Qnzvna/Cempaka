package org.cempaka.cyclone.core.channel;

import java.nio.ByteBuffer;

interface PayloadEncoder<V extends Payload>
{
    ByteBuffer encode(V payload);

    V decode(String testId, byte[] data);
}
