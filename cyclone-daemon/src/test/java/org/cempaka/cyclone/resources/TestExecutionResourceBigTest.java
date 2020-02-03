package org.cempaka.cyclone.resources;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.util.Set;
import java.util.UUID;
import org.cempaka.cyclone.CycloneTestClient;
import org.cempaka.cyclone.tests.TestExecution;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestExecutionResourceBigTest
{
    private static final CycloneTestClient TEST_CLIENT = new CycloneTestClient(Tests.API);

    private static UUID PARCEL_ID;
    private static UUID TEST_EXECUTION_ID;

    @BeforeClass
    public static void setUpClass()
    {
        PARCEL_ID = TEST_CLIENT.uploadParcel(new File(Tests.EXAMPLES));
        TEST_EXECUTION_ID = TEST_CLIENT.startTest(Tests.getExampleTest(PARCEL_ID, ImmutableSet.of(Tests.NODE)));
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
    public void shouldOffsetTestExecutions()
    {
        //given
        final int limit = 10;
        //when
        final Set<TestExecution> testExecutions = TEST_CLIENT.getTestExecutions(limit, 0);
        final Set<TestExecution> emptyExecutions = TEST_CLIENT.getTestExecutions(limit, testExecutions.size());
        //then
        assertThat(testExecutions.size()).isLessThanOrEqualTo(limit);
        assertThat(emptyExecutions).isEmpty();
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
        assertThat(testExecution.getNode()).isEqualTo(Tests.NODE);
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
        final UUID testExecution = TEST_CLIENT.startTest(Tests.getExampleTest(PARCEL_ID, ImmutableSet.of(Tests.NODE)));
        //when
        final boolean before = TEST_CLIENT.getTestExecutionKeys().contains(testExecution);
        TEST_CLIENT.deleteTestExecution(testExecution);
        final boolean after = TEST_CLIENT.getTestExecutionKeys().contains(testExecution);
        //then
        assertThat(before).isTrue();
        assertThat(after).isFalse();
    }
}
