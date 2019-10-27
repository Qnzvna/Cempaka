package org.cempaka.cyclone.configurations;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class AuthenticationConfiguration
{
    private Type type = Type.NONE;
    private Map<String, String> properties = ImmutableMap.of();

    public void setType(final Type type)
    {
        this.type = type;
    }

    public void setProperties(final Map<String, String> properties)
    {
        this.properties = properties;
    }

    public Type getType()
    {
        return type;
    }

    public Map<String, String> getProperties()
    {
        return properties;
    }

    public enum Type
    {
        NONE, BASIC, HEADER
    }
}
