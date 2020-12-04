package org.cempaka.cyclone.managed;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.collect.ImmutableSet;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.cempaka.cyclone.beans.TestState;
import org.cempaka.cyclone.configurations.TestRunnerConfiguration;
import org.cempaka.cyclone.services.MetadataService;
import org.cempaka.cyclone.services.NodeIdentifierProvider;
import org.cempaka.cyclone.storage.repositories.TestExecutionRepository;
import org.cempaka.cyclone.tests.ImmutableTestExecution;
import org.cempaka.cyclone.tests.TestExecutionProperties;
import org.cempaka.cyclone.workers.WorkerManager;
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
    private TestExecutionRepository testExecutionRepository;
    @Mock
    private NodeIdentifierProvider nodeIdentifierProvider;
    @Mock
    private WorkerManager workerManager;
    @Mock
    private TestRunnerConfiguration testRunnerConfiguration;
    @Mock
    private TestExecutionProperties testExecutionProperties;
    @Mock
    private MetadataService metadataService;

    @InjectMocks
    private DaemonTestRunnerManaged daemonTestRunnerManaged;

    @Before
    public void setUp()
    {
        given(testRunnerConfiguration.getPeriodInterval()).willReturn(1);
        given(nodeIdentifierProvider.get()).willReturn(NODE_IDENTIFIER);
        given(metadataService.getEncodedMetadata()).willReturn(Collections.emptyMap());
    }

    @Test
    public void shouldStartTest()
    {
        //given
        given(testExecutionRepository.get(NODE_IDENTIFIER, TestState.INITIALIZED))
            .willReturn(ImmutableSet.of(ImmutableTestExecution.builder()
                .id(TEST_ID)
                .node(NODE_IDENTIFIER)
                .state(TestState.INITIALIZED)
                .properties(testExecutionProperties)
                .build()));
        //when
        daemonTestRunnerManaged.start();
        //then
        await().atMost(2, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                verify(workerManager, times(1)).startTest(any(), any(), anyMap());
                verify(testExecutionRepository, times(1))
                    .setState(TEST_ID, NODE_IDENTIFIER, TestState.STARTED);
            });
    }
}
