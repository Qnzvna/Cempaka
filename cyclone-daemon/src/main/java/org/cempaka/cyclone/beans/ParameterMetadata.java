package org.cempaka.cyclone.beans;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;

public class ParameterMetadata
{
    private final String name;
    private final String type;

    public ParameterMetadata(final String name, final String type)
    {
        this.name = checkNotNull(name);
        this.type = checkNotNull(type);
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final ParameterMetadata that = (ParameterMetadata) o;
        return Objects.equal(name, that.name) &&
            Objects.equal(type, that.type);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(name, type);
    }
}
