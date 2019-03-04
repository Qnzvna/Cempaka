package org.cempaka.cyclone.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.collect.ImmutableMap;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.cempaka.cyclone.beans.EventType;
import org.cempaka.cyclone.beans.TestRunConfiguration;
import org.cempaka.cyclone.protocol.PortProvider;
import org.cempaka.cyclone.storage.TestRunEventDataAccess;
import org.cempaka.cyclone.storage.TestRunMetadataDataAcess;
import org.cempaka.cyclone.worker.WorkerManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestRunnerServiceSmallTest
{
    @Mock
    private WorkerManager workerManager;
    @Mock
    private PortProvider portProvider;
    @Mock
    private TestRunEventDataAccess testRunEventDataAccess;
    @Mock
    private TestRunMetadataDataAcess testRunMetadataDataAcess;

    private TestRunnerService testRunnerService;

    @Before
    public void setUp()
    {
        testRunnerService = new TestRunnerService(workerManager,
            portProvider,
            testRunEventDataAccess,
            testRunMetadataDataAcess,
            Clock.fixed(Instant.EPOCH, ZoneOffset.UTC));
    }

    @Test
    public void shouldStartTest()
    {
        //given
        final UUID parcelId = UUID.randomUUID();
        final String testName = "testName";
        final ImmutableMap<String, String> parameters = ImmutableMap.of();
        final TestRunConfiguration testRunConfiguration =
            new TestRunConfiguration(parcelId,
                testName,
                1,
                1,
                parameters);
        given(workerManager.startTest(any(UUID.class), anyInt(), eq(testRunConfiguration)))
            .willReturn(CompletableFuture.completedFuture(UUID.randomUUID()));
        //when
        final UUID startTest = testRunnerService.startTest(testRunConfiguration);
        //then
        assertThat(startTest).isNotNull();
        verify(portProvider, times(1)).getPort(anyString());
        verify(testRunEventDataAccess, times(1))
            .insertEvent(anyString(), any(Timestamp.class), eq(EventType.CLEANED.toString()));
        verify(workerManager, times(1))
            .startTest(any(UUID.class), anyInt(), eq(testRunConfiguration));
        verify(testRunEventDataAccess, times(1))
            .insertEvent(anyString(), any(Timestamp.class), eq(EventType.INITIALIZED.toString()));
        verify(testRunMetadataDataAcess, times(1))
            .insertInitializationMetadata(anyString(), eq(testName), eq(parameters));
    }
}