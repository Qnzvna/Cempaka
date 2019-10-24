package org.cempaka.cyclone.protocol.payloads;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

import java.util.Map;
import java.util.Objects;

public class RunningPayload implements Payload
{
    private final String testId;
    private final Map<String, Double> measurements;

    public RunningPayload(final String testId, final Map<String, Double> measurements)
    {
        this.testId = checkNotNull(testId);
        this.measurements = checkNotNull(measurements);
    }

    @Override
    public String getTestId()
    {
        return testId;
    }

    @Override
    public PayloadType getType()
    {
        return PayloadType.RUNNING;
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
        return Objects.equals(testId, that.testId) &&
            Objects.equals(measurements, that.measurements);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(testId, measurements);
    }
}
