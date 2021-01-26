package org.cempaka.cyclone.measurements;

import java.util.Collections;
import java.util.Map;

class NoopMeasurement extends Measurement
{
    public static final String NAME = "noop";
    public static Measurement INSTANCE = new NoopMeasurement(NAME);

    NoopMeasurement(final String name)
    {
        super(name);
    }

    @Override
    public Map<String, Double> getSnapshot()
    {
        return Collections.emptyMap();
    }
}
