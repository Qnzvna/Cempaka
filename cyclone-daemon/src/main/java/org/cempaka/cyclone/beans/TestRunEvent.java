package org.cempaka.cyclone.beans;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

import java.util.Objects;

public class TestRunEvent
{
    private final String testRunId;
    private final long timestamp;
    private final EventType eventType;

    public TestRunEvent(final String testRunId,
                        final long timestamp,
                        final EventType eventType)
    {
        this.testRunId = checkNotNull(testRunId);
        this.timestamp = timestamp;
        this.eventType = eventType;
    }

    public String getTestRunId()
    {
        return testRunId;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public EventType getEventType()
    {
        return eventType;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final TestRunEvent that = (TestRunEvent) o;
        return timestamp == that.timestamp &&
            Objects.equals(testRunId, that.testRunId) &&
            eventType == that.eventType;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(testRunId, timestamp, eventType);
    }
}
