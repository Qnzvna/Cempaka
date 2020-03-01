package org.cempaka.cyclone.resources;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.cempaka.cyclone.client.ApacheCycloneClient;
import org.cempaka.cyclone.client.CycloneClient;
import org.junit.Test;

public class ClusterResourceBigTest
{
    private static final CycloneClient TEST_CLIENT = ApacheCycloneClient.builder().withApiUrl(Tests.API).build();

    @Test
    public void shouldReturnClusterStatus()
    {
        //given
        //when
        final Map<String, Boolean> status = TEST_CLIENT.getClusterStatus();
        //then
        assertThat(status).containsEntry(Tests.NODE, true);
    }
}
