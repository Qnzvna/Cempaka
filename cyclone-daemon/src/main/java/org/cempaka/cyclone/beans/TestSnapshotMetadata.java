package org.cempaka.cyclone.beans;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableSet;
import java.util.Objects;
import java.util.Set;

public class TestSnapshotMetadata
{
    private final String id;
    private final long timestamp;
    private final Set<String> testNames;

    public TestSnapshotMetadata(final String id, final long timestamp, final Set<String> testNames)
    {
        this.id = checkNotNull(id);
        this.timestamp = timestamp;
        this.testNames = ImmutableSet.copyOf(testNames);
    }

    public String getId()
    {
        return id;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final TestSnapshotMetadata that = (TestSnapshotMetadata) o;
        return timestamp == that.timestamp &&
            Objects.equals(id, that.id) &&
            Objects.equals(testNames, that.testNames);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, timestamp, testNames);
    }

    public Set<String> getTestNames()
    {
        return testNames;
    }

}
