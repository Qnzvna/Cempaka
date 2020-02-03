package org.cempaka.cyclone.daemon;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import java.time.Clock;
import java.util.Map;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.cempaka.cyclone.configurations.AuthenticationConfiguration;
import org.cempaka.cyclone.configurations.ChannelConfiguration;
import org.cempaka.cyclone.configurations.ClusterConfiguration;
import org.cempaka.cyclone.configurations.DaemonConfiguration;
import org.cempaka.cyclone.configurations.StalledTestCleanerConfiguration;
import org.cempaka.cyclone.configurations.StorageConfiguration;
import org.cempaka.cyclone.configurations.TestRunnerConfiguration;
import org.cempaka.cyclone.configurations.WorkersConfiguration;
import org.cempaka.cyclone.listeners.DaemonChannel;
import org.cempaka.cyclone.listeners.LogFailureListener;
import org.cempaka.cyclone.listeners.PayloadListener;
import org.cempaka.cyclone.listeners.UdpDaemonChannel;
import org.cempaka.cyclone.listeners.payloads.Payload;
import org.cempaka.cyclone.listeners.payloads.PayloadType;
import org.cempaka.cyclone.managed.StalledTestCleanerManaged;
import org.cempaka.cyclone.services.DistributedTestRunnerService;
import org.cempaka.cyclone.services.NodeIdentifierProvider;
import org.cempaka.cyclone.services.TestRunnerService;
import org.cempaka.cyclone.services.listeners.EndedPayloadListener;
import org.cempaka.cyclone.services.listeners.RunningPayloadListener;
import org.cempaka.cyclone.services.listeners.StartedPayloadListener;
import org.cempaka.cyclone.storage.StorageModule;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.jackson2.Jackson2Config;
import org.jdbi.v3.jackson2.Jackson2Plugin;
import org.jdbi.v3.postgres.PostgresPlugin;

public class DaemonModule extends AbstractModule
{
    private final DaemonConfiguration daemonConfiguration;
    private final Environment environment;

    DaemonModule(final DaemonConfiguration daemonConfiguration,
                 final Environment environment)
    {
        this.daemonConfiguration = checkNotNull(daemonConfiguration);
        this.environment = checkNotNull(environment);
    }

    @Override
    protected void configure()
    {
        final StorageConfiguration storageConfiguration = daemonConfiguration.getStorageConfiguration();
        final WorkersConfiguration workersConfiguration = daemonConfiguration.getWorkersConfiguration();
        final ChannelConfiguration channelConfiguration = daemonConfiguration.getChannelConfiguration();
        final ClusterConfiguration clusterConfiguration = daemonConfiguration.getClusterConfiguration();
        final AuthenticationConfiguration authenticationConfiguration =
            daemonConfiguration.getAuthenticationConfiguration();

        bind(ChannelConfiguration.class).toInstance(channelConfiguration);
        bind(StorageConfiguration.class).toInstance(storageConfiguration);
        bind(WorkersConfiguration.class).toInstance(workersConfiguration);
        bind(ClusterConfiguration.class).toInstance(clusterConfiguration);
        bind(AuthenticationConfiguration.class).toInstance(authenticationConfiguration);
        bind(TestRunnerConfiguration.class).toInstance(daemonConfiguration.getTestRunnerConfiguration());
        bind(StalledTestCleanerConfiguration.class)
            .toInstance(daemonConfiguration.getStalledTestCleanerConfiguration());

        bind(TestRunnerService.class).to(DistributedTestRunnerService.class);
        bind(ObjectMapper.class).toInstance(environment.getObjectMapper());
        bind(String.class).annotatedWith(Names.named("guava.path"))
            .toInstance(workersConfiguration.getGuavaPath());
        bind(Integer.class).annotatedWith(Names.named("worker.number"))
            .toInstance(workersConfiguration.getWorkersNumber());
        bind(String.class).annotatedWith(Names.named("worker.logs.directory"))
            .toInstance(workersConfiguration.getLogsPath());
        bind(Integer.class).annotatedWith(Names.named("udp.server.port"))
            .toInstance(channelConfiguration.getUdpServerPort());
        bind(Long.class).annotatedWith(Names.named("heartbeat.interval"))
            .toInstance(clusterConfiguration.getHeartbeatInterval());
        bind(new TypeLiteral<Map<String, String>>() {})
            .annotatedWith(Names.named("node.provider.properties"))
            .toInstance(clusterConfiguration.getNodeProviderProperties());

        bind(NodeIdentifierProvider.class).to(clusterConfiguration.getNodeIdentifierProvider());
        bind(Clock.class).toInstance(Clock.systemUTC());

        install(new StorageModule(storageConfiguration));
    }

    @Inject
    @Provides
    @Singleton
    public DaemonChannel daemonChannel(final PayloadListener payloadListener,
                                       final LogFailureListener logFailureListener)
    {
        final UdpDaemonChannel channel = new UdpDaemonChannel();
        channel.addReadListener(payloadListener);
        channel.addFailureListener(logFailureListener);
        return channel;
    }

    @Inject
    @Provides
    @Singleton
    public Multimap<PayloadType, BiConsumer<String, Payload>> payloadListeners(
        final StartedPayloadListener startedPayloadListener,
        final RunningPayloadListener runningPayloadListener,
        final EndedPayloadListener endedPayloadListener)
    {
        return ImmutableListMultimap.<PayloadType, BiConsumer<String, Payload>>builder()
            .put(PayloadType.STARTED, startedPayloadListener)
            .put(PayloadType.RUNNING, runningPayloadListener)
            .put(PayloadType.ENDED, endedPayloadListener)
            .build();
    }

    @Inject
    @Provides
    @Singleton
    public Jdbi dbi(final ObjectMapper objectMapper)
    {
        checkNotNull(daemonConfiguration.getDataSourceFactory());
        final Jdbi jdbi = new JdbiFactory()
            .build(environment, daemonConfiguration.getDataSourceFactory(), "postgresql");
        jdbi.installPlugin(new Jackson2Plugin());
        jdbi.installPlugin(new PostgresPlugin());
        jdbi.getConfig().get(Jackson2Config.class).setMapper(objectMapper);
        return jdbi;
    }

    @Inject
    @Provides
    @Singleton
    public CloseableHttpClient httpClient()
    {
        return HttpClients.createDefault();
    }
}
