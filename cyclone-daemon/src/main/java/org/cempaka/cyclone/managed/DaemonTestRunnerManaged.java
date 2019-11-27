package org.cempaka.cyclone.managed;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.dropwizard.lifecycle.Managed;
import org.cempaka.cyclone.beans.TestState;
import org.cempaka.cyclone.beans.exceptions.ParcelNotFoundException;
import org.cempaka.cyclone.beans.exceptions.WorkerNotAvailableException;
import org.cempaka.cyclone.configurations.TestRunnerConfiguration;
import org.cempaka.cyclone.services.NodeIdentifierProvider;
import org.cempaka.cyclone.storage.repositories.TestExecutionRepository;
import org.cempaka.cyclone.tests.TestExecution;
import org.cempaka.cyclone.workers.WorkerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
public class DaemonTestRunnerManaged implements Managed
{
    private static final Logger LOG = LoggerFactory.getLogger(DaemonTestRunnerManaged.class);

    private final TestExecutionRepository testExecutionRepository;
    private final NodeIdentifierProvider nodeIdentifierProvider;
    private final WorkerManager workerManager;
    private final TestRunnerConfiguration testRunnerConfiguration;
    private final ScheduledExecutorService executorService;

    @Inject
    public DaemonTestRunnerManaged(final TestExecutionRepository testExecutionRepository,
                                   final NodeIdentifierProvider nodeIdentifierProvider,
                                   final WorkerManager workerManager,
                                   final TestRunnerConfiguration testRunnerConfiguration)
    {
        this.testExecutionRepository = checkNotNull(testExecutionRepository);
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
                testExecutionRepository.get(nodeIdentifierProvider.get(), TestState.INITIALIZED)
                    .forEach(this::startTest);
            } catch (Exception e) {
                LOG.error("Test initialization failure.", e);
            }
        }, 0, periodInterval, TimeUnit.SECONDS);
    }

    private void startTest(final TestExecution testExecution)
    {
        final UUID testId = testExecution.getId();
        try {
            workerManager.startTest(testId, testExecution.getProperties());
            testExecutionRepository.setState(testId, testExecution.getNode(), TestState.STARTED);
        } catch (ParcelNotFoundException e) {
            LOG.warn("Parcel for test {} could'nt be found.", testId);
            testExecutionRepository.setState(testId, testExecution.getNode(), TestState.ERROR);
        } catch (WorkerNotAvailableException e) {
            LOG.warn("Worker is not available right now.");
        }
    }


    @Override
    public void stop() throws InterruptedException
    {
        executorService.shutdown();
        executorService.awaitTermination(testRunnerConfiguration.getAwaitInterval(), TimeUnit.SECONDS);
    }
}
