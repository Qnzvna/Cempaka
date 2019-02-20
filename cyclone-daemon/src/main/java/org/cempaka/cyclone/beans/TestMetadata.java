package org.cempaka.cyclone.beans;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import java.util.Set;

public class TestMetadata
{
    private final String testName;
    private final Set<ParameterMetadata> parameters;

    public TestMetadata(final String testName,
                        final Iterable<ParameterMetadata> parameters)
    {
        this.testName = checkNotNull(testName);
        this.parameters = ImmutableSet.copyOf(parameters);
    }

    public String getTestName()
    {
        return testName;
    }

    public Set<ParameterMetadata> getParameters()
    {
        return parameters;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final TestMetadata that = (TestMetadata) o;
        return Objects.equal(testName, that.testName) &&
            Objects.equal(parameters, that.parameters);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(testName, parameters);
    }
}
