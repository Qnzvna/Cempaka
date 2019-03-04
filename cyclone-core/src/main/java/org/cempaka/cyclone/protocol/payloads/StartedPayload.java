package org.cempaka.cyclone.protocol.payloads;

public class StartedPayload implements Payload
{
    @Override
    public PayloadType getType()
    {
        return PayloadType.STARTED;
    }
}
