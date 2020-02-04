package org.cempaka.cyclone.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.cempaka.cyclone.CycloneTestClient;
import org.cempaka.cyclone.beans.TestState;
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
        await().untilAsserted(() ->
            assertThat(TEST_CLIENT.getTestExecutions(TEST_EXECUTION_ID))
                .hasOnlyOneElementSatisfying(testExecution ->
                    assertThat(testExecution.getState()).isEqualTo(TestState.ENDED)));
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
        final List<TestExecution> testExecutions = TEST_CLIENT.getTestExecutions();
        //then
        assertThat(testExecutions).extracting(TestExecution::getId).contains(TEST_EXECUTION_ID);
    }

    @Test
    public void shouldOffsetTestExecutions()
    {
        //given
        final int limit = 10;
        //when
        final List<TestExecution> testExecutions = TEST_CLIENT.getTestExecutions(limit, 0);
        final List<TestExecution> emptyExecutions = TEST_CLIENT.getTestExecutions(limit, testExecutions.size());
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
    public void shouldSearchByStateReturnTests()
    {
        //given
        final String state = TestState.ENDED;
        //when
        final List<TestExecution> testExecutions = TEST_CLIENT
            .searchTestExecutions(ImmutableSet.of(state), ImmutableSet.of());
        //then
        assertThat(testExecutions).isNotEmpty()
            .allSatisfy(testExecution -> assertThat(testExecution.getState()).isEqualTo(state));
    }

    @Test
    public void shouldSearchByNameReturnTests()
    {
        //given
        final String name = "TeSt";
        //when
        final List<TestExecution> testExecutions = TEST_CLIENT
            .searchTestExecutions(ImmutableSet.of(), ImmutableSet.of(name));
        //then
        assertThat(testExecutions).isNotEmpty()
            .allSatisfy(testExecution ->
                assertThat(testExecution.getProperties().getTestName()).containsIgnoringCase(name));
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
