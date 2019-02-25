package org.cempaka.cyclone.beans;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.cempaka.cyclone.protocol.Status;

public class TestSnapshot
{
    private final String testId;
    private final long timestamp;
    private final Status status;
    private final Map<String, Double> measurements;
    private final Set<String> testNames;

    public TestSnapshot(final String testId,
                        final long timestamp,
                        final Status status,
                        final Map<String, Double> measurements)
    {
        this.testId = checkNotNull(testId);
        this.timestamp = timestamp;
        this.status = status;
        this.measurements = ImmutableMap.copyOf(measurements);
        this.testNames = ImmutableSet.of();
    }

    public TestSnapshot(final String testId,
                        final long timestamp,
                        final Status status,
                        final Map<String, Double> measurements,
                        final List<String> testNames)
    {
        this.testId = checkNotNull(testId);
        this.timestamp = timestamp;
        this.status = status;
        this.measurements = ImmutableMap.copyOf(measurements);
        this.testNames = ImmutableSet.copyOf(testNames);
    }

    public String getTestId()
    {
        return testId;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public Status getStatus()
    {
        return status;
    }

    public Map<String, Double> getMeasurements()
    {
        return measurements;
    }

    public Set<String> getTestNames()
    {
        return testNames;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final TestSnapshot that = (TestSnapshot) o;
        return timestamp == that.timestamp &&
            Objects.equals(testId, that.testId) &&
            status == that.status &&
            Objects.equals(measurements, that.measurements) &&
            Objects.equals(testNames, that.testNames);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(testId, timestamp, status, measurements, testNames);
    }
}
