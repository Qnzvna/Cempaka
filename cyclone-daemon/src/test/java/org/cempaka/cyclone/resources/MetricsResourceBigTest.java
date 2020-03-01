package org.cempaka.cyclone.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.cempaka.cyclone.client.ApacheCycloneClient;
import org.cempaka.cyclone.client.CycloneClient;
import org.cempaka.cyclone.client.MetricDataPoint;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MetricsResourceBigTest
{
    private static final CycloneClient TEST_CLIENT = ApacheCycloneClient.builder().withApiUrl(Tests.API).build();

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
    public void shouldReturnMetrics()
    {
        //given
        final UUID executionId = TEST_CLIENT.startTest(Tests.getExampleTest(PARCEL_ID, ImmutableSet.of(Tests.NODE)));
        //when
        await().pollInterval(5, TimeUnit.SECONDS)
            .atMost(30, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                final Set<MetricDataPoint> metrics = TEST_CLIENT.getTestExecutionMetrics(executionId);
                assertThat(metrics).isNotEmpty().anySatisfy(metricDataPoint -> {
                    assertThat(metricDataPoint.getName()).isEqualTo("test1:success:count");
                    assertThat(metricDataPoint.getValue()).isEqualTo(1);
                });
            });
    }
}
