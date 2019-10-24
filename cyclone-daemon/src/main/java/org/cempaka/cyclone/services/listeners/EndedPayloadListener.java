package org.cempaka.cyclone.services.listeners;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.function.BiConsumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.TestState;
import org.cempaka.cyclone.protocol.payloads.EndedPayload;
import org.cempaka.cyclone.protocol.payloads.Payload;
import org.cempaka.cyclone.protocol.payloads.PayloadType;
import org.cempaka.cyclone.services.NodeIdentifierProvider;
import org.cempaka.cyclone.storage.jdbi.TestRunStackTraceDataAccess;
import org.cempaka.cyclone.storage.jdbi.TestRunStatusDataAccess;

@Singleton
public class EndedPayloadListener implements BiConsumer<String, Payload>
{
    private final NodeIdentifierProvider nodeIdentifierProvider;
    private final TestRunStatusDataAccess testRunStatusDataAccess;
    private final TestRunStackTraceDataAccess testRunStackTraceDataAccess;

    @Inject
    public EndedPayloadListener(final NodeIdentifierProvider nodeIdentifierProvider,
                                final TestRunStatusDataAccess testRunStatusDataAccess,
                                final TestRunStackTraceDataAccess testRunStackTraceDataAccess)
    {
        this.nodeIdentifierProvider = checkNotNull(nodeIdentifierProvider);
        this.testRunStatusDataAccess = checkNotNull(testRunStatusDataAccess);
        this.testRunStackTraceDataAccess = checkNotNull(testRunStackTraceDataAccess);
    }

    @Override
    public void accept(final String testRunId, final Payload payload)
    {
        if (payload.getType() == PayloadType.ENDED) {
            final EndedPayload endedPayload = (EndedPayload) payload;
            testRunStatusDataAccess.updateState(TestState.ENDED,
                testRunId,
                nodeIdentifierProvider.get());
            endedPayload.getStackTrace().ifPresent(stackTrace ->
                testRunStackTraceDataAccess.insertEvent(testRunId, stackTrace));
        }
    }
}
