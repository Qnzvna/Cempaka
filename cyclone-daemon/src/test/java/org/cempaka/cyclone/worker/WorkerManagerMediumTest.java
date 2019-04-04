package org.cempaka.cyclone.worker;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Resources;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.cempaka.cyclone.beans.Parcel;
import org.cempaka.cyclone.beans.exceptions.ProcessFailureException;
import org.cempaka.cyclone.storage.repository.ParcelRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorkerManagerMediumTest
{
    private static final String EXAMPLE_UUID = "67548d96-076d-4f69-bd1d-3b9316b2ba15";
    private static final UUID EXAMPLE_ID = UUID.fromString(EXAMPLE_UUID);
    private static final int WORKER_NUMBER = 2;
    private static final String EXAMPLE_TEST = "org.cempaka.cyclone.examples.ExampleTest";
    private static final String FAILING_TEST = "org.cempaka.cyclone.examples.FailingTest";
    private static final String SUPPRESSED_FAILING_TEST =
        "org.cempaka.cyclone.examples.SupressedFailingTest";
    private static final Set<String> LOCAL = ImmutableSet.of("127.0.0.1");

    @Mock
    private ParcelRepository parcelRepository;

    private WorkerManager workerManager;

    @Before
    public void setUp() throws Exception
    {
        final URL resource = Resources.getResource(EXAMPLE_UUID);
        final byte[] parcelData = Resources.toByteArray(resource);
        final Parcel parcel = Parcel.of(EXAMPLE_ID, parcelData);
        given(parcelRepository.get(EXAMPLE_ID)).willReturn(parcel);
        workerManager = new WorkerManager(parcelRepository, 0, WORKER_NUMBER, "../logs");
    }

    @Test
    public void shouldRunExampleWithoutFailures()
    {
        //given
        final UUID testId = UUID.randomUUID();
        final Map<String, String> parameters = ImmutableMap.of("sleep", "1", "testName", "test");
        //when
        workerManager.startTest(testId, EXAMPLE_ID, EXAMPLE_TEST, 1, 1, parameters).join();
        //then
    }

    @Test
    public void shouldPreserveDefaultParameters()
    {
        //given
        final UUID testId = UUID.randomUUID();
        final Map<String, String> parameters = ImmutableMap.of("sleep", "1");
        //when
        workerManager.startTest(testId, EXAMPLE_ID, EXAMPLE_TEST, 1, 1, parameters).join();
        //then
    }

    @Test
    public void shouldSuppressFailures()
    {
        //given
        final UUID testId = UUID.randomUUID();
        final Map<String, String> parameters = ImmutableMap.of();
        //when
        workerManager.startTest(testId, EXAMPLE_ID, SUPPRESSED_FAILING_TEST, 1, 1, parameters)
            .join();
        //then
    }

    @Test
    public void shouldFailExecution()
    {
        //given
        final UUID testId = UUID.randomUUID();
        final Map<String, String> parameters = ImmutableMap.of();
        //when
        //then
        assertThatThrownBy(
            () -> workerManager
                .startTest(UUID.randomUUID(), EXAMPLE_ID, FAILING_TEST, 1, 1, parameters).join())
            .hasCauseInstanceOf(ProcessFailureException.class);
    }

    @Test
    public void shouldRunTestsInParallel()
    {
        //given
        final Map<String, String> parameters = ImmutableMap.of("sleep", "1000", "testName", "test");

        //when
        final CompletableFuture<UUID> runId1 = workerManager
            .startTest(UUID.randomUUID(), EXAMPLE_ID, EXAMPLE_TEST, 1, 1, parameters);
        final CompletableFuture<UUID> runId2 = workerManager
            .startTest(UUID.randomUUID(), EXAMPLE_ID, EXAMPLE_TEST, 1, 1, parameters);
        final Set<UUID> runningTests = workerManager.getRunningTestsId();
        final List<Worker> idleWorkers = workerManager.getIdleWorkers();
        //then
        CompletableFuture.allOf(runId1, runId2).join();
        assertThat(runningTests).containsExactlyInAnyOrder(runId1.join(), runId2.join());
        assertThat(idleWorkers).isEmpty();
    }

    @Test
    public void shouldAbortTest()
    {
        //given
        final Map<String, String> parameters = ImmutableMap.of("sleep", "1000", "testName", "test");
        //when
        final CompletableFuture<UUID> runId = workerManager
            .startTest(UUID.randomUUID(), EXAMPLE_ID, EXAMPLE_TEST, 1, 1, parameters);
        final Set<UUID> runningTests = workerManager.getRunningTestsId();
        workerManager.abortTest(runId.join());
        final List<Worker> idleWorkers = workerManager.getIdleWorkers();
        //then
        assertThat(runningTests).containsExactly(runId.join());
        assertThat(idleWorkers).hasSize(2);
    }
}