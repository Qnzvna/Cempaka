package org.cempaka.cyclone.services;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.cempaka.cyclone.beans.NodeState;
import org.cempaka.cyclone.beans.NodeStatus;
import org.cempaka.cyclone.storage.repositories.NodeStateDataRepository;

@Singleton
public class NodeStatusService
{
    private static final int HEARTBEAT_RATIO = 2;

    private final NodeStateDataRepository nodeStateDataRepository;
    private final NodeIdentifierProvider nodeIdentifierProvider;
    private final long heartbeatInterval;
    private final Clock clock;

    @Inject
    public NodeStatusService(final NodeStateDataRepository nodeStateDataRepository,
                             final NodeIdentifierProvider nodeIdentifierProvider,
                             @Named("heartbeat.interval") final long heartbeatInterval,
                             final Clock clock)
    {
        this.nodeStateDataRepository = checkNotNull(nodeStateDataRepository);
        this.nodeIdentifierProvider = checkNotNull(nodeIdentifierProvider);
        this.heartbeatInterval = heartbeatInterval;
        this.clock = checkNotNull(clock);
    }

    public void updateStatus(final NodeStatus status)
    {
        final String identifier = nodeIdentifierProvider.get();
        nodeStateDataRepository.put(new NodeState(identifier, status, Instant.now(clock).getEpochSecond()));
    }

    public Set<String> getLiveNodes()
    {
        final long now = Instant.now(clock).getEpochSecond();
        return nodeStateDataRepository.getAll().stream()
            .filter(NodeState::isUp)
            .filter(nodeState -> isAlive(now, nodeState))
            .map(NodeState::getIdentifier)
            .collect(Collectors.toSet());
    }

    public Set<String> getDeadNodes()
    {
        final long now = Instant.now(clock).getEpochSecond();
        return nodeStateDataRepository.getAll().stream()
            .filter(nodeState -> nodeState.isDown() || !isAlive(now, nodeState))
            .map(NodeState::getIdentifier)
            .collect(Collectors.toSet());
    }

    public Map<String, Boolean> getNodesStatus()
    {
        final long now = Instant.now(clock).getEpochSecond();
        return nodeStateDataRepository.getAll().stream()
            .collect(Collectors.toMap(NodeState::getIdentifier,
                nodeState -> nodeState.isUp() && isAlive(now, nodeState)));
    }

    private boolean isAlive(final long now, final NodeState nodeState)
    {
        return nodeState.getTimestamp() > now - heartbeatInterval * HEARTBEAT_RATIO;
    }
}
