package org.cempaka.cyclone.beans;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

public class ParameterMetadata
{
    private final String name;
    private final String type;
    private final String defaultValue;

    public ParameterMetadata(final String name, final String type, final String defaultValue)
    {
        this.name = checkNotNull(name);
        this.type = checkNotNull(type);
        this.defaultValue = checkNotNull(defaultValue);
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final ParameterMetadata that = (ParameterMetadata) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(type, that.type) &&
            Objects.equals(defaultValue, that.defaultValue);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, type, defaultValue);
    }
}
