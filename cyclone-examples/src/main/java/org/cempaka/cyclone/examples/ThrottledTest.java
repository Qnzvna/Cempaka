package org.cempaka.cyclone.examples;

import org.cempaka.cyclone.core.annotations.Throttle;
import org.cempaka.cyclone.core.annotations.Thunderbolt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThrottledTest
{
    private static final Logger LOG = LoggerFactory.getLogger(ThrottledTest.class);

    @Thunderbolt
    @Throttle(10)
    public void throttled()
    {
        LOG.info("Executed");
    }
}
