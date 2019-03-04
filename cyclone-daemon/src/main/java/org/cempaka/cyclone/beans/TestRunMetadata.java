package org.cempaka.cyclone.beans;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;

public class TestRunMetadata
{
    private final String testRunId;
    private final long initializationTime;
    private final String testName;
    private final Map<String, String> parameters;

    public TestRunMetadata(final String testRunId,
                           final long initializationTime,
                           final String testName,
                           final Map<String, String> parameters)
    {
        this.testRunId = checkNotNull(testRunId);
        this.initializationTime = initializationTime;
        this.testName = checkNotNull(testName);
        this.parameters = ImmutableMap.copyOf(parameters);
    }

    public String getTestRunId()
    {
        return testRunId;
    }

    public long getInitializationTime()
    {
        return initializationTime;
    }

    public String getTestName()
    {
        return testName;
    }

    public Map<String, String> getParameters()
    {
        return parameters;
    }

    @Override
    public boolean equals(final Object o)
    {

        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final TestRunMetadata that = (TestRunMetadata) o;
        return initializationTime == that.initializationTime &&
            Objects.equals(testRunId, that.testRunId) &&
            Objects.equals(testName, that.testName) &&
            Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(testRunId, initializationTime, testName, parameters);
    }
}
