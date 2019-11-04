package org.cempaka.cyclone.services.listeners;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.TestState;
import org.cempaka.cyclone.listeners.payloads.Payload;
import org.cempaka.cyclone.listeners.payloads.PayloadType;
import org.cempaka.cyclone.services.NodeIdentifierProvider;
import org.cempaka.cyclone.storage.repositories.TestExecutionRepository;

@Singleton
public class EndedPayloadListener implements BiConsumer<String, Payload>
{
    private final NodeIdentifierProvider nodeIdentifierProvider;
    private final TestExecutionRepository testExecutionRepository;

    @Inject
    public EndedPayloadListener(final NodeIdentifierProvider nodeIdentifierProvider,
                                final TestExecutionRepository testExecutionRepository)
    {
        this.nodeIdentifierProvider = checkNotNull(nodeIdentifierProvider);
        this.testExecutionRepository = checkNotNull(testExecutionRepository);
    }

    @Override
    public void accept(final String testExecutionId, final Payload payload)
    {
        if (payload.getType() == PayloadType.ENDED) {
            testExecutionRepository.setState(UUID.fromString(testExecutionId),
                nodeIdentifierProvider.get(),
                TestState.ENDED);
        }
    }
}
