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
public class EndedPayloadListener implements BiConsumer<String, Payload>
{
    private static final Logger LOG = LoggerFactory.getLogger(EndedPayloadListener.class);

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
            LOG.debug("Ended payload for {} {}.", testExecutionId, payload);
            testExecutionRepository.setState(UUID.fromString(testExecutionId),
                nodeIdentifierProvider.get(),
                TestState.ENDED);
        }
    }
}
