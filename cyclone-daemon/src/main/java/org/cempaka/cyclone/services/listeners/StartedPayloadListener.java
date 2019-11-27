package org.cempaka.cyclone.services.listeners;

import org.cempaka.cyclone.beans.TestState;
import org.cempaka.cyclone.listeners.payloads.Payload;
import org.cempaka.cyclone.listeners.payloads.PayloadType;
import org.cempaka.cyclone.services.NodeIdentifierProvider;
import org.cempaka.cyclone.storage.repositories.TestExecutionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
public class StartedPayloadListener implements BiConsumer<String, Payload>
{
    private static final Logger LOG = LoggerFactory.getLogger(StartedPayloadListener.class);

    private final NodeIdentifierProvider nodeIdentifierProvider;
    private final TestExecutionRepository testExecutionRepository;

    @Inject
    public StartedPayloadListener(final NodeIdentifierProvider nodeIdentifierProvider,
                                  final TestExecutionRepository testExecutionRepository)
    {
        this.nodeIdentifierProvider = checkNotNull(nodeIdentifierProvider);
        this.testExecutionRepository = checkNotNull(testExecutionRepository);
    }

    @Override
    public void accept(final String testExecutionId, final Payload payload)
    {
        if (payload.getType() == PayloadType.STARTED) {
            LOG.debug("Started payload for {} {}.", testExecutionId, payload);
            testExecutionRepository.setState(UUID.fromString(testExecutionId),
                nodeIdentifierProvider.get(),
                TestState.STARTED);
        }
    }
}
