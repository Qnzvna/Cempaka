package org.cempaka.cyclone.invoker;

import org.cempaka.cyclone.annotations.SuppressThrowables;
import org.cempaka.cyclone.annotations.Thunderbolt;
import org.cempaka.cyclone.measurements.Counted;
import org.cempaka.cyclone.measurements.CounterMeasurement;
import org.cempaka.cyclone.measurements.Measure;
import org.cempaka.cyclone.measurements.Measured;
import org.cempaka.cyclone.measurements.tickers.NoopTicker;

public class MeasurementsExample extends AbstractTestExample
{
    static final String GLOBAL_COUNTER_NAME = "global";
    static final String CUSTOM_COUNTER_NAME = "custom_counter";

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
    @Measured(value = CounterMeasurement.class, name = CUSTOM_COUNTER_NAME, ticker = NoopTicker.class)
    public void customCount()
    {
        globalCounter.increment();
    }

    @Thunderbolt
    public void measureScope(@Measure final CounterMeasurement counterMeasurement)
    {
        globalCounter.increment();
        counterMeasurement.increment();
        counterMeasurement.increment();
    }
}
