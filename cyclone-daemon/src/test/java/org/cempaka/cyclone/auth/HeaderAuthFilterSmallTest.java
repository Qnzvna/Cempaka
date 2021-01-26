package org.cempaka.cyclone.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HeaderAuthFilterSmallTest
{
    private static final String HEADER_NAME = "X-Auth";
    private static final String USERNAME = "username";

    @Mock
    private SecurityContext securityContext;
    @Mock
    private ContainerRequestContext containerRequestContext;

    private final HeaderAuthFilter authFilter = new HeaderAuthFilter(HEADER_NAME);

    @Before
    public void setUp()
    {
        given(securityContext.isSecure()).willReturn(true);
        given(containerRequestContext.getSecurityContext()).willReturn(securityContext);
    }

    @Test
    public void shouldPassAuthentication()
    {
        //given
        given(containerRequestContext.getHeaderString(HEADER_NAME)).willReturn(USERNAME);
        //when
        authFilter.filter(containerRequestContext);
        //then
        verify(containerRequestContext, times(1)).setSecurityContext(any());
    }

    @Test
    public void shouldFailAuthenticationWithoutHeader()
    {
        //given
        //when
        final Throwable throwable = catchThrowable(() -> authFilter.filter(containerRequestContext));
        //then
        assertThat(throwable).isInstanceOf(WebApplicationException.class)
            .satisfies(exception -> {
                final Response response = ((WebApplicationException) exception).getResponse();
                assertThat(response.getStatus()).isEqualTo(Status.UNAUTHORIZED.getStatusCode());
            });
    }
}