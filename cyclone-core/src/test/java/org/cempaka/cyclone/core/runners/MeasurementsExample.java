package org.cempaka.cyclone.core.runners;

import org.cempaka.cyclone.core.annotations.SuppressThrowables;
import org.cempaka.cyclone.core.annotations.Thunderbolt;
import org.cempaka.cyclone.core.measurements.Counted;
import org.cempaka.cyclone.core.measurements.CounterMeasurement;
import org.cempaka.cyclone.core.measurements.Measure;

public class MeasurementsExample extends AbstractTestExample
{
    static final String GLOBAL_COUNTER_NAME = "global";

    @Measure(GLOBAL_COUNTER_NAME)
    private CounterMeasurement globalCounter;

    @Counted
    @SuppressThrowables
    @Thunderbolt
    public void counted()
    {
        globalCounter.increment();
        thunder();
    }

    @Thunderbolt
    public void measureScope(@Measure final CounterMeasurement counterMeasurement)
    {
        globalCounter.increment();
        counterMeasurement.increment();
        counterMeasurement.increment();
    }

    @Thunderbolt
    public void emptyThunderbolt()
    {
    }
}
