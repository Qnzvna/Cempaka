package org.cempaka.cyclone.auth;

import static com.google.common.base.Preconditions.checkNotNull;

import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.configuration.AuthenticationConfiguration;

@Singleton
public class AuthFilterFactory
{
    private final AuthenticationConfiguration authenticationConfiguration;

    @Inject
    AuthFilterFactory(final AuthenticationConfiguration authenticationConfiguration)
    {
        this.authenticationConfiguration = checkNotNull(authenticationConfiguration);
    }

    public AuthFilter create()
    {
        final Map<String, String> properties = authenticationConfiguration.getProperties();
        switch (authenticationConfiguration.getType()) {
            case BASIC:
                return new BasicCredentialAuthFilter.Builder<AdminUser>()
                    .setAuthenticator(createPasswordAuthenticator())
                    .setRealm(properties.getOrDefault("realm", ""))
                    .buildAuthFilter();
            case HEADER:
                return new HeaderAuthFilter(properties.getOrDefault("header", "X-Auth"));
            case NONE:
            default:
                return new NoopAuthFilter();
        }
    }

    private AdminPasswordAuthenticator createPasswordAuthenticator()
    {
        return new AdminPasswordAuthenticator(authenticationConfiguration.getProperties()
            .getOrDefault("password", AdminUser.INSTANCE.getName()));
    }
}
