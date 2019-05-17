package org.cempaka.cyclone.metrics.codehale;

/**
 * Measure median execution time (ns) of {@link org.cempaka.cyclone.annotation.Thunderbolt}
 * execution.
 */
public class TimeMeasurement extends TimerMeasurement
{
    @Override
    public String name()
    {
        return "time";
    }

    @Override
    public double getSnapshot()
    {
        return timer.getSnapshot().getMedian();
    }
}
