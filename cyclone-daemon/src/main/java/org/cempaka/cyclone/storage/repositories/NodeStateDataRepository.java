package org.cempaka.cyclone.storage.repositories;

import java.util.Set;
import org.cempaka.cyclone.beans.NodeState;

public interface NodeStateDataRepository
{
    void put(NodeState nodeState);

    Set<NodeState> getAll();
}
