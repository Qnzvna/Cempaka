package org.cempaka.cyclone.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NoopAuthFilterSmallTest
{
    @Mock
    private SecurityContext securityContext;
    @Mock
    private ContainerRequestContext containerRequestContext;

    private final NoopAuthFilter noopAuthFilter = new NoopAuthFilter();

    @Test
    void shouldPassAuthentication()
    {
        //given
        given(securityContext.isSecure()).willReturn(true);
        given(containerRequestContext.getSecurityContext()).willReturn(securityContext);
        //when
        noopAuthFilter.filter(containerRequestContext);
        //then
        verify(containerRequestContext, times(1)).setSecurityContext(any());
    }

}