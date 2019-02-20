package org.cempaka.cyclone.services;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.Clock;
import java.time.Instant;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.TestSnapshot;
import org.cempaka.cyclone.protocol.MeasurementsPayload;
import org.cempaka.cyclone.protocol.Payload;
import org.cempaka.cyclone.storage.TestSnapshotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class MeasurementsListener implements BiConsumer<String, Payload>
{
    private static final Logger LOG = LoggerFactory.getLogger(MeasurementsListener.class);

    private final TestSnapshotRepository testSnapshotRepository;
    private final Clock clock;

    @Inject
    public MeasurementsListener(final TestSnapshotRepository testSnapshotRepository, final Clock clock)
    {
        this.testSnapshotRepository = checkNotNull(testSnapshotRepository);
        this.clock = checkNotNull(clock);
    }

    @Override
    public void accept(final String testUuid, final Payload payload)
    {
        final MeasurementsPayload measurementsPayload = (MeasurementsPayload) payload;
        final TestSnapshot testSnapshot = new TestSnapshot(Instant.now(clock).getEpochSecond(),
            measurementsPayload.getStatus(),
            measurementsPayload.getSnapshots());
        testSnapshotRepository.put(testUuid, testSnapshot);
        LOG.info("Metrics from {} received. {}", testUuid, measurementsPayload.getSnapshots());
    }
}
