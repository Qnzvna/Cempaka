package org.cempaka.cyclone.services;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

@Singleton
public class StaticNodeIdentifierProvider implements NodeIdentifierProvider
{
    private final String nodeId;

    @Inject
    public StaticNodeIdentifierProvider(@Named("node.id") final String nodeId)
    {
        this.nodeId = checkNotNull(nodeId);
    }

    @Override
    public String get()
    {
        return nodeId;
    }
}
