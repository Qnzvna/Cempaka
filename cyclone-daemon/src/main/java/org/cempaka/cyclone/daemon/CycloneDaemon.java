package org.cempaka.cyclone.daemon;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import org.cempaka.cyclone.auth.AuthFilterFactory;
import org.cempaka.cyclone.configurations.DaemonConfiguration;
import org.cempaka.cyclone.core.channel.DaemonChannel;
import org.cempaka.cyclone.managed.DaemonTestRunnerManaged;
import org.cempaka.cyclone.managed.HeartbeatManaged;
import org.cempaka.cyclone.managed.StalledTestCleanerManaged;
import org.cempaka.cyclone.resources.ClusterResource;
import org.cempaka.cyclone.resources.LogMessagesResource;
import org.cempaka.cyclone.resources.MetadataResource;
import org.cempaka.cyclone.resources.MetricsResource;
import org.cempaka.cyclone.resources.ParcelResource;
import org.cempaka.cyclone.resources.TestExecutionResource;
import org.cempaka.cyclone.resources.TestResource;
import org.cempaka.cyclone.storage.MigrationRunner;
import org.cempaka.cyclone.webjars.WebJarBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketException;
import java.net.UnknownHostException;

public class CycloneDaemon extends Application<DaemonConfiguration>
{
    private static final Logger LOG = LoggerFactory.getLogger(CycloneDaemon.class);

    @Override
    public void initialize(final Bootstrap<DaemonConfiguration> bootstrap)
    {
        bootstrap.addBundle(new MultiPartBundle());
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
        bootstrap.addBundle(new WebJarBundle("org.webjars", "org.webjars.npm", "org.webjars.bower"));
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

        final DaemonChannel daemonChannel = injector.getInstance(DaemonChannel.class);
        daemonChannel.listen(daemonConfiguration.getChannelConfiguration().getUdpServerPort());

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
        jersey.register(injector.getInstance(ClusterResource.class));
        jersey.register(injector.getInstance(MetricsResource.class));
        jersey.register(injector.getInstance(TestExecutionResource.class));
        jersey.register(injector.getInstance(LogMessagesResource.class));
        jersey.register(injector.getInstance(MetadataResource.class));
        LOG.info("Resources registered.");
    }

    private void registerManaged(final Environment environment, final Injector injector)
    {
        final LifecycleEnvironment lifecycle = environment.lifecycle();
        lifecycle.manage(injector.getInstance(HeartbeatManaged.class));
        lifecycle.manage(injector.getInstance(DaemonTestRunnerManaged.class));
        lifecycle.manage(injector.getInstance(StalledTestCleanerManaged.class));
    }
}
