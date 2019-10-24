package org.cempaka.cyclone.services;

import static com.google.common.base.Preconditions.checkNotNull;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.cempaka.cyclone.beans.NodeState;
import org.cempaka.cyclone.beans.NodeStatus;
import org.cempaka.cyclone.storage.jdbi.NodeStateDataAccess;

@Singleton
public class NodeStatusService
{
    private static final int HEARTBEAT_RATIO = 2;

    private final NodeStateDataAccess nodeStateDataAccess;
    private final NodeIdentifierProvider nodeIdentifierProvider;
    private final long heartbeatInterval;
    private final Clock clock;

    @Inject
    public NodeStatusService(final NodeStateDataAccess nodeStateDataAccess,
                             final NodeIdentifierProvider nodeIdentifierProvider,
                             @Named("heartbeat.interval") final long heartbeatInterval,
                             final Clock clock)
    {
        this.nodeStateDataAccess = checkNotNull(nodeStateDataAccess);
        this.nodeIdentifierProvider = checkNotNull(nodeIdentifierProvider);
        this.heartbeatInterval = heartbeatInterval;
        this.clock = checkNotNull(clock);
    }

    public void updateStatus(final NodeStatus state)
    {
        final String identifier = nodeIdentifierProvider.get();
        final Timestamp now = Timestamp.from(Instant.now(clock));
        nodeStateDataAccess.upsert(identifier, state.toString(), now);
    }

    public Set<String> getLiveNodes()
    {
        final long now = Instant.now(clock).getEpochSecond();
        return nodeStateDataAccess.getNodes().stream()
            .filter(NodeState::isUp)
            .filter(nodeState -> isAlive(now, nodeState))
            .map(NodeState::getIdentifier)
            .collect(Collectors.toSet());
    }

    public Map<String, Boolean> getNodesStatus()
    {
        final long now = Instant.now(clock).getEpochSecond();
        return nodeStateDataAccess.getNodes().stream()
            .collect(Collectors.toMap(NodeState::getIdentifier,
                nodeState -> nodeState.isUp() && isAlive(now, nodeState)));
    }

    private boolean isAlive(final long now, final NodeState nodeState)
    {
        return nodeState.getTimestamp() > now - heartbeatInterval * HEARTBEAT_RATIO;
    }
}
