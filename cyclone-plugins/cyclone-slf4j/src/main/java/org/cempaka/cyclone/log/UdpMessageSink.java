package org.cempaka.cyclone.log;

import static org.cempaka.cyclone.core.utils.Preconditions.checkNotNull;

import java.io.IOException;
import java.net.SocketException;
import org.cempaka.cyclone.core.channel.DaemonChannel;
import org.cempaka.cyclone.core.channel.UdpDaemonChannel;

public class UdpMessageSink implements MessageSink
{
    private final DaemonChannel daemonChannel = new UdpDaemonChannel();
    private final String testId;

    public UdpMessageSink(final String testId, final int port) throws SocketException
    {
        this.testId = checkNotNull(testId);
        daemonChannel.connect(port);
    }

    @Override
    public void write(final String message)
    {
        daemonChannel.log(testId, message);
    }

    @Override
    public void close() throws IOException
    {
        daemonChannel.close();
    }
}
