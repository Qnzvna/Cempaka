package org.cempaka.cyclone.managed;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.dropwizard.lifecycle.Managed;
import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.cempaka.cyclone.configurations.ClusterConfiguration;
import org.cempaka.cyclone.configurations.StalledTestCleanerConfiguration;
import org.cempaka.cyclone.services.NodeStatusService;
import org.cempaka.cyclone.services.TestRunnerService;
import org.cempaka.cyclone.storage.repositories.TestExecutionRepository;
import org.cempaka.cyclone.tests.TestExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class StalledTestCleanerManaged implements Managed
{
    private static final Logger LOG = LoggerFactory.getLogger(StalledTestCleanerManaged.class);

    private final TestExecutionRepository testExecutionRepository;
    private final NodeStatusService nodeStatusService;
    private final TestRunnerService testRunnerService;
    private final ClusterConfiguration clusterConfiguration;
    private final StalledTestCleanerConfiguration stalledTestCleanerConfiguration;
    private final Clock clock;
    private final ScheduledExecutorService executorService;

    @Inject
    public StalledTestCleanerManaged(final TestExecutionRepository testExecutionRepository,
                                     final NodeStatusService nodeStatusService,
                                     final TestRunnerService testRunnerService,
                                     final ClusterConfiguration clusterConfiguration,
                                     final StalledTestCleanerConfiguration stalledTestCleanerConfiguration,
                                     final Clock clock)
    {
        this.testExecutionRepository = checkNotNull(testExecutionRepository);
        this.nodeStatusService = checkNotNull(nodeStatusService);
        this.testRunnerService = checkNotNull(testRunnerService);
        this.clusterConfiguration = checkNotNull(clusterConfiguration);
        this.stalledTestCleanerConfiguration = checkNotNull(stalledTestCleanerConfiguration);
        this.clock = checkNotNull(clock);
        this.executorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder()
            .setNameFormat("StalledTestCleanerManaged-%d")
            .build());
    }

    @Override
    public void start()
    {
        executorService.scheduleAtFixedRate(() -> {
            try {
                execute();
                LOG.debug("Stalled test executions cleaned.");
            } catch (Exception e) {
                LOG.error("Cleaning stalled test executions failed.", e);
            }
        }, 10, stalledTestCleanerConfiguration.getPeriodInterval(), TimeUnit.SECONDS);
    }

    void execute()
    {
        testExecutionRepository.getUpdatedLaterThan(
            Instant.now(clock).minusSeconds(clusterConfiguration.getHeartbeatInterval()))
            .stream()
            .filter(this::isNodeNotActive)
            .forEach(testExecution -> {
                testRunnerService.stopTest(testExecution.getId());
                LOG.info("Stalled test {} aborted.", testExecution);
            });
    }

    private boolean isNodeNotActive(final TestExecution testExecution)
    {
        return !nodeStatusService.getNodesStatus().getOrDefault(testExecution.getNode(), false);
    }

    @Override
    public void stop() throws InterruptedException
    {
        executorService.shutdown();
        executorService.awaitTermination(stalledTestCleanerConfiguration.getAwaitInterval(), TimeUnit.SECONDS);
    }
}
