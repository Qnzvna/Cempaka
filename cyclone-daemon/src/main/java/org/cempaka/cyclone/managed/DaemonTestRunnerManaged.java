package org.cempaka.cyclone.managed;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.dropwizard.lifecycle.Managed;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.TestRunConfiguration;
import org.cempaka.cyclone.beans.TestState;
import org.cempaka.cyclone.beans.exceptions.ParcelNotFoundException;
import org.cempaka.cyclone.beans.exceptions.WorkerNotAvailableException;
import org.cempaka.cyclone.configuration.TestRunnerConfiguration;
import org.cempaka.cyclone.services.NodeIdentifierProvider;
import org.cempaka.cyclone.storage.data.TestRunStatusDataAccess;
import org.cempaka.cyclone.worker.WorkerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DaemonTestRunnerManaged implements Managed
{
    private static final Logger LOG = LoggerFactory.getLogger(DaemonTestRunnerManaged.class);

    private final TestRunStatusDataAccess testRunStatusDataAccess;
    private final NodeIdentifierProvider nodeIdentifierProvider;
    private final WorkerManager workerManager;
    private final TestRunnerConfiguration testRunnerConfiguration;
    private final ScheduledExecutorService executorService;

    @Inject
    public DaemonTestRunnerManaged(final TestRunStatusDataAccess testRunStatusDataAccess,
                                   final NodeIdentifierProvider nodeIdentifierProvider,
                                   final WorkerManager workerManager,
                                   final TestRunnerConfiguration testRunnerConfiguration)
    {
        this.testRunStatusDataAccess = checkNotNull(testRunStatusDataAccess);
        this.nodeIdentifierProvider = checkNotNull(nodeIdentifierProvider);
        this.workerManager = checkNotNull(workerManager);
        this.testRunnerConfiguration = checkNotNull(testRunnerConfiguration);
        this.executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder()
            .setNameFormat("DaemonTestRunnerManaged-%d")
            .build());
    }

    @Override
    public void start()
    {
        final int periodInterval = testRunnerConfiguration.getPeriodInterval();
        executorService.scheduleAtFixedRate(() -> {
            try {
                startTests();
            } catch (Exception e) {
                LOG.error("Test initialization failure.", e);
            }
        }, 0, periodInterval, TimeUnit.SECONDS);
    }

    private void startTests()
    {
        final String nodeIdentifier = nodeIdentifierProvider.get();
        testRunStatusDataAccess.getTests(nodeIdentifier, TestState.INITIALIZED)
            .forEach(testId -> startTest(nodeIdentifier, testId));
    }

    private void startTest(final String nodeIdentifier, final String testId)
    {
        final TestRunConfiguration testRunConfiguration = testRunStatusDataAccess.getConfiguration(testId);
        final UUID testUuid = UUID.fromString(testId);
        try {
            workerManager.startTest(testUuid,
                testRunConfiguration.getParcelId(),
                testRunConfiguration.getTestName(),
                testRunConfiguration.getLoopCount(),
                testRunConfiguration.getThreadsNumber(),
                testRunConfiguration.getParameters());
            testRunStatusDataAccess.updateState(TestState.STARTED, testId, nodeIdentifier);
        } catch (ParcelNotFoundException e) {
            LOG.warn("Parcel for test {} could'nt be found.", testId);
            testRunStatusDataAccess.updateState(TestState.ERROR, testId, nodeIdentifier);
        } catch (WorkerNotAvailableException e) {
            LOG.warn("Worker is not available right now.");
        }
    }


    @Override
    public void stop() throws InterruptedException
    {
        executorService.shutdown();
        executorService.awaitTermination(testRunnerConfiguration.getAwaitInterval(),
            TimeUnit.SECONDS);
    }
}
