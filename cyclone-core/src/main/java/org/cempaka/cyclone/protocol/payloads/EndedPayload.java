package org.cempaka.cyclone.protocol.payloads;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

import java.util.Objects;
import java.util.Optional;

public class EndedPayload implements Payload
{
    private final String testId;
    private final int exitCode;
    private final Optional<String> stackTrace;

    public EndedPayload(final String testId, final int exitCode, final String stackTrace)
    {
        this.testId = checkNotNull(testId);
        this.exitCode = exitCode;
        this.stackTrace = Optional.ofNullable(stackTrace);
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

    public Optional<String> getStackTrace()
    {
        return stackTrace;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final EndedPayload that = (EndedPayload) o;
        return exitCode == that.exitCode &&
            Objects.equals(testId, that.testId) &&
            Objects.equals(stackTrace, that.stackTrace);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(testId, exitCode, stackTrace);
    }
}
