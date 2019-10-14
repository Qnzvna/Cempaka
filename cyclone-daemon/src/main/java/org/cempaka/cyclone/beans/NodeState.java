package org.cempaka.cyclone.beans;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

public class NodeState
{
    private final String identifier;
    private final NodeStatus nodeStatus;
    private final long timestamp;

    public NodeState(final String identifier, final NodeStatus nodeStatus, final long timestamp)
    {
        this.identifier = checkNotNull(identifier);
        this.nodeStatus = nodeStatus;
        this.timestamp = timestamp;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public NodeStatus getNodeStatus()
    {
        return nodeStatus;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public boolean isUp()
    {
        return getNodeStatus().equals(NodeStatus.UP);
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final NodeState nodeState = (NodeState) o;
        return timestamp == nodeState.timestamp &&
            Objects.equals(identifier, nodeState.identifier) &&
            nodeStatus == nodeState.nodeStatus;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(identifier, nodeStatus, timestamp);
    }
}
