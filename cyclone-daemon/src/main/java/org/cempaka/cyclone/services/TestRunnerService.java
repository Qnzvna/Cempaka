package org.cempaka.cyclone.services;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableMap;
import java.time.Clock;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.TestRunConfiguration;
import org.cempaka.cyclone.beans.TestSnapshot;
import org.cempaka.cyclone.protocol.PortProvider;
import org.cempaka.cyclone.protocol.Status;
import org.cempaka.cyclone.storage.TestSnapshotRepository;
import org.cempaka.cyclone.worker.WorkerManager;

@Singleton
public class TestRunnerService
{
    private final WorkerManager workerManager;
    private final PortProvider portProvider;
    private final TestSnapshotRepository testSnapshotRepository;
    private final Clock clock;

    @Inject
    public TestRunnerService(final WorkerManager workerManager,
                             final PortProvider portProvider,
                             final TestSnapshotRepository testSnapshotRepository,
                             final Clock clock)
    {
        this.workerManager = checkNotNull(workerManager);
        this.portProvider = checkNotNull(portProvider);
        this.testSnapshotRepository = checkNotNull(testSnapshotRepository);
        this.clock = checkNotNull(clock);
    }

    public UUID startTest(final TestRunConfiguration testRunConfiguration)
    {
        checkNotNull(testRunConfiguration);
        final UUID testUuid = UUID.randomUUID();
        final int cliPort = portProvider.getPort(testUuid.toString());
        workerManager.startTest(testUuid, cliPort, testRunConfiguration);
        final TestSnapshot snapshot = new TestSnapshot(testUuid.toString(),
            Instant.now(clock).getEpochSecond(),
            Status.INITIALIZED,
            ImmutableMap.of(),
            testRunConfiguration.getTestNames());
        testSnapshotRepository.put(testUuid.toString(), snapshot);
        return testUuid;
    }

    public void abortTest(final UUID testId)
    {
        checkNotNull(testId);
        workerManager.abortTest(testId);
        portProvider.releasePort(testId.toString());
    }

    public Set<UUID> getRunningTestsId()
    {
        return workerManager.getRunningTestsId();
    }
}
