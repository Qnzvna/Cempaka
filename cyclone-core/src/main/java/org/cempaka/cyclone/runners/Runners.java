package org.cempaka.cyclone.runners;

import java.util.List;
import java.util.Map;
import org.cempaka.cyclone.measurements.MeasurementRegistry;

public final class Runners
{
    public static Runner simpleRunner(final List<Class<?>> testClasses,
                                      final Map<String, String> parameters,
                                      final Map<String, String> metadata,
                                      final MeasurementRegistry measurementRegistry)
    {
        return new SimpleRunner(testClasses,
            parameters,
            metadata,
            measurementRegistry);
    }

    public static Runner threadRunner(final Runner delegate, final int poolSize)
    {
        return new ThreadRunner(delegate, poolSize);
    }

    public static Runner loopRunner(final Runner delegate, final long loopCount)
    {
        return new LoopRunner(delegate, loopCount);
    }
}
