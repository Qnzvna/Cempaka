package org.cempaka.cyclone.configuration;

import javax.validation.constraints.Min;

public class ChannelConfiguration
{
    @Min(1)
    private int udpServerPort = 5100;
    @Min(1)
    private int workerStartPort = 5101;
    @Min(2)
    private int workerEndPort = 5200;

    public int getUdpServerPort()
    {
        return udpServerPort;
    }

    public int getWorkerStartPort()
    {
        return workerStartPort;
    }

    public int getWorkerEndPort()
    {
        return workerEndPort;
    }
}
