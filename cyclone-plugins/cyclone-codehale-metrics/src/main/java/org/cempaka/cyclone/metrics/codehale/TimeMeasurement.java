package org.cempaka.cyclone.metrics.codehale;

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
