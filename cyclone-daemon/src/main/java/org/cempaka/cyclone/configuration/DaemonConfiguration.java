package org.cempaka.cyclone.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.NotEmpty;

public class DaemonConfiguration extends Configuration
{
    @Valid
    private StorageConfiguration storageConfiguration;
    @Valid
    private WorkersConfiguration workersConfiguration;
    @Valid
    private ChannelConfiguration channelConfiguration = new ChannelConfiguration();

    @JsonProperty("storage")
    public StorageConfiguration getStorageConfiguration()
    {
        return storageConfiguration;
    }

    @JsonProperty("workers")
    public WorkersConfiguration getWorkersConfiguration()
    {
        return workersConfiguration;
    }

    @JsonProperty("channel")
    public ChannelConfiguration getChannelConfiguration()
    {
        return channelConfiguration;
    }
}
