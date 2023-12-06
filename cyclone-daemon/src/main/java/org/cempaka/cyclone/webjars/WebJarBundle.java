package org.cempaka.cyclone.webjars;

import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import io.dropwizard.core.Configuration;
import io.dropwizard.core.ConfiguredBundle;
import io.dropwizard.core.setup.Environment;

import java.util.Collections;
import java.util.List;

public class WebJarBundle implements ConfiguredBundle<Configuration>
{
    private CacheBuilder<AssetId, Asset> cacheBuilder = null;
    private List<String> packages = Lists.newArrayList(WebJarServlet.DEFAULT_MAVEN_GROUPS);
    private String urlPrefix = WebJarServlet.DEFAULT_URL_PREFIX;

    public WebJarBundle()
    {
    }

    public WebJarBundle(CacheBuilder<AssetId, Asset> builder)
    {
        cacheBuilder = builder;
    }

    public WebJarBundle(String... additionalPackages)
    {
        Collections.addAll(packages, additionalPackages);
    }

    public WebJarBundle(CacheBuilder<AssetId, Asset> builder, String... additionalPackages)
    {
        cacheBuilder = builder;
        Collections.addAll(packages, additionalPackages);
    }

    public WebJarBundle withUrlPrefix(String prefix)
    {
        urlPrefix = prefix;
        return this;
    }

    private String normalizedUrlPrefix()
    {
        final StringBuilder pathBuilder = new StringBuilder();
        if (!urlPrefix.startsWith("/")) {
            pathBuilder.append('/');
        }
        pathBuilder.append(urlPrefix);
        if (!urlPrefix.endsWith("/")) {
            pathBuilder.append('/');
        }
        return pathBuilder.toString();
    }

    @Override
    public void run(Configuration configuration, Environment environment)
    {
        String prefix = normalizedUrlPrefix();
        WebJarServlet servlet = new WebJarServlet(cacheBuilder, packages, prefix);
        environment.servlets()
            .addServlet("webjars", servlet)
            .addMapping(prefix + "*");
    }
}
