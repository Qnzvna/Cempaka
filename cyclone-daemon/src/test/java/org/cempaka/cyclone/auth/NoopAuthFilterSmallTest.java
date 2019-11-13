package org.cempaka.cyclone.auth;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NoopAuthFilterSmallTest
{
    @Mock
    private SecurityContext securityContext;
    @Mock
    private ContainerRequestContext containerRequestContext;

    private final NoopAuthFilter noopAuthFilter = new NoopAuthFilter();

    @Test
    public void shouldPassAuthentication()
    {
        //given
        given(securityContext.isSecure()).willReturn(true);
        given(containerRequestContext.getSecurityContext()).willReturn(securityContext);
        //when
        noopAuthFilter.filter(containerRequestContext);
        //then
        verify(containerRequestContext, times(1))
            .setSecurityContext(argThat(new PrincipalSecurityContextMatcher(AdminUser.INSTANCE)));
    }

}