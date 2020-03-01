package org.cempaka.cyclone.resources;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.UUID;
import org.cempaka.cyclone.client.ApacheCycloneClient;
import org.cempaka.cyclone.client.CycloneClient;
import org.cempaka.cyclone.client.ImmutableParcelUpload;
import org.junit.Test;

public class ParcelResourceBigTest
{
    private static final CycloneClient TEST_CLIENT = ApacheCycloneClient.builder().withApiUrl(Tests.API).build();
    private static final String EXAMPLES_CENTRAL_LOCATION = "https://oss.sonatype.org/content/repositories/snapshots/org/cempaka/cyclone/cyclone-examples/0.3-SNAPSHOT/cyclone-examples-0.3-20200128.211231-1.jar";

    @Test
    public void shouldUploadParcelFromMavenCentral()
    {
        //given
        //when
        final UUID parcelId = TEST_CLIENT.uploadParcel(ImmutableParcelUpload.builder()
            .location(EXAMPLES_CENTRAL_LOCATION)
            .build());
        final Set<org.cempaka.cyclone.client.Test> tests = TEST_CLIENT.getTests();
        //then
        assertThat(tests).extracting(org.cempaka.cyclone.client.Test::getParcelId).contains(parcelId);
    }
}
