package org.cempaka.cyclone.auth;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.security.Principal;
import jakarta.ws.rs.core.SecurityContext;
import org.assertj.core.matcher.AssertionMatcher;

class PrincipalSecurityContextMatcher extends AssertionMatcher<SecurityContext>
{
    private final Principal principal;

    PrincipalSecurityContextMatcher(final Principal principal)
    {
        this.principal = checkNotNull(principal);
    }

    @Override
    public void assertion(final SecurityContext securityContext) throws AssertionError
    {
        assertThat(securityContext.getUserPrincipal()).isEqualTo(principal);
    }
}
