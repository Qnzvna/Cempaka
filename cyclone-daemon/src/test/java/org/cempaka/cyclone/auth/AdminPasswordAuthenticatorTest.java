package org.cempaka.cyclone.auth;

import static org.assertj.core.api.Assertions.assertThat;

import io.dropwizard.auth.basic.BasicCredentials;
import java.util.Optional;
import org.junit.Test;

public class AdminPasswordAuthenticatorTest
{
    private static final String PASSWORD = "password";

    private final AdminPasswordAuthenticator adminPasswordAuthenticator = new AdminPasswordAuthenticator(PASSWORD);

    @Test
    public void shouldAuthenticateStormAdmin()
    {
        //given
        final BasicCredentials credentials = new BasicCredentials(AdminUser.INSTANCE.getName(), PASSWORD);
        //when
        final Optional<AdminUser> authenticate = adminPasswordAuthenticator.authenticate(credentials);
        //then
        assertThat(authenticate).contains(AdminUser.INSTANCE);
    }

    @Test
    public void shouldNotAuthenticateStormAdminForBadUsername()
    {
        //given
        final BasicCredentials credentials = new BasicCredentials("root", PASSWORD);
        //when
        final Optional<AdminUser> authenticate = adminPasswordAuthenticator.authenticate(credentials);
        //then
        assertThat(authenticate).isEmpty();
    }

    @Test
    public void shouldNotAuthenticateStormAdminForBadPassword()
    {
        //given
        final BasicCredentials credentials = new BasicCredentials(AdminUser.INSTANCE.getName(), "bad_password");
        //when
        final Optional<AdminUser> authenticate = adminPasswordAuthenticator.authenticate(credentials);
        //then
        assertThat(authenticate).isEmpty();
    }
}