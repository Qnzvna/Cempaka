package org.cempaka.cyclone.services;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.TestRunConfiguration;
import org.cempaka.cyclone.beans.TestState;
import org.cempaka.cyclone.storage.data.TestRunConfigurationDataAccess;
import org.cempaka.cyclone.storage.data.TestRunStatusDataAccess;

@Singleton
public class DistributedTestRunnerService implements TestRunnerService
{
    private final NodeStatusService nodeStatusService;
    private final TestRunStatusDataAccess testRunStatusDataAccess;
    private final TestRunConfigurationDataAccess testRunConfigurationDataAccess;

    @Inject
    public DistributedTestRunnerService(final NodeStatusService nodeStatusService,
                                        final TestRunStatusDataAccess testRunStatusDataAccess,
                                        final TestRunConfigurationDataAccess testRunConfigurationDataAccess)
    {
        this.nodeStatusService = checkNotNull(nodeStatusService);
        this.testRunStatusDataAccess = checkNotNull(testRunStatusDataAccess);
        this.testRunConfigurationDataAccess = checkNotNull(testRunConfigurationDataAccess);
    }

    @Override
    public UUID startTest(final TestRunConfiguration testRunConfiguration)
    {
        final Set<String> liveNodes = nodeStatusService.getLiveNodes();
        final Set<String> nodesToRun = testRunConfiguration.getNodeIdentifiers();
        checkState(liveNodes.containsAll(nodesToRun), "Not all nodes are alive");
        final UUID testRunId = UUID.randomUUID();
        testRunConfigurationDataAccess.insertTestRunConfiguration(testRunId.toString(),
            testRunConfiguration);
        nodesToRun.forEach(nodeIdentifier -> testRunStatusDataAccess.insert(testRunId.toString(),
            nodeIdentifier,
            TestState.INITIALIZED));
        return testRunId;
    }

    @Override
    public void abortTest(final UUID testId)
    {
        testRunStatusDataAccess.updateAllStates(TestState.ABORTED, testId.toString());
    }
}
