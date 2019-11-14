package org.cempaka.cyclone.storage;

import static com.google.common.base.Preconditions.checkNotNull;

import io.dropwizard.db.DataSourceFactory;
import org.cempaka.cyclone.configurations.DaemonConfiguration;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MigrationRunner implements Runnable
{
    private static final Logger LOG = LoggerFactory.getLogger(MigrationRunner.class);

    private final DaemonConfiguration daemonConfiguration;

    public MigrationRunner(final DaemonConfiguration daemonConfiguration)
    {
        this.daemonConfiguration = checkNotNull(daemonConfiguration);
    }

    @Override
    public void run()
    {
        final DataSourceFactory dataSourceFactory = daemonConfiguration.getDataSourceFactory();
        if (dataSourceFactory != null) {
            LOG.info("Starting to run migration...");
            final Flyway flyway = Flyway.configure().dataSource(dataSourceFactory.getUrl(),
                dataSourceFactory.getUser(),
                dataSourceFactory.getPassword())
                .connectRetries(3)
                .load();
            final int migrationsApplied = flyway.migrate();
            LOG.info("Database migration completed. {} migrations applied.", migrationsApplied);
        } else {
            LOG.info("Not running database migrations, data source is not configured.");
        }
    }
}
