package org.cempaka.cyclone.beans;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;
import org.cempaka.cyclone.protocol.Status;

public class TestSnapshot
{
    private final long timestamp;
    private final Status status;
    private final Map<String, Double> measurements;

    public TestSnapshot(final long timestamp,
                        final Status status,
                        final Map<String, Double> measurements)
    {
        this.timestamp = timestamp;
        this.status = status;
        this.measurements = ImmutableMap.copyOf(measurements);
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

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final TestSnapshot that = (TestSnapshot) o;
        return timestamp == that.timestamp &&
            status == that.status &&
            Objects.equals(measurements, that.measurements);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(timestamp, status, measurements);
    }
}
