package org.cempaka.cyclone.measurements;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

import java.util.Map;


public abstract class Measurement
{
    private final String name;

    public Measurement(final String name)
    {
        this.name = checkNotNull(name);
    }

    public String getName()
    {
        return name;
    }

    public abstract Map<String, Double> getSnapshot();
}
