package org.cempaka.cyclone.auth;

import java.security.Principal;

public final class AdminUser implements Principal
{
    static final AdminUser INSTANCE = new AdminUser();

    private AdminUser()
    {
    }

    @Override
    public String getName()
    {
        return "storm";
    }
}
