package org.cempaka.cyclone.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.awaitility.Awaitility.await;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.cempaka.cyclone.beans.TestState;
import org.cempaka.cyclone.client.ApacheCycloneClient;
import org.cempaka.cyclone.client.CycloneClient;
import org.cempaka.cyclone.client.InvalidResponseException;
import org.cempaka.cyclone.client.NodeCapacity;
import org.cempaka.cyclone.client.TestExecution;
import org.cempaka.cyclone.client.TestExecutionProperties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestResourceBigTest
{
    private static final CycloneClient TEST_CLIENT = ApacheCycloneClient.builder().withApiUrl(Tests.API).build();

    private static UUID PARCEL_ID;

    @BeforeAll
    public static void setUpClass()
    {
        PARCEL_ID = TEST_CLIENT.uploadParcel(new File(Tests.EXAMPLES));
        Awaitility.setDefaultPollInterval(1, TimeUnit.SECONDS);
        Awaitility.setDefaultTimeout(30, TimeUnit.SECONDS);
    }

    @AfterAll
    public static void tearDownClass()
    {
        TEST_CLIENT.deleteParcel(PARCEL_ID);
        Awaitility.reset();
    }

    @Test
    void shouldReturnTests()
    {
        //given
        //when
        final Set<org.cempaka.cyclone.client.Test> tests = TEST_CLIENT.getTests();
        //then
        assertThat(tests).extracting(org.cempaka.cyclone.client.Test::getParcelId).contains(PARCEL_ID);
    }

    @Test
    void shouldStartTest()
    {
        //given
        //when
        TEST_CLIENT.startTest(Tests.getExampleTest(PARCEL_ID, ImmutableSet.of(Tests.NODE)));
        //then
        // no exceptions
    }

    @Test
    void shouldRunFailingTest()
    {
        //given
        //when
        final UUID testId = TEST_CLIENT.startTest(Tests.getLongCauseTest(PARCEL_ID, ImmutableSet.of(Tests.NODE)));
        //then
        await().untilAsserted(() -> assertThat(TEST_CLIENT.getTestExecutions(testId))
            .extracting(TestExecution::getState)
            .contains(TestState.ENDED));
    }

    @Test
    void shouldStopTest()
    {
        //given
        final UUID testId = TEST_CLIENT.startTest(Tests.getExampleTest(PARCEL_ID,
            ImmutableSet.of(Tests.NODE),
            ImmutableMap.of("sleep", "5000")));
        //when
        await().untilAsserted(() -> assertThat(TEST_CLIENT.getTestExecutions(testId))
            .extracting(TestExecution::getState)
            .contains(TestState.STARTED));
        TEST_CLIENT.stopTest(testId);
        final String state = TEST_CLIENT.getTestExecutions(testId).stream()
            .findFirst()
            .map(TestExecution::getState)
            .orElseThrow(IllegalStateException::new);
        //then
        assertThat(state).isEqualTo(TestState.ABORTED);
        await().untilAsserted(() -> {
            final NodeCapacity nodeCapacity = TEST_CLIENT.getNodeCapacity(Tests.NODE);
            assertThat(nodeCapacity.getIdleWorkers()).isNotZero();
            assertThat(nodeCapacity.getRunningTests()).isZero();
        });
    }

    @Test
    void shouldRunTestUsingMetadata()
    {
        //given
        final String password = "password";
        TEST_CLIENT.uploadMetadata("bytes", password.getBytes());
        //when
        final UUID testId = TEST_CLIENT.startTest(Tests.getMetadataTest(PARCEL_ID, ImmutableSet.of(Tests.NODE)));
        //then
        await().untilAsserted(() -> {
            assertThat(TEST_CLIENT.getTestExecutions(testId))
                .extracting(TestExecution::getState)
                .contains(TestState.ENDED);
            assertThat(TEST_CLIENT.getTestExecutionLogMessages(testId, Instant.EPOCH))
                .hasOnlyOneElementSatisfying(logLine -> assertThat(logLine).contains(password));
        });
    }

    @Test
    void shouldFailStartingNotExistingParcel()
    {
        //given
        //when
        final Throwable throwable = catchThrowable(() ->
            TEST_CLIENT.startTest(Tests.getMetadataTest(UUID.randomUUID(), ImmutableSet.of(Tests.NODE))));
        //then
        assertThat(throwable).isInstanceOf(InvalidResponseException.class);
        assertThat(throwable.getMessage()).contains("404");
    }

    @Test
    void shouldFailStartingNotExistingTest()
    {
        //given
        final TestExecutionProperties testExecutionProperties = Tests.getTest(
            PARCEL_ID,
            ImmutableSet.of(Tests.NODE),
            "not_existing_test",
            ImmutableMap.of()
        );
        //when
        final Throwable throwable = catchThrowable(() -> TEST_CLIENT.startTest(testExecutionProperties));
        //then
        assertThat(throwable).isInstanceOf(InvalidResponseException.class);
        assertThat(throwable.getMessage()).contains("404");
    }

    @Test
    void shouldFailStartingOnNotAliveNode()
    {
        //given
        final String node = "not_alive_node";
        final TestExecutionProperties testProperties = Tests.getExampleTest(
            PARCEL_ID,
            ImmutableSet.of(node)
        );
        //when
        final Throwable throwable = catchThrowable(() -> TEST_CLIENT.startTest(testProperties));
        //then
        assertThat(throwable).isInstanceOf(InvalidResponseException.class);
        assertThat(throwable.getMessage()).contains(node);
    }
}