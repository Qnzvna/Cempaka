package org.cempaka.cyclone.log;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

import java.io.IOException;
import java.net.SocketException;
import org.cempaka.cyclone.channel.DaemonChannel;
import org.cempaka.cyclone.channel.UdpDaemonChannel;
import org.cempaka.cyclone.channel.payloads.LogPayload;

public class UdpMessageSink implements MessageSink
{
    private final DaemonChannel daemonChannel = new UdpDaemonChannel();
    private final String testId;
    private final int port;

    public UdpMessageSink(final String testId, final int port) throws SocketException
    {
        this.testId = checkNotNull(testId);
        this.port = port;
        daemonChannel.connect();
    }

    @Override
    public void write(final String message)
    {
        daemonChannel.write(new LogPayload(testId, message), port);
    }

    @Override
    public void close() throws IOException
    {
        daemonChannel.close();
    }
}
