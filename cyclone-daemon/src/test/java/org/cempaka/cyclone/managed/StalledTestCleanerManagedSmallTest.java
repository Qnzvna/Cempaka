package org.cempaka.cyclone.managed;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;
import org.cempaka.cyclone.configurations.ClusterConfiguration;
import org.cempaka.cyclone.configurations.StalledTestCleanerConfiguration;
import org.cempaka.cyclone.services.NodeStatusService;
import org.cempaka.cyclone.services.TestRunnerService;
import org.cempaka.cyclone.storage.repositories.TestExecutionRepository;
import org.cempaka.cyclone.tests.TestExecution;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StalledTestCleanerManagedSmallTest
{
    private static final Instant NOW = Instant.now();
    public static final long HEARTBEAT_INTERVAl = 10L;
    public static final String ACTIVE_NODE = "active_node";
    public static final String INACTIVE_NODE = "inactive_node";

    @Mock
    private TestExecutionRepository testExecutionRepository;
    @Mock
    private NodeStatusService nodeStatusService;
    @Mock
    private TestRunnerService testRunnerService;
    @Mock
    private ClusterConfiguration clusterConfiguration;
    @Mock
    private StalledTestCleanerConfiguration stalledTestCleanerConfiguration;

    private StalledTestCleanerManaged stalledTestCleanerManaged;

    @Before
    public void setUp()
    {
        given(clusterConfiguration.getHeartbeatInterval()).willReturn(HEARTBEAT_INTERVAl);
        given(nodeStatusService.getNodesStatus())
            .willReturn(ImmutableMap.of(ACTIVE_NODE, true, INACTIVE_NODE, false));
        stalledTestCleanerManaged = new StalledTestCleanerManaged(testExecutionRepository,
            nodeStatusService,
            testRunnerService,
            clusterConfiguration,
            stalledTestCleanerConfiguration,
            Clock.fixed(NOW, ZoneOffset.UTC));
    }

    @Test
    public void shouldNotStopTestsForNoOldExecutions()
    {
        //given
        final ArgumentCaptor<Instant> instantCaptor = ArgumentCaptor.forClass(Instant.class);
        //when
        stalledTestCleanerManaged.execute();
        //then
        verify(testExecutionRepository, times(1)).getUpdatedLaterThan(instantCaptor.capture());
        verify(testRunnerService, never()).stopTest(any());
        assertThat(instantCaptor.getValue()).isEqualTo(NOW.minusSeconds(HEARTBEAT_INTERVAl));
    }

    @Test
    public void shouldNotStopTestsForActiveNode()
    {
        //given
        final TestExecution testExecution = mock(TestExecution.class);
        given(testExecution.getNode()).willReturn(ACTIVE_NODE);
        given(testExecutionRepository.getUpdatedLaterThan(any(Instant.class)))
            .willReturn(ImmutableSet.of(testExecution));
        //when
        stalledTestCleanerManaged.execute();
        //then
        verify(testRunnerService, never()).stopTest(any());
    }

    @Test
    public void shouldStopTestsForInactiveNode()
    {
        //given
        final TestExecution testExecution = mock(TestExecution.class);
        final UUID testId = UUID.randomUUID();
        given(testExecution.getId()).willReturn(testId);
        given(testExecution.getNode()).willReturn(INACTIVE_NODE);
        given(testExecutionRepository.getUpdatedLaterThan(any(Instant.class)))
            .willReturn(ImmutableSet.of(testExecution));
        //when
        stalledTestCleanerManaged.execute();
        //then
        verify(testRunnerService, times(1)).stopTest(testId);
    }

    @Test
    public void shouldStopTestsForMissingNode()
    {
        //given
        final TestExecution testExecution = mock(TestExecution.class);
        final UUID testId = UUID.randomUUID();
        given(testExecution.getId()).willReturn(testId);
        given(testExecution.getNode()).willReturn("missing_node");
        given(testExecutionRepository.getUpdatedLaterThan(any(Instant.class)))
            .willReturn(ImmutableSet.of(testExecution));
        //when
        stalledTestCleanerManaged.execute();
        //then
        verify(testRunnerService, times(1)).stopTest(testId);
    }
}