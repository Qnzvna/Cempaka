package org.cempaka.cyclone.services;

import static org.cempaka.cyclone.core.utils.Preconditions.checkNotNull;

import java.util.Map;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.Singleton;

@Singleton
public class StaticNodeIdentifierProvider implements NodeIdentifierProvider
{
    private final String nodeId;

    @Inject
    public StaticNodeIdentifierProvider(@Named("node.provider.properties") final Map<String, String> properties)
    {
        this.nodeId = checkNotNull(properties.get("nodeId"));
    }

    @Override
    public String get()
    {
        return nodeId;
    }
}
