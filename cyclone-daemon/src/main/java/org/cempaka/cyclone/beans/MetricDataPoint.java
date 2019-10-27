package org.cempaka.cyclone.beans;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

public class MetricDataPoint
{
    private final long timestamp;
    private final String name;
    private final double value;

    public MetricDataPoint(final long timestamp, final String name, final double value)
    {
        this.timestamp = timestamp;
        this.name = checkNotNull(name);
        this.value = value;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public String getName()
    {
        return name;
    }

    public double getValue()
    {
        return value;
    }

    @Override
    public boolean equals(final Object o)
    {

        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final MetricDataPoint that = (MetricDataPoint) o;
        return timestamp == that.timestamp &&
            Double.compare(that.value, value) == 0 &&
            Objects.equals(name, that.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(timestamp, name, value);
    }
}