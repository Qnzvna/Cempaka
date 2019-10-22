package org.cempaka.cyclone.auth;

import io.dropwizard.auth.AuthFilter;
import java.util.Optional;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;

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
