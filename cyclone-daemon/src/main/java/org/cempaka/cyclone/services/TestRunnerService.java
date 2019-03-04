package org.cempaka.cyclone.services;

import static com.google.common.base.Preconditions.checkNotNull;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.EventType;
import org.cempaka.cyclone.beans.TestRunConfiguration;
import org.cempaka.cyclone.protocol.PortProvider;
import org.cempaka.cyclone.storage.TestRunEventDataAccess;
import org.cempaka.cyclone.storage.TestRunMetadataDataAcess;
import org.cempaka.cyclone.worker.WorkerManager;

@Singleton
public class TestRunnerService
{
    private final WorkerManager workerManager;
    private final PortProvider portProvider;
    private final TestRunEventDataAccess testRunEventDataAccess;
    private final TestRunMetadataDataAcess testRunMetadataDataAcess;
    private final Clock clock;

    @Inject
    public TestRunnerService(final WorkerManager workerManager,
                             final PortProvider portProvider,
                             final TestRunEventDataAccess testRunEventDataAccess,
                             final TestRunMetadataDataAcess testRunMetadataDataAcess,
                             final Clock clock)
    {
        this.workerManager = checkNotNull(workerManager);
        this.portProvider = checkNotNull(portProvider);
        this.testRunEventDataAccess = checkNotNull(testRunEventDataAccess);
        this.testRunMetadataDataAcess = checkNotNull(testRunMetadataDataAcess);
        this.clock = checkNotNull(clock);
    }

    public UUID startTest(final TestRunConfiguration testRunConfiguration)
    {
        checkNotNull(testRunConfiguration);
        final UUID testUuid = UUID.randomUUID();
        final int cliPort = portProvider.getPort(testUuid.toString());
        workerManager.startTest(testUuid, cliPort, testRunConfiguration)
            .whenComplete(this::saveResultSnapshot);
        final Timestamp timestamp = Timestamp.from(Instant.now(clock));
        testRunEventDataAccess.insertEvent(testUuid.toString(),
            timestamp,
            EventType.INITIALIZED.toString());
        testRunMetadataDataAcess.insertInitializationMetadata(testUuid.toString(),
            testRunConfiguration.getTestName(),
            testRunConfiguration.getParameters());
        return testUuid;
    }

    private void saveResultSnapshot(final UUID testUuid, final Throwable ignore)
    {
        final Timestamp timestamp = Timestamp.from(Instant.now(clock));
        testRunEventDataAccess.insertEvent(testUuid.toString(),
            timestamp,
            EventType.CLEANED.toString());
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
