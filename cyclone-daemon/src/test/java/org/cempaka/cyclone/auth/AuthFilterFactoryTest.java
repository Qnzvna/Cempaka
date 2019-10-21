package org.cempaka.cyclone.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import org.cempaka.cyclone.configuration.AuthenticationConfiguration;
import org.cempaka.cyclone.configuration.AuthenticationConfiguration.Type;
import org.junit.Test;

public class AuthFilterFactoryTest
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
    public void shouldFailRegisterHeaderAuth()
    {
        //given
        final AuthenticationConfiguration configuration = new AuthenticationConfiguration();
        configuration.setType(Type.HEADER);
        authFilterFactory = new AuthFilterFactory(configuration);
        //when
        //then
        assertThatThrownBy(() -> authFilterFactory.create()).isInstanceOf(IllegalArgumentException.class);
    }
}