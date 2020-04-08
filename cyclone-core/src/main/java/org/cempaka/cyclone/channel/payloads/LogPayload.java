package org.cempaka.cyclone.channel.payloads;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

import java.util.Objects;

public class LogPayload implements Payload
{
    private final PayloadType type = PayloadType.LOG;

    private final String testId;
    private final String logLine;

    public LogPayload(final String testId, final String logLine)
    {
        this.testId = checkNotNull(testId);
        this.logLine = checkNotNull(logLine);
    }

    @Override
    public String getTestId()
    {
        return testId;
    }

    @Override
    public PayloadType getType()
    {
        return type;
    }

    public String getLogLine()
    {
        return logLine;
    }

    @Override
    public String toString()
    {
        return "LogPayload{" +
            "testId='" + testId + '\'' +
            ", type=" + type +
            ", logLine='" + logLine + '\'' +
            '}';
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final LogPayload that = (LogPayload) o;
        return Objects.equals(testId, that.testId) &&
            type == that.type &&
            Objects.equals(logLine, that.logLine);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(testId, type, logLine);
    }
}
