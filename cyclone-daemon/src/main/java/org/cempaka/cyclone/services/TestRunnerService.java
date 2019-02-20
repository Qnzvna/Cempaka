package org.cempaka.cyclone.services;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.TestRunConfiguration;
import org.cempaka.cyclone.protocol.PortProvider;
import org.cempaka.cyclone.worker.WorkerManager;

@Singleton
public class TestRunnerService
{
    private final WorkerManager workerManager;
    private final PortProvider portProvider;

    @Inject
    public TestRunnerService(final WorkerManager workerManager,
                             final PortProvider portProvider)
    {
        this.workerManager = checkNotNull(workerManager);
        this.portProvider = checkNotNull(portProvider);
    }

    public UUID startTest(final TestRunConfiguration testRunConfiguration)
    {
        checkNotNull(testRunConfiguration);
        final UUID testId = UUID.randomUUID();
        final int cliPort = portProvider.getPort(testId.toString());
        workerManager.startTest(testId, cliPort, testRunConfiguration);
        return testId;
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
