package org.cempaka.cyclone.examples;

import org.cempaka.cyclone.core.annotations.Thunderbolt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingTest
{
    private static final Logger LOG = LoggerFactory.getLogger(LoggingTest.class);

    @Thunderbolt
    public void shouldLog()
    {
        LOG.info("Log line.");
    }
}
