package org.cempaka.cyclone.listeners;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

import java.util.UUID;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.channel.LogPayload;
import org.cempaka.cyclone.channel.Payload;
import org.cempaka.cyclone.channel.PayloadType;
import org.cempaka.cyclone.log.ImmutableLogMessage;
import org.cempaka.cyclone.log.LogDataSink;

@Singleton
public class LogPayloadListener implements BiConsumer<String, Payload>
{
    private final LogDataSink logDataSink;

    @Inject
    public LogPayloadListener(final LogDataSink logDataSink)
    {
        this.logDataSink = checkNotNull(logDataSink);
    }

    @Override
    public void accept(final String testExecutionId, final Payload payload)
    {
        if (payload.getType() == PayloadType.LOG) {
            final LogPayload logPayload = (LogPayload) payload;
            logDataSink.accept(ImmutableLogMessage.builder()
                .testExecutionId(UUID.fromString(testExecutionId))
                .logLine(logPayload.getLogLine())
                .build());
        }
    }
}
