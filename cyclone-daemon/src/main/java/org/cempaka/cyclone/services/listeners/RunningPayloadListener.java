package org.cempaka.cyclone.services.listeners;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.Clock;
import java.time.Instant;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.MetricDataPoint;
import org.cempaka.cyclone.listeners.payloads.Payload;
import org.cempaka.cyclone.listeners.payloads.PayloadType;
import org.cempaka.cyclone.listeners.payloads.RunningPayload;
import org.cempaka.cyclone.storage.repositories.TestMetricRepository;

@Singleton
public class RunningPayloadListener implements BiConsumer<String, Payload>
{
    private final TestMetricRepository testMetricRepository;
    private final Clock clock;

    @Inject
    public RunningPayloadListener(final TestMetricRepository testMetricRepository,
                                  final Clock clock)
    {
        this.testMetricRepository = checkNotNull(testMetricRepository);
        this.clock = checkNotNull(clock);
    }

    @Override
    public void accept(final String testRunId, final Payload payload)
    {
        if (payload.getType() == PayloadType.RUNNING) {
            final RunningPayload runningPayload = (RunningPayload) payload;
            final long now = Instant.now(clock).toEpochMilli();
            runningPayload.getMeasurements().forEach((name, value) ->
                testMetricRepository.put(testRunId, new MetricDataPoint(now, name, value)));
        }
    }
}
