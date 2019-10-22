package org.cempaka.cyclone.invoker;

import org.cempaka.cyclone.annotations.Thunderbolt;
import org.cempaka.cyclone.measurements.Counted;
import org.cempaka.cyclone.measurements.CounterMeasurement;
import org.cempaka.cyclone.measurements.Measured;
import org.cempaka.cyclone.measurements.tickers.NoopTicker;

public class MeasurementsExample extends AbstractTestExample
{
    @Counted
    @Thunderbolt(suppressAllThrowables = true)
    public void counted()
    {
        thunder();
    }

    @Thunderbolt(suppressAllThrowables = true)
    @Measured(value = CounterMeasurement.class, name = "custom_counter", ticker = NoopTicker.class)
    public void customCount()
    {
        thunder();
    }
}
