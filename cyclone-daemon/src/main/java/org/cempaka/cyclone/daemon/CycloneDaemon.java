package org.cempaka.cyclone.daemon;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.bundles.webjars.WebJarBundle;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Objects;
import org.cempaka.cyclone.auth.AuthFilterFactory;
import org.cempaka.cyclone.configurations.DaemonConfiguration;
import org.cempaka.cyclone.managed.DaemonTestRunnerManaged;
import org.cempaka.cyclone.managed.HeartbeatManaged;
import org.cempaka.cyclone.protocol.DaemonChannel;
import org.cempaka.cyclone.resources.MetricsResource;
import org.cempaka.cyclone.resources.ParcelResource;
import org.cempaka.cyclone.resources.StatusResource;
import org.cempaka.cyclone.resources.TestResource;
import org.cempaka.cyclone.storage.MigrationRunner;
import org.cempaka.cyclone.storage.ParcelIndexer;
import org.cempaka.cyclone.storage.repositories.ParcelMetadataRepository;
import org.cempaka.cyclone.storage.repositories.ParcelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CycloneDaemon extends Application<DaemonConfiguration>
{
    private static final Logger LOG = LoggerFactory.getLogger(CycloneDaemon.class);

    @Override
    public void initialize(final Bootstrap<DaemonConfiguration> bootstrap)
    {
        bootstrap.addBundle(new MultiPartBundle());
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
        bootstrap.addBundle(new WebJarBundle("org.webjars.npm", "org.webjars.bower"));
    }

    public static void main(final String[] args) throws Exception
    {
        new CycloneDaemon().run(args);
    }

    @Override
    public void run(final DaemonConfiguration daemonConfiguration, final Environment environment)
        throws SocketException, UnknownHostException
    {
        final Injector injector = Guice.createInjector(new DaemonModule(daemonConfiguration, environment));

        new MigrationRunner(daemonConfiguration).run();

        registerResources(environment, injector);
        registerManaged(environment, injector);
        registerFilters(environment, injector);

        indexParcels(injector);

        final DaemonChannel daemonChannel = injector.getInstance(DaemonChannel.class);
        daemonChannel.connect(daemonConfiguration.getChannelConfiguration().getUdpServerPort());

        LOG.info("Daemon started.");
    }

    private void registerFilters(final Environment environment, final Injector injector)
    {
        environment.jersey().register(injector.getInstance(AuthFilterFactory.class).create());
    }

    private void registerResources(final Environment environment, final Injector injector)
    {
        LOG.debug("Registering resources...");
        final JerseyEnvironment jersey = environment.jersey();
        jersey.setUrlPattern("/api/*");
        jersey.register(injector.getInstance(ParcelResource.class));
        jersey.register(injector.getInstance(TestResource.class));
        jersey.register(injector.getInstance(StatusResource.class));
        jersey.register(injector.getInstance(MetricsResource.class));
        LOG.info("Resources registered.");
    }

    private void registerManaged(final Environment environment, final Injector injector)
    {
        environment.lifecycle().manage(injector.getInstance(HeartbeatManaged.class));
        environment.lifecycle().manage(injector.getInstance(DaemonTestRunnerManaged.class));
    }

    private void indexParcels(final Injector injector)
    {
        final ParcelRepository parcelRepository = injector.getInstance(ParcelRepository.class);
        final ParcelIndexer parcelIndexer = injector.getInstance(ParcelIndexer.class);
        final ParcelMetadataRepository parcelMetadataRepository =
            injector.getInstance(ParcelMetadataRepository.class);
        parcelRepository.list()
            .map(parcelRepository::get)
            .filter(Objects::nonNull)
            .map(parcelIndexer::index)
            .forEach(parcelMetadataRepository::put);
    }
}
