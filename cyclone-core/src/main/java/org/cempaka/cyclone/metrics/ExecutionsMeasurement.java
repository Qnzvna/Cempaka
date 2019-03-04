package org.cempaka.cyclone.metrics;

import java.util.concurrent.atomic.LongAdder;

public class ExecutionsMeasurement implements Measurement
{
    private final LongAdder longAdder = new LongAdder();

    @Override
    public String name()
    {
        return "executions";
    }

    @Override
    public void start()
    {
    }

    @Override
    public void stop()
    {
        longAdder.increment();
    }

    @Override
    public double getSnapshot()
    {
        return longAdder.doubleValue();
    }
}
