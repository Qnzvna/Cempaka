package org.cempaka.cyclone.core.channel;

import static org.cempaka.cyclone.core.utils.Preconditions.checkNotNull;

import java.util.Objects;

public class EndedPayload implements Payload
{
    private final String testId;
    private final int exitCode;

    public EndedPayload(final String testId, final int exitCode)
    {
        this.testId = checkNotNull(testId);
        this.exitCode = exitCode;
    }

    @Override
    public String getTestId()
    {
        return testId;
    }

    @Override
    public PayloadType getType()
    {
        return PayloadType.ENDED;
    }

    public int getExitCode()
    {
        return exitCode;
    }

    @Override
    public String toString()
    {
        return "EndedPayload{" +
            "testId='" + testId + '\'' +
            ", exitCode=" + exitCode +
            '}';
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final EndedPayload that = (EndedPayload) o;
        return exitCode == that.exitCode &&
            Objects.equals(testId, that.testId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(testId, exitCode);
    }
}
