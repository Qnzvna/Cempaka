package org.cempaka.cyclone.storage;

import com.google.inject.Exposed;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import javax.inject.Singleton;
import org.cempaka.cyclone.storage.data.NodeStateDataAccess;
import org.cempaka.cyclone.storage.data.ParcelMetadataDataAccess;
import org.cempaka.cyclone.storage.data.TestRunMetricDataAccess;
import org.cempaka.cyclone.storage.data.TestRunStackTraceDataAccess;
import org.cempaka.cyclone.storage.data.TestRunStatusDataAccess;
import org.cempaka.cyclone.storage.repository.FileParcelRepository;
import org.cempaka.cyclone.storage.repository.JdbiParcelMetadataRepository;
import org.cempaka.cyclone.storage.repository.ParcelMetadataRepository;
import org.cempaka.cyclone.storage.repository.ParcelRepository;
import org.jdbi.v3.core.Jdbi;

public class StorageModule extends PrivateModule
{
    @Override
    protected void configure()
    {
        bind(ParcelRepository.class).to(FileParcelRepository.class);
        bind(ParcelMetadataRepository.class).to(JdbiParcelMetadataRepository.class);
        expose(ParcelRepository.class);
        expose(ParcelMetadataRepository.class);
    }

    @Exposed
    @Provides
    @Singleton
    TestRunMetricDataAccess testRunMetricsRepository(final Jdbi jdbi)
    {
        return jdbi.onDemand(TestRunMetricDataAccess.class);
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
}
