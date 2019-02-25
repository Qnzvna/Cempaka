package org.cempaka.cyclone.beans;

import java.util.Objects;

public class NodeStatus
{
    private final int workersNumber;
    private final int runningTests;

    public NodeStatus(final int workersNumber, final int runningTests)
    {
        this.workersNumber = workersNumber;
        this.runningTests = runningTests;
    }

    public int getWorkersNumber()
    {
        return workersNumber;
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
        return workersNumber == that.workersNumber &&
            runningTests == that.runningTests;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(workersNumber, runningTests);
    }
}
