package org.cempaka.cyclone.services;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.TestState;
import org.cempaka.cyclone.storage.repositories.TestExecutionRepository;
import org.cempaka.cyclone.tests.ImmutableTestExecution;
import org.cempaka.cyclone.tests.TestExecutionProperties;

@Singleton
public class DistributedTestRunnerService implements TestRunnerService
{
    private final NodeStatusService nodeStatusService;
    private final TestExecutionRepository testExecutionRepository;

    @Inject
    public DistributedTestRunnerService(final NodeStatusService nodeStatusService,
                                        final TestExecutionRepository testExecutionRepository)
    {
        this.nodeStatusService = checkNotNull(nodeStatusService);
        this.testExecutionRepository = checkNotNull(testExecutionRepository);
    }

    @Override
    public UUID startTest(final TestExecutionProperties testExecutionProperties)
    {
        final Set<String> liveNodes = nodeStatusService.getLiveNodes();
        final Set<String> nodesToRun = testExecutionProperties.getNodes();
        checkState(liveNodes.containsAll(nodesToRun), "Not all nodes are alive");
        final UUID testRunId = UUID.randomUUID();
        nodesToRun.forEach(node -> testExecutionRepository.put(ImmutableTestExecution.builder()
            .id(testRunId)
            .node(node)
            .state(TestState.INITIALIZED)
            .properties(testExecutionProperties)
            .build()));
        return testRunId;
    }

    @Override
    public void stopTest(final UUID testId)
    {
        testExecutionRepository.setStates(testId, TestState.ABORTED);
    }
}
