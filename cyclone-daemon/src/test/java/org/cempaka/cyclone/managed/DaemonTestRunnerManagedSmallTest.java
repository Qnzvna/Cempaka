package org.cempaka.cyclone.managed;

import static org.awaitility.Awaitility.await;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.collect.ImmutableSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.cempaka.cyclone.beans.TestRunConfiguration;
import org.cempaka.cyclone.beans.TestState;
import org.cempaka.cyclone.configuration.TestRunnerConfiguration;
import org.cempaka.cyclone.services.NodeIdentifierProvider;
import org.cempaka.cyclone.storage.data.TestRunStatusDataAccess;
import org.cempaka.cyclone.worker.WorkerManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DaemonTestRunnerManagedSmallTest
{
    private static final UUID TEST_ID = UUID.randomUUID();
    private static final String NODE_IDENTIFIER = "localhost";

    @Mock
    private TestRunStatusDataAccess testRunStatusDataAccess;
    @Mock
    private NodeIdentifierProvider nodeIdentifierProvider;
    @Mock
    private WorkerManager workerManager;
    @Mock
    private TestRunnerConfiguration testRunnerConfiguration;
    @Mock
    private TestRunConfiguration testRunConfiguration;

    @InjectMocks
    private DaemonTestRunnerManaged daemonTestRunnerManaged;

    @Before
    public void setUp()
    {
        given(testRunnerConfiguration.getPeriodInterval()).willReturn(1);
        given(nodeIdentifierProvider.get()).willReturn(NODE_IDENTIFIER);
        given(testRunStatusDataAccess.getConfiguration(TEST_ID.toString())).willReturn(testRunConfiguration);
    }

    @Test
    public void shouldStartTest()
    {
        //given
        given(testRunStatusDataAccess.getTests(NODE_IDENTIFIER, TestState.INITIALIZED))
            .willReturn(ImmutableSet.of(TEST_ID.toString()));
        //when
        daemonTestRunnerManaged.start();
        //then
        await().atMost(2, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                verify(workerManager, times(1)).startTest(any(), any());
                verify(testRunStatusDataAccess, times(1))
                    .updateState(TestState.STARTED, TEST_ID.toString(), NODE_IDENTIFIER);
            });
    }
}
