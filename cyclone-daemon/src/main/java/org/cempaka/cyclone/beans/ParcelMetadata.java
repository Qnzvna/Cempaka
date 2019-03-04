package org.cempaka.cyclone.beans;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.UUID;

public class ParcelMetadata
{
    private final UUID id;
    private final Set<TestMetadata> testsMetadata;

    @JsonCreator
    public ParcelMetadata(@JsonProperty("id") final UUID id,
                          @JsonProperty("testsMetadata") final Set<TestMetadata> testsMetadata)
    {
        this.id = checkNotNull(id);
        this.testsMetadata = ImmutableSet.copyOf(testsMetadata);
    }

    public UUID getId()
    {
        return id;
    }

    public Set<TestMetadata> getTestsMetadata()
    {
        return testsMetadata;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final ParcelMetadata that = (ParcelMetadata) o;
        return Objects.equal(id, that.id) &&
            Objects.equal(testsMetadata, that.testsMetadata);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(id, testsMetadata);
    }
}
