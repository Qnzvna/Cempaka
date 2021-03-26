package org.cempaka.cyclone.examples;

import org.cempaka.cyclone.core.annotations.Thunderbolt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessOwnerTest
{
    private static final Logger LOG = LoggerFactory.getLogger(ProcessOwnerTest.class);

    @Thunderbolt
    public void processOwner()
    {
        LOG.info("Owner: {}", System.getProperty("user.name"));
    }
}
