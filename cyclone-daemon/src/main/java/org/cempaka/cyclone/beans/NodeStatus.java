package org.cempaka.cyclone.beans;

import java.util.Objects;

public class NodeStatus
{
    private final int idleWorkers;
    private final int runningTests;

    public NodeStatus(final int idleWorkers, final int runningTests)
    {
        this.idleWorkers = idleWorkers;
        this.runningTests = runningTests;
    }

    public int getIdleWorkers()
    {
        return idleWorkers;
    }

    public int getRunningTests()
    {
        return runningTests;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final NodeStatus that = (NodeStatus) o;
        return idleWorkers == that.idleWorkers &&
            runningTests == that.runningTests;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(idleWorkers, runningTests);
    }
}
