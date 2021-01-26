package org.cempaka.cyclone.channel;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

import java.nio.ByteBuffer;

class TypePayloadEncoder implements PayloadEncoder<Payload>
{
    private final PayloadEncoder<EndedPayload> endedPayloadEncoder = new EndedPayloadEncoder();
    private final PayloadEncoder<LogPayload> logPayloadEncoder = new LogPayloadEncoder();
    private final PayloadEncoder<RunningPayload> runningPayloadEncoder = new RunningPayloadEncoder();
    private final PayloadEncoder<StartedPayload> startedPayloadEncoder = new StartedPayloadEncoder();

    @Override
    public ByteBuffer encode(final Payload payload)
    {
        checkNotNull(payload);
        final PayloadType type = payload.getType();
        switch (type) {
            case STARTED:
                return startedPayloadEncoder.encode((StartedPayload) payload);
            case RUNNING:
                return runningPayloadEncoder.encode((RunningPayload) payload);
            case ENDED:
                return endedPayloadEncoder.encode((EndedPayload) payload);
            case LOG:
                return logPayloadEncoder.encode((LogPayload) payload);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public Payload decode(final String testId, final byte[] data)
    {
        throw new UnsupportedOperationException();
    }

    public Payload decode(final PayloadType type, final String testId, final byte[] data)
    {
        checkNotNull(testId);
        checkNotNull(data);
        switch (type) {
            case STARTED:
                return new StartedPayload(testId);
            case RUNNING:
                return runningPayloadEncoder.decode(testId, data);
            case ENDED:
                return endedPayloadEncoder.decode(testId, data);
            case LOG:
                return logPayloadEncoder.decode(testId, data);
            default:
                throw new IllegalArgumentException();
        }
    }
}
