package org.cempaka.cyclone.services.listeners;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.function.BiConsumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.TestState;
import org.cempaka.cyclone.protocol.payloads.Payload;
import org.cempaka.cyclone.protocol.payloads.PayloadType;
import org.cempaka.cyclone.services.NodeIdentifierProvider;
import org.cempaka.cyclone.storage.jdbi.TestRunStatusDataAccess;

@Singleton
public class StartedPayloadListener implements BiConsumer<String, Payload>
{
    private final NodeIdentifierProvider nodeIdentifierProvider;
    private final TestRunStatusDataAccess testRunStatusDataAccess;

    @Inject
    public StartedPayloadListener(
        final NodeIdentifierProvider nodeIdentifierProvider,
        final TestRunStatusDataAccess testRunStatusDataAccess)
    {
        this.nodeIdentifierProvider = checkNotNull(nodeIdentifierProvider);
        this.testRunStatusDataAccess = checkNotNull(testRunStatusDataAccess);
    }

    @Override
    public void accept(final String testRunId, final Payload payload)
    {
        if (payload.getType() == PayloadType.STARTED) {
            testRunStatusDataAccess.updateState(TestState.STARTED,
                testRunId,
                nodeIdentifierProvider.get());
        }
    }
}
