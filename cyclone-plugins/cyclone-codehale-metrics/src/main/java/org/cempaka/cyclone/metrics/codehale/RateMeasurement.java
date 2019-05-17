package org.cempaka.cyclone.metrics.codehale;

/**
 * Measure rate (event per second) of {@link org.cempaka.cyclone.annotation.Thunderbolt} execution.
 */
public class RateMeasurement extends TimerMeasurement
{
    @Override
    public String name()
    {
        return "rate";
    }

    @Override
    public double getSnapshot()
    {
        return timer.getMeanRate();
    }
}
