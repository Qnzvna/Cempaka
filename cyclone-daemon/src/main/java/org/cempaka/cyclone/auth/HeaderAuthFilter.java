package org.cempaka.cyclone.auth;

import static com.google.common.base.Preconditions.checkNotNull;

import io.dropwizard.auth.AuthFilter;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Priority(Priorities.AUTHENTICATION)
class HeaderAuthFilter extends AuthFilter<String, AdminUser>
{
    private static final String SCHEME = "header";

    private final String headerName;

    HeaderAuthFilter(final String headerName)
    {
        this.headerName = checkNotNull(headerName);
        this.authenticator = ignore -> Optional.of(AdminUser.INSTANCE);
    }

    @Override
    public void filter(final ContainerRequestContext requestContext)
    {
        if (!authenticate(requestContext, requestContext.getHeaderString(headerName), SCHEME)) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
