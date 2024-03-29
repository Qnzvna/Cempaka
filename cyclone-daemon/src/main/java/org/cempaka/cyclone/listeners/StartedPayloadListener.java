package org.cempaka.cyclone.listeners;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;
import java.util.function.BiConsumer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.cempaka.cyclone.beans.TestState;
import org.cempaka.cyclone.core.channel.Payload;
import org.cempaka.cyclone.core.channel.PayloadType;
import org.cempaka.cyclone.services.NodeIdentifierProvider;
import org.cempaka.cyclone.storage.repositories.TestExecutionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
