package org.cempaka.cyclone.log;

import static org.cempaka.cyclone.utils.Preconditions.checkArgument;

import java.io.UncheckedIOException;
import java.net.SocketException;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class CycloneLoggerFactory implements ILoggerFactory
{
    @Override
    public Logger getLogger(final String name)
    {
        return new CycloneLogger(name, new LoggerConfiguration(
            LoggerFactoryConfiguration.LOG_LEVEL),
            this::createMessageSink);
    }

    private MessageSink createMessageSink()
    {
        if (LoggerFactoryConfiguration.ENABLED) {
            checkArgument(LoggerFactoryConfiguration.TEST_ID != null);
            checkArgument(LoggerFactoryConfiguration.PORT > 0);
            try {
                return new UdpMessageSink(LoggerFactoryConfiguration.TEST_ID, LoggerFactoryConfiguration.PORT);
            } catch (SocketException e) {
                throw new UncheckedIOException(e);
            }
        } else {
            return new MessageSink()
            {
                @Override
                public void close()
                {
                }

                @Override
                public void write(final String message)
                {
                }
            };
        }
    }
}
