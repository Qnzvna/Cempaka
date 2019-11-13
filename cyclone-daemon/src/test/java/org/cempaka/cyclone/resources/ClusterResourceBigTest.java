package org.cempaka.cyclone.resources;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.cempaka.cyclone.CycloneTestClient;
import org.junit.Test;

public class ClusterResourceBigTest
{
    private static final CycloneTestClient TEST_CLIENT = new CycloneTestClient(Tests.API);

    @Test
    public void shouldReturnClusterStatus()
    {
        //given
        //when
        final Map<String, Boolean> status = TEST_CLIENT.getStatus();
        //then
        assertThat(status).containsEntry(Tests.NODE, true);
    }
}
