package org.cempaka.cyclone.listeners;

import static org.cempaka.cyclone.core.utils.Preconditions.checkNotNull;

import java.util.UUID;
import java.util.function.BiConsumer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.cempaka.cyclone.core.channel.LogPayload;
import org.cempaka.cyclone.core.channel.Payload;
import org.cempaka.cyclone.core.channel.PayloadType;
import org.cempaka.cyclone.core.log.ImmutableLogMessage;
import org.cempaka.cyclone.core.log.LogDataSink;

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
