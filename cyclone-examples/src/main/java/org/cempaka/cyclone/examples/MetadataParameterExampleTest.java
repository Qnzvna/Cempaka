package org.cempaka.cyclone.examples;

import org.cempaka.cyclone.core.annotations.MetadataParameter;
import org.cempaka.cyclone.core.annotations.Thunderbolt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetadataParameterExampleTest
{
    private static final Logger LOG = LoggerFactory.getLogger(MetadataParameterExampleTest.class);

    @MetadataParameter("bytes")
    byte[] byteMetadata;

    @Thunderbolt
    public void shouldAccessMetadata()
    {
        LOG.info("metadata={}", new String(byteMetadata));
    }
}
