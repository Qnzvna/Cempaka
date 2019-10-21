package org.cempaka.cyclone.auth;

import static com.google.common.base.Preconditions.checkNotNull;

import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.configuration.AuthenticationConfiguration;

@Singleton
public class AuthFilterFactory
{
    private final AuthenticationConfiguration authenticationConfiguration;

    @Inject
    public AuthFilterFactory(final AuthenticationConfiguration authenticationConfiguration)
    {
        this.authenticationConfiguration = checkNotNull(authenticationConfiguration);
    }

    public AuthFilter create()
    {
        switch (authenticationConfiguration.getType()) {
            case BASIC:
                return new BasicCredentialAuthFilter.Builder<AdminUser>()
                    .setAuthenticator(createPasswordAuthenticator())
                    .setRealm(authenticationConfiguration.getProperties().getOrDefault("realm", ""))
                    .buildAuthFilter();
            case HEADER:
                throw new IllegalArgumentException("Header type authorization is not supported yet.");
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
