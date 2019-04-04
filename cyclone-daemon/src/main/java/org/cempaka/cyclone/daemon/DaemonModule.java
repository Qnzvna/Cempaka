package org.cempaka.cyclone.daemon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.ProvisionException;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import org.cempaka.cyclone.configuration.ChannelConfiguration;
import org.cempaka.cyclone.configuration.ClusterConfiguration;
import org.cempaka.cyclone.configuration.DaemonConfiguration;
import org.cempaka.cyclone.configuration.StorageConfiguration;
import org.cempaka.cyclone.configuration.WorkersConfiguration;
import org.cempaka.cyclone.protocol.DaemonChannel;
import org.cempaka.cyclone.protocol.LogFailureListener;
import org.cempaka.cyclone.protocol.PayloadListener;
import org.cempaka.cyclone.protocol.UdpDaemonChannel;
import org.cempaka.cyclone.protocol.payloads.Payload;
import org.cempaka.cyclone.protocol.payloads.PayloadType;
import org.cempaka.cyclone.services.DistributedTestRunnerService;
import org.cempaka.cyclone.services.NodeIdentifierProvider;
import org.cempaka.cyclone.services.StaticNodeIdentifierProvider;
import org.cempaka.cyclone.services.TestRunnerService;
import org.cempaka.cyclone.services.listeners.EndedPayloadListener;
import org.cempaka.cyclone.services.listeners.RunningPayloadListener;
import org.cempaka.cyclone.services.listeners.StartedPayloadListener;
import org.cempaka.cyclone.storage.StorageModule;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.jackson2.Jackson2Config;
import org.jdbi.v3.jackson2.Jackson2Plugin;
import org.jdbi.v3.postgres.PostgresPlugin;

import javax.inject.Inject;
import java.time.Clock;
import java.util.function.BiConsumer;

import static com.google.common.base.Preconditions.checkNotNull;

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
        final StorageConfiguration storageConfiguration =
            daemonConfiguration.getStorageConfiguration();
        final WorkersConfiguration workersConfiguration =
            daemonConfiguration.getWorkersConfiguration();
        final ChannelConfiguration channelConfiguration =
            daemonConfiguration.getChannelConfiguration();
        final ClusterConfiguration clusterConfiguration =
            daemonConfiguration.getClusterConfiguration();

        bind(ChannelConfiguration.class).toInstance(channelConfiguration);
        bind(StorageConfiguration.class).toInstance(storageConfiguration);
        bind(WorkersConfiguration.class).toInstance(workersConfiguration);
        bind(ClusterConfiguration.class).toInstance(clusterConfiguration);

        bind(TestRunnerService.class).to(DistributedTestRunnerService.class);
        bind(ObjectMapper.class).toInstance(environment.getObjectMapper());
        bind(String.class).annotatedWith(Names.named("storage.path"))
            .toInstance(storageConfiguration.getStoragePath());
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
        bind(String.class).annotatedWith(Names.named("node.id"))
                .toInstance(clusterConfiguration.getNodeId());

        bind(NodeIdentifierProvider.class).to(StaticNodeIdentifierProvider.class);
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
        final Jdbi jdbi = new JdbiFactory()
            .build(environment, daemonConfiguration.getDataSourceFactory(), "postgresql");
        jdbi.installPlugin(new Jackson2Plugin());
        jdbi.installPlugin(new PostgresPlugin());
        jdbi.getConfig().get(Jackson2Config.class).setMapper(objectMapper);
        return jdbi;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> createClass(final String className)
    {
        try {
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            final String message = String.format("Can't find %s in classpath.", className);
            throw new ProvisionException(message, e);
        }
    }
}
