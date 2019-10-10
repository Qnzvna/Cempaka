package org.cempaka.cyclone.beans;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;

public class TestRunConfiguration
{
    private final UUID parcelId;
    private final String testName;
    private final int loopCount;
    private final int threadsNumber;
    private final Map<String, String> parameters;
    private final Set<String> nodeIdentifiers;
    private final String jvmOptions;

    @JsonCreator
    public TestRunConfiguration(@JsonProperty("parcelId") final UUID parcelId,
                                @JsonProperty("testName") final String testNames,
                                @JsonProperty("loopCount") final int loopCount,
                                @JsonProperty("threadsNumber") final int threadsNumber,
                                @JsonProperty("parameters") final Map<String, String> parameters,
                                @JsonProperty("nodeIdentifiers") final Set<String> nodeIdentifiers,
                                @Nullable @JsonProperty("jvmOptions") final String jvmOptions)
    {
        this.parcelId = checkNotNull(parcelId);
        this.testName = checkNotNull(testNames);
        this.loopCount = loopCount;
        this.threadsNumber = threadsNumber;
        this.parameters = ImmutableMap.copyOf(parameters);
        this.nodeIdentifiers = ImmutableSet.copyOf(nodeIdentifiers);
        this.jvmOptions = jvmOptions == null ? "" : jvmOptions;
    }

    public UUID getParcelId()
    {
        return parcelId;
    }

    public String getTestName()
    {
        return testName;
    }

    public int getLoopCount()
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

    public Set<String> getNodeIdentifiers()
    {
        return nodeIdentifiers;
    }

    public String getJvmOptions()
    {
        return jvmOptions;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final TestRunConfiguration that = (TestRunConfiguration) o;
        return loopCount == that.loopCount &&
            threadsNumber == that.threadsNumber &&
            Objects.equals(parcelId, that.parcelId) &&
            Objects.equals(testName, that.testName) &&
            Objects.equals(parameters, that.parameters) &&
            Objects.equals(nodeIdentifiers, that.nodeIdentifiers) &&
            Objects.equals(jvmOptions, that.jvmOptions);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(parcelId, testName, loopCount, threadsNumber, parameters, nodeIdentifiers, jvmOptions);
    }

    @Override
    public String toString()
    {
        return "TestRunConfiguration{" +
            "parcelId=" + parcelId +
            ", testName='" + testName + '\'' +
            ", loopCount=" + loopCount +
            ", threadsNumber=" + threadsNumber +
            ", parameters=" + parameters +
            ", nodeIdentifiers=" + nodeIdentifiers +
            ", jvmOptions='" + jvmOptions + '\'' +
            '}';
    }
}
