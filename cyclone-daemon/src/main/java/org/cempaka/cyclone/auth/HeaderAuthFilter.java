package org.cempaka.cyclone.auth;

import static com.google.common.base.Preconditions.checkNotNull;

import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.Authenticator;
import java.util.Optional;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
            throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
        }
    }
}
