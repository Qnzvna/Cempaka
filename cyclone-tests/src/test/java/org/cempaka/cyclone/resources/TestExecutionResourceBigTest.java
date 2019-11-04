package org.cempaka.cyclone.resources;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableSet;
import io.dropwizard.testing.junit.DropwizardAppRule;
import java.io.File;
import java.util.Set;
import java.util.UUID;
import org.cempaka.cyclone.CycloneTestClient;
import org.cempaka.cyclone.configurations.DaemonConfiguration;
import org.cempaka.cyclone.daemon.CycloneDaemon;
import org.cempaka.cyclone.tests.TestExecution;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

public class TestExecutionResourceBigTest
{
    @ClassRule
    public static final DropwizardAppRule<DaemonConfiguration> APP_RULE =
        new DropwizardAppRule<>(CycloneDaemon.class, Tests.PATH);

    private static final CycloneTestClient TEST_CLIENT = new CycloneTestClient("http://localhost:8080/api");
    private static final String NODE = "localhost";

    private static UUID PARCEL_ID;
    private static UUID TEST_EXECUTION_ID;

    @BeforeClass
    public static void setUpClass()
    {
        PARCEL_ID = TEST_CLIENT.uploadParcel(new File("cyclone-tests/target/examples.jar"));
        TEST_EXECUTION_ID = TEST_CLIENT.runTest(Tests.getExampleTest(PARCEL_ID, ImmutableSet.of(NODE)));
    }

    @AfterClass
    public static void tearDownClass()
    {
        TEST_CLIENT.deleteTestExecution(TEST_EXECUTION_ID);
        TEST_CLIENT.deleteParcel(PARCEL_ID);
    }

    @Test
    public void shouldReturnTestExecutions()
    {
        //given
        //when
        final Set<TestExecution> testExecutions = TEST_CLIENT.getTestExecutions();
        //then
        assertThat(testExecutions).extracting(TestExecution::getId).contains(TEST_EXECUTION_ID);
    }

    @Test
    public void shouldReturnTestExecution()
    {
        //given
        //when
        final TestExecution testExecution = TEST_CLIENT.getTestExecutions(TEST_EXECUTION_ID).stream()
            .findFirst()
            .orElseThrow(IllegalStateException::new);
        //then
        assertThat(testExecution.getId()).isEqualTo(TEST_EXECUTION_ID);
        assertThat(testExecution.getNode()).isEqualTo(NODE);
    }

    @Test
    public void shouldReturnKeys()
    {
        //given
        //when
        final Set<UUID> keys = TEST_CLIENT.getTestExecutionKeys();
        //then
        assertThat(keys).contains(TEST_EXECUTION_ID);
    }

    @Test
    public void shouldDeleteTestExecution()
    {
        //given
        final UUID testExecution = TEST_CLIENT.runTest(Tests.getExampleTest(PARCEL_ID, ImmutableSet.of(NODE)));
        //when
        final boolean before = TEST_CLIENT.getTestExecutionKeys().contains(testExecution);
        TEST_CLIENT.deleteTestExecution(testExecution);
        final boolean after = TEST_CLIENT.getTestExecutionKeys().contains(testExecution);
        //then
        assertThat(before).isTrue();
        assertThat(after).isFalse();
    }
}
