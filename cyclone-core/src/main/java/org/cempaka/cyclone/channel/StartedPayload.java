package org.cempaka.cyclone.channel;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

import java.util.Objects;

public class StartedPayload implements Payload
{
    private final String testId;

    public StartedPayload(final String testId)
    {
        this.testId = checkNotNull(testId);
    }

    @Override
    public String getTestId()
    {
        return testId;
    }

    @Override
    public PayloadType getType()
    {
        return PayloadType.STARTED;
    }

    @Override
    public String toString()
    {
        return "StartedPayload{" +
            "testId='" + testId + '\'' +
            '}';
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final StartedPayload that = (StartedPayload) o;
        return Objects.equals(testId, that.testId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(testId);
    }
}
