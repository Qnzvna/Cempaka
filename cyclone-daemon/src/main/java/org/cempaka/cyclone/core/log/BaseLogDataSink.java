package org.cempaka.cyclone.core.log;

import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class BaseLogDataSink implements LogDataSink
{
    private static final Logger LOG = LoggerFactory.getLogger(BaseLogDataSink.class);

    @Override
    public void accept(final LogMessage logMessage)
    {
        LOG.info("Log from {} test received:\n{}", logMessage.getTestExecutionId(), logMessage.getLogLine());
    }
}
