package org.cempaka.cyclone.storage;

import com.google.inject.Exposed;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.cempaka.cyclone.configuration.StorageConfiguration;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class StorageModule extends PrivateModule
{
    private static final Logger LOG = LoggerFactory.getLogger(StorageModule.class);

    private final StorageConfiguration storageConfiguration;

    public StorageModule(final StorageConfiguration storageConfiguration)
    {
        this.storageConfiguration = checkNotNull(storageConfiguration);
    }

    @Override
    protected void configure()
    {
        bind(ParcelRepository.class).to(create(storageConfiguration.getParcelRepository()));
        bind(ParcelMetadataRepository.class).to(create(storageConfiguration.getParcelMetadataRepository()));
        expose(ParcelRepository.class);
        expose(ParcelMetadataRepository.class);
    }

    @Exposed
    @Provides
    @Singleton
    TestRunEventDataAccess testRunEventRepository(final Jdbi jdbi)
    {
        return jdbi.onDemand(TestRunEventDataAccess.class);
    }

    @Exposed
    @Provides
    @Singleton
    TestRunMetadataDataAcess testRunMetadataRepository(final Jdbi jdbi)
    {
        return jdbi.onDemand(TestRunMetadataDataAcess.class);
    }

    @Exposed
    @Provides
    @Singleton
    TestRunMetricDataAcess testRunMetricsRepository(final Jdbi jdbi)
    {
        return jdbi.onDemand(TestRunMetricDataAcess.class);
    }

    @Exposed
    @Provides
    @Singleton
    TestRunStackTraceDataAcess testRunStackTracesRepository(final Jdbi jdbi)
    {
        return jdbi.onDemand(TestRunStackTraceDataAcess.class);
    }

    @Provides
    @Singleton
    ParcelMetadataDataAcess parcelMetadataDataAcess(final Jdbi jdbi)
    {
        return jdbi.onDemand(ParcelMetadataDataAcess.class);
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
