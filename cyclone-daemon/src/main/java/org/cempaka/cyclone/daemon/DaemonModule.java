package org.cempaka.cyclone.daemon;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import java.time.Clock;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import org.cempaka.cyclone.configuration.ChannelConfiguration;
import org.cempaka.cyclone.configuration.DaemonConfiguration;
import org.cempaka.cyclone.configuration.StorageConfiguration;
import org.cempaka.cyclone.configuration.WorkersConfiguration;
import org.cempaka.cyclone.protocol.DaemonChannel;
import org.cempaka.cyclone.protocol.LogFailureListener;
import org.cempaka.cyclone.protocol.Payload;
import org.cempaka.cyclone.protocol.PayloadListener;
import org.cempaka.cyclone.protocol.PayloadType;
import org.cempaka.cyclone.protocol.UdpDaemonChannel;
import org.cempaka.cyclone.services.MeasurementsListener;
import org.cempaka.cyclone.storage.ParcelMetadataRepository;
import org.cempaka.cyclone.storage.ParcelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaemonModule extends AbstractModule
{
    private static final Logger LOG = LoggerFactory.getLogger(DaemonModule.class);

    private final DaemonConfiguration daemonConfiguration;

    DaemonModule(final DaemonConfiguration daemonConfiguration)
    {
        this.daemonConfiguration = checkNotNull(daemonConfiguration);
    }

    @Override
    protected void configure()
    {
        final StorageConfiguration storageConfiguration = daemonConfiguration.getStorageConfiguration();
        final WorkersConfiguration workersConfiguration = daemonConfiguration.getWorkersConfiguration();
        final ChannelConfiguration channelConfiguration = daemonConfiguration.getChannelConfiguration();

        bind(ChannelConfiguration.class).toInstance(channelConfiguration);
        bind(StorageConfiguration.class).toInstance(storageConfiguration);
        bind(WorkersConfiguration.class).toInstance(workersConfiguration);

        bind(ParcelRepository.class).to(create(storageConfiguration.getParcelRepository()));

        bind(ParcelMetadataRepository.class).to(create(storageConfiguration.getParcelMetadataRepository()));

        bind(String.class).annotatedWith(Names.named("storage.path")).toInstance(storageConfiguration.getStoragePath());
        bind(String.class).annotatedWith(Names.named("guava.path")).toInstance(workersConfiguration.getGuavaPath());
        bind(Integer.class).annotatedWith(Names.named("worker.number"))
            .toInstance(workersConfiguration.getWorkersNumber());
        bind(Integer.class).annotatedWith(Names.named("udp.server.port"))
            .toInstance(channelConfiguration.getUdpServerPort());
        bind(Clock.class).toInstance(Clock.systemUTC());
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
        final MeasurementsListener measurementsListener)
    {
        return ImmutableListMultimap.<PayloadType, BiConsumer<String, Payload>>builder()
            .put(PayloadType.MEASUREMENTS, measurementsListener)
            .build();
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> create(final String className)
    {
        try {
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            LOG.error("Class [{}] can't be find in the classpath.", className);
            throw new RuntimeException(e);
        }
    }
}
