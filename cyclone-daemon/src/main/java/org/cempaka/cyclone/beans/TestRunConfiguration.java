package org.cempaka.cyclone.beans;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TestRunConfiguration
{
    private final UUID parcelId;
    private final List<String> testNames;
    private final long loopCount;
    private final int threadsNumber;
    private final Map<String, String> parameters;

    @JsonCreator
    public TestRunConfiguration(@JsonProperty("parcelId") final UUID parcelId,
                                @JsonProperty("testNames") final List<String> testNames,
                                @JsonProperty("loopCount") final long loopCount,
                                @JsonProperty("threadsNumber") final int threadsNumber,
                                @JsonProperty("parameters") final Map<String, String> parameters)
    {
        this.parcelId = checkNotNull(parcelId);
        this.testNames = checkNotNull(testNames);
        this.loopCount = loopCount;
        this.threadsNumber = threadsNumber;
        this.parameters = ImmutableMap.copyOf(parameters);
    }

    public UUID getParcelId()
    {
        return parcelId;
    }

    public List<String> getTestNames()
    {
        return testNames;
    }

    public long getLoopCount()
    {
        return loopCount;
    }

    public int getThreadsNumber()
    {
        return threadsNumber;
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
        final TestRunConfiguration that = (TestRunConfiguration) o;
        return loopCount == that.loopCount &&
            threadsNumber == that.threadsNumber &&
            Objects.equal(parcelId, that.parcelId) &&
            Objects.equal(testNames, that.testNames) &&
            Objects.equal(parameters, that.parameters);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(parcelId, testNames, loopCount, threadsNumber, parameters);
    }
}
