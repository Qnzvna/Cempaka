package org.cempaka.cyclone.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class DaemonConfiguration extends Configuration
{
    @Valid
    @NotNull
    private DataSourceFactory dataSourceFactory = new DataSourceFactory();
    @Valid
    @NotNull
    private StorageConfiguration storageConfiguration;
    @Valid
    @NotNull
    private WorkersConfiguration workersConfiguration;
    @Valid
    @NotNull
    private ChannelConfiguration channelConfiguration = new ChannelConfiguration();

    @JsonProperty("database")
    public void setDataSourceFactory(final DataSourceFactory dataSourceFactory)
    {
        this.dataSourceFactory = dataSourceFactory;
    }

    @JsonProperty("database")
    public DataSourceFactory getDatasourceFactory()
    {
        return dataSourceFactory;
    }

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
