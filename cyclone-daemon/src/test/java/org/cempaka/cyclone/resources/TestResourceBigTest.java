package org.cempaka.cyclone.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.cempaka.cyclone.CycloneTestClient;
import org.cempaka.cyclone.beans.TestState;
import org.cempaka.cyclone.tests.TestExecution;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestResourceBigTest
{
    private static final CycloneTestClient TEST_CLIENT = new CycloneTestClient(Tests.API);

    private static UUID PARCEL_ID;

    @BeforeClass
    public static void setUpClass()
    {
        PARCEL_ID = TEST_CLIENT.uploadParcel(new File(Tests.EXAMPLES));
    }

    @AfterClass
    public static void tearDownClass()
    {
        TEST_CLIENT.deleteParcel(PARCEL_ID);
    }

    @Test
    public void shouldReturnTests()
    {
        //given
        //when
        final List<org.cempaka.cyclone.tests.Test> tests = TEST_CLIENT.getTests();
        //then
        assertThat(tests).extracting(org.cempaka.cyclone.tests.Test::getParcelId).contains(PARCEL_ID);
    }

    @Test
    public void shouldStartTest()
    {
        //given
        //when
        TEST_CLIENT.startTest(Tests.getExampleTest(PARCEL_ID, ImmutableSet.of(Tests.NODE)));
        //then
        // no exceptions
    }

    @Test
    public void shouldRunFailingTest()
    {
        //given
        //when
        final UUID testId = TEST_CLIENT.startTest(Tests.getLongCauseTest(PARCEL_ID, ImmutableSet.of(Tests.NODE)));
        //then
        await().pollInterval(5, TimeUnit.SECONDS)
            .atMost(30, TimeUnit.SECONDS)
            .untilAsserted(() -> assertThat(TEST_CLIENT.getTestExecutions(testId))
                .extracting(TestExecution::getState)
                .contains(TestState.ENDED));
    }

    @Test
    public void shouldStopTest()
    {
        //given
        final UUID testId = TEST_CLIENT.startTest(Tests.getExampleTest(PARCEL_ID, ImmutableSet.of(Tests.NODE)));
        //when
        TEST_CLIENT.stopTest(testId);
        final String state = TEST_CLIENT.getTestExecutions(testId).stream()
            .findFirst()
            .map(TestExecution::getState)
            .orElseThrow(IllegalStateException::new);
        //then
        assertThat(state).isEqualTo(TestState.ABORTED);
    }
}