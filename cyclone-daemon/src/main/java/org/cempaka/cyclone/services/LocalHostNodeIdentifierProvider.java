package org.cempaka.cyclone.services;

import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LocalHostNodeIdentifierProvider implements NodeIdentifierProvider
{
    private final String identifier;

    @Inject
    public LocalHostNodeIdentifierProvider()
    {
        try {
            identifier = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String get()
    {
        return identifier;
    }
}
