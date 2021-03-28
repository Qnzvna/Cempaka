package org.cempaka.cyclone.services;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.NotActiveException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.TestState;
import org.cempaka.cyclone.beans.exceptions.ParcelNotFoundException;
import org.cempaka.cyclone.beans.exceptions.TestNotFoundException;
import org.cempaka.cyclone.storage.repositories.ParcelRepository;
import org.cempaka.cyclone.storage.repositories.TestExecutionRepository;
import org.cempaka.cyclone.storage.repositories.TestRepository;
import org.cempaka.cyclone.tests.ImmutableTestExecution;
import org.cempaka.cyclone.tests.Test;
import org.cempaka.cyclone.tests.TestExecutionProperties;

@Singleton
public class DistributedTestRunnerService implements TestRunnerService
{
    private final NodeStatusService nodeStatusService;
    private final ParcelRepository parcelRepository;
    private final TestExecutionRepository testExecutionRepository;
    private final TestRepository testRepository;

    @Inject
    public DistributedTestRunnerService(final NodeStatusService nodeStatusService,
                                        final ParcelRepository parcelRepository,
                                        final TestExecutionRepository testExecutionRepository,
                                        final TestRepository testRepository)
    {
        this.nodeStatusService = checkNotNull(nodeStatusService);
        this.parcelRepository = checkNotNull(parcelRepository);
        this.testExecutionRepository = checkNotNull(testExecutionRepository);
        this.testRepository = checkNotNull(testRepository);
    }

    @Override
    public UUID startTest(final TestExecutionProperties testExecutionProperties)
    {
        final Set<String> nodesToRun = testExecutionProperties.getNodes();

        checkAvailableNodes(nodesToRun);
        checkParcel(testExecutionProperties.getParcelId());
        checkTest(testExecutionProperties);

        final UUID testRunId = UUID.randomUUID();
        nodesToRun.forEach(node -> testExecutionRepository.put(ImmutableTestExecution.builder()
            .id(testRunId)
            .node(node)
            .state(TestState.INITIALIZED)
            .properties(testExecutionProperties)
            .build()));
        return testRunId;
    }

    private void checkTest(final TestExecutionProperties testExecutionProperties)
    {
        final Optional<Test> test = testRepository.getByIdAndName(testExecutionProperties.getParcelId(),
            testExecutionProperties.getTestName());
        if (!test.isPresent()) {
            throw new TestNotFoundException();
        }
    }

    private void checkParcel(final UUID parcelId)
    {
        if (!parcelRepository.contains(parcelId)) {
            throw new ParcelNotFoundException();
        }
    }

    private void checkAvailableNodes(final Set<String> nodesToRun)
    {
        final Set<String> liveNodes = nodeStatusService.getLiveNodes();
        for (final String node : nodesToRun) {
            if (!liveNodes.contains(node)) {
                throw new NodeNotAliveException(node);
            }
        }
    }

    @Override
    public void stopTest(final UUID testId)
    {
        testExecutionRepository.setStates(testId, TestState.ABORTED);
    }
}
