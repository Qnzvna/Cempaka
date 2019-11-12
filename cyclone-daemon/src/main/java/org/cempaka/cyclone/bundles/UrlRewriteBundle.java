package org.cempaka.cyclone.bundles;

import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import javax.servlet.FilterRegistration;
import org.cempaka.cyclone.configurations.DaemonConfiguration;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

public class UrlRewriteBundle implements ConfiguredBundle<DaemonConfiguration>
{
    @Override
    public void run(final DaemonConfiguration configuration, final Environment environment) throws Exception
    {
        FilterRegistration.Dynamic registration = environment.servlets()
            .addFilter("UrlRewriteFilter", new UrlRewriteFilter());
        registration.addMappingForUrlPatterns(null, true, "/*");
        registration.setInitParameter("confPath", "urlrewrite.xml");
    }

    @Override
    public void initialize(final Bootstrap<?> bootstrap)
    {
    }
}
