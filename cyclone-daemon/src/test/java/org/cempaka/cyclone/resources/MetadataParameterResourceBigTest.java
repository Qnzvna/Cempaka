package org.cempaka.cyclone.resources;

import org.cempaka.cyclone.client.ApacheCycloneClient;
import org.cempaka.cyclone.client.CycloneClient;
import org.junit.jupiter.api.Test;

class MetadataParameterResourceBigTest
{
    private static final CycloneClient TEST_CLIENT = ApacheCycloneClient.builder().withApiUrl(Tests.API).build();

    @Test
    void shouldUploadMetadata()
    {
        //given
        final String id = "id";
        //when
        TEST_CLIENT.uploadMetadata(id, new byte[]{42});
        //then
        // no exceptions
    }

    @Test
    void shouldDeleteMetadata()
    {
        //given
        final String id = "id";
        //when
        TEST_CLIENT.deleteMetadata(id);
        //then
        // no exceptions
    }
}
