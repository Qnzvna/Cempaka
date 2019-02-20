package org.cempaka.cyclone.metrics.codehale;

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
