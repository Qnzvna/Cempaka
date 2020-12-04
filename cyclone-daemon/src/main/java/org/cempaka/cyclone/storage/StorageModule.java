package org.cempaka.cyclone.storage;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Exposed;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import java.util.Map;
import javax.inject.Singleton;
import org.cempaka.cyclone.configurations.StorageConfiguration;
import org.cempaka.cyclone.configurations.TypedConfiguration;
import org.cempaka.cyclone.storage.jdbi.LogMessageDataAccess;
import org.cempaka.cyclone.storage.jdbi.NodeStateDataAccess;
import org.cempaka.cyclone.storage.jdbi.ParcelDataAccess;
import org.cempaka.cyclone.storage.jdbi.TestDataAccess;
import org.cempaka.cyclone.storage.jdbi.TestExecutionDataAccess;
import org.cempaka.cyclone.storage.jdbi.TestMetricDataAccess;
import org.cempaka.cyclone.storage.repositories.JdbiTestMetricRepository;
import org.cempaka.cyclone.storage.repositories.JdbiTestRepository;
import org.cempaka.cyclone.storage.repositories.JooqMetadataRepository;
import org.cempaka.cyclone.storage.repositories.LogMessageRepository;
import org.cempaka.cyclone.storage.repositories.MetadataRepository;
import org.cempaka.cyclone.storage.repositories.NodeStateDataRepository;
import org.cempaka.cyclone.storage.repositories.ParcelRepository;
import org.cempaka.cyclone.storage.repositories.TestExecutionRepository;
import org.cempaka.cyclone.storage.repositories.TestMetricRepository;
import org.cempaka.cyclone.storage.repositories.TestRepository;
import org.jdbi.v3.core.Jdbi;

public class StorageModule extends PrivateModule
{
    private final StorageConfiguration storageConfiguration;

    public StorageModule(final StorageConfiguration storageConfiguration)
    {
        this.storageConfiguration = checkNotNull(storageConfiguration);
    }

    @Override
    protected void configure()
    {
        final TypedConfiguration<ParcelRepository> parcelRepositoryConfiguration =
            storageConfiguration.getParcelRepositoryConfiguration();
        bind(ParcelRepository.class).to(parcelRepositoryConfiguration.getType());
        bind(new TypeLiteral<Map<String, String>>() {}).annotatedWith(Names.named("parcel.repository.parameters"))
            .toInstance(parcelRepositoryConfiguration.getParameters());
        bind(TestExecutionRepository.class)
            .to(storageConfiguration.getTestExecutionRepositoryConfiguration().getType());
        bind(NodeStateDataRepository.class)
            .to(storageConfiguration.getNodeStateRepositoryConfiguration().getType());
        bind(TestRepository.class)
            .to(storageConfiguration.getTestRepositoryConfiguration().getType());
        bind(TestMetricRepository.class)
            .to(storageConfiguration.getMetricsRepository().getType());
        bind(LogMessageRepository.class)
            .to(storageConfiguration.getLogRepository().getType());
        bind(MetadataRepository.class)
            .to(JooqMetadataRepository.class); // TODO

        expose(ParcelRepository.class);
        expose(TestRepository.class);
        expose(TestMetricRepository.class);
        expose(TestExecutionRepository.class);
        expose(NodeStateDataRepository.class);
        expose(LogMessageRepository.class);
        expose(MetadataRepository.class);
    }

    @Provides
    @Singleton
    NodeStateDataAccess nodeStateDataAccess(final Jdbi jdbi)
    {
        return jdbi.onDemand(NodeStateDataAccess.class);
    }

    @Provides
    @Singleton
    TestExecutionDataAccess testExecutionDataAccess(final Jdbi jdbi)
    {
        return jdbi.onDemand(TestExecutionDataAccess.class);
    }

    @Provides
    @Singleton
    ParcelDataAccess parcelDataAccess(final Jdbi jdbi)
    {
        return jdbi.onDemand(ParcelDataAccess.class);
    }

    @Provides
    @Singleton
    TestDataAccess parcelMetadataDataAccess(final Jdbi jdbi)
    {
        return jdbi.onDemand(TestDataAccess.class);
    }

    @Provides
    @Singleton
    TestMetricDataAccess testMetricsDataAccess(final Jdbi jdbi)
    {
        return jdbi.onDemand(TestMetricDataAccess.class);
    }

    @Provides
    @Singleton
    LogMessageDataAccess logMessageDataAccess(final Jdbi jdbi)
    {
        return jdbi.onDemand(LogMessageDataAccess.class);
    }
}
