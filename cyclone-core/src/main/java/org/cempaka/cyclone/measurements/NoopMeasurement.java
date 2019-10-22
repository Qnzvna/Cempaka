package org.cempaka.cyclone.measurements;

import java.util.Collections;
import java.util.Map;

public class NoopMeasurement extends Measurement
{
    public static Measurement INSTANCE = new NoopMeasurement();

    private NoopMeasurement()
    {
        super("");
    }

    @Override
    public Map<String, Double> getSnapshot()
    {
        return Collections.emptyMap();
    }
}
