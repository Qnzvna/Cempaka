package org.cempaka.cyclone.resources;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableSet;
import io.dropwizard.testing.junit.DropwizardAppRule;
import java.io.File;
import java.util.List;
import java.util.UUID;
import org.cempaka.cyclone.CycloneTestClient;
import org.cempaka.cyclone.beans.TestState;
import org.cempaka.cyclone.configurations.DaemonConfiguration;
import org.cempaka.cyclone.daemon.CycloneDaemon;
import org.cempaka.cyclone.tests.TestExecution;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

public class TestResourceBigTest
{
    @ClassRule
    public static final DropwizardAppRule<DaemonConfiguration> APP_RULE =
        new DropwizardAppRule<>(CycloneDaemon.class, Tests.PATH);

    private static final CycloneTestClient TEST_CLIENT = new CycloneTestClient("http://localhost:8080/api");
    private static final String NODE = "localhost";

    private static UUID PARCEL_ID;

    @BeforeClass
    public static void setUpClass()
    {
        PARCEL_ID = TEST_CLIENT.uploadParcel(new File("cyclone-tests/target/examples.jar"));
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
        TEST_CLIENT.runTest(Tests.getExampleTest(PARCEL_ID, ImmutableSet.of("localhost")));
        //then
        // no exceptions
    }

    @Test
    public void shouldStopTest()
    {
        //given
        final UUID testId = TEST_CLIENT.runTest(Tests.getExampleTest(PARCEL_ID, ImmutableSet.of(NODE)));
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