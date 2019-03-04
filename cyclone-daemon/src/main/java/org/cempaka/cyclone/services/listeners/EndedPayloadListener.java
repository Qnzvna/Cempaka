package org.cempaka.cyclone.services.listeners;

import static com.google.common.base.Preconditions.checkNotNull;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.EventType;
import org.cempaka.cyclone.protocol.payloads.EndedPayload;
import org.cempaka.cyclone.protocol.payloads.Payload;
import org.cempaka.cyclone.protocol.payloads.PayloadType;
import org.cempaka.cyclone.storage.TestRunEventDataAccess;
import org.cempaka.cyclone.storage.TestRunStackTraceDataAcess;

@Singleton
public class EndedPayloadListener implements BiConsumer<String, Payload>
{
    private final TestRunEventDataAccess testRunEventDataAccess;
    private final TestRunStackTraceDataAcess testRunStackTraceDataAcess;
    private final Clock clock;

    @Inject
    public EndedPayloadListener(final TestRunEventDataAccess testRunEventDataAccess,
                                final TestRunStackTraceDataAcess testRunStackTraceDataAcess,
                                final Clock clock)
    {
        this.testRunEventDataAccess = checkNotNull(testRunEventDataAccess);
        this.testRunStackTraceDataAcess = checkNotNull(testRunStackTraceDataAcess);
        this.clock = checkNotNull(clock);
    }

    @Override
    public void accept(final String testRunId, final Payload payload)
    {
        if (payload.getType() == PayloadType.ENDED) {
            final EndedPayload endedPayload = (EndedPayload) payload;
            final Timestamp timestamp = Timestamp.from(Instant.now(clock));
            testRunEventDataAccess.insertEvent(testRunId, timestamp, EventType.ENDED.toString());
            endedPayload.getStackTrace().ifPresent(stackTrace ->
                testRunStackTraceDataAcess.insertEvent(testRunId, stackTrace));
        }
    }
}
