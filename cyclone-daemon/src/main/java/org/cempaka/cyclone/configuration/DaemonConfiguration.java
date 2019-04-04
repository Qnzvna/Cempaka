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
    @Valid
    @NotNull
    private ClusterConfiguration clusterConfiguration;
    @Valid
    @NotNull
    private TestRunnerConfiguration testRunnerConfiguration= new TestRunnerConfiguration();

    @JsonProperty("database")
    public void setDataSourceFactory(final DataSourceFactory dataSourceFactory)
    {
        this.dataSourceFactory = dataSourceFactory;
    }

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory()
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

    @JsonProperty("cluster")
    public ClusterConfiguration getClusterConfiguration()
    {
        return clusterConfiguration;
    }

    @JsonProperty("testRunner")
    public TestRunnerConfiguration getTestRunnerConfiguration()
    {
        return testRunnerConfiguration;
    }
}
