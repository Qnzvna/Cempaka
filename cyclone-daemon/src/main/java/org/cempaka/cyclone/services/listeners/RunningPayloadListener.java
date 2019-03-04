package org.cempaka.cyclone.services.listeners;

import static com.google.common.base.Preconditions.checkNotNull;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.protocol.payloads.Payload;
import org.cempaka.cyclone.protocol.payloads.PayloadType;
import org.cempaka.cyclone.protocol.payloads.RunningPayload;
import org.cempaka.cyclone.storage.TestRunMetricDataAcess;

@Singleton
public class RunningPayloadListener implements BiConsumer<String, Payload>
{
    private final TestRunMetricDataAcess testRunMetricDataAcess;
    private final Clock clock;

    @Inject
    public RunningPayloadListener(final TestRunMetricDataAcess testRunMetricDataAcess,
                                  final Clock clock)
    {
        this.testRunMetricDataAcess = checkNotNull(testRunMetricDataAcess);
        this.clock = checkNotNull(clock);
    }

    @Override
    public void accept(final String testRunId, final Payload payload)
    {
        if (payload.getType() == PayloadType.RUNNING) {
            final RunningPayload runningPayload = (RunningPayload) payload;
            final Timestamp timestamp = Timestamp.from(Instant.now(clock));
            runningPayload.getMeasurements().forEach((name, value) ->
                testRunMetricDataAcess.insertMetric(testRunId,
                    timestamp,
                    MetricType.MEASUREMENT.toString(),
                    name,
                    value));
            runningPayload.getFailedExecutions().forEach((name, value) ->
                testRunMetricDataAcess.insertMetric(testRunId,
                    timestamp,
                    MetricType.FAILURE.toString(),
                    name,
                    value));
            runningPayload.getSucccessExecutions().forEach((name, value) ->
                testRunMetricDataAcess.insertMetric(testRunId,
                    timestamp,
                    MetricType.SUCCESS.toString(),
                    name,
                    value));
        }
    }

    enum MetricType
    {
        MEASUREMENT,
        SUCCESS,
        FAILURE;
    }
}
