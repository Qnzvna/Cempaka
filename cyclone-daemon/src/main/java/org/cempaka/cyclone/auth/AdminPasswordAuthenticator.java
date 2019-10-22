package org.cempaka.cyclone.auth;

import static com.google.common.base.Preconditions.checkNotNull;

import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import java.util.Optional;

class AdminPasswordAuthenticator implements Authenticator<BasicCredentials, AdminUser>
{
    private final String password;

    AdminPasswordAuthenticator(final String password)
    {
        this.password = checkNotNull(password);
    }

    @Override
    public Optional<AdminUser> authenticate(final BasicCredentials credentials)
    {
        if (credentials.getUsername().equals(AdminUser.INSTANCE.getName()) &&
            credentials.getPassword().equals(password)) {
            return Optional.of(AdminUser.INSTANCE);
        } else {
            return Optional.empty();
        }
    }
}
