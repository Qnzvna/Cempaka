package org.cempaka.cyclone.storage.repositories;

import static com.google.common.base.Preconditions.checkNotNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.NodeState;
import org.cempaka.cyclone.storage.jdbi.NodeStateDataAccess;

@Singleton
public class JdbiNodeStateRepository implements NodeStateDataRepository
{
    private final NodeStateDataAccess nodeStateDataAccess;

    @Inject
    public JdbiNodeStateRepository(final NodeStateDataAccess nodeStateDataAccess)
    {
        this.nodeStateDataAccess = checkNotNull(nodeStateDataAccess);
    }

    @Override
    public void put(final NodeState nodeState)
    {
        nodeStateDataAccess.upsert(nodeState.getIdentifier(),
            nodeState.getNodeStatus().toString(),
            Timestamp.from(Instant.ofEpochSecond(nodeState.getTimestamp())));
    }

    @Override
    public Set<NodeState> getAll()
    {
        return nodeStateDataAccess.getAll();
    }
}
