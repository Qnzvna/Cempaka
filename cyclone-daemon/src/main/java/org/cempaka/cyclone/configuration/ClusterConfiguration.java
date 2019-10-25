package org.cempaka.cyclone.configuration;

import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.cempaka.cyclone.services.NodeIdentifierProvider;
import org.cempaka.cyclone.services.StaticNodeIdentifierProvider;
import org.hibernate.validator.constraints.NotEmpty;

public class ClusterConfiguration
{
    @NotNull
    private Class<? extends NodeIdentifierProvider> nodeIdentifierProvider = StaticNodeIdentifierProvider.class;
    private Map<String, String> nodeProviderProperties = new HashMap<>();
    @Min(10)
    @Max(600)
    private long heartbeatInterval = 10;
    @Min(60)
    private long heartbeatManagedAwaitInterval = 60;

    public Class<? extends NodeIdentifierProvider> getNodeIdentifierProvider()
    {
        return nodeIdentifierProvider;
    }

    public Map<String, String> getNodeProviderProperties()
    {
        return nodeProviderProperties;
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
