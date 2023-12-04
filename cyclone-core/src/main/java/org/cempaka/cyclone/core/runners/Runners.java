package org.cempaka.cyclone.core.runners;

import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.cempaka.cyclone.core.measurements.MeasurementRegistry;

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
        return threadRunner(delegate, poolSize, 0);
    }

    public static Runner threadRunner(final Runner delegate, final int poolSize, int warmups)
    {
        return new ThreadRunner(delegate, poolSize, warmups);
    }

    public static Runner loopRunner(final Runner delegate, final long loopCount)
    {
        return new LoopRunner(delegate, loopCount);
    }

    public static Runner durationRunner(final Runner delegate, final Duration duration)
    {
        return new DurationRunner(delegate, duration, Clock.systemDefaultZone());
    }
}
