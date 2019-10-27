package org.cempaka.cyclone.storage;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Exposed;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import java.util.Map;
import javax.inject.Singleton;
import org.cempaka.cyclone.configurations.TypedConfiguration;
import org.cempaka.cyclone.configurations.StorageConfiguration;
import org.cempaka.cyclone.storage.jdbi.NodeStateDataAccess;
import org.cempaka.cyclone.storage.jdbi.ParcelMetadataDataAccess;
import org.cempaka.cyclone.storage.jdbi.TestMetricsDataAccess;
import org.cempaka.cyclone.storage.jdbi.TestRunStackTraceDataAccess;
import org.cempaka.cyclone.storage.jdbi.TestRunStatusDataAccess;
import org.cempaka.cyclone.storage.repositories.JdbiMeasurementRepository;
import org.cempaka.cyclone.storage.repositories.JdbiParcelMetadataRepository;
import org.cempaka.cyclone.storage.repositories.MeasurementsRepository;
import org.cempaka.cyclone.storage.repositories.ParcelMetadataRepository;
import org.cempaka.cyclone.storage.repositories.ParcelRepository;
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
        bind(ParcelMetadataRepository.class).to(JdbiParcelMetadataRepository.class);
        bind(MeasurementsRepository.class).to(JdbiMeasurementRepository.class);
        expose(ParcelRepository.class);
        expose(ParcelMetadataRepository.class);
        expose(MeasurementsRepository.class);

        final TypedConfiguration<ParcelRepository> parcelRepositoryConfiguration =
            storageConfiguration.getParcelRepositoryConfiguration();
        bind(ParcelRepository.class).to(parcelRepositoryConfiguration.getType());
        bind(new TypeLiteral<Map<String, String>>() {}).annotatedWith(Names.named("parcel.repository.parameters"))
            .toInstance(parcelRepositoryConfiguration.getParameters());
    }

    @Exposed
    @Provides
    @Singleton
    TestRunStackTraceDataAccess testRunStackTracesRepository(final Jdbi jdbi)
    {
        return jdbi.onDemand(TestRunStackTraceDataAccess.class);
    }

    @Exposed
    @Provides
    @Singleton
    NodeStateDataAccess nodeStateDataAccess(final Jdbi jdbi)
    {
        return jdbi.onDemand(NodeStateDataAccess.class);
    }

    @Exposed
    @Provides
    @Singleton
    TestRunStatusDataAccess testRunStateDataAccess(final Jdbi jdbi)
    {
        return jdbi.onDemand(TestRunStatusDataAccess.class);
    }

    @Provides
    @Singleton
    ParcelMetadataDataAccess parcelMetadataDataAccess(final Jdbi jdbi)
    {
        return jdbi.onDemand(ParcelMetadataDataAccess.class);
    }

    @Provides
    @Singleton
    TestMetricsDataAccess testMetricsDataAccess(final Jdbi jdbi)
    {
        return jdbi.onDemand(TestMetricsDataAccess.class);
    }
}
