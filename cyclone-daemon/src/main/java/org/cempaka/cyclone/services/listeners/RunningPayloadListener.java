package org.cempaka.cyclone.services.listeners;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.Clock;
import java.time.Instant;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.MetricDataPoint;
import org.cempaka.cyclone.protocol.payloads.Payload;
import org.cempaka.cyclone.protocol.payloads.PayloadType;
import org.cempaka.cyclone.protocol.payloads.RunningPayload;
import org.cempaka.cyclone.storage.repositories.MeasurementsRepository;

@Singleton
public class RunningPayloadListener implements BiConsumer<String, Payload>
{
    private final MeasurementsRepository measurementsRepository;
    private final Clock clock;

    @Inject
    public RunningPayloadListener(final MeasurementsRepository measurementsRepository,
                                  final Clock clock)
    {
        this.measurementsRepository = checkNotNull(measurementsRepository);
        this.clock = checkNotNull(clock);
    }

    @Override
    public void accept(final String testRunId, final Payload payload)
    {
        if (payload.getType() == PayloadType.RUNNING) {
            final RunningPayload runningPayload = (RunningPayload) payload;
            final long now = Instant.now(clock).getEpochSecond();
            runningPayload.getMeasurements().forEach((name, value) ->
                measurementsRepository.put(testRunId, new MetricDataPoint(now, name, value)));
        }
    }
}
