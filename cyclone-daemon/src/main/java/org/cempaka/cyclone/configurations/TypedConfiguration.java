package org.cempaka.cyclone.configurations;

import java.util.HashMap;
import java.util.Map;
import jakarta.validation.constraints.NotNull;

public class TypedConfiguration<T>
{
    @NotNull
    private Class<? extends T> type;
    private Map<String, String> parameters = new HashMap<>();

    private TypedConfiguration()
    {
    }

    public TypedConfiguration(final Class<? extends T> type)
    {
        this.type = type;
    }

    public Class<? extends T> getType()
    {
        return type;
    }

    public Map<String, String> getParameters()
    {
        return parameters;
    }
}
