package org.cempaka.cyclone.configurations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.cempaka.cyclone.services.NodeIdentifierProvider;
import org.cempaka.cyclone.services.StaticNodeIdentifierProvider;

public class ClusterConfiguration
{
    @Valid
    @JsonProperty("nodeIdentifierProvider")
    private TypedConfiguration<NodeIdentifierProvider> nodeIdentifierProviderConfiguration =
        new TypedConfiguration<>(StaticNodeIdentifierProvider.class);
    @Min(10)
    @Max(600)
    private long heartbeatInterval = 10;
    @Min(60)
    private long heartbeatManagedAwaitInterval = 60;

    @JsonIgnore
    public Class<? extends NodeIdentifierProvider> getNodeIdentifierProvider()
    {
        return nodeIdentifierProviderConfiguration.getType();
    }

    @JsonIgnore
    public Map<String, String> getNodeProviderProperties()
    {
        return nodeIdentifierProviderConfiguration.getParameters();
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
