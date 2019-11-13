package org.cempaka.cyclone.auth;

import static org.assertj.core.api.Assertions.assertThat;

import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import org.cempaka.cyclone.configurations.AuthenticationConfiguration;
import org.cempaka.cyclone.configurations.AuthenticationConfiguration.Type;
import org.junit.Test;

public class AuthFilterFactorySmallTest
{
    private AuthFilterFactory authFilterFactory;

    @Test
    public void shouldProvideNoopDynamicFeature()
    {
        //given
        authFilterFactory = new AuthFilterFactory(new AuthenticationConfiguration());
        //when
        final AuthFilter authFilter = authFilterFactory.create();
        //then
        assertThat(authFilter).isInstanceOf(NoopAuthFilter.class);
    }

    @Test
    public void shouldRegisterBasicAuth()
    {
        //given
        final AuthenticationConfiguration configuration = new AuthenticationConfiguration();
        configuration.setType(Type.BASIC);
        authFilterFactory = new AuthFilterFactory(configuration);
        //when
        final AuthFilter authFilter = authFilterFactory.create();
        //then
        assertThat(authFilter).isInstanceOf(BasicCredentialAuthFilter.class);
    }

    @Test
    public void shouldProviderHeaderAuthFilter()
    {
        //given
        final AuthenticationConfiguration configuration = new AuthenticationConfiguration();
        configuration.setType(Type.HEADER);
        authFilterFactory = new AuthFilterFactory(configuration);
        //when
        final AuthFilter authFilter = authFilterFactory.create();
        //then
        assertThat(authFilter).isInstanceOf(HeaderAuthFilter.class);
    }
}