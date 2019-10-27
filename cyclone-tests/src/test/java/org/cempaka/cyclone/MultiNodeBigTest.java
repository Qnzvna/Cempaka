package org.cempaka.cyclone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.cempaka.cyclone.beans.TestRunConfiguration;
import org.cempaka.cyclone.beans.TestState;
import org.cempaka.cyclone.examples.ExampleTest;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

public class MultiNodeBigTest
{
    @ClassRule
    public static final CycloneSpawnRule CYCLONE_SPAWN_RULE = new CycloneSpawnRule();

    private final CycloneTestClient cycloneTestClient = new CycloneTestClient();

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
        final UUID parcelId = cycloneTestClient.uploadParcel(new File("target/examples.jar"));
        final TestRunConfiguration testRunConfiguration = new TestRunConfiguration(parcelId,
            ExampleTest.class.getName(),
            1,
            1,
            ImmutableMap.of(),
            nodes,
            null);
        //when
        final UUID testId = cycloneTestClient.runTest(testRunConfiguration);
        //then
        await().untilAsserted(() ->
            nodes.forEach(node ->
                assertThat(cycloneTestClient.getTestState(testId, node)).isEqualTo("ENDED")));
    }
}
