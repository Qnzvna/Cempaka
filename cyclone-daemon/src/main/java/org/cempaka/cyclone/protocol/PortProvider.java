package org.cempaka.cyclone.protocol;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.configuration.ChannelConfiguration;

@Singleton
public class PortProvider
{
    private final static int FUSE = 1000;

    private final int startPort;
    private final int endPort;
    private int currentPort;
    private final Map<Integer, String> usedPorts;

    @Inject
    public PortProvider(final ChannelConfiguration channelConfiguration)
    {
        this.startPort = channelConfiguration.getWorkerStartPort();
        this.endPort = channelConfiguration.getWorkerEndPort();
        checkArgument(startPort > 0);
        checkArgument(endPort > startPort);
        this.currentPort = startPort;
        this.usedPorts = new HashMap<>();
    }

    public synchronized int getPort(final String testRunUuid)
    {
        int i = 0;
        while (usedPorts.containsKey(currentPort++)) {
            if (currentPort > endPort) {
                currentPort = startPort;
            }
            if (i > FUSE) {
                throw new IllegalStateException();
            }
            i++;
        }
        usedPorts.put(currentPort, testRunUuid);
        return currentPort;
    }

    public synchronized void releasePort(final String testUuid)
    {
        usedPorts.entrySet().stream()
            .filter(entry -> entry.getValue().equals(testUuid))
            .findFirst()
            .map(Map.Entry::getKey)
            .ifPresent(usedPorts::remove);
    }

    public String getTestUuid(final int port)
    {
        return usedPorts.get(port);
    }
}
