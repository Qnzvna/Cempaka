package org.cempaka.cyclone.listeners;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import java.util.function.BiConsumer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.cempaka.cyclone.beans.MetricDataPoint;
import org.cempaka.cyclone.core.channel.Payload;
import org.cempaka.cyclone.core.channel.PayloadType;
import org.cempaka.cyclone.core.channel.RunningPayload;
import org.cempaka.cyclone.services.NodeIdentifierProvider;
import org.cempaka.cyclone.storage.repositories.TestExecutionRepository;
import org.cempaka.cyclone.storage.repositories.TestMetricRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class RunningPayloadListener implements BiConsumer<String, Payload>
{
    private static final Logger LOG = LoggerFactory.getLogger(RunningPayloadListener.class);

    private final TestMetricRepository testMetricRepository;
    private final TestExecutionRepository testExecutionRepository;
    private final NodeIdentifierProvider nodeIdentifierProvider;
    private final Clock clock;

    @Inject
    public RunningPayloadListener(final TestMetricRepository testMetricRepository,
                                  final TestExecutionRepository testExecutionRepository,
                                  final NodeIdentifierProvider nodeIdentifierProvider,
                                  final Clock clock)
    {
        this.testMetricRepository = checkNotNull(testMetricRepository);
        this.testExecutionRepository = checkNotNull(testExecutionRepository);
        this.nodeIdentifierProvider = checkNotNull(nodeIdentifierProvider);
        this.clock = checkNotNull(clock);
    }

    @Override
    public void accept(final String testExecutionId, final Payload payload)
    {
        if (payload.getType() == PayloadType.RUNNING) {
            final UUID executionId = UUID.fromString(testExecutionId);
            LOG.debug("Running payload for {} {}.", testExecutionId, payload);
            final RunningPayload runningPayload = (RunningPayload) payload;
            final Instant now = Instant.now(clock);
            testExecutionRepository.updateTimestamp(executionId, nodeIdentifierProvider.get(), now);
            runningPayload.getMeasurements().forEach((name, value) ->
                testMetricRepository.put(executionId, new MetricDataPoint(now.toEpochMilli(), name, value)));
        }
    }
}
