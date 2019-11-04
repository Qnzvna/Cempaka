package org.cempaka.cyclone.listeners;

import com.google.inject.Singleton;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class LogFailureListener implements Consumer<Exception>
{
    private static final Logger LOG = LoggerFactory.getLogger(LogFailureListener.class);

    @Override
    public void accept(final Exception e)
    {
        LOG.warn("Failed to receive data on channel", e);
    }
}
