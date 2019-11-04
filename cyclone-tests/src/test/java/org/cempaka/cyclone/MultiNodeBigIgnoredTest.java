package org.cempaka.cyclone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.cempaka.cyclone.beans.TestState;
import org.cempaka.cyclone.tests.ImmutableTestExecutionProperties;
import org.cempaka.cyclone.tests.TestExecution;
import org.cempaka.cyclone.tests.TestExecutionProperties;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class MultiNodeBigIgnoredTest
{
    @ClassRule
    public static final CycloneSpawnRule CYCLONE_SPAWN_RULE = new CycloneSpawnRule();

    private static final String API_URL = "http://127.0.0.1:50000/api";

    private final CycloneTestClient cycloneTestClient = new CycloneTestClient(API_URL);

    @BeforeClass
    public static void setUpClass()
    {
        CYCLONE_SPAWN_RULE.spawnTo(2);
    }

    @Test
    public void shouldReportStatus()
    {
        //given
        //when
        final Map<String, Boolean> status = cycloneTestClient.getStatus();
        //then
        assertThat(status).hasSize(2).containsValues(true, true);
    }

    @Test
    public void shouldRunOnMultipleHosts()
    {
        //given
        final Set<String> nodes = cycloneTestClient.getStatus().keySet();
        final UUID parcelId = cycloneTestClient.uploadParcel(new File("cyclone-tests/target/examples.jar"));
        final TestExecutionProperties testExecutionProperties = ImmutableTestExecutionProperties.builder()
            .parcelId(parcelId)
            .testName("org.cempaka.cyclone.examples.ExampleTest")
            .loopCount(1)
            .threadsNumber(1)
            .nodes(nodes)
            .build();
        //when
        final UUID testId = cycloneTestClient.runTest(testExecutionProperties);
        //then
        await().untilAsserted(() ->
            nodes.forEach(node -> assertThat(cycloneTestClient.getTestExecutions(testId))
                .extracting(TestExecution::getState)
                .allSatisfy(state -> assertThat(state).isEqualTo(TestState.ENDED))));
    }
}
