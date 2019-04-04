package org.cempaka.cyclone.configuration;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class ClusterConfiguration
{
    @NotEmpty
    private String nodeId;
    @Min(10)
    @Max(600)
    private long heartbeatInterval = 10;
    @Min(60)
    private long heartbeatManagedAwaitInterval = 60;

    public String getNodeId()
    {
        return nodeId;
    }

    public long getHeartbeatInterval()
    {
        return heartbeatInterval;
    }

    public long getHeartbeatManagedAwaitInterval()
    {
        return heartbeatManagedAwaitInterval;
    }
}
