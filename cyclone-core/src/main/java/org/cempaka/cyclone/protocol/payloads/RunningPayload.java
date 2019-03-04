package org.cempaka.cyclone.protocol.payloads;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class RunningPayload implements Payload
{
    private final Map<String, Long> succcessExecutions;
    private final Map<String, Long> failedExecutions;
    private final Map<String, Double> measurements;

    public RunningPayload(final Map<String, Double> measurements)
    {
        this.succcessExecutions = new Hashtable<>();
        this.failedExecutions = new HashMap<>();
        this.measurements = checkNotNull(measurements);
    }

    @Override
    public PayloadType getType()
    {
        return PayloadType.RUNNING;
    }

    public Map<String, Long> getSucccessExecutions()
    {
        return succcessExecutions;
    }

    public Map<String, Long> getFailedExecutions()
    {
        return failedExecutions;
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
        final RunningPayload that = (RunningPayload) o;
        return Objects.equals(succcessExecutions, that.succcessExecutions) &&
            Objects.equals(failedExecutions, that.failedExecutions) &&
            Objects.equals(measurements, that.measurements);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(succcessExecutions, failedExecutions, measurements);
    }
}
