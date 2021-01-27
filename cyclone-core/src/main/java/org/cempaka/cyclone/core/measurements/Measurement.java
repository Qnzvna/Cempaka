package org.cempaka.cyclone.core.measurements;

import static org.cempaka.cyclone.core.utils.Preconditions.checkNotNull;

import java.util.Map;


/**
 * Base class for creating new measurements.
 */
public abstract class Measurement
{
    private final String name;

    public Measurement(final String name)
    {
        this.name = checkNotNull(name);
    }

    /**
     * @return name of the measurement
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return map of values that was captured by this measurement
     */
    public abstract Map<String, Double> getSnapshot();
}
