package org.cempaka.cyclone.auth;

import io.dropwizard.auth.AuthFilter;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;

@Priority(Priorities.AUTHENTICATION)
public class NoopAuthFilter extends AuthFilter
{
    @Override
    public void filter(final ContainerRequestContext requestContext)
    {
    }
}
