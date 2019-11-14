package org.cempaka.cyclone.storage.repositories;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.NodeState;

@Singleton
public class MemoryNodeStateRepository implements NodeStateDataRepository
{
    private final Map<String, NodeState> storage = Maps.newConcurrentMap();

    @Override
    public void put(final NodeState nodeState)
    {
        storage.put(nodeState.getIdentifier(), nodeState);
    }

    @Override
    public Set<NodeState> getAll()
    {
        return ImmutableSet.copyOf(storage.values());
    }
}
