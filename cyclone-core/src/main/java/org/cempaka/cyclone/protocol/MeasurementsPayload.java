package org.cempaka.cyclone.protocol;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MeasurementsPayload implements Payload
{
    private final Status status;
    private final Map<String, Double> snapshots;

    public MeasurementsPayload(final Status status)
    {
        this.status = status;
        this.snapshots = new HashMap<>();
    }

    public MeasurementsPayload(final Status status,
                               final Map<String, Double> snapshots)
    {
        this.status = status;
        this.snapshots = checkNotNull(snapshots);
    }

    @Override
    public PayloadType getType()
    {
        return PayloadType.MEASUREMENTS;
    }

    public Status getStatus()
    {
        return status;
    }

    public Map<String, Double> getSnapshots()
    {
        return snapshots;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final MeasurementsPayload that = (MeasurementsPayload) o;
        return status == that.status &&
            Objects.equals(snapshots, that.snapshots);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(status, snapshots);
    }
}
