package org.cempaka.cyclone.auth;

import io.dropwizard.auth.AuthFilter;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;

import java.util.Optional;

@Priority(Priorities.AUTHENTICATION)
class NoopAuthFilter extends AuthFilter<Object, AdminUser>
{
    private static final String SCHEME = "noop";

    NoopAuthFilter()
    {
        authenticator = ignore -> Optional.of(AdminUser.INSTANCE);
    }

    @Override
    public void filter(final ContainerRequestContext requestContext)
    {
        authenticate(requestContext, new Object(), SCHEME);
    }
}
